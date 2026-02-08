package com.example.alea.data.model

import com.google.firebase.Timestamp
import com.google.firebase.firestore.DocumentId
import com.google.firebase.firestore.ServerTimestamp

data class Message(
    @DocumentId
    val id: String = "",
    val chatId: String = "",
    val senderId: String = "",
    val senderName: String = "",
    val text: String = "",
    @ServerTimestamp
    val timestamp: Timestamp? = null,
    val isRead: Boolean = false
)
