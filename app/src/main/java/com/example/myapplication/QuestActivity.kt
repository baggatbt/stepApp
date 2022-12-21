package com.example.myapplication

import com.example.myapplication.MainActivity
import android.os.Bundle
import android.view.View
import android.content.Intent
import android.content.Context
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
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



    }

    fun backToMain(view: View?) {
        val intent = Intent(this@QuestActivity, MainActivity::class.java)
        startActivity(intent)
        MainActivity.currentSteps = currentSteps

    }

    fun useSteps(view: View) {
        val quest = Quest("First Quest", 10, experienceGained = 3, goldGained = 1)

        if (currentSteps - quest.stepCost >= 0) {
            currentSteps -= quest.stepCost;

            player.experience += quest.experienceGained

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
        println("saved")
    }

    override fun onPause(){
        super.onPause()
        println("paused")
        println(currentSteps)
        saveData()
    }


}