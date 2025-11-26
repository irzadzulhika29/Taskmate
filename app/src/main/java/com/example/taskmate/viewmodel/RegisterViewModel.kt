package com.example.taskmate.viewmodel

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

data class RegisterUiState(
    val email: String = "",
    val password: String = "",
    val confirmPassword: String = "",
    val isPasswordVisible: Boolean = false,
    val isConfirmPasswordVisible: Boolean = false,
    val isLoading: Boolean = false,
    val errorMessage: String? = null
)

class RegisterViewModel : ViewModel() {
    private val _uiState = MutableStateFlow(RegisterUiState())
    val uiState: StateFlow<RegisterUiState> = _uiState.asStateFlow()

    fun onEmailChange(email: String) {
        _uiState.value = _uiState.value.copy(email = email, errorMessage = null)
    }

    fun onPasswordChange(password: String) {
        _uiState.value = _uiState.value.copy(password = password, errorMessage = null)
    }

    fun onConfirmPasswordChange(confirmPassword: String) {
        _uiState.value = _uiState.value.copy(confirmPassword = confirmPassword, errorMessage = null)
    }

    fun togglePasswordVisibility() {
        _uiState.value = _uiState.value.copy(isPasswordVisible = !_uiState.value.isPasswordVisible)
    }

    fun toggleConfirmPasswordVisibility() {
        _uiState.value = _uiState.value.copy(isConfirmPasswordVisible = !_uiState.value.isConfirmPasswordVisible)
    }

    fun register(onSuccess: () -> Unit) {
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
        if (state.password.length < 6) {
            _uiState.value = state.copy(errorMessage = "Password minimal 6 karakter")
            return
        }
        if (state.password != state.confirmPassword) {
            _uiState.value = state.copy(errorMessage = "Password tidak sama")
            return
        }

        // Simulate registration
        _uiState.value = state.copy(isLoading = true)
        // In real app, call repository/API here
        onSuccess()
    }

    fun registerWithGoogle(onSuccess: () -> Unit) {
        // Handle Google sign in
        onSuccess()
    }

    fun registerWithApple(onSuccess: () -> Unit) {
        // Handle Apple sign in
        onSuccess()
    }
}

