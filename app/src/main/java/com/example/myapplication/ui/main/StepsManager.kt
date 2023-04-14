package com.example.myapplication.ui.main

import android.content.Context
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.widget.Toast

interface StepListener {
    fun onStep(steps: Int)
}


class StepsManager(private val context: Context) : SensorEventListener {
    private val sharedPreferences = context.getSharedPreferences("steps_pref", Context.MODE_PRIVATE)
    private var sensorManager: SensorManager? = null
    private var running = false
    var totalSteps = 0f
    var previousTotalSteps = 0f
    private var stepListener: StepListener? = null


    var steps: Int
        get() = sharedPreferences.getInt("steps", 0)
        set(value) = sharedPreferences.edit().putInt("steps", value).apply()

    fun addSteps(amount: Int) {
        steps += amount
    }

    fun subtractSteps(amount: Int) {
        steps -= amount
    }

    init {
        sensorManager = context.getSystemService(Context.SENSOR_SERVICE) as SensorManager
    }

    fun onResume() {
        running = true
        val stepSensor = sensorManager?.getDefaultSensor(Sensor.TYPE_STEP_COUNTER)
        if (stepSensor == null) {
            // This will give a toast message to the user if there is no sensor in the device
            Toast.makeText(context, "No sensor detected on this device", Toast.LENGTH_SHORT).show()
        } else {
            // Rate suitable for the user interface
            sensorManager?.registerListener(this, stepSensor, SensorManager.SENSOR_DELAY_GAME)
            previousTotalSteps = totalSteps - steps // Add this line
        }
    }


    fun onPause() {
        running = false
        sensorManager?.unregisterListener(this)
    }

    fun setStepListener(listener: (Int) -> Unit) {
        stepListener = object : StepListener {
            override fun onStep(steps: Int) {
                listener(steps)
            }
        }
    }


    override fun onSensorChanged(event: SensorEvent?) {
        if (event != null && event.sensor.type == Sensor.TYPE_STEP_COUNTER) {
            totalSteps = event.values[0]
            steps = (totalSteps - previousTotalSteps).toInt()
            stepListener?.onStep(steps) // Add this line
            println("Steps: $steps")
        }
    }


    override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {
    }
}

