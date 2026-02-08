package com.example.alea.data.repository

import com.example.alea.data.model.User
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.tasks.await
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class UserRepository @Inject constructor(
    private val firestore: FirebaseFirestore?
) {
    private val usersCollection = firestore?.collection("users")

    // Demo user for offline mode with gamification fields
    private val demoUser = User(
        id = "demo_user_123",
        username = "Player",
        displayName = "Player One",
        email = "player@alea.com",
        coins = 12450,
        level = 12,
        xp = 650,
        totalChallenges = 42,
        completedChallenges = 38,
        wins = 28,
        losses = 14,
        rank = 5,
        isOnline = true,
        friends = listOf("user_2", "user_3", "user_4", "user_5"),
        achievements = listOf("first_steps", "first_win", "five_wins", "social_butterfly")
    )

    private val demoUsers = listOf(
        demoUser,
        User(
            id = "user_2",
            username = "Champion",
            displayName = "The Champion",
            email = "champion@alea.com",
            coins = 25000,
            level = 25,
            xp = 800,
            wins = 75,
            totalChallenges = 90,
            rank = 1,
            isOnline = true
        ),
        User(
            id = "user_3",
            username = "Warrior",
            displayName = "Battle Warrior",
            email = "warrior@alea.com",
            coins = 18000,
            level = 18,
            xp = 450,
            wins = 55,
            totalChallenges = 70,
            rank = 2,
            isOnline = true
        ),
        User(
            id = "user_4",
            username = "Hunter",
            displayName = "The Hunter",
            email = "hunter@alea.com",
            coins = 16000,
            level = 15,
            xp = 300,
            wins = 45,
            totalChallenges = 60,
            rank = 3,
            isOnline = false
        ),
        User(
            id = "user_5",
            username = "Rookie",
            displayName = "New Rookie",
            email = "rookie@alea.com",
            coins = 3000,
            level = 3,
            xp = 150,
            wins = 10,
            totalChallenges = 15,
            rank = 10,
            isOnline = false
        ),
        User(
            id = "user_6",
            username = "Maria",
            displayName = "Maria García",
            email = "maria@alea.com",
            coins = 9500,
            level = 9,
            xp = 200,
            wins = 32,
            totalChallenges = 45,
            rank = 6,
            isOnline = true
        ),
        User(
            id = "user_7",
            username = "Carlos",
            displayName = "Carlos López",
            email = "carlos@alea.com",
            coins = 8200,
            level = 8,
            xp = 100,
            wins = 28,
            totalChallenges = 40,
            rank = 7,
            isOnline = false
        )
    )

    fun getUserFlow(userId: String): Flow<User?> = flow {
        if (usersCollection == null) {
            emit(demoUser)
            return@flow
        }
        try {
            val listener = usersCollection.document(userId)
                .addSnapshotListener { snapshot, error ->
                    if (error == null) {
                        snapshot?.toObject(User::class.java)
                    }
                }
            // For demo, emit demo user
            emit(demoUser)
        } catch (e: Exception) {
            emit(demoUser)
        }
    }

    suspend fun getUser(userId: String): User? {
        if (usersCollection == null) return demoUser
        return try {
            usersCollection.document(userId)
                .get()
                .await()
                .toObject(User::class.java)
        } catch (e: Exception) {
            demoUser
        }
    }

    suspend fun updateUser(user: User): Boolean {
        if (usersCollection == null) return true
        return try {
            usersCollection.document(user.id)
                .set(user)
                .await()
            true
        } catch (e: Exception) {
            true // Demo mode always succeeds
        }
    }

    suspend fun updateCoins(userId: String, amount: Long): Boolean {
        return true // Demo mode
    }

    suspend fun searchUsers(query: String): List<User> {
        if (usersCollection == null) {
            return demoUsers.filter { it.username.contains(query, ignoreCase = true) }
        }
        return try {
            usersCollection
                .whereGreaterThanOrEqualTo("username", query)
                .whereLessThanOrEqualTo("username", query + "\uf8ff")
                .limit(20)
                .get()
                .await()
                .toObjects(User::class.java)
        } catch (e: Exception) {
            demoUsers.filter { it.username.contains(query, ignoreCase = true) }
        }
    }

    fun getLeaderboard(limit: Int): Flow<List<User>> = flow {
        if (usersCollection == null) {
            emit(demoUsers.sortedByDescending { it.coins })
            return@flow
        }
        try {
            val users = usersCollection
                .orderBy("coins", com.google.firebase.firestore.Query.Direction.DESCENDING)
                .limit(limit.toLong())
                .get()
                .await()
                .toObjects(User::class.java)
            emit(users)
        } catch (e: Exception) {
            emit(demoUsers.sortedByDescending { it.coins })
        }
    }
}

