package com.example.alea.data.model

data class Notification(
    val id: Int,
    val senderName: String,
    val text: String,
    val time: String,
    val isRead: Boolean,
    val hasActions: Boolean = false,
    val hasTrophy: Boolean = false
)
