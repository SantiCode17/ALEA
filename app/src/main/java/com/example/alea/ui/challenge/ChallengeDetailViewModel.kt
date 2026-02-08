package com.example.alea.ui.challenge

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.alea.data.model.Challenge
import com.example.alea.data.model.ChallengeStatus
import com.example.alea.data.repository.AuthRepository
import com.example.alea.data.repository.ChallengeRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

data class ChallengeDetailUiState(
    val isLoading: Boolean = true,
    val challenge: Challenge? = null,
    val isCurrentUserCreator: Boolean = false,
    val isCurrentUserOpponent: Boolean = false,
    val canAccept: Boolean = false,
    val canComplete: Boolean = false,
    val canCancel: Boolean = false,
    val actionSuccess: Boolean = false,
    val error: String? = null
)

@HiltViewModel
class ChallengeDetailViewModel @Inject constructor(
    private val authRepository: AuthRepository,
    private val challengeRepository: ChallengeRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(ChallengeDetailUiState())
    val uiState: StateFlow<ChallengeDetailUiState> = _uiState.asStateFlow()

    private var challengeId: String = ""
    private val currentUserId: String
        get() = authRepository.currentUserId ?: "demo_user_123"

    fun loadChallenge(id: String) {
        challengeId = id
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true)

            val challenge = challengeRepository.getChallenge(id)

            if (challenge != null) {
                val isCreator = challenge.creatorId == currentUserId
                val isOpponent = challenge.opponentId == currentUserId

                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    challenge = challenge,
                    isCurrentUserCreator = isCreator,
                    isCurrentUserOpponent = isOpponent,
                    canAccept = isOpponent && challenge.status == ChallengeStatus.PENDING,
                    canComplete = (isCreator || isOpponent) && challenge.status == ChallengeStatus.ACTIVE,
                    canCancel = isCreator && challenge.status == ChallengeStatus.PENDING
                )
            } else {
                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    error = "Challenge not found"
                )
            }
        }
    }

    fun acceptChallenge() {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true)

            val success = challengeRepository.updateChallengeStatus(challengeId, ChallengeStatus.ACTIVE)

            if (success) {
                _uiState.value = _uiState.value.copy(actionSuccess = true)
                loadChallenge(challengeId)
            } else {
                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    error = "Error al aceptar el reto"
                )
            }
        }
    }

    fun declineChallenge() {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true)

            val success = challengeRepository.updateChallengeStatus(challengeId, ChallengeStatus.CANCELLED)

            if (success) {
                _uiState.value = _uiState.value.copy(actionSuccess = true)
                loadChallenge(challengeId)
            } else {
                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    error = "Error al rechazar el reto"
                )
            }
        }
    }

    fun completeChallenge() {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true)

            val success = challengeRepository.completeChallenge(challengeId, currentUserId)

            if (success) {
                _uiState.value = _uiState.value.copy(actionSuccess = true)
                loadChallenge(challengeId)
            } else {
                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    error = "Error al completar el reto"
                )
            }
        }
    }

    fun cancelChallenge() {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true)

            val success = challengeRepository.updateChallengeStatus(challengeId, ChallengeStatus.CANCELLED)

            if (success) {
                _uiState.value = _uiState.value.copy(actionSuccess = true)
                loadChallenge(challengeId)
            } else {
                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    error = "Error al cancelar el reto"
                )
            }
        }
    }

    fun clearError() {
        _uiState.value = _uiState.value.copy(error = null)
    }

    fun clearActionSuccess() {
        _uiState.value = _uiState.value.copy(actionSuccess = false)
    }
}
