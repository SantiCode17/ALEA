package com.example.alea.ui.auth

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.alea.data.repository.AuthRepository
import com.example.alea.data.repository.AuthResult
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

sealed class AuthUiState {
    object Idle : AuthUiState()
    object Loading : AuthUiState()
    object Success : AuthUiState()
    data class Error(val message: String) : AuthUiState()
}

@HiltViewModel
class AuthViewModel @Inject constructor(
    private val authRepository: AuthRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow<AuthUiState>(AuthUiState.Idle)
    val uiState: StateFlow<AuthUiState> = _uiState.asStateFlow()

    fun signIn(email: String, password: String) {
        if (!validateEmail(email)) {
            _uiState.value = AuthUiState.Error("Introduce un correo electrónico válido")
            return
        }
        if (password.length < 6) {
            _uiState.value = AuthUiState.Error("La contraseña debe tener al menos 6 caracteres")
            return
        }

        viewModelScope.launch {
            _uiState.value = AuthUiState.Loading
            when (val result = authRepository.signIn(email, password)) {
                is AuthResult.Success -> _uiState.value = AuthUiState.Success
                is AuthResult.Error -> _uiState.value = AuthUiState.Error(result.message)
            }
        }
    }

    fun signUp(username: String, email: String, password: String, confirmPassword: String) {
        if (username.length < 3) {
            _uiState.value = AuthUiState.Error("El nombre de usuario debe tener al menos 3 caracteres")
            return
        }
        if (!validateEmail(email)) {
            _uiState.value = AuthUiState.Error("Introduce un correo electrónico válido")
            return
        }
        if (password.length < 6) {
            _uiState.value = AuthUiState.Error("La contraseña debe tener al menos 6 caracteres")
            return
        }
        if (password != confirmPassword) {
            _uiState.value = AuthUiState.Error("Las contraseñas no coinciden")
            return
        }

        viewModelScope.launch {
            _uiState.value = AuthUiState.Loading
            when (val result = authRepository.signUp(username, email, password)) {
                is AuthResult.Success -> _uiState.value = AuthUiState.Success
                is AuthResult.Error -> _uiState.value = AuthUiState.Error(result.message)
            }
        }
    }

    fun resetState() {
        _uiState.value = AuthUiState.Idle
    }

    private fun validateEmail(email: String): Boolean {
        return android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()
    }
}
