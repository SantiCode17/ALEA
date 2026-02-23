package com.example.alea.data.model

enum class ChallengeStatus {
    PENDING, ACTIVE, COMPLETED, REJECTED
}

data class Challenge(
    val id: Int = 0,
    val title: String,
    val challenger: String,
    val bet: Int,
    val status: ChallengeStatus,
    val imageRes: Int = 0,
    val category: String = "",
    val deadline: String = "",
    val description: String = "",
    val coinsWon: Int = 0
)
