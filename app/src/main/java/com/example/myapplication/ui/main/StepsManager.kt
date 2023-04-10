package com.example.myapplication.ui.main

import android.content.Context

class StepsManager(private val context: Context) {
    private val sharedPreferences = context.getSharedPreferences("steps_pref", Context.MODE_PRIVATE)

    var steps: Int
        get() = sharedPreferences.getInt("steps", 0)
        set(value) = sharedPreferences.edit().putInt("steps", value).apply()

    fun addSteps(amount: Int) {
        steps += amount
    }

    fun subtractSteps(amount: Int) {
        steps -= amount
    }
}
