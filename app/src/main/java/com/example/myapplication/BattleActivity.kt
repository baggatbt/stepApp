package com.example.myapplication

import android.os.Bundle
import android.view.View
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import com.example.myapplication.ui.main.*

import kotlin.random.Random


class BattleActivity : AppCompatActivity() {
    private lateinit var attackButton: Button
    private lateinit var enemy: Enemy
    lateinit var battle: Battle

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_battle)

        attackButton = findViewById(R.id.attackButton)

        // Create a list of enemies
        val enemies = listOf(Goblin(0,0), Slime(0, 0))

        // Shuffle the list and select the first element as the enemy
        enemy = enemies.shuffled().first()

        println("You've been attacked by a ${enemy.name}")
    }

    fun playerAttack(view: View) {
        println(enemy.attack)
    }
}


