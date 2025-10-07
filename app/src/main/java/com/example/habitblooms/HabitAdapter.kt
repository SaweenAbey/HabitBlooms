package com.example.habitblooms

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class HabitAdapter(
    private var habits: MutableList<Habit>,
    private val onHabitClick: (Habit) -> Unit
) : RecyclerView.Adapter<HabitAdapter.HabitViewHolder>() {

    class HabitViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val iconImageView: ImageView = itemView.findViewById(R.id.iv_habit_icon)
        val titleTextView: TextView = itemView.findViewById(R.id.tv_habit_title)
        val descriptionTextView: TextView = itemView.findViewById(R.id.tv_habit_description)
        val progressBar: ProgressBar = itemView.findViewById(R.id.progress_bar_habit)
        val progressText: TextView = itemView.findViewById(R.id.tv_progress_text)
        val completeButton: TextView = itemView.findViewById(R.id.btn_mark_complete)
        val completeCheckbox: CheckBox = itemView.findViewById(R.id.cb_complete)
        val optionsButton: ImageView = itemView.findViewById(R.id.iv_options)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HabitViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_habit, parent, false)
        return HabitViewHolder(view)
    }

    override fun onBindViewHolder(holder: HabitViewHolder, position: Int) {
        val habit = habits[position]
        
        holder.iconImageView.setImageResource(habit.iconRes)
        holder.titleTextView.text = habit.title
        holder.descriptionTextView.text = habit.description
        
        // Set progress
        holder.progressBar.progress = habit.progress
        holder.progressText.text = "${habit.progress}%"
        
        // Handle completion state
        if (habit.isCompleted) {
            holder.completeCheckbox.isChecked = true
            holder.completeButton.visibility = View.GONE
            holder.completeCheckbox.visibility = View.VISIBLE
            holder.progressBar.progress = 100
            holder.progressText.text = "100%"
            holder.progressText.setTextColor(holder.itemView.context.getColor(R.color.forest_green))
            holder.progressBar.progressDrawable = holder.itemView.context.getDrawable(R.drawable.progress_bar_completed)
            // Set completed background
            holder.itemView.background = holder.itemView.context.getDrawable(R.drawable.habit_card_completed_background)
        } else {
            holder.completeCheckbox.isChecked = false
            holder.completeButton.visibility = View.VISIBLE
            holder.completeCheckbox.visibility = View.GONE
            holder.progressText.setTextColor(holder.itemView.context.getColor(R.color.forest_green))
            holder.progressBar.progressDrawable = holder.itemView.context.getDrawable(R.drawable.progress_bar_habit)
            // Set normal background
            holder.itemView.background = holder.itemView.context.getDrawable(R.drawable.habit_card_background)
        }
        
        // Set click listeners
        holder.completeButton.setOnClickListener {
            onHabitClick(habit)
        }
        
        holder.completeCheckbox.setOnClickListener {
            onHabitClick(habit)
        }
        
        holder.optionsButton.setOnClickListener {
            // Handle options menu
            // For now, just toggle completion
            onHabitClick(habit)
        }
    }

    override fun getItemCount(): Int = habits.size
    
    fun getHabits(): List<Habit> = habits.toList()
    
    fun addHabit(habit: Habit) {
        habits.add(habit)
        notifyItemInserted(habits.size - 1)
    }
}
