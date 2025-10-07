package com.example.habitblooms

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController

class SigninFragment : Fragment() {

    private lateinit var inputEmail: EditText
    private lateinit var inputPassword: EditText
    private lateinit var btnSignIn: Button
    private lateinit var tvSignUp: TextView

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.activity_signin, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        
        try {
            // Initialize views safely
            initializeViews(view)
            
            // Setup click listeners
            setupClickListeners()
            
            // Create test user for easy testing
            createTestUser()
            
        } catch (e: Exception) {
            android.util.Log.e("SigninFragment", "Error in onViewCreated: ${e.message}")
            showErrorToast("Error initializing sign in screen")
        }
    }

    private fun initializeViews(view: View) {
        try {
            inputEmail = view.findViewById(R.id.inputEmail)
            inputPassword = view.findViewById(R.id.inputPassword)
            btnSignIn = view.findViewById(R.id.btnSignIn)
            tvSignUp = view.findViewById(R.id.tvSignUp)
            
            android.util.Log.d("SigninFragment", "Views initialized successfully")
        } catch (e: Exception) {
            android.util.Log.e("SigninFragment", "Error initializing views: ${e.message}")
            throw e
        }
    }

    private fun setupClickListeners() {
        try {
            btnSignIn.setOnClickListener {
                handleSignIn()
            }
            
            tvSignUp.setOnClickListener {
                try {
                    findNavController().navigate(R.id.action_signin_to_signup)
                } catch (e: Exception) {
                    android.util.Log.e("SigninFragment", "Navigation error: ${e.message}")
                    showErrorToast("Navigation error")
                }
            }
            
            android.util.Log.d("SigninFragment", "Click listeners set up successfully")
        } catch (e: Exception) {
            android.util.Log.e("SigninFragment", "Error setting up click listeners: ${e.message}")
            showErrorToast("Error setting up buttons")
        }
    }

    private fun handleSignIn() {
        try {
            android.util.Log.d("SigninFragment", "Sign in button clicked")
            
            // Get input values safely
            val email = inputEmail?.text?.toString()?.trim() ?: ""
            val password = inputPassword?.text?.toString() ?: ""
            
            android.util.Log.d("SigninFragment", "Email: $email, Password length: ${password.length}")

            // Basic validation
            if (email.isEmpty()) {
                showErrorToast("Please enter your email")
                inputEmail?.requestFocus()
                return
            }
            
            if (password.isEmpty()) {
                showErrorToast("Please enter your password")
                inputPassword?.requestFocus()
                return
            }
            
            if (!android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                showErrorToast("Please enter a valid email address")
                inputEmail?.requestFocus()
                return
            }

            // Show loading state
            btnSignIn?.isEnabled = false
            btnSignIn?.text = "Signing In..."

            // Simple authentication check
            if (isValidCredentials(email, password)) {
                android.util.Log.d("SigninFragment", "Login successful")
                showSuccessToast("Welcome back!")
                
                // Navigate to home
                try {
                    findNavController().navigate(R.id.action_signin_to_home)
                } catch (e: Exception) {
                    android.util.Log.e("SigninFragment", "Navigation error: ${e.message}")
                    showErrorToast("Navigation error")
                }
            } else {
                android.util.Log.d("SigninFragment", "Invalid credentials")
                showErrorToast("Invalid email or password")
                resetButtonState()
            }
            
        } catch (e: Exception) {
            android.util.Log.e("SigninFragment", "Error in handleSignIn: ${e.message}")
            showErrorToast("An error occurred. Please try again.")
            resetButtonState()
        }
    }
    
    private fun isValidCredentials(email: String, password: String): Boolean {
        return try {
            // Simple hardcoded test credentials for now
            (email == "test@example.com" && password == "test123") ||
            (email == "user@example.com" && password == "password123")
        } catch (e: Exception) {
            android.util.Log.e("SigninFragment", "Error validating credentials: ${e.message}")
            false
        }
    }
    
    private fun resetButtonState() {
        try {
            btnSignIn?.isEnabled = true
            btnSignIn?.text = "Sign In"
        } catch (e: Exception) {
            android.util.Log.e("SigninFragment", "Error resetting button state: ${e.message}")
        }
    }
    
    private fun createTestUser() {
        try {
            android.util.Log.d("SigninFragment", "Creating test user")
            showInfoToast("Test credentials: test@example.com / test123")
        } catch (e: Exception) {
            android.util.Log.e("SigninFragment", "Error creating test user: ${e.message}")
        }
    }
    
    private fun showErrorToast(message: String) {
        try {
            Toast.makeText(requireContext(), message, Toast.LENGTH_LONG).show()
        } catch (e: Exception) {
            android.util.Log.e("SigninFragment", "Error showing toast: ${e.message}")
        }
    }
    
    private fun showSuccessToast(message: String) {
        try {
            Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show()
        } catch (e: Exception) {
            android.util.Log.e("SigninFragment", "Error showing success toast: ${e.message}")
        }
    }
    
    private fun showInfoToast(message: String) {
        try {
            Toast.makeText(requireContext(), message, Toast.LENGTH_LONG).show()
        } catch (e: Exception) {
            android.util.Log.e("SigninFragment", "Error showing info toast: ${e.message}")
        }
    }
}


