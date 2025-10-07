package com.example.habitblooms

data class Habit(
    val id: Int,
    val title: String,
    val description: String,
    val iconRes: Int,
    val progress: Int, // 0-100
    var isCompleted: Boolean = false
)
