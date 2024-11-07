package com.cmt.openctmadminapp.login.ui.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.cmt.openctmadminapp.core.di.TokenProvider
import com.cmt.openctmadminapp.login.data.LoginRepository
import com.cmt.openctmadminapp.login.data.network.response.AuthResponse
import com.cmt.openctmadminapp.login.domain.ValidateCredentialsUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val repository: LoginRepository,
    private val validateCredentialsUseCase: ValidateCredentialsUseCase,
    private val tokenProvider: TokenProvider
) : ViewModel() {

    private val _loginState = MutableStateFlow<LoginState>(LoginState.Idle)
    val loginState: StateFlow<LoginState> = _loginState

    fun login(usuario: String, contrasenia: String) {
        when (val validationResult = validateCredentialsUseCase.execute(usuario, contrasenia)) {
            is ValidateCredentialsUseCase.ValidationResult.Error -> {
                _loginState.value = LoginState.ValidationError(validationResult.message)
            }

            ValidateCredentialsUseCase.ValidationResult.Success -> {
                performLogin(usuario, contrasenia)
            }
        }
    }

    private fun performLogin(usuario: String, contrasenia: String) {
        viewModelScope.launch {
            _loginState.value = LoginState.Loading
            try {
                val response = repository.login(usuario, contrasenia)
                Log.d("AuthToken", "Token recibido")
                tokenProvider.saveToken(response.token)
                _loginState.value = LoginState.Success(response)
            } catch (e: Exception) {
                _loginState.value = LoginState.Error("Error inesperado")
                Log.e("AuthError", "Error inesperado durante la autenticación", e)
            }
        }
    }

}

sealed class LoginState {
    object Idle : LoginState()
    object Loading : LoginState()
    data class Success(val response: AuthResponse) : LoginState()
    data class ValidationError(val message: String) : LoginState()
    data class Error(val message: String) : LoginState()
}