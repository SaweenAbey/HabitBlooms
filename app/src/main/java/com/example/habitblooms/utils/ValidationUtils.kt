package com.example.habitblooms.utils

import android.widget.EditText
import android.widget.Toast

object ValidationUtils {
    
    // Validate name
    fun validateName(name: String): ValidationResult {
        return when {
            name.trim().isEmpty() -> ValidationResult(false, "Name cannot be empty")
            name.trim().length < 2 -> ValidationResult(false, "Name must be at least 2 characters")
            name.trim().length > 50 -> ValidationResult(false, "Name cannot exceed 50 characters")
            !name.trim().matches(Regex("^[a-zA-Z\\s]+$")) -> ValidationResult(false, "Name can only contain letters and spaces")
            else -> ValidationResult(true, "")
        }
    }
    
    // Validate email
    fun validateEmail(email: String): ValidationResult {
        return when {
            email.trim().isEmpty() -> ValidationResult(false, "Email cannot be empty")
            !android.util.Patterns.EMAIL_ADDRESS.matcher(email.trim()).matches() -> 
                ValidationResult(false, "Please enter a valid email address")
            email.trim().length > 100 -> ValidationResult(false, "Email cannot exceed 100 characters")
            else -> ValidationResult(true, "")
        }
    }
    
    // Validate password
    fun validatePassword(password: String): ValidationResult {
        return when {
            password.isEmpty() -> ValidationResult(false, "Password cannot be empty")
            password.length < 6 -> ValidationResult(false, "Password must be at least 6 characters")
            password.length > 50 -> ValidationResult(false, "Password cannot exceed 50 characters")
            !password.any { it.isDigit() } -> ValidationResult(false, "Password must contain at least one number")
            !password.any { it.isLetter() } -> ValidationResult(false, "Password must contain at least one letter")
            else -> ValidationResult(true, "")
        }
    }
    
    // Validate confirm password
    fun validateConfirmPassword(password: String, confirmPassword: String): ValidationResult {
        return when {
            confirmPassword.isEmpty() -> ValidationResult(false, "Please confirm your password")
            password != confirmPassword -> ValidationResult(false, "Passwords do not match")
            else -> ValidationResult(true, "")
        }
    }
    
    // Validate EditText and show error
    fun validateEditText(editText: EditText, validationResult: ValidationResult): Boolean {
        if (!validationResult.isValid) {
            editText.error = validationResult.errorMessage
            editText.requestFocus()
            return false
        } else {
            editText.error = null
            return true
        }
    }
    
    // Show validation error toast
    fun showValidationError(context: android.content.Context, message: String) {
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
    }
    
    // Validate all signup fields
    fun validateSignupForm(
        name: String,
        email: String,
        password: String,
        confirmPassword: String
    ): ValidationResult {
        val nameValidation = validateName(name)
        if (!nameValidation.isValid) return nameValidation
        
        val emailValidation = validateEmail(email)
        if (!emailValidation.isValid) return emailValidation
        
        val passwordValidation = validatePassword(password)
        if (!passwordValidation.isValid) return passwordValidation
        
        val confirmPasswordValidation = validateConfirmPassword(password, confirmPassword)
        if (!confirmPasswordValidation.isValid) return confirmPasswordValidation
        
        return ValidationResult(true, "")
    }
    
    // Validate login form
    fun validateLoginForm(email: String, password: String): ValidationResult {
        val emailValidation = validateEmail(email)
        if (!emailValidation.isValid) return emailValidation
        
        if (password.isEmpty()) {
            return ValidationResult(false, "Password cannot be empty")
        }
        
        return ValidationResult(true, "")
    }
}

data class ValidationResult(
    val isValid: Boolean,
    val errorMessage: String
)
