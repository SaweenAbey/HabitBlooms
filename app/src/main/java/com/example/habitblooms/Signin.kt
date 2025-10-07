package com.example.habitblooms

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class Signin : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_signin)
        
        android.util.Log.d("Signin", "Activity created successfully")
        
        val btnSignIn: Button? = findViewById(R.id.btnSignIn)
        if (btnSignIn == null) {
            android.util.Log.e("Signin", "btnSignIn button not found!")
            android.widget.Toast.makeText(this, "Sign in button not found!", android.widget.Toast.LENGTH_SHORT).show()
            return
        }
        
        android.util.Log.d("Signin", "Button found, setting click listener")
        
        btnSignIn.setOnClickListener {
            android.util.Log.d("Signin", "Sign in button clicked")
            try {
                // Navigate to MainActivity (home page) after successful signin
                android.util.Log.d("Signin", "Creating intent for MainActivity")
                val intent = Intent(this, MainActivity::class.java)
                android.util.Log.d("Signin", "Starting MainActivity")
                startActivity(intent)
                android.util.Log.d("Signin", "MainActivity started successfully")
                finish() // Close the sign-in activity
            } catch (e: Exception) {
                android.util.Log.e("Signin", "Error navigating to MainActivity", e)
                android.widget.Toast.makeText(this, "Unable to proceed. Please try again.", android.widget.Toast.LENGTH_SHORT).show()
            }
        }
        
        android.util.Log.d("Signin", "Click listener set successfully")
    }
}