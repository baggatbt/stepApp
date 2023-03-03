package com.example.myapplication

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.animation.ObjectAnimator
import android.content.Context
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
        player = Player(player.level,player.attack,player.defense,player.playerJob,player.gold,player.health,player.experience)

        val goblin = Enemy(EnemyType.GOBLIN)
        val slime = Enemy(EnemyType.SLIME)
        // Create a list of enemies
        val enemies = listOf(goblin,slime)

        // Shuffle the list and select the first element as the enemy
        enemy = enemies.shuffled().first()

        if (enemy.enemyName == "Goblin"){
            goblinImageView.visibility = View.VISIBLE
        }

        else if ( enemy.enemyName == "Slime"){
            slimeImageView.visibility = View.VISIBLE
        }

        println("You've been attacked by a ${enemy.enemyName}")

        //Initialize the battle
        battle = Battle()




        battle.start(this.player,this.enemy, this)
    }


}


