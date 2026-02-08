package com.example.alea.ui.profile

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.alea.data.model.Achievement
import com.example.alea.data.model.Achievements
import com.example.alea.data.model.Challenge
import com.example.alea.data.model.User
import com.example.alea.data.repository.AuthRepository
import com.example.alea.data.repository.ChallengeRepository
import com.example.alea.data.repository.UserRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.launch
import javax.inject.Inject

data class ProfileUiState(
    val isLoading: Boolean = true,
    val user: User? = null,
    val challenges: List<Challenge> = emptyList(),
    val unlockedAchievements: List<Achievement> = emptyList(),
    val showCompleted: Boolean = true,
    val error: String? = null
)

@HiltViewModel
class ProfileViewModel @Inject constructor(
    private val authRepository: AuthRepository,
    private val userRepository: UserRepository,
    private val challengeRepository: ChallengeRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(ProfileUiState())
    val uiState: StateFlow<ProfileUiState> = _uiState.asStateFlow()

    init {
        loadProfile()
    }

    private fun loadProfile() {
        val userId = authRepository.currentUserId ?: "demo_user_123"

        viewModelScope.launch {
            try {
                // Load user data
                val user = userRepository.getUserFlow(userId).firstOrNull()

                // Load user's unlocked achievements
                val userAchievements = user?.achievements?.mapNotNull { achievementId ->
                    Achievements.getAllAchievements().find { it.id == achievementId }
                } ?: emptyList()

                // Load user's challenges separately
                val challenges = try {
                    challengeRepository.getUserChallenges(userId).firstOrNull() ?: emptyList()
                } catch (_: Exception) {
                    emptyList()
                }

                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    user = user,
                    challenges = challenges,
                    unlockedAchievements = userAchievements
                )
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    error = e.message
                )
            }
        }
    }

    fun toggleFilter(showCompleted: Boolean) {
        _uiState.value = _uiState.value.copy(showCompleted = showCompleted)
    }

    fun refreshProfile() {
        _uiState.value = _uiState.value.copy(isLoading = true)
        loadProfile()
    }
}
