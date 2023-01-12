package com.example.myapplication

import android.os.Bundle
import android.view.View
import android.content.Intent
import android.content.Context
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.example.myapplication.ui.main.Quest
import com.example.myapplication.ui.main.Player

class QuestActivity : AppCompatActivity()   {


     var currentSteps = MainActivity.currentSteps

     var player = MainActivity.player



    // TODO: pass all this to the use Steps so you dont copy and past the function for each new quest
    //  var questOne = Quest("First Quest", 10, experienceGained = 3, goldGained = 1)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_quest)
        println(currentSteps)
        println(player.defense)



    }

    fun backToMap(view: View?) {
        val intent = Intent(this@QuestActivity, MapActivity::class.java)
        startActivity(intent)
        MainActivity.currentSteps = currentSteps

    }

    fun battleOneStart(view: View?) {
        // Create an intent to launch the BattleActivity
        val intent = Intent(this@QuestActivity, BattleActivity::class.java)
        startActivity(intent)
    }







    fun useSteps(view: View) {
        val quest = Quest("First Quest", 10, experienceGained = 3, goldGained = 1)

        if (currentSteps - quest.stepCost >= 0) {
            currentSteps -= quest.stepCost;

            player.experience += quest.experienceGained
            checkIfPlayerLevelUp()

            player.gold += quest.goldGained
            Toast.makeText( this,"You've gained " + quest.experienceGained + " exp!" + " and " + quest.goldGained + " gold", Toast.LENGTH_LONG).show()

        }
    }

    fun saveData() {

        // Shared Preferences will allow saving
        // and retrieve data in the form of key,value pair.
        // In this function save data
        val sharedPreferences = getSharedPreferences("myPrefs", Context.MODE_PRIVATE)
        val editor = sharedPreferences.edit()
        editor.putInt("key1", MainActivity.currentSteps)
        editor.putInt("playerLevel", MainActivity.player.level)
        editor.putInt("playerExp", MainActivity.player.experience)
        editor.putInt("playerGold", MainActivity.player.gold)
        editor.apply()

    }

    fun calculateExpToLevel(currentLevel: Int): Int {
      return currentLevel * 10 + Math.pow(currentLevel.toDouble(), 1.5).toInt()
    }
    //TODO: change this to handle if a player levels more than once.
    fun checkIfPlayerLevelUp() {
        val expToLevel = calculateExpToLevel(player.level)
        if (player.experience > expToLevel) {
            player.level += 1
            Toast.makeText(this, "You are now level " + player.level, Toast.LENGTH_SHORT).show()
        }
    }

    override fun onPause(){
        super.onPause()
        println("paused")
        println(currentSteps)
        saveData()
    }






}