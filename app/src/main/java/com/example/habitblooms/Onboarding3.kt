package com.example.habitblooms

import android.content.Intent
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.google.android.material.floatingactionbutton.FloatingActionButton

class Onboarding3 : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_onboarding3)
        
        android.util.Log.d("Onboarding3", "Activity created successfully")
        
        val nextBtn: FloatingActionButton? = findViewById(R.id.nextBtn)
        if (nextBtn == null) {
            android.util.Log.e("Onboarding3", "nextBtn FloatingActionButton not found!")
            android.widget.Toast.makeText(this, "Next button not found!", android.widget.Toast.LENGTH_SHORT).show()
            return
        }
        
        android.util.Log.d("Onboarding3", "FloatingActionButton found, setting click listener")
        
        nextBtn.setOnClickListener {
            android.util.Log.d("Onboarding3", "Next button clicked")
            try {
                // Navigate to Onboarding4
                android.util.Log.d("Onboarding3", "Creating intent for Onboarding4")
                val intent = Intent(this, Onboarding4::class.java)
                android.util.Log.d("Onboarding3", "Starting Onboarding4 activity")
                startActivity(intent)
                android.util.Log.d("Onboarding3", "Onboarding4 activity started successfully")
            } catch (e: Exception) {
                // Log error and show user-friendly message
                android.util.Log.e("Onboarding3", "Error navigating to Onboarding4", e)
                android.widget.Toast.makeText(this, "Unable to proceed. Please try again.", android.widget.Toast.LENGTH_SHORT).show()
            }
        }
        
        android.util.Log.d("Onboarding3", "Click listener set successfully")
    }
}