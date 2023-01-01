package com.example.myapplication

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.example.myapplication.ui.main.Battle
import com.example.myapplication.ui.main.Goblin
import org.w3c.dom.Text
import kotlin.properties.Delegates

class VictoryActivity: AppCompatActivity() {

    private lateinit var backButton: Button
    var expGained = Battle.expGained
    var goldGained = Battle.goldGained

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_victory)

        var expGainedView: TextView = findViewById(R.id.expGained)
        expGainedView.text = ("$expGained")
        println(expGainedView.text)

        var goldGainedView: TextView = findViewById(R.id.goldGained)
        goldGainedView.text = ("$goldGained")
        println(goldGainedView.text)

        val backButton: Button = findViewById(R.id.backButton)






    }

    fun backToQuestScreen(view: View) {
        val intent = Intent(this@VictoryActivity, QuestActivity::class.java)
        startActivity((intent))
    }
}