package com.example.alea.ui.notifications

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.alea.data.model.Notification
import com.example.alea.data.repository.AuthRepository
import com.example.alea.data.repository.NotificationRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.launch
import javax.inject.Inject

data class NotificationsUiState(
    val notifications: List<Notification> = emptyList(),
    val unreadCount: Int = 0,
    val isLoading: Boolean = false,
    val error: String? = null
)

@HiltViewModel
class NotificationsViewModel @Inject constructor(
    private val authRepository: AuthRepository,
    private val notificationRepository: NotificationRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(NotificationsUiState())
    val uiState: StateFlow<NotificationsUiState> = _uiState.asStateFlow()

    init {
        loadNotifications()
    }

    private fun loadNotifications() {
        val userId = authRepository.currentUserId ?: "demo_user_123"

        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true)

            notificationRepository.getNotifications(userId)
                .catch { e ->
                    _uiState.value = _uiState.value.copy(
                        isLoading = false,
                        error = e.message
                    )
                }
                .collect { notifications ->
                    val unreadCount = notifications.count { !it.isRead }
                    _uiState.value = _uiState.value.copy(
                        notifications = notifications,
                        unreadCount = unreadCount,
                        isLoading = false
                    )
                }
        }
    }

    fun markAsRead(notificationId: String) {
        viewModelScope.launch {
            notificationRepository.markAsRead(notificationId)

            // Update local state
            val updatedNotifications = _uiState.value.notifications.map {
                if (it.id == notificationId) it.copy(isRead = true) else it
            }
            _uiState.value = _uiState.value.copy(
                notifications = updatedNotifications,
                unreadCount = updatedNotifications.count { !it.isRead }
            )
        }
    }

    fun markAllAsRead() {
        val userId = authRepository.currentUserId ?: "demo_user_123"

        viewModelScope.launch {
            notificationRepository.markAllAsRead(userId)

            // Update local state
            val updatedNotifications = _uiState.value.notifications.map {
                it.copy(isRead = true)
            }
            _uiState.value = _uiState.value.copy(
                notifications = updatedNotifications,
                unreadCount = 0
            )
        }
    }

    fun deleteNotification(notificationId: String) {
        viewModelScope.launch {
            notificationRepository.deleteNotification(notificationId)

            // Update local state
            val updatedNotifications = _uiState.value.notifications.filter {
                it.id != notificationId
            }
            _uiState.value = _uiState.value.copy(
                notifications = updatedNotifications,
                unreadCount = updatedNotifications.count { !it.isRead }
            )
        }
    }

    fun refresh() {
        loadNotifications()
    }
}
