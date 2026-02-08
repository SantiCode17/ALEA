package com.example.alea.data.model

data class RankingEntry(
    val rank: Int,
    val userId: String,
    val username: String,
    val avatarUrl: String,
    val points: Long,
    val wins: Int
)
