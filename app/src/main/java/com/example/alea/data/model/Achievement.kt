package com.example.alea.data.model

data class Achievement(
    val name: String,
    val description: String,
    val emoji: String,
    val isUnlocked: Boolean = true
)
