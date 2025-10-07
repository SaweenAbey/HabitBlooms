package com.example.habitblooms

import android.app.AlertDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.habitblooms.adapter.WaterIntakeAdapter
import com.example.habitblooms.data.HydrationRepository
import java.text.SimpleDateFormat
import java.util.*

class Hydration : Fragment() {
    
    private lateinit var tvGreeting: TextView
    private lateinit var tvDate: TextView
    private lateinit var tvProgress: TextView
    private lateinit var tvProgressPercentage: TextView
    private lateinit var progressBar: ProgressBar
    private lateinit var tvIntakeCount: TextView
    private lateinit var rvActivity: RecyclerView
    
    private lateinit var btn100ml: Button
    private lateinit var btn250ml: Button
    private lateinit var btn500ml: Button
    private lateinit var btnCustom: Button
    
    private lateinit var adapter: WaterIntakeAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_hydration, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        
        initViews(view)
        setupRecyclerView()
        setupClickListeners()
        updateUI()
    }

    private fun initViews(view: View) {
        tvGreeting = view.findViewById(R.id.tv_greeting)
        tvDate = view.findViewById(R.id.tv_date)
        tvProgress = view.findViewById(R.id.tv_water_progress)
        tvProgressPercentage = view.findViewById(R.id.tv_progress_percentage)
        progressBar = view.findViewById(R.id.progress_bar_hydration)
        tvIntakeCount = view.findViewById(R.id.tv_intake_count)
        // Remove references to non-existent views
        // tvDailyAverage = view.findViewById(R.id.tv_daily_average)
        // tvDayStreak = view.findViewById(R.id.tv_day_streak)
        // tvBestDay = view.findViewById(R.id.tv_best_day)
        // tvNextReminder = view.findViewById(R.id.tv_next_reminder)
        // switchReminders = view.findViewById(R.id.switch_reminders)
        rvActivity = view.findViewById(R.id.rv_water_intakes)
        
        btn100ml = view.findViewById(R.id.btn_100ml)
        btn250ml = view.findViewById(R.id.btn_250ml)
        btn500ml = view.findViewById(R.id.btn_500ml)
        btnCustom = view.findViewById(R.id.btn_custom)
    }

    private fun setupRecyclerView() {
        adapter = WaterIntakeAdapter(HydrationRepository.getTodayIntakes())
        rvActivity.layoutManager = LinearLayoutManager(context)
        rvActivity.adapter = adapter
    }

    private fun setupClickListeners() {
        btn100ml.setOnClickListener { addWaterIntake(100) }
        btn250ml.setOnClickListener { addWaterIntake(250) }
        btn500ml.setOnClickListener { addWaterIntake(500) }
        btnCustom.setOnClickListener { showCustomAmountDialog() }
        
        // Remove switchReminders reference since it doesn't exist in layout
        // switchReminders.setOnCheckedChangeListener { _, isChecked ->
        //     // Handle reminder toggle
        //     updateReminderStatus(isChecked)
        // }
    }

    private fun addWaterIntake(amount: Int) {
        HydrationRepository.addIntake(amount)
        updateUI()
        
        // Show a brief confirmation
        Toast.makeText(context, "Added ${amount}ml water!", Toast.LENGTH_SHORT).show()
    }

    private fun showCustomAmountDialog() {
        val builder = AlertDialog.Builder(requireContext())
        builder.setTitle("Custom Amount")
        
        val input = EditText(requireContext())
        input.hint = "Enter amount in ml"
        input.inputType = android.text.InputType.TYPE_CLASS_NUMBER
        builder.setView(input)
        
        builder.setPositiveButton("Add") { _, _ ->
            val amountText = input.text.toString()
            if (amountText.isNotEmpty()) {
                try {
                    val amount = amountText.toInt()
                    if (amount > 0 && amount <= 2000) {
                        addWaterIntake(amount)
                    } else {
                        Toast.makeText(context, "Please enter a valid amount (1-2000ml)", Toast.LENGTH_SHORT).show()
                    }
                } catch (e: NumberFormatException) {
                    Toast.makeText(context, "Please enter a valid number", Toast.LENGTH_SHORT).show()
                }
            }
        }
        
        builder.setNegativeButton("Cancel") { dialog, _ ->
            dialog.cancel()
        }
        
        builder.show()
    }

    private fun updateUI() {
        // Update greeting and date
        val currentDate = SimpleDateFormat("EEEE, MMMM d", Locale.getDefault()).format(Date())
        tvDate.text = "Today, $currentDate"
        
        // Update hydration stats
        val stats = HydrationRepository.getHydrationStats()
        tvProgress.text = stats.getFormattedProgress()
        tvProgressPercentage.text = "${stats.getProgressPercentage()}% of daily goal"
        progressBar.progress = stats.getProgressPercentage()
        
        // Update activity list
        val todayIntakes = HydrationRepository.getTodayIntakes()
        adapter.updateIntakes(todayIntakes)
        tvIntakeCount.text = "${todayIntakes.size} intakes"
        
        // Remove references to non-existent views
        // tvDailyAverage.text = stats.getFormattedDailyAverage()
        // tvDayStreak.text = stats.dayStreak.toString()
        // tvBestDay.text = stats.getFormattedBestDay()
        
        // Update next reminder
        // updateNextReminderText()
    }

    // Removed methods that reference non-existent views

    companion object {
        @JvmStatic
        fun newInstance() = Hydration()
    }
}