package com.example.habitblooms

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class Onboarding4 : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_onboarding4)
        
        android.util.Log.d("Onboarding4", "Activity created successfully")
        
        val btnNext: Button? = findViewById(R.id.btnNext)
        if (btnNext == null) {
            android.util.Log.e("Onboarding4", "btnNext button not found!")
            android.widget.Toast.makeText(this, "Get Started button not found!", android.widget.Toast.LENGTH_SHORT).show()
            return
        }
        
        android.util.Log.d("Onboarding4", "Button found, setting click listener")
        
        btnNext.setOnClickListener {
            android.util.Log.d("Onboarding4", "Get Started button clicked")
            try {
                // Navigate to Signup
                android.util.Log.d("Onboarding4", "Creating intent for Signup")
                val intent = Intent(this, Signup::class.java)
                android.util.Log.d("Onboarding4", "Starting Signup activity")
                startActivity(intent)
                android.util.Log.d("Onboarding4", "Signup activity started successfully")
            } catch (e: Exception) {
                // Log error and show user-friendly message
                android.util.Log.e("Onboarding4", "Error navigating to Signup", e)
                android.widget.Toast.makeText(this, "Unable to proceed. Please try again.", android.widget.Toast.LENGTH_SHORT).show()
            }
        }
        
        android.util.Log.d("Onboarding4", "Click listener set successfully")
    }
}