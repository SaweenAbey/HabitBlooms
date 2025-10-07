package com.example.habitblooms

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.habitblooms.adapter.HabitAdapter
import com.example.habitblooms.data.Habit
import com.example.habitblooms.data.HabitRepository
import com.google.android.material.floatingactionbutton.FloatingActionButton
import java.text.SimpleDateFormat
import java.util.*

class Habbit : Fragment() {

    private lateinit var habitRepository: HabitRepository
    private lateinit var habitAdapter: HabitAdapter
    private lateinit var rvHabits: RecyclerView
    private lateinit var progressBar: ProgressBar
    private lateinit var tvProgressPercentage: TextView
    private lateinit var tvCompletedCount: TextView
    private lateinit var tvTotalCount: TextView
    private lateinit var tvMotivationalMessage: TextView
    private lateinit var fabAddHabit: FloatingActionButton

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_habbit, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        
        // Initialize repository
        habitRepository = HabitRepository.getInstance(requireContext())
        
        // Initialize views
        initializeViews(view)
        
        // Setup RecyclerView
        setupRecyclerView()
        
        // Setup click listeners
        setupClickListeners()
        
        // Load and display habits
        loadHabits()
    }

    private fun initializeViews(view: View) {
        rvHabits = view.findViewById(R.id.rv_habits)
        progressBar = view.findViewById(R.id.progress_bar)
        tvProgressPercentage = view.findViewById(R.id.tv_progress_percentage)
        fabAddHabit = view.findViewById(R.id.fab_add_habit)
        
        // Additional views for enhanced UI
        tvCompletedCount = view.findViewById(R.id.tv_completed_count)
        tvTotalCount = view.findViewById(R.id.tv_total_count)
        tvMotivationalMessage = view.findViewById(R.id.tv_motivational_message)
    }

    private fun setupRecyclerView() {
        habitAdapter = HabitAdapter(
            habits = emptyList(),
            onHabitClick = { habit ->
                showHabitDetails(habit)
            },
            onMarkComplete = { habit ->
                markHabitComplete(habit)
            }
        )
        
        rvHabits.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = habitAdapter
        }
    }

    private fun setupClickListeners() {
        fabAddHabit.setOnClickListener {
            showAddHabitDialog()
        }
    }

    private fun loadHabits() {
        val habits = habitRepository.getTodayHabits()
        habitAdapter.updateHabits(habits)
        updateProgress()
        updateMotivationalMessage()
    }

    private fun markHabitComplete(habit: Habit) {
        if (habit.targetValue > 1) {
            // For habits with progress, increment
            habitRepository.incrementHabitProgress(habit.id)
            Toast.makeText(context, "Great! Keep going with ${habit.title} 💪", Toast.LENGTH_SHORT).show()
        } else {
            // For simple habits, mark as completed
            habitRepository.markHabitCompleted(habit.id)
            Toast.makeText(context, "Excellent! ${habit.title} completed! 🎉", Toast.LENGTH_SHORT).show()
        }
        
        // Reload habits to reflect changes
        loadHabits()
    }

    private fun updateProgress() {
        val progress = habitRepository.getTodayProgress()
        val completedCount = habitRepository.getCompletedHabitsCount()
        val totalCount = habitRepository.getTotalHabitsCount()
        
        progressBar.progress = progress
        tvProgressPercentage.text = "$progress%"
        
        // Update additional UI elements
        tvCompletedCount.text = completedCount.toString()
        tvTotalCount.text = totalCount.toString()
    }

    private fun updateMotivationalMessage() {
        val progress = habitRepository.getTodayProgress()
        val message = when {
            progress >= 100 -> "🎉 Amazing! You've completed all your habits today!"
            progress >= 75 -> "🔥 You're on fire! Almost there!"
            progress >= 50 -> "💪 Great progress! Keep it up!"
            progress >= 25 -> "🌟 Good start! You've got this!"
            else -> "🚀 Ready to build some great habits today?"
        }
        tvMotivationalMessage.text = message
    }

    private fun showHabitDetails(habit: Habit) {
        // Show habit details dialog
        val details = """
            📋 ${habit.title}
            
            📝 ${habit.description}
            
            🎯 Target: ${habit.targetValue} ${if (habit.targetValue > 1) "times" else "time"} per day
            📊 Progress: ${habit.getFormattedProgress()}
            🔥 Streak: ${habit.streak} days
            ✅ Completed: ${if (habit.isCompleted) "Yes" else "No"}
        """.trimIndent()
        
        android.app.AlertDialog.Builder(requireContext())
            .setTitle("Habit Details")
            .setMessage(details)
            .setPositiveButton("OK", null)
            .show()
    }

    private fun showAddHabitDialog() {
        val habitTitles = arrayOf(
            "Drink Water",
            "Meditate", 
            "Exercise",
            "Read Books",
            "Mindful Eating",
            "Take Vitamins",
            "Practice Gratitude",
            "Get Enough Sleep",
            "Limit Screen Time",
            "Practice Yoga"
        )
        
        android.app.AlertDialog.Builder(requireContext())
            .setTitle("Add New Habit")
            .setItems(habitTitles) { _, which ->
                val selectedTitle = habitTitles[which]
                createNewHabit(selectedTitle)
            }
            .setNegativeButton("Cancel", null)
            .show()
    }

    private fun createNewHabit(title: String) {
        val descriptions = mapOf(
            "Drink Water" to "Stay hydrated by drinking 8 glasses of water throughout the day",
            "Meditate" to "Spend 10-15 minutes in mindfulness meditation for mental clarity",
            "Exercise" to "Engage in 30 minutes of physical activity to boost your health",
            "Read Books" to "Read for at least 20 minutes to expand your knowledge",
            "Mindful Eating" to "Practice mindful eating by paying attention to your food",
            "Take Vitamins" to "Take your daily vitamins to support your health",
            "Practice Gratitude" to "Write down 3 things you're grateful for each day",
            "Get Enough Sleep" to "Aim for 7-8 hours of quality sleep each night",
            "Limit Screen Time" to "Reduce screen time and focus on real-world activities",
            "Practice Yoga" to "Do 20 minutes of yoga for flexibility and relaxation"
        )
        
        val targetValues = mapOf(
            "Drink Water" to 8,
            "Meditate" to 1,
            "Exercise" to 1,
            "Read Books" to 1,
            "Mindful Eating" to 3,
            "Take Vitamins" to 1,
            "Practice Gratitude" to 1,
            "Get Enough Sleep" to 1,
            "Limit Screen Time" to 1,
            "Practice Yoga" to 1
        )
        
        val iconResIds = mapOf(
            "Drink Water" to com.example.habitblooms.R.drawable.water,
            "Meditate" to com.example.habitblooms.R.drawable.mood,
            "Exercise" to com.example.habitblooms.R.drawable.habbitt,
            "Read Books" to com.example.habitblooms.R.drawable.ic_list,
            "Mindful Eating" to com.example.habitblooms.R.drawable.hydration,
            "Take Vitamins" to com.example.habitblooms.R.drawable.ic_water,
            "Practice Gratitude" to com.example.habitblooms.R.drawable.mood,
            "Get Enough Sleep" to com.example.habitblooms.R.drawable.ic_list,
            "Limit Screen Time" to com.example.habitblooms.R.drawable.habbitt,
            "Practice Yoga" to com.example.habitblooms.R.drawable.hydration
        )
        
        val newHabit = Habit(
            title = title,
            description = descriptions[title] ?: "Build this healthy habit",
            iconResId = iconResIds[title] ?: com.example.habitblooms.R.drawable.ic_list,
            targetValue = targetValues[title] ?: 1
        )
        
        habitRepository.addHabit(newHabit)
        loadHabits()
        
        Toast.makeText(context, "✅ New habit '${title}' added successfully!", Toast.LENGTH_SHORT).show()
    }
}