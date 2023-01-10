package com.example.myapplication.ui.main

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.animation.ObjectAnimator
import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.CountDownTimer
import android.view.MotionEvent
import android.view.View
import kotlinx.coroutines.Runnable

import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import com.example.myapplication.*
import kotlinx.coroutines.NonCancellable.cancel
import org.w3c.dom.Text
import java.util.*
import kotlin.properties.Delegates


class Battle {
    companion object {
        var expGained = 0
        var goldGained = 0
    }

    lateinit var battlePlayerTextView: TextView
    lateinit var battleEnemyTextView: TextView
    lateinit var attackButton: Button
    lateinit var defendButton: Button
    lateinit var enemy: Enemy
    lateinit var player: Player
    lateinit var context: Context
    lateinit var basicKnight : ImageView
    lateinit var goblinPicture : ImageView
    lateinit var rootView: View
    var runnable: Runnable = Runnable {}



    fun checkVictoryAndDefeat(rootView: View) {
        val victoryActivity = VictoryActivity()
        // Check if the enemy is defeated
        if (enemy.health <= 0) {
            battlePlayerTextView.text = "${battlePlayerTextView.text}\nYou have defeated the ${enemy.name}!"


            //Updates player exp and gold
            MainActivity.player.experience += enemy.experienceReward
            MainActivity.player.gold += enemy.goldReward

            // Passing values to companion object so that it can be passed to victory activity
            expGained = enemy.experienceReward
            goldGained = enemy.goldReward

            //Sends exp gained to victory screen for display
            victoryActivity.expGained = enemy.experienceReward

            // Use View.postDelayed to delay the transition to the rewards screen
            battlePlayerTextView.postDelayed({
                //Moves to the rewards screen
                val victoryIntent = Intent(context, VictoryActivity::class.java)
                context.startActivity(victoryIntent)
            }, 2000) // Delay for 2 seconds (2000 milliseconds)
            rootView.removeCallbacks(runnable)
        }
        // Check if the player is defeated
        if (player.health <= 0) {
            battlePlayerTextView.text = "${battlePlayerTextView.text}\nYou have been defeated by the ${enemy.name}!"
            attackButton.isEnabled = false
            defendButton.isEnabled = false
            rootView.removeCallbacks(runnable)

        }

    }


    fun start(player: Player, enemy: Enemy, context: Context) {
        this.player = player
        this.enemy = enemy
        this.context = context
        battlePlayerTextView = (context as Activity).findViewById(R.id.battlePlayerText)
        battleEnemyTextView = (context as Activity).findViewById(R.id.battleEnemyText)
        attackButton = (context as Activity).findViewById(R.id.attackButton)
        defendButton = (context as Activity).findViewById(R.id.defenseButton)
        basicKnight = (context as Activity).findViewById(R.id.basicKnight)
        goblinPicture = (context as Activity).findViewById(R.id.goblinImage)
        rootView = (context as Activity).findViewById<View>(R.id.root)
        var countDownTimer: CountDownTimer? = null
        var timerRunning = false

        attackButton.setOnClickListener {
            attack()
        }

        //This runs the enemy attack every 3 seconds as long as the player is above 0
        runnable = Runnable {
            if (player.health > 0) {
                enemyAttack()
                rootView.postDelayed(runnable, 3000)

            }
            else {
                checkVictoryAndDefeat(rootView)

            }
        }
        rootView.postDelayed(runnable, 3000)

    }



    private var attackOnCooldown = false
    private val cooldownPeriod: Long = 1000
    //TODO: Combat is turn based around timers? Enemy attacks off cooldown, player can respond/attack on their own with cooldowns
    //this allows a almost active combat with possibilities for reactive play. EX: player use shield right before enemy hits
    //Allow player to make macros for autocombat, such as use ability A/B/C off cooldown
    //function, isBattleRunning() to keep allowing inputs and cooldowns
    private fun attack() {
        // code to handle the player's attack
        animateKnight()
        battlePlayerTextView.text = "You attack ${enemy.name} for ${player.attack} damage"
        enemy.health -= player.attack
        checkVictoryAndDefeat(rootView)

        //start cooldown
        attackOnCooldown = true
        attackButton.isEnabled = false
        attackButton.text = "Cooldown"
        attackButton.postDelayed({
            attackOnCooldown = false
            attackButton.isEnabled = true
            attackButton.text = "Attack" }, cooldownPeriod)
    }

    private fun enemyAttack(){
        player.health -= enemy.attack
        battleEnemyTextView.text = "The ${enemy.name} attacks you for ${enemy.attack} damage"
    }


    private fun animateKnight() {
        // Create an ObjectAnimator to animate the x position of the basicKnight image
        val animator = ObjectAnimator.ofFloat(basicKnight, "x", basicKnight.x, basicKnight.x + 50f)
        animator.duration = 500
        animator.start()

        // Add a listener to the animator to reverse the animation when it's finished
        animator.addListener(object : AnimatorListenerAdapter() {
            override fun onAnimationEnd(animation: Animator) {
                super.onAnimationEnd(animation)
                val reverseAnimator = ObjectAnimator.ofFloat(basicKnight, "x", basicKnight.x, basicKnight.x - 50f)
                reverseAnimator.duration = 500
                reverseAnimator.start()
            }
        })
    }

 /**  private fun animateGoblin() {
        // Create an ObjectAnimator to animate the x position of the basicKnight image
        val animator = ObjectAnimator.ofFloat(goblinPicture, "x", goblinPicture.x, goblinPicture.x - 50f)
        animator.duration = 500
        animator.start()

        // Add a listener to the animator to reverse the animation when it's finished
        animator.addListener(object : AnimatorListenerAdapter() {
            override fun onAnimationEnd(animation: Animator) {
                super.onAnimationEnd(animation)
                val reverseAnimator = ObjectAnimator.ofFloat(goblinPicture, "x", goblinPicture.x, goblinPicture.x + 50f)
                reverseAnimator.duration = 500
                reverseAnimator.start()
            }
        })
    } **/
}







