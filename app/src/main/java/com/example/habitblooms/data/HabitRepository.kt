package com.example.habitblooms.data

import android.content.Context
import android.content.SharedPreferences
import com.example.habitblooms.R
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import java.util.*

class HabitRepository private constructor(context: Context) {
    
    private val sharedPreferences: SharedPreferences = 
        context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
    private val gson = Gson()
    
    companion object {
        private const val PREFS_NAME = "habit_prefs"
        private const val KEY_HABITS = "habits"
        
        @Volatile
        private var INSTANCE: HabitRepository? = null
        
        fun getInstance(context: Context): HabitRepository {
            return INSTANCE ?: synchronized(this) {
                INSTANCE ?: HabitRepository(context.applicationContext).also { INSTANCE = it }
            }
        }
    }
    
    fun getAllHabits(): List<Habit> {
        val json = sharedPreferences.getString(KEY_HABITS, null)
        return if (json != null) {
            val type = object : TypeToken<List<Habit>>() {}.type
            gson.fromJson(json, type) ?: getDefaultHabits()
        } else {
            getDefaultHabits()
        }
    }
    
    fun getTodayHabits(): List<Habit> {
        return getAllHabits().map { habit ->
            // Reset daily progress if it's a new day
            if (!habit.isCompletedToday()) {
                habit.copy(currentValue = 0, isCompleted = false, completedDate = null)
            } else {
                habit
            }
        }
    }
    
    fun updateHabit(habit: Habit) {
        val habits = getAllHabits().toMutableList()
        val index = habits.indexOfFirst { it.id == habit.id }
        if (index != -1) {
            habits[index] = habit
        } else {
            habits.add(habit)
        }
        saveHabits(habits)
    }
    
    fun markHabitCompleted(habitId: String) {
        val habits = getAllHabits().toMutableList()
        val habit = habits.find { it.id == habitId }
        habit?.let {
            it.markCompleted()
            updateHabit(it)
        }
    }
    
    fun incrementHabitProgress(habitId: String) {
        val habits = getAllHabits().toMutableList()
        val habit = habits.find { it.id == habitId }
        habit?.let {
            it.incrementProgress()
            updateHabit(it)
        }
    }
    
    fun getTodayProgress(): Int {
        val todayHabits = getTodayHabits()
        if (todayHabits.isEmpty()) return 0
        
        val completedCount = todayHabits.count { it.isCompleted }
        return ((completedCount.toFloat() / todayHabits.size) * 100).toInt()
    }
    
    fun getCompletedHabitsCount(): Int {
        return getTodayHabits().count { it.isCompleted }
    }
    
    fun getTotalHabitsCount(): Int {
        return getTodayHabits().size
    }
    
    fun addHabit(habit: Habit) {
        val habits = getAllHabits().toMutableList()
        habits.add(habit)
        saveHabits(habits)
    }
    
    fun deleteHabit(habitId: String) {
        val habits = getAllHabits().toMutableList()
        habits.removeAll { it.id == habitId }
        saveHabits(habits)
    }
    
    fun clearAllHabits() {
        sharedPreferences.edit().remove(KEY_HABITS).apply()
    }
    
    private fun saveHabits(habits: List<Habit>) {
        val json = gson.toJson(habits)
        sharedPreferences.edit().putString(KEY_HABITS, json).apply()
    }
    
    private fun getDefaultHabits(): List<Habit> {
        return listOf(
            Habit(
                title = "Drink Water",
                description = "Aim for 8 glasses of water throughout the day to stay hydrated and energized.",
                iconResId = R.drawable.water,
                targetValue = 8,
                currentValue = 6
            ),
            Habit(
                title = "Meditate",
                description = "Dedicate 15 minutes to mindfulness meditation for mental clarity and stress reduction.",
                iconResId = R.drawable.mood,
                targetValue = 1,
                currentValue = 1,
                isCompleted = true,
                completedDate = System.currentTimeMillis()
            ),
            Habit(
                title = "Daily Exercise",
                description = "Engage in 30 minutes of moderate physical activity to boost your mood and fitness.",
                iconResId = R.drawable.habbitt,
                targetValue = 1,
                currentValue = 0
            ),
            Habit(
                title = "Read a Book",
                description = "Spend 20 minutes reading before bed to relax your mind and learn something new.",
                iconResId = R.drawable.ic_list,
                targetValue = 1,
                currentValue = 0
            ),
            Habit(
                title = "Mindful Eating",
                description = "Practice mindful eating by paying attention to your food and body's signals.",
                iconResId = R.drawable.hydration,
                targetValue = 1,
                currentValue = 0
            )
        )
    }
}
