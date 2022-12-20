package com.example.myapplication

import com.example.myapplication.MainActivity
import android.os.Bundle
import android.view.View
import android.content.Intent
import android.content.Context
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.example.myapplication.ui.main.Quest

class QuestActivity : AppCompatActivity()   {


     var currentSteps = MainActivity.currentSteps

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
            println(currentSteps)

            /**  var stepsTaken = findViewById<TextView>(R.id.tv_stepsTaken)
            stepsTaken.text = ("$currentSteps")
             **/

        }
    }

    private fun saveData(){
        val sharedPreferences = getSharedPreferences("myPrefs", Context.MODE_PRIVATE)
        val editor = sharedPreferences.edit()
        editor.putInt("key1", MainActivity.currentSteps)
        editor.apply()

    }

    override fun onPause(){
        super.onPause()
        println("paused")
        println(currentSteps)
        saveData()
    }


}