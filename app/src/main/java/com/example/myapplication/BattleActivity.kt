package com.example.myapplication

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.animation.ObjectAnimator
import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.LinearLayout
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout
import com.example.myapplication.ui.main.*
import com.example.myapplication.MainActivity


import kotlin.random.Random


class BattleActivity : AppCompatActivity() {
    private lateinit var abilityOneButton: Button
    private lateinit var enemy: Enemy
    lateinit var battle: Battle
    var player = MainActivity.player

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
            setContentView(R.layout.activity_battle)

        var goblinImageView: ImageView = findViewById(R.id.goblinImage)
        var slimeImageView: ImageView = findViewById(R.id.slimeImage)

        // Initialize the player
        player = Player(player.level,player.attack,player.defense,player.playerJob,player.gold,player.health,player.experience,player.loadout)

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

        //Creates the ability buttons and puts them over the battle view
        //Creates the ability buttons and puts them over the battle view
        val abilityCardsLayout = LayoutInflater.from(this).inflate(R.layout.ability_cards, null)
        val bottomLayout = findViewById<ConstraintLayout>(R.id.root)

// Add constraints to position the ability cards at the bottom of the root layout
        val params = ConstraintLayout.LayoutParams(
            ConstraintLayout.LayoutParams.MATCH_PARENT,
            ConstraintLayout.LayoutParams.WRAP_CONTENT
        )
        params.bottomToBottom = ConstraintLayout.LayoutParams.PARENT_ID
        abilityCardsLayout.layoutParams = params

        bottomLayout.addView(abilityCardsLayout)
        abilityOneButton = abilityCardsLayout.findViewById(R.id.ability_card_1)



        //Initialize the battle
        battle = Battle()




        battle.start(this.player,this.enemy, this)
    }


}


