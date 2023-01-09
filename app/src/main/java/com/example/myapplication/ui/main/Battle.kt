package com.example.myapplication.ui.main

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.CountDownTimer
import android.view.MotionEvent
import android.view.View

import android.widget.Button
import android.widget.TextView
import com.example.myapplication.*
import kotlinx.coroutines.NonCancellable.cancel
import java.util.*
import kotlin.properties.Delegates


class Battle {
    companion object {
        var expGained = 0
        var goldGained = 0
    }
    lateinit var battleTextView: TextView
    lateinit var attackButton: Button
    lateinit var defendButton: Button
    lateinit var enemy: Enemy
    lateinit var player: Player
    lateinit var context: Context




    fun checkVictory() {
        val victoryActivity = VictoryActivity()
        // Check if the enemy is defeated
        if (enemy.health <= 0) {
            battleTextView.text = "${battleTextView.text}\nYou have defeated the ${enemy.name}!"


            //Updates player exp and gold
            MainActivity.player.experience += enemy.experienceReward
            MainActivity.player.gold += enemy.goldReward

            // Passing values to companion object so that it can be passed to victory activity
            expGained = enemy.experienceReward
            goldGained = enemy.goldReward

            //Sends exp gained to victory screen for display
            victoryActivity.expGained = enemy.experienceReward

            // Use View.postDelayed to delay the transition to the rewards screen
            battleTextView.postDelayed({
                //Moves to the rewards screen
                val victoryIntent = Intent(context, VictoryActivity::class.java)
                context.startActivity(victoryIntent)
            }, 2000) // Delay for 2 seconds (2000 milliseconds)
        }
        // Check if the player is defeated
        if (player.health <= 0) {
            battleTextView.text = "${battleTextView.text}\nYou have been defeated by the ${enemy.name}!"
            attackButton.isEnabled = false
            defendButton.isEnabled = false
        }
    }

    @SuppressLint("ClickableViewAccessibility")
    fun start(player: Player, enemy: Enemy, context: Context, action: String) {
        this.player = player
        this.enemy = enemy
        this.context = context
        battleTextView = (context as Activity).findViewById(R.id.battle_text)
        attackButton = (context as Activity).findViewById(R.id.attackButton)
        defendButton = (context as Activity).findViewById(R.id.defenseButton)
        val rootView = (context as Activity).findViewById<View>(R.id.root)
        var countDownTimer: CountDownTimer? = null
        var timerRunning = false


      /**  when (action) {
            "attack" -> {
                attackButton.isEnabled = false

                // Only start the timer if it is not already running
                if (!timerRunning) {
                    countDownTimer = object : CountDownTimer(3000,1000) {
                        override fun onTick(millisUntilFinished: Long) {
                            //Checks to see time is between the range given.
                            if ((millisUntilFinished >= 500 && millisUntilFinished <= 1500)) {
                                // Attach the touch listener to the root view
                                rootView.setOnTouchListener { view, motionEvent ->
                                    when (motionEvent.action) {
                                        MotionEvent.ACTION_DOWN -> {
                                            // The root view has been tapped
                                            battleTextView.text = "bonus damage!"
                                            enemy.health -= player.skills["basic attack"]?.attack ?: 0
                                            checkVictory()
                                            rootView.setOnTouchListener(null)

                                            //Cancel the timer
                                            countDownTimer?.cancel()
                                            timerRunning = false
                                            attackButton.isEnabled = true
                                        }
                                    }
                                    true
                                }
                            } else {
                                // Remove the touch listener from the root view
                                rootView.setOnTouchListener(null)
                            }
                        }

                        override fun onFinish() {
                            // The timer has finished
                            timerRunning = false
                            countDownTimer?.cancel()
                            attackButton.isEnabled = true
                        }
                    }

                    // Set the timerRunning flag to true

                    countDownTimer.start()
                }

                // Player's turn
                enemy.health -= player.skills["basic attack"]?.attack ?: 0


                battleTextView.text = "You attack the ${enemy.name} for ${player.attack} damage!"
                checkVictory()



                // Enemy's turn
                player.health -= enemy.attack
                battleTextView.text = "${battleTextView.text}\nThe ${enemy.name} attacks you for ${enemy.attack} damage!"
                checkVictory()

            }
                "defend" -> {
                //add defense to the player
                player.defense += 1
                battleTextView.text = "You prepare for an attack, +1 Defense!"

                // Enemy's turn
                player.health -= (enemy.attack - player.defense)
                battleTextView.text = "${battleTextView.text}\nThe ${enemy.name} attacks you for ${enemy.attack - player.defense} damage!"

                //Take away the temporary defense increase
                player.defense -= 1
            }
    }
      */



}}

