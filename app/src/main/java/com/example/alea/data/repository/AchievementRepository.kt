package com.example.alea.data.repository

import com.example.alea.data.model.Achievement
import com.example.alea.data.model.AchievementCategory
import com.example.alea.data.model.Achievements
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.tasks.await
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AchievementRepository @Inject constructor(
    private val firestore: FirebaseFirestore?
) {
    private val usersCollection = firestore?.collection("users")

    /**
     * Get all achievements with user's unlock status
     */
    fun getAllAchievementsForUser(userId: String): Flow<List<Achievement>> = flow {
        val allAchievements = Achievements.getAllAchievements()

        if (usersCollection == null) {
            // Demo mode - unlock first few achievements
            val demoUnlocked = setOf("first_steps", "first_win", "first_chat")
            val achievements = allAchievements.map { achievement ->
                if (demoUnlocked.contains(achievement.id)) {
                    achievement.copy(unlockedAt = System.currentTimeMillis())
                } else {
                    achievement
                }
            }
            emit(achievements)
            return@flow
        }

        try {
            val userDoc = usersCollection.document(userId).get().await()
            @Suppress("UNCHECKED_CAST")
            val unlockedIds = userDoc.get("achievements") as? List<String> ?: emptyList()

            val achievements = allAchievements.map { achievement ->
                if (unlockedIds.contains(achievement.id)) {
                    achievement.copy(unlockedAt = System.currentTimeMillis())
                } else {
                    achievement
                }
            }
            emit(achievements)
        } catch (e: Exception) {
            emit(allAchievements)
        }
    }

    /**
     * Get only unlocked achievements for a user
     */
    fun getUnlockedAchievements(userId: String): Flow<List<Achievement>> = flow {
        val allAchievements = Achievements.getAllAchievements()

        if (usersCollection == null) {
            // Demo mode
            val demoUnlocked = listOf("first_steps", "first_win", "first_chat")
            val achievements = allAchievements
                .filter { demoUnlocked.contains(it.id) }
                .map { it.copy(unlockedAt = System.currentTimeMillis()) }
            emit(achievements)
            return@flow
        }

        try {
            val userDoc = usersCollection.document(userId).get().await()
            @Suppress("UNCHECKED_CAST")
            val unlockedIds = userDoc.get("achievements") as? List<String> ?: emptyList()

            val achievements = allAchievements
                .filter { unlockedIds.contains(it.id) }
                .map { it.copy(unlockedAt = System.currentTimeMillis()) }
            emit(achievements)
        } catch (e: Exception) {
            emit(emptyList())
        }
    }

    /**
     * Unlock an achievement for a user
     */
    suspend fun unlockAchievement(userId: String, achievementId: String): Boolean {
        if (usersCollection == null) {
            return true // Demo mode
        }

        return try {
            val userRef = usersCollection.document(userId)
            firestore?.runTransaction { transaction ->
                val userDoc = transaction.get(userRef)
                @Suppress("UNCHECKED_CAST")
                val currentAchievements = userDoc.get("achievements") as? MutableList<String>
                    ?: mutableListOf()

                if (!currentAchievements.contains(achievementId)) {
                    currentAchievements.add(achievementId)
                    transaction.update(userRef, "achievements", currentAchievements)

                    // Add XP and coins rewards
                    val achievement = Achievements.getAllAchievements().find { it.id == achievementId }
                    if (achievement != null) {
                        val currentXp = userDoc.getLong("xp") ?: 0
                        val currentCoins = userDoc.getLong("coins") ?: 0
                        transaction.update(userRef, "xp", currentXp + achievement.xpReward)
                        transaction.update(userRef, "coins", currentCoins + achievement.coinsReward)
                    }
                }
            }?.await()
            true
        } catch (e: Exception) {
            false
        }
    }

    /**
     * Check and unlock achievements based on user stats
     */
    suspend fun checkAndUnlockAchievements(
        userId: String,
        wins: Int = 0,
        challenges: Int = 0,
        friends: Int = 0,
        coins: Long = 0,
        rank: Int = Int.MAX_VALUE
    ): List<Achievement> {
        val unlockedNow = mutableListOf<Achievement>()
        val allAchievements = Achievements.getAllAchievements()

        // Check each achievement
        for (achievement in allAchievements) {
            val shouldUnlock = when (achievement.id) {
                "first_steps" -> challenges >= 1
                "first_win" -> wins >= 1
                "five_wins" -> wins >= 5
                "ten_wins" -> wins >= 10
                "twenty_five_wins" -> wins >= 25
                "fifty_wins" -> wins >= 50
                "social_butterfly" -> friends >= 10
                "popular" -> friends >= 25
                "coin_collector" -> coins >= 10000
                "wealthy" -> coins >= 50000
                "top_10" -> rank <= 10
                "top_1" -> rank == 1
                else -> false
            }

            if (shouldUnlock) {
                val success = unlockAchievement(userId, achievement.id)
                if (success) {
                    unlockedNow.add(achievement)
                }
            }
        }

        return unlockedNow
    }

    /**
     * Get achievements by category
     */
    fun getAchievementsByCategory(category: AchievementCategory): List<Achievement> {
        return Achievements.getByCategory(category)
    }
}
