package com.example.alea.ui.ranking

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.alea.data.model.User
import com.example.alea.data.repository.AuthRepository
import com.example.alea.data.repository.UserRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.launch
import javax.inject.Inject

data class RankingUiState(
    val isLoading: Boolean = true,
    val topThree: List<User> = emptyList(),
    val restOfRanking: List<User> = emptyList(),
    val isWeekly: Boolean = true,
    val currentUserName: String = "",
    val currentUserRank: Int = 0,
    val currentUserPoints: Long = 0,
    val error: String? = null
)

@HiltViewModel
class RankingViewModel @Inject constructor(
    private val authRepository: AuthRepository,
    private val userRepository: UserRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(RankingUiState())
    val uiState: StateFlow<RankingUiState> = _uiState.asStateFlow()

    init {
        loadRanking()
    }

    fun loadRanking() {
        viewModelScope.launch {
            val isWeekly = _uiState.value.isWeekly
            val leaderboardFlow = if (isWeekly) {
                userRepository.getWeeklyLeaderboard(50)
            } else {
                userRepository.getLeaderboard(50)
            }

            leaderboardFlow
                .catch { e ->
                    _uiState.value = _uiState.value.copy(
                        isLoading = false,
                        error = e.message
                    )
                }
                .collect { users ->
                    val currentUserId = authRepository.currentUserId ?: "demo_user_123"
                    val userIndex = users.indexOfFirst { it.id == currentUserId }
                    val currentUser = if (userIndex >= 0) users[userIndex] else null
                    val pointsValue = if (isWeekly) {
                        currentUser?.wins?.toLong() ?: 0
                    } else {
                        currentUser?.coins ?: 450
                    }

                    _uiState.value = _uiState.value.copy(
                        isLoading = false,
                        topThree = users.take(3),
                        restOfRanking = users.drop(3),
                        currentUserName = currentUser?.username ?: "Tú",
                        currentUserRank = if (userIndex >= 0) userIndex + 1 else users.size + 1,
                        currentUserPoints = pointsValue
                    )
                }
        }
    }

    fun togglePeriod(isWeekly: Boolean) {
        _uiState.value = _uiState.value.copy(isWeekly = isWeekly, isLoading = true)
        loadRanking()
    }
}
