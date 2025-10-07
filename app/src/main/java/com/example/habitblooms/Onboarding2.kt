package com.example.habitblooms

import android.content.Intent
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.floatingactionbutton.FloatingActionButton

class Onboarding2 : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_onboarding2)
        
        android.util.Log.d("Onboarding2", "Activity created successfully")
        
        val btnNext: FloatingActionButton? = findViewById(R.id.nextBtn)
        if (btnNext == null) {
            android.util.Log.e("Onboarding2", "nextBtn FloatingActionButton not found!")
            android.widget.Toast.makeText(this, "Next button not found!", android.widget.Toast.LENGTH_SHORT).show()
            return
        }
        
        android.util.Log.d("Onboarding2", "FloatingActionButton found, setting click listener")
        
        btnNext.setOnClickListener {
            android.util.Log.d("Onboarding2", "Next button clicked")
            try {
                // Navigate to Onboarding3
                android.util.Log.d("Onboarding2", "Creating intent for Onboarding3")
                val intent = Intent(this, Onboarding3::class.java)
                android.util.Log.d("Onboarding2", "Starting Onboarding3 activity")
                startActivity(intent)
                android.util.Log.d("Onboarding2", "Onboarding3 activity started successfully")
            } catch (e: Exception) {
                // Log error and show user-friendly message
                android.util.Log.e("Onboarding2", "Error navigating to Onboarding3", e)
                android.widget.Toast.makeText(this, "Unable to proceed. Please try again.", android.widget.Toast.LENGTH_SHORT).show()
            }
        }
        
        android.util.Log.d("Onboarding2", "Click listener set successfully")
    }
}