package com.example.alea.ui.chat

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.alea.data.model.Message
import com.example.alea.data.repository.AuthRepository
import com.example.alea.data.repository.ChatRepository
import com.example.alea.data.repository.UserRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.launch
import javax.inject.Inject

data class ChatUiState(
    val messages: List<Message> = emptyList(),
    val currentUserId: String = "",
    val friendName: String = "",
    val isLoading: Boolean = true,
    val error: String? = null
)

@HiltViewModel
class ChatViewModel @Inject constructor(
    private val authRepository: AuthRepository,
    private val chatRepository: ChatRepository,
    private val userRepository: UserRepository,
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val friendId: String = savedStateHandle.get<String>("friendId") ?: ""
    private val friendName: String = savedStateHandle.get<String>("friendName") ?: ""

    private val _uiState = MutableStateFlow(ChatUiState())
    val uiState: StateFlow<ChatUiState> = _uiState.asStateFlow()

    private val chatId: String

    init {
        val currentUserId = authRepository.currentUserId ?: "demo_user_123"
        chatId = chatRepository.getChatId(currentUserId, friendId)

        _uiState.value = _uiState.value.copy(
            currentUserId = currentUserId,
            friendName = friendName
        )

        loadMessages()
    }

    private fun loadMessages() {
        viewModelScope.launch {
            chatRepository.getChatMessages(chatId)
                .catch { e ->
                    _uiState.value = _uiState.value.copy(
                        isLoading = false,
                        error = e.message
                    )
                }
                .collect { messages ->
                    _uiState.value = _uiState.value.copy(
                        isLoading = false,
                        messages = messages
                    )
                }
        }
    }

    fun sendMessage(text: String) {
        if (text.isBlank()) return

        val currentUserId = _uiState.value.currentUserId.ifEmpty { "demo_user_123" }

        viewModelScope.launch {
            val user = userRepository.getUser(currentUserId)

            val message = Message(
                chatId = chatId,
                senderId = currentUserId,
                senderName = user?.username ?: "Tú",
                text = text.trim()
            )

            // Add message to local list immediately for instant feedback
            val currentMessages = _uiState.value.messages.toMutableList()
            currentMessages.add(message)
            _uiState.value = _uiState.value.copy(messages = currentMessages)

            try {
                chatRepository.sendMessage(message)
            } catch (_: Exception) {
                // Message already shown locally, silently handle remote failure
            }
        }
    }
}
