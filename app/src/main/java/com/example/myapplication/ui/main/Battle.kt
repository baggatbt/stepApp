package com.example.myapplication.ui.main

import android.content.Context
import android.content.Intent
import android.view.View
import android.widget.Button
import com.example.myapplication.BattleActivity
import com.example.myapplication.MainActivity
import com.example.myapplication.QuestActivity
import com.example.myapplication.VictoryActivity


class Battle {
    companion object {
        var expGained = 0
    }

    fun start(player: Player, enemy: Enemy, context: Context) {
        println("You've been attack by a ${enemy.name}")
        var victoryActivity = VictoryActivity()
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
                //Updates player exp
                MainActivity.player.experience += enemy.experienceReward

                // Passing value to companion object so that it can be passed to victoryactivity
                expGained = enemy.experienceReward

                //Sends exp gained to victory screen for display
                victoryActivity.expGained = enemy.experienceReward

                val victoryIntent = Intent(context, VictoryActivity::class.java) //
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