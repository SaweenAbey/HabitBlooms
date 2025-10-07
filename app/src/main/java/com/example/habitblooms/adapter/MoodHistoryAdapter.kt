package com.example.habitblooms.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.habitblooms.R
import com.example.habitblooms.data.MoodEntry

class MoodHistoryAdapter(
    private var moodEntries: List<MoodEntry>,
    private val onItemClick: ((MoodEntry) -> Unit)? = null
) : RecyclerView.Adapter<MoodHistoryAdapter.MoodViewHolder>() {

    class MoodViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val tvMoodEmoji: TextView = view.findViewById(R.id.tv_mood_emoji)
        val tvMoodName: TextView = view.findViewById(R.id.tv_mood_name)
        val tvMoodDate: TextView = view.findViewById(R.id.tv_mood_date)
        val tvMoodTime: TextView = view.findViewById(R.id.tv_mood_time)
        val tvMoodCount: TextView = view.findViewById(R.id.tv_mood_count)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MoodViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_mood_entry, parent, false)
        return MoodViewHolder(view)
    }

    override fun onBindViewHolder(holder: MoodViewHolder, position: Int) {
        val moodEntry = moodEntries[position]
        
        holder.tvMoodEmoji.text = moodEntry.moodType.emoji
        holder.tvMoodName.text = moodEntry.moodType.displayName
        holder.tvMoodDate.text = moodEntry.getFormattedDate()
        holder.tvMoodTime.text = moodEntry.getFormattedTime()
        
        // Count how many times this mood was logged today
        val todayEntries = moodEntries.filter { 
            it.moodType == moodEntry.moodType && 
            it.getFormattedDate() == moodEntry.getFormattedDate() 
        }
        holder.tvMoodCount.text = todayEntries.size.toString()
        
        holder.itemView.setOnClickListener {
            onItemClick?.invoke(moodEntry)
        }
    }

    override fun getItemCount() = moodEntries.size

    fun updateMoodEntries(newEntries: List<MoodEntry>) {
        moodEntries = newEntries
        notifyDataSetChanged()
    }
}
