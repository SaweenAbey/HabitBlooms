package com.example.habitblooms

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ProgressBar
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.habitblooms.data.MoodRepository
import com.example.habitblooms.data.MoodType
import com.example.habitblooms.data.UserRepository
import java.text.SimpleDateFormat
import java.util.*

class AftersigninFragment : Fragment() {
    
    // UI Components
    private lateinit var tvGreeting: TextView
    private lateinit var tvCurrentTime: TextView
    private lateinit var tvCurrentDate: TextView
    private lateinit var tvCompletedTasks: TextView
    private lateinit var tvWaterPercentage: TextView
    private lateinit var tvMoodEmoji: TextView
    private lateinit var tvMoodStatus: TextView
    private lateinit var tvMoodDescription: TextView
    private lateinit var tvMoodTime: TextView
    private lateinit var tvOverallProgress: TextView
    private lateinit var progressBarOverall: ProgressBar
    private lateinit var tvHabitsCompleted: TextView
    private lateinit var tvWaterGlasses: TextView
    private lateinit var tvMoodCount: TextView
    
    // Action Buttons
    private lateinit var logMoodBtn: Button
    private lateinit var addWaterBtn: Button
    private lateinit var addHabitBtn: Button
    
    // Data
    private lateinit var moodRepository: MoodRepository
    private lateinit var userRepository: UserRepository
    
    // Sample data (in a real app, this would come from repositories)
    private var completedTasks = 5
    private var totalTasks = 8
    private var waterGlassesDrunk = 6
    private var totalWaterGlasses = 8
    private var moodEntriesToday = 3
    
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_home, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        
        initializeViews(view)
        setupMoodRepository()
        setupClickListeners()
        updateUI()
    }
    
    private fun initializeViews(view: View) {
        // Header views
        tvGreeting = view.findViewById(R.id.tv_greeting)
        tvCurrentTime = view.findViewById(R.id.tv_current_time)
        tvCurrentDate = view.findViewById(R.id.tv_current_date)
        
        // Overview cards
        tvCompletedTasks = view.findViewById(R.id.tv_completed_tasks)
        tvWaterPercentage = view.findViewById(R.id.tv_water_percentage)
        
        // Mood section
        tvMoodEmoji = view.findViewById(R.id.tv_mood_emoji)
        tvMoodStatus = view.findViewById(R.id.tv_mood_status)
        tvMoodDescription = view.findViewById(R.id.tv_mood_description)
        tvMoodTime = view.findViewById(R.id.tv_mood_time)
        
        // Progress section
        tvOverallProgress = view.findViewById(R.id.tv_overall_progress)
        progressBarOverall = view.findViewById(R.id.progress_bar_overall)
        tvHabitsCompleted = view.findViewById(R.id.tv_habits_completed)
        tvWaterGlasses = view.findViewById(R.id.tv_water_glasses)
        tvMoodCount = view.findViewById(R.id.tv_mood_count)
        
        // Action buttons
        logMoodBtn = view.findViewById(R.id.logMoodBtn)
        addWaterBtn = view.findViewById(R.id.addWaterBtn)
        addHabitBtn = view.findViewById(R.id.addHabitBtn)
    }
    
    private fun setupMoodRepository() {
        moodRepository = MoodRepository.getInstance(requireContext())
        userRepository = UserRepository.getInstance(requireContext())
    }
    
    private fun setupClickListeners() {
        logMoodBtn.setOnClickListener {
            try {
                findNavController().navigate(R.id.moodFragment)
            } catch (e: Exception) {
                android.widget.Toast.makeText(context, "Navigation error: ${e.message}", android.widget.Toast.LENGTH_SHORT).show()
            }
        }
        
        addWaterBtn.setOnClickListener {
            try {
                findNavController().navigate(R.id.hydrationFragment)
            } catch (e: Exception) {
                android.widget.Toast.makeText(context, "Navigation error: ${e.message}", android.widget.Toast.LENGTH_SHORT).show()
            }
        }
        
        addHabitBtn.setOnClickListener {
            try {
                findNavController().navigate(R.id.habbitFragment)
            } catch (e: Exception) {
                android.widget.Toast.makeText(context, "Navigation error: ${e.message}", android.widget.Toast.LENGTH_SHORT).show()
            }
        }
    }
    
    private fun updateUI() {
        // Update personalized greeting
        updateGreeting()
        
        // Update time and date
        updateTimeAndDate()
        
        // Update completed tasks
        tvCompletedTasks.text = completedTasks.toString()
        
        // Update water percentage
        val waterPercentage = (waterGlassesDrunk * 100) / totalWaterGlasses
        tvWaterPercentage.text = "$waterPercentage%"
        
        // Update mood information
        updateMoodInfo()
        
        // Update progress
        updateProgressInfo()
        
        // Update detailed stats
        tvHabitsCompleted.text = "$completedTasks/$totalTasks"
        tvWaterGlasses.text = "$waterGlassesDrunk/$totalWaterGlasses"
        tvMoodCount.text = moodEntriesToday.toString()
    }
    
    private fun updateTimeAndDate() {
        val currentTime = SimpleDateFormat("h:mm a", Locale.getDefault()).format(Date())
        val currentDate = SimpleDateFormat("EEEE, MMMM d, yyyy", Locale.getDefault()).format(Date())
        
        tvCurrentTime.text = currentTime
        tvCurrentDate.text = currentDate
    }
    
    private fun updateMoodInfo() {
        // Get today's mood entries
        val todayMoods = moodRepository.getTodayMoodEntries()
        
        if (todayMoods.isNotEmpty()) {
            // Get the latest mood entry
            val latestMood = todayMoods.first()
            val moodType = latestMood.moodType
            
            tvMoodEmoji.text = moodType.emoji
            tvMoodStatus.text = moodType.displayName
            tvMoodDescription.text = getMoodDescription(moodType)
            tvMoodTime.text = latestMood.getFormattedTime()
        } else {
            // Default mood if no entries today
            tvMoodEmoji.text = "😊"
            tvMoodStatus.text = "Feeling Good"
            tvMoodDescription.text = "Ready to start your day!"
            tvMoodTime.text = "No entries today"
        }
    }
    
    private fun getMoodDescription(moodType: MoodType): String {
        return when (moodType) {
            MoodType.HAPPY -> "You're radiating positivity today!"
            MoodType.NEUTRAL -> "Feeling balanced and steady."
            MoodType.SAD -> "It's okay to feel this way. You're strong."
            MoodType.EXCITED -> "Your energy is contagious!"
            MoodType.CALM -> "Peaceful and centered. Perfect."
            MoodType.STRESSED -> "Take a deep breath. You've got this."
        }
    }
    
    private fun updateProgressInfo() {
        // Calculate overall progress (average of habits and water)
        val habitsProgress = (completedTasks * 100) / totalTasks
        val waterProgress = (waterGlassesDrunk * 100) / totalWaterGlasses
        val overallProgress = (habitsProgress + waterProgress) / 2
        
        tvOverallProgress.text = "$overallProgress%"
        progressBarOverall.progress = overallProgress
        
        // Update progress bar color based on completion
        if (overallProgress >= 80) {
            progressBarOverall.progressDrawable = context?.getDrawable(R.drawable.progress_bar_completed)
        } else {
            progressBarOverall.progressDrawable = context?.getDrawable(R.drawable.progress_bar_main)
        }
    }
    
    private fun updateGreeting() {
        val currentUser = userRepository.getCurrentUser()
        val userName = currentUser?.name ?: "User"
        
        val calendar = Calendar.getInstance()
        val hour = calendar.get(Calendar.HOUR_OF_DAY)
        
        val greeting = when (hour) {
            in 5..11 -> "Good Morning"
            in 12..17 -> "Good Afternoon"
            in 18..21 -> "Good Evening"
            else -> "Good Night"
        }
        
        tvGreeting.text = "$greeting, $userName! 👋"
    }
    
    // Method to refresh data (can be called when returning from other fragments)
    fun refreshData() {
        // Update mood entries count
        moodEntriesToday = moodRepository.getTodayMoodEntries().size
        
        // In a real app, you would also update:
        // - completedTasks from habit repository
        // - waterGlassesDrunk from water repository
        
        updateUI()
    }
}