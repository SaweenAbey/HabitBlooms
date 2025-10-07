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

class SignupFragment : Fragment() {

    private lateinit var inputName: EditText
    private lateinit var inputEmail: EditText
    private lateinit var inputPassword: EditText
    private lateinit var inputConfirmPassword: EditText
    private lateinit var btnSignup: Button
    private lateinit var tvSignin: TextView

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.activity_signup, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        
        try {
            // Initialize views safely
            initializeViews(view)
            
            // Setup click listeners
            setupClickListeners()
            
        } catch (e: Exception) {
            android.util.Log.e("SignupFragment", "Error in onViewCreated: ${e.message}")
            showErrorToast("Error initializing sign up screen")
        }
    }

    private fun initializeViews(view: View) {
        try {
            inputName = view.findViewById(R.id.inputName)
            inputEmail = view.findViewById(R.id.inputEmail)
            inputPassword = view.findViewById(R.id.inputPassword)
            inputConfirmPassword = view.findViewById(R.id.inputConfirmPassword)
            btnSignup = view.findViewById(R.id.btnSignup)
            tvSignin = view.findViewById(R.id.tvSignin)
            
            android.util.Log.d("SignupFragment", "Views initialized successfully")
        } catch (e: Exception) {
            android.util.Log.e("SignupFragment", "Error initializing views: ${e.message}")
            throw e
        }
    }

    private fun setupClickListeners() {
        try {
            btnSignup.setOnClickListener {
                handleSignup()
            }
            
            tvSignin.setOnClickListener {
                try {
                    findNavController().navigate(R.id.action_signup_to_signin)
                } catch (e: Exception) {
                    android.util.Log.e("SignupFragment", "Navigation error: ${e.message}")
                    showErrorToast("Navigation error")
                }
            }
            
            android.util.Log.d("SignupFragment", "Click listeners set up successfully")
        } catch (e: Exception) {
            android.util.Log.e("SignupFragment", "Error setting up click listeners: ${e.message}")
            showErrorToast("Error setting up buttons")
        }
    }

    private fun handleSignup() {
        try {
            android.util.Log.d("SignupFragment", "Sign up button clicked")
            
            // Get input values safely
            val name = inputName?.text?.toString()?.trim() ?: ""
            val email = inputEmail?.text?.toString()?.trim() ?: ""
            val password = inputPassword?.text?.toString() ?: ""
            val confirmPassword = inputConfirmPassword?.text?.toString() ?: ""
            
            android.util.Log.d("SignupFragment", "Name: $name, Email: $email, Password length: ${password.length}")

            // Basic validation
            if (name.isEmpty()) {
                showErrorToast("Please enter your name")
                inputName?.requestFocus()
                return
            }
            
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
            
            if (confirmPassword.isEmpty()) {
                showErrorToast("Please confirm your password")
                inputConfirmPassword?.requestFocus()
                return
            }
            
            if (!android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                showErrorToast("Please enter a valid email address")
                inputEmail?.requestFocus()
                return
            }
            
            if (password.length < 6) {
                showErrorToast("Password must be at least 6 characters")
                inputPassword?.requestFocus()
                return
            }
            
            if (password != confirmPassword) {
                showErrorToast("Passwords do not match")
                inputConfirmPassword?.requestFocus()
                return
            }

            // Show loading state
            btnSignup?.isEnabled = false
            btnSignup?.text = "Creating Account..."

            // Simulate account creation
            android.util.Log.d("SignupFragment", "Account creation successful")
            showSuccessToast("Account created successfully! Welcome $name!")
            
            // Navigate to home
            try {
                findNavController().navigate(R.id.action_signup_to_home)
            } catch (e: Exception) {
                android.util.Log.e("SignupFragment", "Navigation error: ${e.message}")
                showErrorToast("Navigation error")
                resetButtonState()
            }
            
        } catch (e: Exception) {
            android.util.Log.e("SignupFragment", "Error in handleSignup: ${e.message}")
            showErrorToast("An error occurred. Please try again.")
            resetButtonState()
        }
    }
    
    private fun resetButtonState() {
        try {
            btnSignup?.isEnabled = true
            btnSignup?.text = "Create Account"
        } catch (e: Exception) {
            android.util.Log.e("SignupFragment", "Error resetting button state: ${e.message}")
        }
    }
    
    private fun showErrorToast(message: String) {
        try {
            Toast.makeText(requireContext(), message, Toast.LENGTH_LONG).show()
        } catch (e: Exception) {
            android.util.Log.e("SignupFragment", "Error showing toast: ${e.message}")
        }
    }
    
    private fun showSuccessToast(message: String) {
        try {
            Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show()
        } catch (e: Exception) {
            android.util.Log.e("SignupFragment", "Error showing success toast: ${e.message}")
        }
    }
}


