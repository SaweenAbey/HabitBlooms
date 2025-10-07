package com.example.habitblooms

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.habitblooms.adapter.MoodHistoryAdapter
import com.example.habitblooms.data.MoodRepository
import com.example.habitblooms.data.MoodType
import java.text.SimpleDateFormat
import java.util.*

class Mood : Fragment() {
    
    private lateinit var moodRepository: MoodRepository
    private lateinit var moodHistoryAdapter: MoodHistoryAdapter
    
    private lateinit var tvDate: TextView
    private lateinit var rvMoodHistory: RecyclerView
    
    // Navigation buttons
    private lateinit var btnNavHome: LinearLayout
    private lateinit var btnNavHydration: LinearLayout
    
    // Mood selection views
    private lateinit var moodHappy: LinearLayout
    private lateinit var moodNeutral: LinearLayout
    private lateinit var moodSad: LinearLayout
    private lateinit var moodExcited: LinearLayout
    private lateinit var moodCalm: LinearLayout
    private lateinit var moodStressed: LinearLayout

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_mood, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        
        initViews(view)
        setupMoodRepository()
        setupRecyclerView()
        setupMoodSelections()
        setupNavigation()
        updateUI()
    }

    private fun initViews(view: View) {
        tvDate = view.findViewById(R.id.tv_date)
        rvMoodHistory = view.findViewById(R.id.rv_mood_history)
        
        // Navigation buttons
        btnNavHome = view.findViewById(R.id.btn_nav_home)
        btnNavHydration = view.findViewById(R.id.btn_nav_hydration)
        
        // Mood selection views
        moodHappy = view.findViewById(R.id.mood_happy)
        moodNeutral = view.findViewById(R.id.mood_neutral)
        moodSad = view.findViewById(R.id.mood_sad)
        moodExcited = view.findViewById(R.id.mood_excited)
        moodCalm = view.findViewById(R.id.mood_calm)
        moodStressed = view.findViewById(R.id.mood_stressed)
    }

    private fun setupMoodRepository() {
        moodRepository = MoodRepository.getInstance(requireContext())
    }

    private fun setupRecyclerView() {
        moodHistoryAdapter = MoodHistoryAdapter(moodRepository.getMoodEntries()) { moodEntry ->
            // Handle mood entry click (optional - could show details)
            Toast.makeText(context, "Mood: ${moodEntry.moodType.displayName}", Toast.LENGTH_SHORT).show()
        }
        
        rvMoodHistory.layoutManager = LinearLayoutManager(context)
        rvMoodHistory.adapter = moodHistoryAdapter
    }

    private fun setupMoodSelections() {
        moodHappy.setOnClickListener { selectMood(MoodType.HAPPY) }
        moodNeutral.setOnClickListener { selectMood(MoodType.NEUTRAL) }
        moodSad.setOnClickListener { selectMood(MoodType.SAD) }
        moodExcited.setOnClickListener { selectMood(MoodType.EXCITED) }
        moodCalm.setOnClickListener { selectMood(MoodType.CALM) }
        moodStressed.setOnClickListener { selectMood(MoodType.STRESSED) }
    }

    private fun setupNavigation() {
        btnNavHome.setOnClickListener {
            try {
                findNavController().navigate(R.id.aftersigninFragment)
            } catch (e: Exception) {
                Toast.makeText(context, "Navigation error: ${e.message}", Toast.LENGTH_SHORT).show()
            }
        }
        
        btnNavHydration.setOnClickListener {
            try {
                findNavController().navigate(R.id.hydrationFragment)
            } catch (e: Exception) {
                Toast.makeText(context, "Navigation error: ${e.message}", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun selectMood(moodType: MoodType) {
        // Add visual feedback
        showMoodSelection(moodType)
        
        // Save mood to repository
        moodRepository.addMoodEntry(moodType)
        
        // Update UI
        updateMoodHistory()
        
        // Show confirmation
        Toast.makeText(
            context, 
            "Mood logged: ${moodType.displayName}", 
            Toast.LENGTH_SHORT
        ).show()
    }

    private fun showMoodSelection(selectedMood: MoodType) {
        // Reset all mood selections
        resetMoodSelections()
        
        // Highlight selected mood (you can customize this visual feedback)
        val selectedView = when (selectedMood) {
            MoodType.HAPPY -> moodHappy
            MoodType.NEUTRAL -> moodNeutral
            MoodType.SAD -> moodSad
            MoodType.EXCITED -> moodExcited
            MoodType.CALM -> moodCalm
            MoodType.STRESSED -> moodStressed
        }
        
        // Add visual feedback (you can customize this)
        selectedView.alpha = 0.7f
        selectedView.postDelayed({ selectedView.alpha = 1.0f }, 200)
    }

    private fun resetMoodSelections() {
        listOf(moodHappy, moodNeutral, moodSad, moodExcited, moodCalm, moodStressed)
            .forEach { it.alpha = 1.0f }
    }

    private fun updateUI() {
        // Update date
        val currentDate = SimpleDateFormat("EEEE, MMMM d, yyyy", Locale.getDefault()).format(Date())
        tvDate.text = "📅 $currentDate"
        
        // Update mood history
        updateMoodHistory()
    }

    private fun updateMoodHistory() {
        val moodEntries = moodRepository.getMoodEntries()
        moodHistoryAdapter.updateMoodEntries(moodEntries)
    }

    companion object {
        @JvmStatic
        fun newInstance() = Mood()
    }
}