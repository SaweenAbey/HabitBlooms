package com.example.habitblooms

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class Signup : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_signup)

        val btnSignup: Button = findViewById(R.id.btnSignup)
        val tvSignin: TextView = findViewById(R.id.tvSignin)

        btnSignup.setOnClickListener {
            // Navigate to Signin after successful signup
            val intent = Intent(this, Signin::class.java)
            startActivity(intent)
        }

        tvSignin.setOnClickListener {
            // Navigate to Signin
            val intent = Intent(this, Signin::class.java)
            startActivity(intent)
        }
    }
}