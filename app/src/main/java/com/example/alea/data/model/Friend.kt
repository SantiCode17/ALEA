package com.example.alea.data.model

data class Friend(
    val name: String,
    val username: String,
    val status: String,
    val activeChallenge: Boolean = false,
    val activeChallengeCount: Int = 0,
    val color: Int = 0xFF4E54C8.toInt(),
    val phone: String = "+34 123-456-7890"
) {
    val initials: String get() = name.split(" ").take(2).map { it.first().uppercaseChar() }.joinToString("")
    val isOnline: Boolean get() = status == "Online"
}
