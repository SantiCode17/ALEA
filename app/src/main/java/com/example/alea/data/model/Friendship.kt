package com.example.alea.data.model

import com.google.firebase.Timestamp
import com.google.firebase.firestore.DocumentId
import com.google.firebase.firestore.ServerTimestamp

enum class FriendshipStatus {
    PENDING,
    ACCEPTED,
    REJECTED
}

data class Friendship(
    @DocumentId
    val id: String = "",
    val userId: String = "",
    val friendId: String = "",
    val friendName: String = "",
    val friendAvatarUrl: String = "",
    val status: FriendshipStatus = FriendshipStatus.PENDING,
    @ServerTimestamp
    val createdAt: Timestamp? = null
)
