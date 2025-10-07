package com.example.habitblooms.data

import android.content.Context
import android.content.SharedPreferences
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class UserRepository private constructor(context: Context) {
    
    private val sharedPreferences: SharedPreferences = 
        context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
    private val gson = Gson()
    
    companion object {
        private const val PREFS_NAME = "user_prefs"
        private const val KEY_USERS = "users"
        private const val KEY_CURRENT_USER = "current_user"
        private const val KEY_IS_LOGGED_IN = "is_logged_in"
        
        @Volatile
        private var INSTANCE: UserRepository? = null
        
        fun getInstance(context: Context): UserRepository {
            return INSTANCE ?: synchronized(this) {
                INSTANCE ?: UserRepository(context.applicationContext).also { INSTANCE = it }
            }
        }
    }
    
    // Register a new user
    fun registerUser(name: String, email: String, password: String): Result<User> {
        return try {
            // Validate input
            if (name.trim().isEmpty()) {
                return Result.failure(Exception("Name cannot be empty"))
            }
            if (name.trim().length < 2) {
                return Result.failure(Exception("Name must be at least 2 characters"))
            }
            if (!android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                return Result.failure(Exception("Please enter a valid email address"))
            }
            if (password.length < 6) {
                return Result.failure(Exception("Password must be at least 6 characters"))
            }
            
            // Check if user already exists
            val existingUsers = getAllUsers()
            if (existingUsers.any { it.email.equals(email, ignoreCase = true) }) {
                return Result.failure(Exception("User with this email already exists"))
            }
            
            // Create new user
            val newUser = User(
                name = name.trim(),
                email = email.trim().lowercase(),
                password = password
            )
            
            // Save user
            val updatedUsers = existingUsers.toMutableList()
            updatedUsers.add(newUser)
            saveUsers(updatedUsers)
            
            Result.success(newUser)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
    
    // Login user
    fun loginUser(email: String, password: String): Result<User> {
        return try {
            // Validate input
            if (email.trim().isEmpty()) {
                return Result.failure(Exception("Email cannot be empty"))
            }
            if (password.isEmpty()) {
                return Result.failure(Exception("Password cannot be empty"))
            }
            
            // Find user
            val users = getAllUsers()
            val user = users.find { 
                it.email.equals(email.trim().lowercase(), ignoreCase = true) && 
                it.password == password 
            }
            
            if (user == null) {
                return Result.failure(Exception("Invalid email or password"))
            }
            
            // Update login status
            val updatedUser = user.copy(isLoggedIn = true)
            updateUser(updatedUser)
            
            // Save current user
            saveCurrentUser(updatedUser)
            setLoggedInStatus(true)
            
            Result.success(updatedUser)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
    
    // Logout user
    fun logoutUser(): Result<Unit> {
        return try {
            val currentUser = getCurrentUser()
            if (currentUser != null) {
                val updatedUser = currentUser.copy(isLoggedIn = false)
                updateUser(updatedUser)
            }
            
            clearCurrentUser()
            setLoggedInStatus(false)
            
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
    
    // Get current logged-in user
    fun getCurrentUser(): User? {
        val json = sharedPreferences.getString(KEY_CURRENT_USER, null)
        return if (json != null) {
            gson.fromJson(json, User::class.java)
        } else null
    }
    
    // Check if user is logged in
    fun isUserLoggedIn(): Boolean {
        return sharedPreferences.getBoolean(KEY_IS_LOGGED_IN, false)
    }
    
    // Get all users (for debugging/admin purposes)
    private fun getAllUsers(): List<User> {
        val json = sharedPreferences.getString(KEY_USERS, null)
        return if (json != null) {
            val type = object : TypeToken<List<User>>() {}.type
            gson.fromJson(json, type) ?: emptyList()
        } else {
            emptyList()
        }
    }
    
    // Update user
    private fun updateUser(user: User) {
        val users = getAllUsers().toMutableList()
        val index = users.indexOfFirst { it.id == user.id }
        if (index != -1) {
            users[index] = user
            saveUsers(users)
        }
    }
    
    // Save users list
    private fun saveUsers(users: List<User>) {
        val json = gson.toJson(users)
        sharedPreferences.edit().putString(KEY_USERS, json).apply()
    }
    
    // Save current user
    private fun saveCurrentUser(user: User) {
        val json = gson.toJson(user)
        sharedPreferences.edit().putString(KEY_CURRENT_USER, json).apply()
    }
    
    // Clear current user
    private fun clearCurrentUser() {
        sharedPreferences.edit().remove(KEY_CURRENT_USER).apply()
    }
    
    // Set logged in status
    private fun setLoggedInStatus(isLoggedIn: Boolean) {
        sharedPreferences.edit().putBoolean(KEY_IS_LOGGED_IN, isLoggedIn).apply()
    }
    
    // Clear all user data (for testing/logout)
    fun clearAllUserData() {
        sharedPreferences.edit().clear().apply()
    }
}
