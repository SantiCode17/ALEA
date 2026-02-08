package com.example.alea.ui.onboarding

import androidx.lifecycle.ViewModel
import com.example.alea.data.repository.AuthRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class OnboardingViewModel @Inject constructor(
    private val authRepository: AuthRepository
) : ViewModel() {

    /**
     * Checks if the user is logged in.
     * Uses the async method to properly wait for Firebase to restore session.
     */
    suspend fun isLoggedIn(): Boolean = authRepository.checkIsLoggedIn()
}
