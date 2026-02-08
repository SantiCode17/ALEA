package com.example.alea.data.repository

import com.example.alea.data.model.Challenge
import com.example.alea.data.model.ChallengeCategory
import com.example.alea.data.model.ChallengeDifficulty
import com.example.alea.data.model.ChallengeStatus
import com.google.firebase.Timestamp
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.tasks.await
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ChallengeRepository @Inject constructor(
    private val firestore: FirebaseFirestore?
) {
    private val challengesCollection = firestore?.collection("challenges")

    // Demo challenges with categories and difficulty
    private val demoChallenges = listOf(
        Challenge(
            id = "challenge_1",
            title = "Beat my high score!",
            description = "Can you beat 1000 points in Space Invaders?",
            creatorId = "demo_user_123",
            creatorName = "Player",
            opponentId = "user_2",
            opponentName = "Champion",
            betAmount = 150,
            category = ChallengeCategory.GAMING,
            difficulty = ChallengeDifficulty.MEDIUM,
            status = ChallengeStatus.ACTIVE
        ),
        Challenge(
            id = "challenge_2",
            title = "100 pushups challenge",
            description = "Complete 100 pushups in under 5 minutes",
            creatorId = "user_3",
            creatorName = "Warrior",
            opponentId = "demo_user_123",
            opponentName = "Player",
            betAmount = 200,
            category = ChallengeCategory.FITNESS,
            difficulty = ChallengeDifficulty.HARD,
            status = ChallengeStatus.ACTIVE
        ),
        Challenge(
            id = "challenge_3",
            title = "Learn a new song",
            description = "Learn to play 'Bohemian Rhapsody' on guitar",
            creatorId = "demo_user_123",
            creatorName = "Player",
            opponentId = "user_4",
            opponentName = "Hunter",
            betAmount = 100,
            category = ChallengeCategory.MUSIC,
            difficulty = ChallengeDifficulty.HARD,
            status = ChallengeStatus.COMPLETED
        ),
        Challenge(
            id = "challenge_4",
            title = "Cook a 5-star meal",
            description = "Make a restaurant-quality pasta from scratch",
            creatorId = "user_6",
            creatorName = "Maria",
            opponentId = "demo_user_123",
            opponentName = "Player",
            betAmount = 75,
            category = ChallengeCategory.COOKING,
            difficulty = ChallengeDifficulty.EASY,
            status = ChallengeStatus.PENDING
        ),
        Challenge(
            id = "challenge_5",
            title = "Study marathon",
            description = "Complete 4 hours of focused study session",
            creatorId = "demo_user_123",
            creatorName = "Player",
            opponentId = "user_7",
            opponentName = "Carlos",
            betAmount = 50,
            category = ChallengeCategory.STUDY,
            difficulty = ChallengeDifficulty.MEDIUM,
            status = ChallengeStatus.ACTIVE
        ),
        Challenge(
            id = "challenge_6",
            title = "Art battle",
            description = "Create a digital portrait in 30 minutes",
            creatorId = "user_5",
            creatorName = "Rookie",
            opponentId = "demo_user_123",
            opponentName = "Player",
            betAmount = 250,
            category = ChallengeCategory.ART,
            difficulty = ChallengeDifficulty.EXTREME,
            status = ChallengeStatus.PENDING,
            isPublic = true
        )
    )

    suspend fun createChallenge(challenge: Challenge): String? {
        if (challengesCollection == null) {
            return "demo_challenge_${System.currentTimeMillis()}"
        }
        return try {
            val docRef = challengesCollection.add(challenge).await()
            docRef.id
        } catch (e: Exception) {
            "demo_challenge_${System.currentTimeMillis()}"
        }
    }

    fun getUserChallenges(userId: String): Flow<List<Challenge>> = flow {
        try {
            emit(demoChallenges.filter { it.creatorId == userId || it.opponentId == userId })
        } catch (e: Exception) {
            emit(demoChallenges)
        }
    }

    fun getActiveChallenges(userId: String): Flow<List<Challenge>> = flow {
        try {
            val active = demoChallenges.filter {
                (it.creatorId == userId || it.opponentId == userId) &&
                (it.status == ChallengeStatus.ACTIVE || it.status == ChallengeStatus.PENDING)
            }
            emit(active)
        } catch (e: Exception) {
            emit(demoChallenges.filter { it.status == ChallengeStatus.ACTIVE })
        }
    }

    suspend fun getChallenge(challengeId: String): Challenge? {
        if (challengesCollection == null) {
            return demoChallenges.find { it.id == challengeId } ?: demoChallenges.first()
        }
        return try {
            challengesCollection.document(challengeId)
                .get()
                .await()
                .toObject(Challenge::class.java)
        } catch (e: Exception) {
            demoChallenges.find { it.id == challengeId } ?: demoChallenges.first()
        }
    }

    suspend fun updateChallengeStatus(challengeId: String, status: ChallengeStatus): Boolean {
        return true // Demo mode
    }

    suspend fun completeChallenge(challengeId: String, winnerId: String): Boolean {
        return true // Demo mode
    }
}
