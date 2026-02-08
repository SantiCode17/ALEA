package com.example.alea.data.model

import com.google.firebase.Timestamp
import com.google.firebase.firestore.DocumentId
import com.google.firebase.firestore.ServerTimestamp

/**
 * User data class with gamification fields
 */
data class User(
    @DocumentId
    val id: String = "",
    val username: String = "",
    val displayName: String = "",
    val email: String = "",
    val avatarUrl: String = "",
    val coins: Long = 1000,
    val level: Int = 1,
    val xp: Int = 0,
    val totalChallenges: Int = 0,
    val completedChallenges: Int = 0,
    val wins: Int = 0,
    val losses: Int = 0,
    val rank: Int = 0,
    val isOnline: Boolean = false,
    val friends: List<String> = emptyList(),
    val achievements: List<String> = emptyList(),
    val themeColor: String = "#FF8C42",
    @ServerTimestamp
    val createdAt: Timestamp? = null,
    @ServerTimestamp
    val lastSeen: Timestamp? = null
) {
    /**
     * Calculate win rate as percentage
     */
    val winRate: Float
        get() = if (totalChallenges > 0) (wins.toFloat() / totalChallenges) * 100 else 0f

    /**
     * Get XP needed for next level
     */
    val xpToNextLevel: Int
        get() = level * 100

    /**
     * Get XP progress percentage to next level
     */
    val xpProgress: Float
        get() = (xp.toFloat() / xpToNextLevel) * 100

    /**
     * Get user title based on level
     */
    val title: String
        get() = when {
            level >= 50 -> "Legend"
            level >= 30 -> "Master"
            level >= 20 -> "Champion"
            level >= 10 -> "Warrior"
            level >= 5 -> "Challenger"
            else -> "Rookie"
        }
}
