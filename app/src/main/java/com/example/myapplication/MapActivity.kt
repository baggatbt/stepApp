package com.example.myapplication

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity

class MapActivity : AppCompatActivity(){

    var currentSteps = MainActivity.currentSteps

    var player = MainActivity.player


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_map   )

    }

    fun switchToQuests(view: View?) {
        val intent = Intent(this@MapActivity, TownOneActivity::class.java)
        startActivity(intent)

    }
    fun backToMain(view: View?) {
        val intent = Intent(this@MapActivity, MainActivity::class.java)
        startActivity(intent)
        MainActivity.currentSteps = currentSteps

    }


}