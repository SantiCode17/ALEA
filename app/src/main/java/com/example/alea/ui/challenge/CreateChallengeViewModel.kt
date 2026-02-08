package com.example.alea.ui.challenge

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.alea.data.model.Challenge
import com.example.alea.data.model.ChallengeCategory
import com.example.alea.data.model.ChallengeDifficulty
import com.example.alea.data.model.Friendship
import com.example.alea.data.repository.AuthRepository
import com.example.alea.data.repository.ChallengeRepository
import com.example.alea.data.repository.FriendsRepository
import com.example.alea.data.repository.UserRepository
import com.google.firebase.Timestamp
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.launch
import java.util.Date
import javax.inject.Inject

data class CreateChallengeUiState(
    val currentStep: Int = 0,
    val title: String = "",
    val description: String = "",
    val category: ChallengeCategory = ChallengeCategory.OTHER,
    val difficulty: ChallengeDifficulty = ChallengeDifficulty.MEDIUM,
    val selectedOpponent: Friendship? = null,
    val friends: List<Friendship> = emptyList(),
    val betAmount: Long = 100,
    val deadline: Date = Date(System.currentTimeMillis() + 7 * 24 * 60 * 60 * 1000), // 1 week from now
    val isPublic: Boolean = false,
    val isLoading: Boolean = false,
    val isSuccess: Boolean = false,
    val error: String? = null
) {
    /**
     * Calculate actual stake based on difficulty multiplier
     */
    val actualStake: Long
        get() = (betAmount * difficulty.multiplier).toLong()

    /**
     * Get available categories for selection
     */
    val availableCategories: List<ChallengeCategory>
        get() = ChallengeCategory.entries

    /**
     * Get available difficulties for selection
     */
    val availableDifficulties: List<ChallengeDifficulty>
        get() = ChallengeDifficulty.entries
}

@HiltViewModel
class CreateChallengeViewModel @Inject constructor(
    private val authRepository: AuthRepository,
    private val challengeRepository: ChallengeRepository,
    private val friendsRepository: FriendsRepository,
    private val userRepository: UserRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(CreateChallengeUiState())
    val uiState: StateFlow<CreateChallengeUiState> = _uiState.asStateFlow()

    init {
        loadFriends()
    }

    private fun loadFriends() {
        val userId = authRepository.currentUserId ?: "demo_user_123"

        viewModelScope.launch {
            friendsRepository.getFriends(userId)
                .catch { }
                .collect { friends ->
                    _uiState.value = _uiState.value.copy(friends = friends)
                }
        }
    }

    fun updateTitle(title: String) {
        _uiState.value = _uiState.value.copy(title = title)
    }

    fun updateDescription(description: String) {
        _uiState.value = _uiState.value.copy(description = description)
    }

    fun updateCategory(category: ChallengeCategory) {
        _uiState.value = _uiState.value.copy(category = category)
    }

    fun updateDifficulty(difficulty: ChallengeDifficulty) {
        _uiState.value = _uiState.value.copy(difficulty = difficulty)
    }

    fun togglePublic(isPublic: Boolean) {
        _uiState.value = _uiState.value.copy(isPublic = isPublic)
    }

    fun selectOpponent(opponent: Friendship) {
        _uiState.value = _uiState.value.copy(selectedOpponent = opponent)
    }

    fun updateBetAmount(amount: Long) {
        _uiState.value = _uiState.value.copy(betAmount = amount.coerceIn(10, 5000))
    }

    fun updateDeadline(date: Date) {
        _uiState.value = _uiState.value.copy(deadline = date)
    }

    fun nextStep() {
        val current = _uiState.value.currentStep
        if (current < 2) {
            _uiState.value = _uiState.value.copy(currentStep = current + 1)
        }
    }

    fun previousStep() {
        val current = _uiState.value.currentStep
        if (current > 0) {
            _uiState.value = _uiState.value.copy(currentStep = current - 1)
        }
    }

    fun clearError() {
        _uiState.value = _uiState.value.copy(error = null)
    }

    fun createChallenge() {
        val state = _uiState.value
        val userId = authRepository.currentUserId ?: "demo_user_123"
        val opponent = state.selectedOpponent

        // Validate required fields
        if (state.title.isBlank()) {
            _uiState.value = _uiState.value.copy(error = "Por favor, introduce un título")
            return
        }

        if (opponent == null && !state.isPublic) {
            _uiState.value = _uiState.value.copy(error = "Selecciona un oponente o hazlo público")
            return
        }

        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true)

            val user = userRepository.getUser(userId)

            val challenge = Challenge(
                title = state.title,
                description = state.description,
                category = state.category,
                difficulty = state.difficulty,
                creatorId = userId,
                creatorName = user?.username ?: "",
                creatorAvatarUrl = user?.avatarUrl ?: "",
                opponentId = opponent?.friendId ?: "",
                opponentName = opponent?.friendName ?: "",
                betAmount = state.betAmount,
                isPublic = state.isPublic,
                deadline = Timestamp(state.deadline)
            )

            val result = challengeRepository.createChallenge(challenge)

            if (result != null) {
                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    isSuccess = true
                )
            } else {
                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    error = "Error al crear el reto"
                )
            }
        }
    }
}
