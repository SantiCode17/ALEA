package com.example.alea.data.model

data class RankUser(
    val position: String,
    val name: String,
    val username: String,
    val points: Int,
    val isCurrentUser: Boolean = false,
    val color: Int = 0xFF4E54C8.toInt()
) {
    val initials: String get() = name.split(" ").take(2).map { it.first().uppercaseChar() }.joinToString("")
}
