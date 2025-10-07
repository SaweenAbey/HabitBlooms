package com.example.habitblooms.data

import android.content.Context
import android.content.SharedPreferences
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import java.util.*

class MoodRepository private constructor(context: Context) {
    
    private val sharedPreferences: SharedPreferences = 
        context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
    private val gson = Gson()
    
    companion object {
        private const val PREFS_NAME = "mood_prefs"
        private const val KEY_MOOD_ENTRIES = "mood_entries"
        
        @Volatile
        private var INSTANCE: MoodRepository? = null
        
        fun getInstance(context: Context): MoodRepository {
            return INSTANCE ?: synchronized(this) {
                INSTANCE ?: MoodRepository(context.applicationContext).also { INSTANCE = it }
            }
        }
    }
    
    fun addMoodEntry(moodType: MoodType, note: String = "") {
        val entries = getMoodEntries().toMutableList()
        val newEntry = MoodEntry(
            moodType = moodType,
            note = note
        )
        entries.add(0, newEntry) // Add to beginning for chronological order
        saveMoodEntries(entries)
    }
    
    fun getMoodEntries(): List<MoodEntry> {
        val json = sharedPreferences.getString(KEY_MOOD_ENTRIES, null)
        return if (json != null) {
            val type = object : TypeToken<List<MoodEntry>>() {}.type
            gson.fromJson(json, type) ?: emptyList()
        } else {
            // Return sample data if no entries exist
            getSampleMoodEntries()
        }
    }
    
    fun getTodayMoodEntries(): List<MoodEntry> {
        val today = Calendar.getInstance()
        today.set(Calendar.HOUR_OF_DAY, 0)
        today.set(Calendar.MINUTE, 0)
        today.set(Calendar.SECOND, 0)
        today.set(Calendar.MILLISECOND, 0)
        val startOfDay = today.timeInMillis
        
        return getMoodEntries().filter { it.timestamp >= startOfDay }
    }
    
    fun getMoodEntriesGrouped(): Map<String, List<MoodEntry>> {
        return getMoodEntries().groupBy { it.moodType.displayName }
    }
    
    fun getMoodCount(moodType: MoodType): Int {
        return getMoodEntries().count { it.moodType == moodType }
    }
    
    fun deleteMoodEntry(entryId: String) {
        val entries = getMoodEntries().toMutableList()
        entries.removeAll { it.id == entryId }
        saveMoodEntries(entries)
    }
    
    fun clearAllEntries() {
        sharedPreferences.edit().remove(KEY_MOOD_ENTRIES).apply()
    }
    
    private fun saveMoodEntries(entries: List<MoodEntry>) {
        val json = gson.toJson(entries)
        sharedPreferences.edit().putString(KEY_MOOD_ENTRIES, json).apply()
    }
    
    private fun getSampleMoodEntries(): List<MoodEntry> {
        val now = System.currentTimeMillis()
        return listOf(
            MoodEntry(
                moodType = MoodType.HAPPY,
                timestamp = now - 3600000, // 1 hour ago
                note = "Had a great morning!"
            ),
            MoodEntry(
                moodType = MoodType.CALM,
                timestamp = now - 7200000, // 2 hours ago
                note = "Meditation session was peaceful"
            ),
            MoodEntry(
                moodType = MoodType.SAD,
                timestamp = now - 10800000, // 3 hours ago
                note = "Feeling a bit down today"
            ),
            MoodEntry(
                moodType = MoodType.EXCITED,
                timestamp = now - 14400000, // 4 hours ago
                note = "Looking forward to the weekend!"
            )
        )
    }
}
