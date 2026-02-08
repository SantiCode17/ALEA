package com.example.alea.data.repository

import com.example.alea.data.model.Notification
import com.example.alea.data.model.NotificationType
import com.google.firebase.Timestamp
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.tasks.await
import java.util.Date
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class NotificationRepository @Inject constructor(
    private val firestore: FirebaseFirestore?
) {
    private val notificationsCollection = firestore?.collection("notifications")

    // Demo notifications
    private val demoNotifications = listOf(
        Notification(
            id = "notif_1",
            userId = "demo_user_123",
            type = NotificationType.CHALLENGE_RECEIVED,
            title = "New Challenge!",
            message = "Maria challenged you to \"100 Pushups\"",
            actionId = "challenge_1",
            isRead = false,
            createdAt = Timestamp(Date(System.currentTimeMillis() - 300_000))
        ),
        Notification(
            id = "notif_2",
            userId = "demo_user_123",
            type = NotificationType.CHALLENGE_WON,
            title = "Victory! 🎉",
            message = "You won the challenge against Carlos and earned 200₳",
            actionId = "challenge_2",
            isRead = false,
            createdAt = Timestamp(Date(System.currentTimeMillis() - 3_600_000))
        ),
        Notification(
            id = "notif_3",
            userId = "demo_user_123",
            type = NotificationType.FRIEND_REQUEST,
            title = "New Friend Request",
            message = "John wants to be your friend",
            actionId = "user_5",
            isRead = true,
            createdAt = Timestamp(Date(System.currentTimeMillis() - 7_200_000))
        ),
        Notification(
            id = "notif_4",
            userId = "demo_user_123",
            type = NotificationType.ACHIEVEMENT_UNLOCKED,
            title = "Achievement Unlocked!",
            message = "You earned \"First Steps\" - Create your first challenge",
            actionId = "first_steps",
            isRead = true,
            createdAt = Timestamp(Date(System.currentTimeMillis() - 86_400_000))
        ),
        Notification(
            id = "notif_5",
            userId = "demo_user_123",
            type = NotificationType.LEVEL_UP,
            title = "Level Up!",
            message = "Congratulations! You reached Level 5",
            actionId = "",
            isRead = true,
            createdAt = Timestamp(Date(System.currentTimeMillis() - 172_800_000))
        ),
        Notification(
            id = "notif_6",
            userId = "demo_user_123",
            type = NotificationType.CHALLENGE_ACCEPTED,
            title = "Challenge Accepted",
            message = "Ana accepted your challenge \"Study Marathon\"",
            actionId = "challenge_3",
            isRead = true,
            createdAt = Timestamp(Date(System.currentTimeMillis() - 259_200_000))
        )
    )

    /**
     * Get all notifications for a user
     */
    fun getNotifications(userId: String): Flow<List<Notification>> = flow {
        if (notificationsCollection == null) {
            emit(demoNotifications)
            return@flow
        }

        try {
            val snapshot = notificationsCollection
                .whereEqualTo("userId", userId)
                .orderBy("createdAt", Query.Direction.DESCENDING)
                .limit(50)
                .get()
                .await()

            val notifications = snapshot.documents.mapNotNull {
                it.toObject(Notification::class.java)
            }
            emit(notifications.ifEmpty { demoNotifications })
        } catch (e: Exception) {
            emit(demoNotifications)
        }
    }

    /**
     * Get unread notification count
     */
    suspend fun getUnreadCount(userId: String): Int {
        if (notificationsCollection == null) {
            return demoNotifications.count { !it.isRead }
        }

        return try {
            val snapshot = notificationsCollection
                .whereEqualTo("userId", userId)
                .whereEqualTo("isRead", false)
                .get()
                .await()
            snapshot.size()
        } catch (e: Exception) {
            demoNotifications.count { !it.isRead }
        }
    }

    /**
     * Mark a notification as read
     */
    suspend fun markAsRead(notificationId: String): Boolean {
        if (notificationsCollection == null) {
            return true
        }

        return try {
            notificationsCollection.document(notificationId)
                .update("isRead", true)
                .await()
            true
        } catch (e: Exception) {
            true
        }
    }

    /**
     * Mark all notifications as read
     */
    suspend fun markAllAsRead(userId: String): Boolean {
        if (notificationsCollection == null) {
            return true
        }

        return try {
            val snapshot = notificationsCollection
                .whereEqualTo("userId", userId)
                .whereEqualTo("isRead", false)
                .get()
                .await()

            snapshot.documents.forEach { doc ->
                doc.reference.update("isRead", true)
            }
            true
        } catch (e: Exception) {
            true
        }
    }

    /**
     * Create a new notification
     */
    suspend fun createNotification(notification: Notification): String? {
        if (notificationsCollection == null) {
            return "demo_notif_${System.currentTimeMillis()}"
        }

        return try {
            val docRef = notificationsCollection.add(notification).await()
            docRef.id
        } catch (e: Exception) {
            null
        }
    }

    /**
     * Delete a notification
     */
    suspend fun deleteNotification(notificationId: String): Boolean {
        if (notificationsCollection == null) {
            return true
        }

        return try {
            notificationsCollection.document(notificationId).delete().await()
            true
        } catch (e: Exception) {
            false
        }
    }
}
