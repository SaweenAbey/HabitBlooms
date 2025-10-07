package com.example.habitblooms

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.habitblooms.adapter.WaterIntakeAdapter
import com.example.habitblooms.data.WaterIntake
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import java.text.SimpleDateFormat
import java.util.*

class HydrationFragment : Fragment() {

    private lateinit var waterIntakeAdapter: WaterIntakeAdapter
    private lateinit var waterIntakeRecyclerView: RecyclerView
    private lateinit var greetingTextView: TextView
    private lateinit var dateTextView: TextView
    private lateinit var waterProgressTextView: TextView
    private lateinit var progressPercentageTextView: TextView
    private lateinit var progressBar: ProgressBar
    private lateinit var intakeCountTextView: TextView
    private lateinit var btn100ml: Button
    private lateinit var btn250ml: Button
    private lateinit var btn500ml: Button
    private lateinit var btnCustom: Button
    private lateinit var fabAddWater: FloatingActionButton

    private var currentWaterIntake = 0.0 // Current water intake in liters
    private val dailyGoal = 2.0 // Daily goal in liters (8 glasses of 250ml each)
    private val glassSize = 0.25 // Standard glass size in liters
    private var glassesDrunk = 0
    private val totalGlasses = 8 // Daily goal in glasses

    // Water intake history
    private val waterIntakes = mutableListOf<WaterIntake>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_hydration, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initializeViews(view)
        setupRecyclerView()
        setupQuickAddButtons()
        setupFloatingActionButton()
        setupBottomNavigation(view)
        updateUI()
        setupWaterReminders()
    }

    private fun initializeViews(view: View) {
        greetingTextView = view.findViewById(R.id.tv_greeting)
        dateTextView = view.findViewById(R.id.tv_date)
        waterProgressTextView = view.findViewById(R.id.tv_water_progress)
        progressPercentageTextView = view.findViewById(R.id.tv_progress_percentage)
        progressBar = view.findViewById(R.id.progress_bar_hydration)
        intakeCountTextView = view.findViewById(R.id.tv_intake_count)
        waterIntakeRecyclerView = view.findViewById(R.id.rv_water_intakes)
        btn100ml = view.findViewById(R.id.btn_100ml)
        btn250ml = view.findViewById(R.id.btn_250ml)
        btn500ml = view.findViewById(R.id.btn_500ml)
        btnCustom = view.findViewById(R.id.btn_custom)
        fabAddWater = view.findViewById(R.id.fab_add_water)
    }

    private fun setupRecyclerView() {
        // Initialize with some sample data if empty
        if (waterIntakes.isEmpty()) {
            addSampleData()
        }
        
        waterIntakeAdapter = WaterIntakeAdapter(waterIntakes)
        waterIntakeRecyclerView.layoutManager = LinearLayoutManager(context)
        waterIntakeRecyclerView.adapter = waterIntakeAdapter
        intakeCountTextView.text = "${waterIntakes.size} intakes"
    }

    private fun addSampleData() {
        val sampleIntakes = listOf(
            WaterIntake(1, "7:53 PM", 250, "Regular glass", 0.25),
            WaterIntake(2, "7:53 PM", 100, "Small glass", 0.35),
            WaterIntake(3, "2:07 PM", 500, "Large glass", 0.85),
            WaterIntake(4, "2:07 PM", 100, "Small glass", 0.95)
        )
        waterIntakes.addAll(sampleIntakes)
        currentWaterIntake = 0.95
        glassesDrunk = 4
    }

    private fun setupQuickAddButtons() {
        btn100ml.setOnClickListener { addWater(0.1) }
        btn250ml.setOnClickListener { addWater(0.25) }
        btn500ml.setOnClickListener { addWater(0.5) }
        btnCustom.setOnClickListener { 
            // Handle custom amount - could show a dialog
            addWater(0.2) // Default custom amount
        }
    }

    private fun setupFloatingActionButton() {
        fabAddWater.setOnClickListener {
            // Handle adding new water intake
            addWater(0.2) // Default amount for FAB
        }
    }

    private fun addWater(amount: Double) {
        currentWaterIntake += amount
        
        // Calculate glasses drunk (assuming 250ml = 1 glass)
        val glassesAdded = (amount / glassSize).toInt()
        glassesDrunk += glassesAdded
        
        // Add to activity list
        val currentTime = SimpleDateFormat("h:mm a", Locale.getDefault()).format(Date())
        val newIntake = WaterIntake(
            id = waterIntakes.size + 1,
            time = currentTime,
            amountMl = (amount * 1000).toInt(),
            type = getGlassType(amount),
            cumulativeL = currentWaterIntake
        )
        
        waterIntakes.add(0, newIntake) // Add to beginning
        waterIntakeAdapter.updateIntakes(waterIntakes)
        intakeCountTextView.text = "${waterIntakes.size} intakes"
        
        updateUI()
        
        // Show success message
        val message = if (glassesAdded > 0) {
            "Added ${glassesAdded} glass${if (glassesAdded > 1) "es" else ""} of water! 💧"
        } else {
            "Added ${(amount * 1000).toInt()}ml of water! 💧"
        }
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
        
        // Check if daily goal is reached
        if (currentWaterIntake >= dailyGoal) {
            Toast.makeText(context, "🎉 Congratulations! You've reached your daily water goal!", Toast.LENGTH_LONG).show()
        }
    }

    private fun getGlassType(amount: Double): String {
        return when {
            amount <= 0.15 -> "Small sip"
            amount <= 0.2 -> "Small glass"
            amount <= 0.3 -> "Regular glass"
            amount <= 0.5 -> "Large glass"
            else -> "Extra large"
        }
    }

    private fun updateUI() {
        val progressPercentage = ((currentWaterIntake / dailyGoal) * 100).toInt()
        
        waterProgressTextView.text = "${String.format("%.1f", currentWaterIntake)}L / ${dailyGoal}L"
        progressPercentageTextView.text = "$glassesDrunk/$totalGlasses glasses • $progressPercentage% of daily goal"
        progressBar.progress = progressPercentage
        
        // Update progress bar color based on completion
        if (progressPercentage >= 100) {
            progressBar.progressDrawable = context?.getDrawable(R.drawable.progress_bar_completed)
        } else {
            progressBar.progressDrawable = context?.getDrawable(R.drawable.progress_bar_main)
        }
        
        // Update date with current date
        val currentDate = SimpleDateFormat("EEEE, MMMM d", Locale.getDefault()).format(Date())
        dateTextView.text = "Today, $currentDate"
    }

    private fun setupBottomNavigation(view: View) {
        val bottomNav = view.findViewById<BottomNavigationView>(R.id.bottomNav)
        bottomNav?.selectedItemId = R.id.hydrationFragment // Highlight Hydration
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
                    // Already on Hydration Fragment
                    true
                }
                R.id.habbitFragment -> {
                    findNavController().navigate(R.id.habbitFragment)
                    true
                }
                R.id.settingsFragment -> {
                    findNavController().navigate(R.id.settingsFragment)
                    true
                }
                else -> false
            }
        }
    }

    private fun setupWaterReminders() {
        // Set up hourly water reminders
        val alarmManager = context?.getSystemService(Context.ALARM_SERVICE) as? AlarmManager
        
        // Create a simple reminder system (in a real app, you'd use a proper notification system)
        // For now, we'll just show a toast when the fragment is created
        Toast.makeText(context, "💧 Water reminder: Don't forget to stay hydrated!", Toast.LENGTH_SHORT).show()
        
        // You can expand this to set up actual alarms for water reminders
        // Example: Set reminder every 2 hours
        /*
        val intent = Intent(context, WaterReminderReceiver::class.java)
        val pendingIntent = PendingIntent.getBroadcast(
            context, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT
        )
        
        val calendar = Calendar.getInstance()
        calendar.add(Calendar.HOUR, 2)
        
        alarmManager?.setRepeating(
            AlarmManager.RTC_WAKEUP,
            calendar.timeInMillis,
            AlarmManager.INTERVAL_HOUR * 2,
            pendingIntent
        )
        */
    }
}