package com.example.habitblooms.data

import java.util.*

data class User(
    val id: String = UUID.randomUUID().toString(),
    val name: String,
    val email: String,
    val password: String,
    val createdAt: Long = System.currentTimeMillis(),
    val isLoggedIn: Boolean = false
) {
    // Helper function to validate email format
    fun isValidEmail(): Boolean {
        return android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()
    }
    
    // Helper function to validate password strength
    fun isValidPassword(): Boolean {
        return password.length >= 6
    }
    
    // Helper function to validate name
    fun isValidName(): Boolean {
        return name.trim().length >= 2
    }
}
