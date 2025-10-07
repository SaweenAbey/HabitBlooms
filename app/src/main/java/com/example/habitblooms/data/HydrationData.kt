package com.example.habitblooms.data

import java.util.*

data class HydrationStats(
    val dailyGoal: Int = 2000, // in ml
    val currentIntake: Int = 0, // in ml
    val dailyAverage: Double = 0.0, // in L
    val dayStreak: Int = 0,
    val bestDay: Double = 0.0 // in L
) {
    fun getProgressPercentage(): Int {
        return ((currentIntake.toDouble() / dailyGoal) * 100).toInt()
    }

    fun getFormattedProgress(): String {
        return String.format("%.1fL / %.1fL", currentIntake / 1000.0, dailyGoal / 1000.0)
    }

    fun getFormattedDailyAverage(): String {
        return String.format("%.1fL", dailyAverage)
    }

    fun getFormattedBestDay(): String {
        return String.format("%.1fL", bestDay)
    }
}

object HydrationRepository {
    private val intakes = mutableListOf<WaterIntake>()
    
    init {
        // Sample data matching the UI
        val now = System.currentTimeMillis()
        intakes.addAll(listOf(
            WaterIntake(id = 1, time = "7:53 PM", amountMl = 250, type = "Regular glass", cumulativeL = 0.3),
            WaterIntake(id = 2, time = "7:53 PM", amountMl = 100, type = "Small glass", cumulativeL = 0.4),
            WaterIntake(id = 3, time = "2:07 PM", amountMl = 500, type = "Large glass", cumulativeL = 0.9),
            WaterIntake(id = 4, time = "2:07 PM", amountMl = 100, type = "Small glass", cumulativeL = 1.0)
        ))
    }

    fun addIntake(amount: Int, description: String = getDescriptionForAmount(amount)) {
        val currentTime = java.text.SimpleDateFormat("h:mm a", java.util.Locale.getDefault()).format(java.util.Date())
        val newId = intakes.maxOfOrNull { it.id }?.plus(1) ?: 1
        val cumulativeL = intakes.sumOf { it.amountMl } / 1000.0 + amount / 1000.0
        intakes.add(WaterIntake(id = newId, time = currentTime, amountMl = amount, type = description, cumulativeL = cumulativeL))
    }

    fun getTodayIntakes(): List<WaterIntake> {
        // For now, return all intakes since we don't have timestamp in our WaterIntake
        return intakes.sortedByDescending { it.id }
    }

    fun getHydrationStats(): HydrationStats {
        val todayIntakes = getTodayIntakes()
        val currentIntake = todayIntakes.sumOf { it.amountMl }
        
        return HydrationStats(
            currentIntake = currentIntake,
            dailyAverage = 0.2,
            dayStreak = 0,
            bestDay = 1.0
        )
    }

    private fun getDescriptionForAmount(amount: Int): String {
        return when (amount) {
            100 -> "Small glass"
            250 -> "Regular glass"
            500 -> "Large glass"
            else -> "Custom amount"
        }
    }
}
