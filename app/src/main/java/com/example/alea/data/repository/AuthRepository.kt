package com.example.alea.data.repository

import android.util.Log
import com.example.alea.data.SessionManager
import com.example.alea.data.model.User
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.delay
import kotlinx.coroutines.tasks.await
import javax.inject.Inject
import javax.inject.Singleton

sealed class AuthResult {
    data class Success(val userId: String) : AuthResult()
    data class Error(val message: String) : AuthResult()
}

@Singleton
class AuthRepository @Inject constructor(
    private val auth: FirebaseAuth?,
    private val firestore: FirebaseFirestore?,
    private val sessionManager: SessionManager
) {
    companion object {
        private const val TAG = "AuthRepository"
        private const val DEMO_USER_ID = "demo_user_123"
    }

    private val _authState = MutableStateFlow<String?>(null)

    init {
        if (auth != null) {
            try {
                // Initialize auth state from Firebase current user
                auth.currentUser?.uid?.let {
                    _authState.value = it
                }

                // Listen for auth state changes
                auth.addAuthStateListener { firebaseAuth ->
                    _authState.value = firebaseAuth.currentUser?.uid
                }
            } catch (e: Exception) {
                Log.w(TAG, "Firebase not properly initialized, running in demo mode", e)
            }
        } else {
            Log.i(TAG, "Firebase not available, running in demo mode")
        }
    }

    val currentUser: FirebaseUser?
        get() = try {
            auth?.currentUser
        } catch (e: Exception) {
            null
        }

    val currentUserId: String?
        get() = currentUser?.uid

    val isLoggedIn: Boolean
        get() = currentUser != null

    /**
     * Returns a Flow that emits the authentication state.
     */
    fun authStateFlow(): Flow<String?> = _authState.asStateFlow()

    /**
     * Check if user is logged in using persisted session.
     * This handles both Firebase auth and demo mode.
     */
    suspend fun checkIsLoggedIn(): Boolean {
        // First check persisted session (works for demo mode)
        if (sessionManager.isLoggedInSync()) {
            return true
        }

        // Then check Firebase (for real auth)
        return try {
            auth?.currentUser != null
        } catch (e: Exception) {
            false
        }
    }

    /**
     * Get the current user ID from session or Firebase.
     */
    suspend fun getCurrentUserIdAsync(): String? {
        return sessionManager.getUserIdSync() ?: currentUser?.uid
    }

    suspend fun signIn(email: String, password: String): AuthResult {
        // If Firebase is not available, use demo mode directly
        if (auth == null) {
            delay(500) // Simulate network delay
            sessionManager.saveSession(DEMO_USER_ID, isDemoMode = true)
            _authState.value = DEMO_USER_ID
            return AuthResult.Success(DEMO_USER_ID)
        }

        return try {
            val result = auth.signInWithEmailAndPassword(email, password).await()
            result.user?.let {
                sessionManager.saveSession(it.uid, isDemoMode = false)
                _authState.value = it.uid
                AuthResult.Success(it.uid)
            } ?: AuthResult.Error("Login failed")
        } catch (e: Exception) {
            Log.w(TAG, "Firebase login failed, using demo mode", e)
            delay(500)
            sessionManager.saveSession(DEMO_USER_ID, isDemoMode = true)
            _authState.value = DEMO_USER_ID
            AuthResult.Success(DEMO_USER_ID)
        }
    }

    suspend fun signUp(username: String, email: String, password: String): AuthResult {
        // If Firebase is not available, use demo mode directly
        if (auth == null || firestore == null) {
            delay(500)
            sessionManager.saveSession(DEMO_USER_ID, isDemoMode = true)
            _authState.value = DEMO_USER_ID
            return AuthResult.Success(DEMO_USER_ID)
        }

        return try {
            val result = auth.createUserWithEmailAndPassword(email, password).await()
            result.user?.let { firebaseUser ->
                val user = User(
                    id = firebaseUser.uid,
                    username = username,
                    email = email,
                    coins = 1000
                )
                firestore.collection("users")
                    .document(firebaseUser.uid)
                    .set(user)
                    .await()
                sessionManager.saveSession(firebaseUser.uid, isDemoMode = false)
                _authState.value = firebaseUser.uid
                AuthResult.Success(firebaseUser.uid)
            } ?: AuthResult.Error("Registration failed")
        } catch (e: Exception) {
            Log.w(TAG, "Firebase registration failed, using demo mode", e)
            delay(500)
            sessionManager.saveSession(DEMO_USER_ID, isDemoMode = true)
            _authState.value = DEMO_USER_ID
            AuthResult.Success(DEMO_USER_ID)
        }
    }

    suspend fun signOut() {
        try {
            auth?.signOut()
        } catch (e: Exception) {
            Log.w(TAG, "Firebase signOut failed", e)
        }
        sessionManager.clearSession()
        _authState.value = null
    }
}
