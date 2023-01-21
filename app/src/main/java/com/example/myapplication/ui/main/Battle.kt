package com.example.myapplication.ui.main

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.animation.ObjectAnimator
import android.animation.ValueAnimator
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.CountDownTimer
import android.view.View
import kotlinx.coroutines.Runnable

import android.widget.Button
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView
import com.example.myapplication.*
import com.example.myapplication.ui.main.Skill.Companion.defend
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
    lateinit var enemyAttackProgressBar: ProgressBar
    lateinit var playerHealthBar: ProgressBar
    lateinit var enemyHealthBar: ProgressBar
    var runnable: Runnable = Runnable {}
    private var isBattleOver = false


    fun checkVictoryAndDefeat(rootView: View) {
        val victoryActivity = VictoryActivity()
        // Check if the enemy is defeated
        if (enemy.health <= 0) {
            battlePlayerTextView.text = "${battlePlayerTextView.text}\nYou have defeated the ${enemy.name}!"
            isBattleOver = true


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
        enemyAttackProgressBar = (context as Activity).findViewById(R.id.enemyAttackTimerProgressBar)
        enemyAttackProgressBar.max = 100
        playerHealthBar = (context as Activity).findViewById(R.id.playerHealthBar)
        playerHealthBar.max = player.health
        playerHealthBar.progress = player.health
        enemyHealthBar =  (context as Activity).findViewById(R.id.enemyHealthBar)
        enemyHealthBar.max = enemy.health
        enemyHealthBar.progress = enemy.health

        println(player.defense)
        val animator = ValueAnimator.ofInt(0, 100).setDuration(3000)
        animator.addUpdateListener { valueAnimator ->
            enemyAttackProgressBar.progress = valueAnimator.animatedValue as Int
        }
        animator.start()

        var countDownTimer: CountDownTimer? = null
        var timerRunning = false

        attackButton.setOnClickListener {
            attack()
        }

        defendButton.setOnClickListener {
            defend()
        }

        //This runs the enemy attack every 3 seconds as long as the player is above 0
        runnable = Runnable {
            if (player.health > 0) {
                //This controls the progress bar that indicates when an enemy attacks
                animator.start()
                enemyAttack()
                enemyAttackProgressBar.progress = 0
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

    //Change this to Ability 1 (will handle calling abilities in this function)
    //For now it will be coded to be attack
    private fun attack() {
        // code to handle the player's attack
        if (!isBattleOver) {
            animateKnight()
            battlePlayerTextView.text = "You attack ${enemy.name} for ${player.attack} damage"
            var damageDealt = (player.attack - enemy.defense)
            enemyHealthBar.progress -= damageDealt
            basicAtk
            checkVictoryAndDefeat(rootView)
            //start cooldown
            attackOnCooldown = true
            attackButton.isEnabled = false
            attackButton.text = "Cooldown"
            attackButton.postDelayed({
                attackOnCooldown = false
                attackButton.isEnabled = true
                attackButton.text = "Attack"
            }, cooldownPeriod)
        }
    }
    //change this to Ability2
    private fun defend() {
        // code to use defend ability
        if (!isBattleOver) {

            battlePlayerTextView.text = "You increase your defense by 1"
            Skill.defend(player)
            println(player.defense)
            checkVictoryAndDefeat(rootView)

            //start cooldown
            attackOnCooldown = true
            defendButton.isEnabled = false
            defendButton.text = "Cooldown"
            defendButton.postDelayed({

                attackOnCooldown = false
                defendButton.isEnabled = true
                defendButton.text = "Defend"
            }, cooldownPeriod)
            println(player.defense)
        }
    }


    private fun enemyAttack() {
        var damageDealt = (enemy.attack - player.defense)
        if (damageDealt >= 0) {
            playerHealthBar.progress -= damageDealt
            battleEnemyTextView.text = "The ${enemy.name} attacks you for ${damageDealt} damage"
        } else {
            "The ${enemy.name} attacks you for 0 damage"
        }
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







