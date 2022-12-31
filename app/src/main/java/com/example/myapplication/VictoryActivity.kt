package com.example.myapplication

import android.annotation.SuppressLint
import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class VictoryActivity: AppCompatActivity() {

    private lateinit var backButton: Button


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_victory)

        var expGained: TextView = findViewById(R.id.expGained)
        var goldGained: TextView = findViewById(R.id.goldGained)

        val backButton: Button = findViewById(R.id.backButton)
    }
}