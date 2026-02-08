package com.example.alea.ui.settings

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.alea.data.SessionManager
import com.example.alea.data.repository.AuthRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import javax.inject.Inject

data class SettingsUiState(
    val email: String = "",
    val themeColor: Int = 0,
    val isLoggedOut: Boolean = false
)

@HiltViewModel
class SettingsViewModel @Inject constructor(
    private val authRepository: AuthRepository,
    private val sessionManager: SessionManager
) : ViewModel() {

    private val _uiState = MutableStateFlow(SettingsUiState())
    val uiState: StateFlow<SettingsUiState> = _uiState.asStateFlow()

    init {
        loadSettings()
    }

    private fun loadSettings() {
        viewModelScope.launch {
            val user = authRepository.currentUser
            val savedColor = sessionManager.getThemeColorSync()
            _uiState.value = _uiState.value.copy(
                email = user?.email ?: "player@alea.com",
                themeColor = savedColor
            )
        }
    }

    fun setThemeColor(color: Int) {
        viewModelScope.launch {
            sessionManager.setThemeColor(color)
            _uiState.value = _uiState.value.copy(themeColor = color)
        }
    }

    fun logout() {
        viewModelScope.launch {
            authRepository.signOut()
            _uiState.value = _uiState.value.copy(isLoggedOut = true)
        }
    }
}
