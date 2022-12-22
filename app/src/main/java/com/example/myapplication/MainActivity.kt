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
import com.example.myapplication.ui.main.Quest
import com.example.myapplication.ui.main.Player


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
    companion object{

        var currentSteps = 0

        val player = Player("Test",1,0,0)


    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        title = "Step App"
        loadData()


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

        var levelDisplay = findViewById<TextView>(R.id.level)

        var expDisplay = findViewById<TextView>(R.id.exp)

        var goldDisplay = findViewById<TextView>(R.id.gold)

        if (running) {
            totalSteps = event!!.values[0]

            // Current steps are calculated by taking the difference of total steps
            // and previous steps
             var stepCountDifference = totalSteps.toInt() - previousTotalSteps.toInt()
            currentSteps++

            // It will show the current steps to the user
            stepsTaken.text = ("$currentSteps")
            levelDisplay.text = ("${player.level}")
            expDisplay.text = ("${player.experience}")
            goldDisplay.text = ("${player.gold}")



        }
    }

   /** fun resetSteps() {
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
            println(totalSteps)
            println(previousTotalSteps)
            println(currentSteps)

            // This will save the data
            saveData()

            true
        }
    }
   **/

       fun saveData() {

           // Shared Preferences will allow saving
           // and retrieve data in the form of key,value pair.
           // In this function save data
           val sharedPreferences = getSharedPreferences("myPrefs", Context.MODE_PRIVATE)
           val editor = sharedPreferences.edit()
           editor.putInt("key1", currentSteps)
           editor.putInt("playerLevel", player.level)
           editor.putInt("playerExp",player.experience)
           editor.putInt("playerGold", player.gold)
           editor.apply()
           println("saved")
       }


    private fun loadData() {


        val sharedPreferences = getSharedPreferences("myPrefs", Context.MODE_PRIVATE)
        currentSteps = sharedPreferences.getInt("key1", -1)
        player.level = sharedPreferences.getInt("playerLevel",1)
        player.experience = sharedPreferences.getInt("playerExp",0)
        player.gold  = sharedPreferences.getInt("playerGold",0)
    }


    override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {
    }


    fun switchToQuests(view: View?) {
        val intent = Intent(this@MainActivity, QuestActivity::class.java)
        startActivity(intent)

    }






    //Saves step data on closing the app
    override fun onStop() {
        super.onStop()
        saveData()
    }
}