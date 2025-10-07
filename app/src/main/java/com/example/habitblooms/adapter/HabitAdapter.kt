package com.example.habitblooms.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.CheckBox
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.ProgressBar
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.habitblooms.R
import com.example.habitblooms.data.Habit

class HabitAdapter(
    private var habits: List<Habit>,
    private val onHabitClick: ((Habit) -> Unit)? = null,
    private val onMarkComplete: ((Habit) -> Unit)? = null
) : RecyclerView.Adapter<HabitAdapter.HabitViewHolder>() {

    class HabitViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val ivHabitIcon: ImageView = view.findViewById(R.id.iv_habit_icon)
        val tvHabitTitle: TextView = view.findViewById(R.id.tv_habit_title)
        val tvHabitDescription: TextView = view.findViewById(R.id.tv_habit_description)
        val layoutProgress: LinearLayout = view.findViewById(R.id.layout_progress)
        val progressBarHabit: ProgressBar = view.findViewById(R.id.progress_bar_habit)
        val tvProgressText: TextView = view.findViewById(R.id.tv_progress_text)
        val btnMarkComplete: TextView = view.findViewById(R.id.btn_mark_complete)
        val cbCompleted: CheckBox = view.findViewById(R.id.cb_complete)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HabitViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_habit, parent, false)
        return HabitViewHolder(view)
    }

    override fun onBindViewHolder(holder: HabitViewHolder, position: Int) {
        val habit = habits[position]
        
        // Set habit details
        holder.ivHabitIcon.setImageResource(habit.iconResId)
        holder.tvHabitTitle.text = habit.title
        holder.tvHabitDescription.text = habit.description
        
        // Handle progress display
        if (habit.targetValue > 1) {
            // Show progress bar for habits with multiple targets
            holder.layoutProgress.visibility = View.VISIBLE
            holder.progressBarHabit.progress = habit.getProgressPercentage()
            holder.tvProgressText.text = "${habit.getProgressPercentage()}%"
            holder.btnMarkComplete.visibility = View.VISIBLE
            holder.cbCompleted.visibility = View.GONE
        } else {
            // Show checkbox for simple habits
            holder.layoutProgress.visibility = View.GONE
            holder.btnMarkComplete.visibility = View.GONE
            holder.cbCompleted.visibility = View.VISIBLE
            holder.cbCompleted.isChecked = habit.isCompleted
        }
        
        // Set button text based on completion status
        if (habit.isCompleted) {
            holder.btnMarkComplete.text = "Completed"
            holder.btnMarkComplete.isEnabled = false
        } else {
            holder.btnMarkComplete.text = "Mark as Complete"
            holder.btnMarkComplete.isEnabled = true
        }
        
        // Set click listeners
        holder.itemView.setOnClickListener {
            onHabitClick?.invoke(habit)
        }
        
        holder.btnMarkComplete.setOnClickListener {
            onMarkComplete?.invoke(habit)
        }
        
        holder.cbCompleted.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                onMarkComplete?.invoke(habit)
            }
        }
    }

    override fun getItemCount() = habits.size

    fun updateHabits(newHabits: List<Habit>) {
        habits = newHabits
        notifyDataSetChanged()
    }
    
    fun getHabits(): List<Habit> {
        return habits
    }
}
