package com.cmt.openctmadminapp.login.domain

import android.util.Patterns
import javax.inject.Inject

class ValidateCredentialsUseCase @Inject constructor() {

    sealed class ValidationResult {
        object Success : ValidationResult()
        data class Error(val message: String) : ValidationResult()
    }

    fun execute(email: String, password: String): ValidationResult {
        if (email.isBlank() || !Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            return ValidationResult.Error("Correo inválido")
        }
        if (password.isBlank()) {
            return ValidationResult.Error("Contraseña requerida")
        }
        return ValidationResult.Success
    }
}