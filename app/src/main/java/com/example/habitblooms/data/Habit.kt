package com.example.habitblooms.data

import com.example.habitblooms.R
import java.util.*

data class Habit(
    val id: String = UUID.randomUUID().toString(),
    val title: String,
    val description: String,
    val iconResId: Int,
    val targetValue: Int = 1, // Target number of times per day
    var currentValue: Int = 0, // Current progress
    var isCompleted: Boolean = false,
    var completedDate: Long? = null,
    val streak: Int = 0,
    val createdAt: Long = System.currentTimeMillis()
) {
    fun getProgressPercentage(): Int {
        return if (targetValue > 0) {
            ((currentValue.toFloat() / targetValue) * 100).toInt().coerceAtMost(100)
        } else 0
    }

    fun getFormattedProgress(): String {
        return "$currentValue / $targetValue"
    }

    fun isCompletedToday(): Boolean {
        val today = Calendar.getInstance()
        today.set(Calendar.HOUR_OF_DAY, 0)
        today.set(Calendar.MINUTE, 0)
        today.set(Calendar.SECOND, 0)
        today.set(Calendar.MILLISECOND, 0)
        val startOfDay = today.timeInMillis

        val completedDateValue = completedDate
        return completedDateValue != null && completedDateValue >= startOfDay
    }

    fun markCompleted() {
        if (!isCompletedToday()) {
            // Reset current value for new day
            currentValue = 0
        }
        currentValue = targetValue
        isCompleted = true
        completedDate = System.currentTimeMillis()
    }

    fun incrementProgress() {
        if (currentValue < targetValue) {
            currentValue++
            if (currentValue >= targetValue) {
                markCompleted()
            }
        }
    }
}

enum class HabitType(val displayName: String, val iconResId: Int) {
    DRINK_WATER("Drink Water", R.drawable.water),
    MEDITATE("Meditate", R.drawable.mood),
    EXERCISE("Daily Exercise", R.drawable.habbitt),
    READ_BOOK("Read a Book", R.drawable.ic_list),
    MINDFUL_EATING("Mindful Eating", R.drawable.hydration);

    companion object {
        fun fromDisplayName(name: String): HabitType? {
            return values().find { it.displayName == name }
        }
    }
}
