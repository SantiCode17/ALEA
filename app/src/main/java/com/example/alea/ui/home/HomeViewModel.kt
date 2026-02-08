package com.example.alea.ui.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.alea.data.model.Challenge
import com.example.alea.data.model.User
import com.example.alea.data.repository.AuthRepository
import com.example.alea.data.repository.ChallengeRepository
import com.example.alea.data.repository.UserRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.launch
import javax.inject.Inject

data class HomeUiState(
    val isLoading: Boolean = true,
    val user: User? = null,
    val challenges: List<Challenge> = emptyList(),
    val error: String? = null
)

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val authRepository: AuthRepository,
    private val userRepository: UserRepository,
    private val challengeRepository: ChallengeRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(HomeUiState())
    val uiState: StateFlow<HomeUiState> = _uiState.asStateFlow()

    init {
        loadUserData()
        loadChallenges()
    }

    private fun loadUserData() {
        viewModelScope.launch {
            val userId = authRepository.getCurrentUserIdAsync() ?: "demo_user_123"

            userRepository.getUserFlow(userId)
                .catch { e ->
                    _uiState.value = _uiState.value.copy(error = e.message)
                }
                .collect { user ->
                    _uiState.value = _uiState.value.copy(
                        user = user,
                        isLoading = false
                    )
                }
        }
    }

    private fun loadChallenges() {
        viewModelScope.launch {
            val userId = authRepository.getCurrentUserIdAsync() ?: "demo_user_123"

            challengeRepository.getActiveChallenges(userId)
                .catch { e ->
                    _uiState.value = _uiState.value.copy(error = e.message)
                }
                .collect { challenges ->
                    _uiState.value = _uiState.value.copy(challenges = challenges)
                }
        }
    }

    fun refresh() {
        _uiState.value = _uiState.value.copy(isLoading = true)
        loadUserData()
        loadChallenges()
    }
}
