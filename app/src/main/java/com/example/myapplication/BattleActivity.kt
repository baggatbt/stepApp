package com.example.myapplication

import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import com.example.myapplication.ui.main.*
import com.example.myapplication.MainActivity

import kotlin.random.Random


class BattleActivity : AppCompatActivity() {
    private lateinit var attackButton: Button
    private lateinit var enemy: Enemy
    lateinit var battle: Battle
    var player = MainActivity.player


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_battle)

        var goblinImageView: ImageView = findViewById(R.id.goblinImage)
        var slimeImageView: ImageView = findViewById(R.id.slimeImage)

        attackButton = findViewById(R.id.attackButton)

        // Initialize the player
        player = Player(player.level,player.gold,player.attack,player.defense,player.health,player.experience,player.skills)

        //Initialize the battle
        battle = Battle()

        // Create a list of enemies
        val enemies = listOf(Goblin(0, 0), Slime(0, 0))

        // Shuffle the list and select the first element as the enemy
        enemy = enemies.shuffled().first()

        if (enemy.name == "Goblin"){
            goblinImageView.visibility = View.VISIBLE
        }

        else if ( enemy.name == "Slime"){
            slimeImageView.visibility = View.VISIBLE
        }

        println("You've been attacked by a ${enemy.name}")
    }

    fun playerAttack(view: View) {
        val context = view.context
        battle.start(player, enemy, context)
    }
}


