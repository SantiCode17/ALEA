package com.example.alea.data.model

data class User(
    val id: String,
    val name: String,
    val username: String,
    val email: String,
    val bio: String,
    val aleaCoins: Int,
    val level: Int,
    val totalChallenges: Int,
    val weeklyPoints: Int,
    val avatarRes: Int
)
