package com.example.alea.data.model

data class Message(
    val isMe: Boolean,
    val text: String,
    val time: String,
    val dateHeader: String? = null
)
