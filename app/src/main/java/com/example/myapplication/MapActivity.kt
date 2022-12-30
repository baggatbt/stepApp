package com.example.myapplication

import android.content.Context
import android.content.Intent
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.os.Bundle
import android.view.View
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.myapplication.MainActivity
import com.example.myapplication.ui.main.Quest
import com.example.myapplication.ui.main.Player

class MapActivity : AppCompatActivity(){

    var currentSteps = MainActivity.currentSteps

    var player = MainActivity.player


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_map   )

    }

    fun switchToQuests(view: View?) {
        val intent = Intent(this@MapActivity, QuestActivity::class.java)
        startActivity(intent)

    }
    fun backToMain(view: View?) {
        val intent = Intent(this@MapActivity, MainActivity::class.java)
        startActivity(intent)
        MainActivity.currentSteps = currentSteps

    }


}