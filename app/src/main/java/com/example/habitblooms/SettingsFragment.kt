package com.example.habitblooms

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.Switch
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.google.android.material.bottomnavigation.BottomNavigationView

class SettingsFragment : Fragment() {

    // Notification switches
    private lateinit var switchAllNotifications: Switch
    private lateinit var switchSoundAlerts: Switch
    private lateinit var switchEmailSummaries: Switch
    
    // Data management buttons
    private lateinit var btnExportData: Button
    private lateinit var btnResetData: Button
    
    // Profile button
    private lateinit var profileButton: View

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_settings, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initializeViews(view)
        setupProfileButton()
        setupNotificationSwitches()
        setupDataManagementButtons()
        setupAboutSupportClicks()
        setupBottomNavigation(view)
    }

    private fun initializeViews(view: View) {
        // Notification switches
        switchAllNotifications = view.findViewById(R.id.switch_all_notifications)
        switchSoundAlerts = view.findViewById(R.id.switch_sound_alerts)
        switchEmailSummaries = view.findViewById(R.id.switch_email_summaries)
        
        // Data management buttons
        btnExportData = view.findViewById(R.id.btn_export_data)
        btnResetData = view.findViewById(R.id.btn_reset_data)
        
        // Profile button
        profileButton = view.findViewById(R.id.profile_button)
    }

    private fun setupProfileButton() {
        profileButton.setOnClickListener {
            // Navigate to profile fragment
            findNavController().navigate(R.id.profileFragment)
        }
    }

    private fun setupNotificationSwitches() {
        // Enable All Notifications switch
        switchAllNotifications.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                // Enable all other notification options
                switchSoundAlerts.isChecked = true
                switchEmailSummaries.isChecked = true
                Toast.makeText(context, "All notifications enabled", Toast.LENGTH_SHORT).show()
            } else {
                // Disable all other notification options
                switchSoundAlerts.isChecked = false
                switchEmailSummaries.isChecked = false
                Toast.makeText(context, "All notifications disabled", Toast.LENGTH_SHORT).show()
            }
        }

        // Sound Alerts switch
        switchSoundAlerts.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                Toast.makeText(context, "Sound alerts enabled", Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(context, "Sound alerts disabled", Toast.LENGTH_SHORT).show()
            }
        }

        // Email Summaries switch
        switchEmailSummaries.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                Toast.makeText(context, "Email summaries enabled", Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(context, "Email summaries disabled", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun setupDataManagementButtons() {
        // Export Data button
        btnExportData.setOnClickListener {
            // Handle data export
            Toast.makeText(context, "Exporting your wellness data...", Toast.LENGTH_LONG).show()
            // Here you would implement actual data export logic
        }

        // Reset Data button
        btnResetData.setOnClickListener {
            // Show confirmation dialog for data reset
            showResetDataConfirmation()
        }
    }

    private fun showResetDataConfirmation() {
        // Create a simple confirmation dialog
        android.app.AlertDialog.Builder(requireContext())
            .setTitle("Reset All Data")
            .setMessage("Are you sure you want to permanently delete all your habit and mood entries? This action cannot be undone.")
            .setPositiveButton("Reset") { _, _ ->
                // Perform data reset
                Toast.makeText(context, "All data has been reset", Toast.LENGTH_LONG).show()
                // Here you would implement actual data reset logic
            }
            .setNegativeButton("Cancel", null)
            .show()
    }

    private fun setupAboutSupportClicks() {
        // Set up click listeners for About & Support items
        // These would typically navigate to different fragments or open web pages
        
        // For now, we'll just show toast messages
        view?.findViewById<View>(R.id.scrollView)?.let { scrollView ->
            // Find and set up click listeners for about items
            // This is a simplified approach - in a real app, you'd have proper IDs for each item
        }
    }

    private fun performSignOut() {
        // Clear any stored user data/session
        // For now, just navigate to sign in screen
        findNavController().navigate(R.id.signinFragment)
    }

    private fun setupBottomNavigation(view: View) {
        val bottomNav = view.findViewById<BottomNavigationView>(R.id.bottomNav)
        bottomNav?.setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.aftersigninFragment -> {
                    findNavController().navigate(R.id.aftersigninFragment)
                    true
                }
                R.id.moodFragment -> {
                    findNavController().navigate(R.id.moodFragment)
                    true
                }
                R.id.hydrationFragment -> {
                    findNavController().navigate(R.id.hydrationFragment)
                    true
                }
                R.id.habbitFragment -> {
                    findNavController().navigate(R.id.habbitFragment)
                    true
                }
                R.id.settingsFragment -> {
                    // Already on settings/profile
                    true
                }
                else -> false
            }
        }
        
        // Set the current item as selected
        bottomNav?.selectedItemId = R.id.settingsFragment
    }
}
