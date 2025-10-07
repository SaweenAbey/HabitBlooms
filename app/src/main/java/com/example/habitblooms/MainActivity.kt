package com.example.habitblooms

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import com.google.android.material.bottomnavigation.BottomNavigationView

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        
        setupNavigation()
    }
    
    private fun setupNavigation() {
        try {
            val navHostFragment = supportFragmentManager
                .findFragmentById(R.id.nav_host_fragment) as? NavHostFragment
            
            if (navHostFragment == null) {
                android.util.Log.e("MainActivity", "NavHostFragment not found!")
                return
            }
            
            val navController = navHostFragment.navController
            android.util.Log.d("MainActivity", "Navigation controller found")
            
            // Setup bottom navigation
            val bottomNav = findViewById<BottomNavigationView>(R.id.bottomNav)
            if (bottomNav == null) {
                android.util.Log.e("MainActivity", "BottomNavigationView not found!")
                return
            }
            
            // Connect bottom navigation with navigation controller
            bottomNav.setupWithNavController(navController)
            android.util.Log.d("MainActivity", "Bottom navigation connected successfully")
            
        } catch (e: Exception) {
            android.util.Log.e("MainActivity", "Error setting up navigation", e)
            android.widget.Toast.makeText(this, "Navigation setup failed", android.widget.Toast.LENGTH_SHORT).show()
        }
    }
}