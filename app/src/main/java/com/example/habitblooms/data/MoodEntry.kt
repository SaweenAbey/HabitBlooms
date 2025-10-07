package com.example.habitblooms.data

import java.text.SimpleDateFormat
import java.util.*

data class MoodEntry(
    val id: String = UUID.randomUUID().toString(),
    val moodType: MoodType,
    val timestamp: Long = System.currentTimeMillis(),
    val note: String = ""
) {
    fun getFormattedTime(): String {
        val calendar = Calendar.getInstance()
        calendar.timeInMillis = timestamp
        val hour = calendar.get(Calendar.HOUR_OF_DAY)
        val minute = calendar.get(Calendar.MINUTE)
        val amPm = if (hour < 12) "AM" else "PM"
        val displayHour = if (hour == 0) 12 else if (hour > 12) hour - 12 else hour
        return String.format("%d:%02d %s", displayHour, minute, amPm)
    }

    fun getFormattedDate(): String {
        val sdf = SimpleDateFormat("MMM dd", Locale.getDefault())
        return sdf.format(Date(timestamp))
    }

    fun getFormattedDateTime(): String {
        val sdf = SimpleDateFormat("MMM dd, yyyy", Locale.getDefault())
        return sdf.format(Date(timestamp))
    }
}

enum class MoodType(val emoji: String, val displayName: String) {
    HAPPY("😊", "Feeling happy"),
    NEUTRAL("😐", "Feeling neutral"), 
    SAD("😢", "Feeling a bit sad"),
    EXCITED("🤩", "Feeling excited"),
    CALM("😌", "Feeling calm"),
    STRESSED("😰", "Feeling stressed");

    companion object {
        fun fromDisplayName(name: String): MoodType? {
            return values().find { it.displayName == name }
        }
    }
}
