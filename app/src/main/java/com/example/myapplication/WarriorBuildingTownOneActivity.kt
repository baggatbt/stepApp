package com.example.myapplication

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity


class WarriorBuildingTownOneActivity: AppCompatActivity(), SensorEventListener {
     private lateinit var sensorManager: SensorManager
     private lateinit var stepSensor: Sensor
     private var currentSteps = MainActivity.currentSteps

    lateinit var stepsToGoView: TextView
    lateinit var trainingButton: Button

    companion object{
        var stepsToGo: Int = 0
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_warriorbuildingtownone)
        trainingButton = findViewById(R.id.trainingButton)
        stepsToGoView = findViewById(R.id.stepsToGo)
        sensorManager = getSystemService(Context.SENSOR_SERVICE) as SensorManager
        stepSensor = sensorManager.getDefaultSensor(Sensor.TYPE_STEP_COUNTER)
        if (stepSensor == null) {
            // Step sensor not available
        } else {
            sensorManager.registerListener(this, stepSensor, SensorManager.SENSOR_DELAY_NORMAL)
        }
        if (stepsToGo >= 0){
            stepsToGoView.text = ("$stepsToGo")
            stepsToGoView.visibility = View.VISIBLE
        }
    }

    override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {
        // implementation here
    }

    override fun onSensorChanged(event: SensorEvent?) {
        if (stepsToGo > 0) {
            stepsToGo--
            stepsToGoView.text = ("$stepsToGo")
        }
        println(stepsToGo)

    }



    fun backToTown(view: View?) {
        val intent = Intent(this@WarriorBuildingTownOneActivity, MainActivity::class.java)
        startActivity(intent)
    }

    fun startTraining(view: View?){
        stepsToGo = 500
        stepsToGoView.visibility = View.VISIBLE
            stepsToGoView.text = ("$stepsToGo")
        }
    }



