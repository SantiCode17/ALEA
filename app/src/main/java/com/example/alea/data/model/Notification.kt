package com.example.alea.data.model

import com.google.firebase.Timestamp
import com.google.firebase.firestore.DocumentId
import com.google.firebase.firestore.ServerTimestamp

/**
 * Types of notifications in the app
 */
enum class NotificationType {
    CHALLENGE_RECEIVED,     // Someone challenged you
    CHALLENGE_ACCEPTED,     // Your challenge was accepted
    CHALLENGE_COMPLETED,    // A challenge you're in was completed
    CHALLENGE_WON,          // You won a challenge
    CHALLENGE_LOST,         // You lost a challenge
    FRIEND_REQUEST,         // New friend request
    FRIEND_ACCEPTED,        // Friend request accepted
    ACHIEVEMENT_UNLOCKED,   // New achievement unlocked
    LEVEL_UP,               // Level up notification
    SYSTEM                  // System notification
}

/**
 * Notification data class
 */
data class Notification(
    @DocumentId
    val id: String = "",
    val userId: String = "",
    val type: NotificationType = NotificationType.SYSTEM,
    val title: String = "",
    val message: String = "",
    val imageUrl: String = "",
    val actionId: String = "",  // ID for navigation (challengeId, friendId, etc.)
    val isRead: Boolean = false,
    @ServerTimestamp
    val createdAt: Timestamp? = null
) {
    /**
     * Get the emoji for this notification type
     */
    val emoji: String
        get() = when (type) {
            NotificationType.CHALLENGE_RECEIVED -> "⚔️"
            NotificationType.CHALLENGE_ACCEPTED -> "✅"
            NotificationType.CHALLENGE_COMPLETED -> "🏁"
            NotificationType.CHALLENGE_WON -> "🏆"
            NotificationType.CHALLENGE_LOST -> "😔"
            NotificationType.FRIEND_REQUEST -> "👋"
            NotificationType.FRIEND_ACCEPTED -> "🤝"
            NotificationType.ACHIEVEMENT_UNLOCKED -> "🎖️"
            NotificationType.LEVEL_UP -> "⬆️"
            NotificationType.SYSTEM -> "📢"
        }

    /**
     * Get relative time string (localized in Spanish)
     */
    fun getRelativeTime(): String {
        val now = System.currentTimeMillis()
        val notificationTime = createdAt?.toDate()?.time ?: now
        val diff = now - notificationTime

        return when {
            diff < 60_000 -> "Ahora"
            diff < 3_600_000 -> "Hace ${diff / 60_000}m"
            diff < 86_400_000 -> "Hace ${diff / 3_600_000}h"
            diff < 604_800_000 -> "Hace ${diff / 86_400_000}d"
            else -> "Hace ${diff / 604_800_000}sem"
        }
    }
}
