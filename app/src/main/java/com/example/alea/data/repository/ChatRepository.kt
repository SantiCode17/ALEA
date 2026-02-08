package com.example.alea.data.repository

import com.example.alea.data.model.Message
import com.google.firebase.Timestamp
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ChatRepository @Inject constructor(
    private val firestore: FirebaseFirestore?
) {
    private val messagesCollection = firestore?.collection("messages")

    // Demo messages storage
    private val demoMessages = mutableMapOf<String, MutableList<Message>>()

    init {
        // Add some demo messages
        val chatId = "demo_user_123_user_2"
        demoMessages[chatId] = mutableListOf(
            Message(id = "m1", chatId = chatId, senderId = "user_2", senderName = "Champion", text = "Hey! Ready for the challenge?", timestamp = Timestamp.now()),
            Message(id = "m2", chatId = chatId, senderId = "demo_user_123", senderName = "Player", text = "Let's do this! 💪", timestamp = Timestamp.now()),
            Message(id = "m3", chatId = chatId, senderId = "user_2", senderName = "Champion", text = "Good luck, you'll need it!", timestamp = Timestamp.now())
        )
    }

    fun getChatMessages(chatId: String): Flow<List<Message>> {
        val flow = MutableStateFlow(demoMessages[chatId]?.toList() ?: emptyList())
        return flow.asStateFlow()
    }

    suspend fun sendMessage(message: Message): Boolean {
        val messages = demoMessages.getOrPut(message.chatId) { mutableListOf() }
        messages.add(message.copy(id = "m_${System.currentTimeMillis()}", timestamp = Timestamp.now()))
        return true
    }

    suspend fun markAsRead(messageId: String): Boolean {
        return true // Demo mode
    }

    fun getChatId(userId1: String, userId2: String): String {
        return if (userId1 < userId2) "${userId1}_${userId2}" else "${userId2}_${userId1}"
    }
}
