package com.example.alea.data.repository

import com.example.alea.data.model.Friendship
import com.example.alea.data.model.FriendshipStatus
import com.example.alea.data.model.User
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class FriendsRepository @Inject constructor(
    private val firestore: FirebaseFirestore?
) {
    private val friendshipsCollection = firestore?.collection("friendships")

    // Demo friendships
    private val demoFriends = listOf(
        Friendship(id = "f1", userId = "demo_user_123", friendId = "user_2", friendName = "Champion", status = FriendshipStatus.ACCEPTED),
        Friendship(id = "f2", userId = "demo_user_123", friendId = "user_3", friendName = "Warrior", status = FriendshipStatus.ACCEPTED),
        Friendship(id = "f3", userId = "demo_user_123", friendId = "user_4", friendName = "Hunter", status = FriendshipStatus.ACCEPTED),
        Friendship(id = "f4", userId = "demo_user_123", friendId = "user_5", friendName = "Rookie", status = FriendshipStatus.ACCEPTED)
    )

    fun getFriends(userId: String): Flow<List<Friendship>> = flow {
        try {
            emit(demoFriends)
        } catch (e: Exception) {
            emit(demoFriends)
        }
    }

    suspend fun addFriend(userId: String, friend: User): Boolean {
        return true // Demo mode
    }

    suspend fun acceptFriendRequest(friendshipId: String): Boolean {
        return true // Demo mode
    }

    suspend fun removeFriend(friendshipId: String): Boolean {
        return true // Demo mode
    }

    fun getPendingRequests(userId: String): Flow<List<Friendship>> = flow {
        emit(emptyList()) // Demo mode - no pending requests
    }
}
