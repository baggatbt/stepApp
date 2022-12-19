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


    var currentSteps = 0;

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_quest)


    }

    fun backToMain(view: View?) {
        val intent = Intent(this@QuestActivity, MainActivity::class.java)
        startActivity(intent)
    }

    fun useSteps(view: View) {
        val quest = Quest("First Quest", 10)
        if (currentSteps - quest.stepCost >= 0) {
            currentSteps -= quest.stepCost;

            /**  var stepsTaken = findViewById<TextView>(R.id.tv_stepsTaken)
            stepsTaken.text = ("$currentSteps")
             **/

        }
    }
}