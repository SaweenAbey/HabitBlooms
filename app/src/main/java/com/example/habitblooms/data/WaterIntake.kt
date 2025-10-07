package com.example.habitblooms.data

data class WaterIntake(
    val id: Int,
    val time: String,
    val amountMl: Int,
    val type: String,
    val cumulativeL: Double
)