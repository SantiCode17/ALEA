package com.example.alea.di

import android.util.Log
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object FirebaseModule {

    private const val TAG = "FirebaseModule"

    @Volatile
    private var firebaseChecked = false

    @Volatile
    private var firebaseAvailable = false

    private fun checkFirebaseAvailability(): Boolean {
        if (firebaseChecked) return firebaseAvailable

        synchronized(this) {
            if (firebaseChecked) return firebaseAvailable

            firebaseAvailable = try {
                // Try to access Firebase to see if it's properly configured
                val auth = FirebaseAuth.getInstance()
                // Try to access a property to ensure it's really initialized
                auth.currentUser
                true
            } catch (e: Exception) {
                Log.w(TAG, "Firebase not configured (missing google-services.json). Running in demo mode.", e)
                false
            } catch (e: Error) {
                Log.w(TAG, "Firebase initialization error. Running in demo mode.", e)
                false
            }
            firebaseChecked = true
        }
        return firebaseAvailable
    }

    @Provides
    @Singleton
    fun provideFirebaseAuth(): FirebaseAuth? {
        if (!checkFirebaseAvailability()) return null
        return try {
            FirebaseAuth.getInstance()
        } catch (e: Exception) {
            Log.e(TAG, "Failed to get FirebaseAuth instance", e)
            null
        }
    }

    @Provides
    @Singleton
    fun provideFirebaseFirestore(): FirebaseFirestore? {
        if (!checkFirebaseAvailability()) return null
        return try {
            FirebaseFirestore.getInstance()
        } catch (e: Exception) {
            Log.e(TAG, "Failed to get Firestore instance", e)
            null
        }
    }

    @Provides
    @Singleton
    fun provideFirebaseStorage(): FirebaseStorage? {
        if (!checkFirebaseAvailability()) return null
        return try {
            FirebaseStorage.getInstance()
        } catch (e: Exception) {
            Log.e(TAG, "Failed to get Storage instance", e)
            null
        }
    }
}
