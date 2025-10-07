package com.example.habitblooms

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class Onboarding1 : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_onboarding1)
        
        android.util.Log.d("Onboarding1", "Activity created successfully")
        
        val startedButton: Button? = findViewById(R.id.btnGetStarted)
        if (startedButton == null) {
            android.util.Log.e("Onboarding1", "btnGetStarted button not found!")
            android.widget.Toast.makeText(this, "Button not found!", android.widget.Toast.LENGTH_SHORT).show()
            return
        }
        
        android.util.Log.d("Onboarding1", "Button found, setting click listener")
        
        startedButton.setOnClickListener {
            android.util.Log.d("Onboarding1", "Get Started button clicked")
            try {
                // Navigate to Onboarding2
                android.util.Log.d("Onboarding1", "Creating intent for Onboarding2")
                val intent = Intent(this, Onboarding2::class.java)
                android.util.Log.d("Onboarding1", "Starting Onboarding2 activity")
                startActivity(intent)
                android.util.Log.d("Onboarding1", "Onboarding2 activity started successfully")
            } catch (e: Exception) {
                // Log error and show user-friendly message
                android.util.Log.e("Onboarding1", "Error navigating to Onboarding2", e)
                android.widget.Toast.makeText(this, "Unable to proceed. Please try again.", android.widget.Toast.LENGTH_SHORT).show()
            }
        }
        
        android.util.Log.d("Onboarding1", "Click listener set successfully")
    }
}