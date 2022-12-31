package com.example.myapplication.ui.main

import android.content.Context
import android.content.Intent
import android.view.View
import android.widget.Button
import com.example.myapplication.BattleActivity
import com.example.myapplication.QuestActivity


class Battle {


    fun start(player: Player, enemy: Enemy, context: Context) {
        println("You've been attack by a ${enemy.name}")

        // Start the battle loop
        while (player.health > 0 && enemy.health > 0) {
            // Player's turn
            println("Choose your attack!")
            //the ?.attack ?: 0 is a null check
            enemy.health -= player.skills["basic attack"]?.attack ?: 0
            println("You attack the ${enemy.name} for ${player.attack} damage!")

            // Check if the enemy is defeated
            if (enemy.health <= 0) {
                println("You have defeated the ${enemy.name}!")
                val victoryIntent = Intent(context, QuestActivity::class.java) //TODO: Change this line to go to a victory activity screen that shows rewards and gains, and then updates player object
                context.startActivity(victoryIntent)
            }

            // Enemy's turn
            player.health -= enemy.attack
            println("The ${enemy.name} attacks you for ${enemy.attack} damage!")

            // Check if the player is defeated
            if (player.health <= 0) {
                println("You have been defeated by the ${enemy.name}!")
                break
            }
        }
    }
}