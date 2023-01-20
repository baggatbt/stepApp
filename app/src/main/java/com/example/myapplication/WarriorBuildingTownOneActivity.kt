package com.example.myapplication

import android.content.Intent
import android.hardware.Sensor.TYPE_STEP_COUNTER
import android.hardware.SensorEvent
import android.os.Bundle
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.myapplication.MainActivity.Companion.currentSteps
import com.example.myapplication.MainActivity.Companion.player
import com.example.myapplication.ui.main.Quest

class WarriorBuildingTownOneActivity: AppCompatActivity() {
    var currentSteps = MainActivity.currentSteps

    var player = MainActivity.player

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_warriorbuildingtownone)
        println(currentSteps)
        println(player.defense)

    }

    fun backToTown(view: View?) {
        val intent = Intent(this@WarriorBuildingTownOneActivity, MainActivity::class.java)
        startActivity(intent)
    }
    //TODO: DOESNT WORK, NEED TO DECREMENT EACH STEP
    fun startTraining(view: View){
        var stepsToGoView: TextView = findViewById(R.id.stepsToGo)
        var stepsToGo = 500
        stepsToGoView.visibility = View.VISIBLE
        var stepsTaken = TYPE_STEP_COUNTER
        if (stepsTaken == 1){
            stepsToGo -= 1
            stepsToGoView.text = ("$stepsTaken")
        }
    }



}