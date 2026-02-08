package com.example.alea.ui.friends

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.alea.data.model.Friendship
import com.example.alea.data.model.User
import com.example.alea.data.repository.AuthRepository
import com.example.alea.data.repository.FriendsRepository
import com.example.alea.data.repository.UserRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.launch
import javax.inject.Inject

data class FriendsUiState(
    val isLoading: Boolean = true,
    val friends: List<Friendship> = emptyList(),
    val searchResults: List<User> = emptyList(),
    val error: String? = null
)

@HiltViewModel
class FriendsViewModel @Inject constructor(
    private val authRepository: AuthRepository,
    private val friendsRepository: FriendsRepository,
    private val userRepository: UserRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(FriendsUiState())
    val uiState: StateFlow<FriendsUiState> = _uiState.asStateFlow()

    init {
        loadFriends()
    }

    private fun loadFriends() {
        val userId = authRepository.currentUserId ?: "demo_user_123"

        viewModelScope.launch {
            friendsRepository.getFriends(userId)
                .catch { e ->
                    _uiState.value = _uiState.value.copy(
                        isLoading = false,
                        error = e.message
                    )
                }
                .collect { friends ->
                    _uiState.value = _uiState.value.copy(
                        isLoading = false,
                        friends = friends
                    )
                }
        }
    }

    fun searchUsers(query: String) {
        if (query.length < 2) {
            _uiState.value = _uiState.value.copy(searchResults = emptyList())
            return
        }

        viewModelScope.launch {
            val results = userRepository.searchUsers(query)
            _uiState.value = _uiState.value.copy(searchResults = results)
        }
    }

    fun addFriend(user: User) {
        val userId = authRepository.currentUserId ?: "demo_user_123"

        viewModelScope.launch {
            friendsRepository.addFriend(userId, user)
            _uiState.value = _uiState.value.copy(searchResults = emptyList())
        }
    }
}
