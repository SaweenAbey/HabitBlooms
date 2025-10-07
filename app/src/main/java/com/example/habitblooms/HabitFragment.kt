package com.example.habitblooms

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.habitblooms.data.Habit
import com.example.habitblooms.adapter.HabitAdapter

class HabitFragment : Fragment() {
    
    private lateinit var habitAdapter: HabitAdapter
    private lateinit var recyclerView: androidx.recyclerview.widget.RecyclerView
    
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_habbit, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        
        setupRecyclerView(view)
        setupFloatingActionButton(view)
        updateProgressBar(view)
    }
    
    private fun setupRecyclerView(view: View) {
        recyclerView = view.findViewById(R.id.rv_habits)
        val sampleHabits = getSampleHabits().toMutableList()
        habitAdapter = HabitAdapter(sampleHabits) { habit ->
            // Handle habit completion
            val updatedHabits = habitAdapter.getHabits().toMutableList()
            val index = updatedHabits.indexOfFirst { it.id == habit.id }
            if (index != -1) {
                updatedHabits[index] = habit.copy(
                    isCompleted = !habit.isCompleted,
                    currentValue = if (!habit.isCompleted) habit.targetValue else 0
                )
                habitAdapter.updateHabits(updatedHabits)
                updateProgressBar(view)
            }
        }
        
        recyclerView.layoutManager = LinearLayoutManager(context)
        recyclerView.adapter = habitAdapter
    }
    
    private fun setupFloatingActionButton(view: View) {
        view.findViewById<com.google.android.material.floatingactionbutton.FloatingActionButton>(R.id.fab_add_habit)?.setOnClickListener {
            // Handle add new habit
            // For now, just add a sample habit
            val newHabit = Habit(
                title = "New Habit",
                description = "A new habit to track",
                iconResId = R.drawable.ic_heart,
                targetValue = 1,
                currentValue = 0,
                isCompleted = false
            )
            val updatedHabits = habitAdapter.getHabits().toMutableList()
            updatedHabits.add(newHabit)
            habitAdapter.updateHabits(updatedHabits)
            updateProgressBar(view)
        }
    }
    
    private fun updateProgressBar(view: View) {
        val habits = habitAdapter.getHabits()
        val completedHabits = habits.count { it.isCompleted }
        val totalHabits = habits.size
        val progressPercentage = if (totalHabits > 0) (completedHabits * 100) / totalHabits else 0
        
        val progressBar = view.findViewById<android.widget.ProgressBar>(R.id.progress_bar)
        val progressText = view.findViewById<android.widget.TextView>(R.id.tv_progress_percentage)
        
        progressBar?.progress = progressPercentage
        progressText?.text = "$progressPercentage%"
        
        // Update progress text color based on completion
        progressText?.setTextColor(
            if (progressPercentage == 100) 
                context?.getColor(R.color.orange_accent) ?: android.graphics.Color.parseColor("#FF8F00")
            else 
                context?.getColor(R.color.forest_green) ?: android.graphics.Color.parseColor("#2D5016")
        )
        
        // Update progress bar color based on completion
        if (progressPercentage == 100) {
            progressBar?.progressDrawable = context?.getDrawable(R.drawable.progress_bar_completed)
        } else {
            progressBar?.progressDrawable = context?.getDrawable(R.drawable.progress_bar_main)
        }
    }
    
    private fun getSampleHabits(): List<Habit> {
        return listOf(
            Habit(
                title = "Drinking water",
                description = "Aim for 8 glasses of water throughout the day to stay hydrated and energized.",
                iconResId = R.drawable.ic_water,
                targetValue = 8,
                currentValue = 6,
                isCompleted = false
            ),
            Habit(
                title = "Meditate",
                description = "Dedicate 15 minutes to mindfulness meditation for mental clarity and stress reduction.",
                iconResId = R.drawable.ic_meditation,
                targetValue = 1,
                currentValue = 1,
                isCompleted = true
            ),
            Habit(
                title = "Daily exercise",
                description = "Engage in 30 minutes of moderate physical activity to boost your mood and fitness.",
                iconResId = R.drawable.ic_exercise,
                targetValue = 1,
                currentValue = 0,
                isCompleted = false
            ),
            Habit(
                title = "Reading book",
                description = "Spend 20 minutes reading before bed to relax your mind and learn something new.",
                iconResId = R.drawable.ic_book,
                targetValue = 1,
                currentValue = 0,
                isCompleted = false
            )
        )
    }
}
