package com.example.taskmate.viewmodel

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

data class LoginUiState(
    val email: String = "",
    val password: String = "",
    val isPasswordVisible: Boolean = false,
    val isLoading: Boolean = false,
    val errorMessage: String? = null
)

class LoginViewModel : ViewModel() {
    private val _uiState = MutableStateFlow(LoginUiState())
    val uiState: StateFlow<LoginUiState> = _uiState.asStateFlow()

    fun onEmailChange(email: String) {
        _uiState.value = _uiState.value.copy(email = email, errorMessage = null)
    }

    fun onPasswordChange(password: String) {
        _uiState.value = _uiState.value.copy(password = password, errorMessage = null)
    }

    fun togglePasswordVisibility() {
        _uiState.value = _uiState.value.copy(isPasswordVisible = !_uiState.value.isPasswordVisible)
    }

    fun login(onSuccess: () -> Unit) {
        val state = _uiState.value

        // Validation
        if (state.email.isBlank()) {
            _uiState.value = state.copy(errorMessage = "Email tidak boleh kosong")
            return
        }
        if (!android.util.Patterns.EMAIL_ADDRESS.matcher(state.email).matches()) {
            _uiState.value = state.copy(errorMessage = "Format email tidak valid")
            return
        }
        if (state.password.isBlank()) {
            _uiState.value = state.copy(errorMessage = "Password tidak boleh kosong")
            return
        }

        // Simulate login
        _uiState.value = state.copy(isLoading = true)
        // In real app, call repository/API here
        onSuccess()
    }

    fun loginWithGoogle(onSuccess: () -> Unit) {
        // Handle Google sign in
        onSuccess()
    }

    fun loginWithApple(onSuccess: () -> Unit) {
        // Handle Apple sign in
        onSuccess()
    }
}

