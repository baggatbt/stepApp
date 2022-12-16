package com.example.myapplication

import android.content.Context
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.TextView
import android.widget.Toast
import com.example.myapplication.R

class MainActivity : AppCompatActivity(), SensorEventListener {
    // Added SensorEventListener the com.example.myapplication.MainActivity class
    // Implement all the members in the class com.example.myapplication.MainActivity
    // after adding SensorEventListener

    // assigned sensorManger to nullable
    private var sensorManager: SensorManager? = null

    // Creating a variable which will give the running status
    // and initially given the boolean value as false
    private var running = false

    // Creating a variable which will counts total steps
    // and it has been given the value of 0 float
    private var totalSteps = 0f

    // Creating a variable  which counts previous total
    // steps and it has also been given the value of 0 float
    private var previousTotalSteps = 0f

    // Current steps are calculated by taking the difference of total steps
    // and previous steps
    var currentSteps = totalSteps.toInt() - previousTotalSteps.toInt()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        loadData()
        resetSteps()

        // Adding a context of SENSOR_SERVICE as Sensor Manager
        sensorManager = getSystemService(Context.SENSOR_SERVICE) as SensorManager
    }

    override fun onResume() {
        super.onResume()
        running = true

        // Returns the number of steps taken by the user since the last reboot while activated
        // This sensor requires permission android.permission.ACTIVITY_RECOGNITION.
        // So don't forget to add the following permission in AndroidManifest.xml present in manifest folder of the app.
        val stepSensor = sensorManager?.getDefaultSensor(Sensor.TYPE_STEP_COUNTER)


        if (stepSensor == null) {
            // This will give a toast message to the user if there is no sensor in the device
            Toast.makeText(this, "No sensor detected on this device", Toast.LENGTH_SHORT).show()
        } else {
            // Rate suitable for the user interface
            sensorManager?.registerListener(this, stepSensor, SensorManager.SENSOR_DELAY_UI)
        }
    }

    override fun onSensorChanged(event: SensorEvent?) {

        // Calling the TextView that we made in activity_main.xml
        // by the id given to that TextView
        var stepsTaken = findViewById<TextView>(R.id.tv_stepsTaken)

        if (running) {
            totalSteps = event!!.values[0]

            // Current steps are calculated by taking the difference of total steps
            // and previous steps
             currentSteps = totalSteps.toInt() - previousTotalSteps.toInt()

            // It will show the current steps to the user
            stepsTaken.text = ("$currentSteps")


        }
    }

    fun resetSteps() {
        var stepsTaken = findViewById<TextView>(R.id.tv_stepsTaken)
        stepsTaken.setOnClickListener {
            // This will give a toast message if the user want to reset the steps
            Toast.makeText(this, "Long tap to reset steps", Toast.LENGTH_SHORT).show()
        }

        stepsTaken.setOnLongClickListener {

            previousTotalSteps = totalSteps

            // When the user will click long tap on the screen,
            // the steps will be reset to 0
            stepsTaken.text = 0.toString()

            // This will save the data
            saveData()



            true
        }
    }

    private fun saveData() {

        // Shared Preferences will allow saving
        // and retrieve data in the form of key,value pair.
        // In this function save data
        val sharedPreferences = getSharedPreferences("myPrefs", Context.MODE_PRIVATE)

        val editor = sharedPreferences.edit()
        editor.putFloat("key1", previousTotalSteps)
        editor.apply()
    }

    private fun loadData() {


        val sharedPreferences = getSharedPreferences("myPrefs", Context.MODE_PRIVATE)
        val savedNumber = sharedPreferences.getFloat("key1", 0f)

        previousTotalSteps = savedNumber
    }

    override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {
    }

    fun useSteps(view: View) {
        if (currentSteps - 20 >= 0) {
            currentSteps -= 20;
            totalSteps -= 20;
            println(currentSteps);
            println("20 steps deducted");
            var stepsTaken = findViewById<TextView>(R.id.tv_stepsTaken)
            stepsTaken.text = ("$currentSteps")
            //TODO: get rid of the hard coded values, make a quest class with these parameters to pass
        }
    }


}