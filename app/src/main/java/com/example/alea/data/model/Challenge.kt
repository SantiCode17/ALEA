package com.example.alea.data.model

import com.google.firebase.Timestamp
import com.google.firebase.firestore.DocumentId
import com.google.firebase.firestore.ServerTimestamp

/**
 * Challenge status representing the lifecycle of a challenge
 */
enum class ChallengeStatus {
    PENDING,    // Waiting for opponent to accept
    ACTIVE,     // Challenge is in progress
    COMPLETED,  // Challenge finished with a winner
    CANCELLED   // Challenge was cancelled
}

/**
 * Result of a challenge from the user's perspective
 */
enum class ChallengeResult {
    NONE,
    WON,
    LOST
}

/**
 * Challenge categories with emoji and display name
 */
enum class ChallengeCategory(val emoji: String, val displayName: String) {
    FITNESS("💪", "Fitness"),
    GAMING("🎮", "Gaming"),
    ART("🎨", "Art"),
    COOKING("🍳", "Cooking"),
    STUDY("📚", "Study"),
    MUSIC("🎵", "Music"),
    SPORTS("⚽", "Sports"),
    OTHER("🎯", "Other")
}

/**
 * Challenge difficulty levels with coin multiplier
 */
enum class ChallengeDifficulty(val multiplier: Float, val displayName: String) {
    EASY(1f, "Easy"),
    MEDIUM(2f, "Medium"),
    HARD(3f, "Hard"),
    EXTREME(5f, "Extreme")
}

/**
 * Main Challenge data class
 */
data class Challenge(
    @DocumentId
    val id: String = "",
    val title: String = "",
    val description: String = "",
    val creatorId: String = "",
    val creatorName: String = "",
    val creatorAvatarUrl: String = "",
    val opponentId: String = "",
    val opponentName: String = "",
    val opponentAvatarUrl: String = "",
    val betAmount: Long = 0,
    val category: ChallengeCategory = ChallengeCategory.OTHER,
    val difficulty: ChallengeDifficulty = ChallengeDifficulty.MEDIUM,
    val status: ChallengeStatus = ChallengeStatus.PENDING,
    val result: ChallengeResult = ChallengeResult.NONE,
    val winnerId: String = "",
    val isPublic: Boolean = false,
    val participants: List<String> = emptyList(),
    val deadline: Timestamp? = null,
    @ServerTimestamp
    val createdAt: Timestamp? = null,
    @ServerTimestamp
    val updatedAt: Timestamp? = null,
    val imageUrl: String = ""
) {
    /**
     * Calculate actual stake based on difficulty multiplier
     */
    val actualStake: Long
        get() = (betAmount * difficulty.multiplier).toLong()

    /**
     * Get formatted category with emoji
     */
    val categoryDisplay: String
        get() = "${category.emoji} ${category.displayName}"
}
