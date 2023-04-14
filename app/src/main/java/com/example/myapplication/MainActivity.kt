package com.example.myapplication

import android.content.Context
import android.content.Intent
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import android.widget.Button
import android.widget.PopupMenu
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.myapplication.jobClasses.Warrior.Companion.warrior
import com.example.myapplication.ui.main.*


class MainActivity : AppCompatActivity() {
    var stepsToGo = WarriorBuildingTownOneActivity.stepsToGo

    private lateinit var stepsManager: StepsManager


    // Current steps are calculated by taking the difference of total steps
    // and previous steps
    // MainActivity.kt

    companion object {
        var currentSteps = 0
        var initialLoadout = Player.generateLoadoutForClass("Warrior")
        var player = Player(
            name = "Player",
            speed = 1,
            health = 10,
            level = 1,
            attack = 10,
            defense = 5,
            playerClass = warrior,
            gold = 0,
            experience = 0,
            loadout = initialLoadout
        )
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        stepsManager.setStepListener { steps ->
            onSensorChanged(steps)
        }


        stepsManager = StepsManager(this)
        loadData() // Add this line

        stepsManager.setStepListener { steps ->
            currentSteps = steps
            updateUI()
        }
        updateUI() // Add this line
    }

    private fun updateUI() {
        val stepsTaken = findViewById<TextView>(R.id.tv_stepsTaken)
        val levelDisplay = findViewById<TextView>(R.id.level)
        val expDisplay = findViewById<TextView>(R.id.exp)
        val goldDisplay = findViewById<TextView>(R.id.gold)

        stepsTaken.text = "$currentSteps"
        levelDisplay.text = "${player.level}"
        expDisplay.text = "${player.experience}"
        goldDisplay.text = "${player.gold}"
    }

    private fun onSensorChanged(steps: Int) {
        currentSteps = steps
        updateUI()
    }




    fun showMenu(view: View) {
        val popup = PopupMenu(this, view)
        popup.menuInflater.inflate(R.menu.menu_main, popup.menu)
        popup.show()
    }


    override fun onResume() {
        super.onResume()
        stepsManager.onResume()
        stepsManager.setStepListener { steps ->
            updateUI()
        }
    }

    override fun onPause() {
        super.onPause()
        stepsManager.onPause()
    }


    fun saveData() {
        val sharedPreferences = getSharedPreferences("myPrefs", Context.MODE_PRIVATE)
        val editor = sharedPreferences.edit()
        editor.putInt("key1", currentSteps)
        editor.putFloat("previousTotalSteps", stepsManager.previousTotalSteps)
        editor.putInt("playerLevel", player.level)
        editor.putInt("playerExp", player.experience)
        editor.putInt("playerGold", player.gold)
        editor.apply()
        println("saved")
    }



    private fun loadData() {
        val sharedPreferences = getSharedPreferences("myPrefs", Context.MODE_PRIVATE)
        currentSteps = sharedPreferences.getInt("key1", -1)
        if (currentSteps == -1) currentSteps = 0
        stepsManager.previousTotalSteps = sharedPreferences.getFloat("previousTotalSteps", stepsManager.totalSteps - currentSteps)
        player.level = sharedPreferences.getInt("playerLevel", 1)
        player.experience = sharedPreferences.getInt("playerExp", 0)
        player.gold = sharedPreferences.getInt("playerGold", 0)
    }





    //Saves step data on closing the app
    override fun onStop() {
        super.onStop()
        saveData()
    }

    fun switchToMap(item: MenuItem) {
        val intent = Intent(this@MainActivity, MapActivity::class.java)
        startActivity((intent))
    }

    fun startRandomBattle(item:MenuItem){
        // Create an intent to launch the BattleActivity
        val intent = Intent(this@MainActivity, CombatActivity::class.java)
        startActivity(intent)
    }

    fun switchToWarriorBuilding(view: View?) {
        // Create an intent to launch the BattleActivity
        val intent = Intent(this@MainActivity, WarriorBuildingTownOneActivity::class.java)
        startActivity(intent)
        WarriorBuildingTownOneActivity.stepsToGo = stepsToGo
    }


}