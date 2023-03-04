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
import android.view.ViewGroup
import android.widget.*

import androidx.constraintlayout.widget.ConstraintLayout
import com.example.myapplication.*
import kotlinx.coroutines.*
import kotlinx.coroutines.NonCancellable.cancel
import org.w3c.dom.Text
import java.util.*
import kotlin.properties.Delegates


class Battle {
    companion object {
        var expGained = 0
        var goldGained = 0
    }


    lateinit var abilityOneButton: Button
    lateinit var abilityTwoButton: Button
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
    lateinit var enemyImageIcon : ImageView
    lateinit var abilityOneSkill: Skills
    lateinit var abilityTwoSkill: Skills

    var enemyCanAttackFlag = false
    var runnable: Runnable = Runnable {}
    private var isBattleOver = false
    val enemyList = mutableListOf<Enemy>()



//INITIALIZE EVERYTHING REQUIRED FOR BATTLE TO RUN
    fun start(player: Player, enemy: Enemy, context: Context) {
        this.player = player

        //TODO: Implement setPlayerSkills()
       //THIS FUNCTION WILL ASSIGN THE EQUIPPED SKILLS TO THE BUTTONS
        // setPlayerSkills()
        //FOR NOW ITS HARDCODED TO SLASH FOR TESTING PURPOSES
        abilityOneSkill = Skills(AbilityType.SLASH)
        abilityTwoSkill = Skills(AbilityType.HEAVYSLASH)

        this.enemy = enemy
        enemyList.add(enemy)
        println(enemy)
        this.context = context

        abilityOneButton = (context as Activity).findViewById(R.id.ability_card_1)
        abilityTwoButton = (context as Activity).findViewById(R.id.ability_card_2)

         // turnOrderBar = (context as Activity).findViewById<ProgressBar>(R.id.turn_order_bar)
          enemyImageIcon = (context as Activity).findViewById<ImageView>(R.id.enemyImage)


    //  defendButton = (context as Activity).findViewById(R.id.defenseButton)
        basicKnight = (context as Activity).findViewById(R.id.basicKnight)
        goblinPicture = (context as Activity).findViewById(R.id.goblinImage)
        rootView = (context as Activity).findViewById<View>(R.id.root)

       // turnOrderBar = (context as Activity).findViewById(R.id.turn_order_bar)
       // turnOrderBar.max = 10
        playerHealthBar = (context as Activity).findViewById(R.id.playerHealthBar)
        playerHealthBar.max = player.health
        playerHealthBar.progress = player.health
        enemyHealthBar =  (context as Activity).findViewById(R.id.enemyHealthBar)
        enemyHealthBar.max = enemy.health
        enemyHealthBar.progress = enemy.health


        //THESE SET THE SKILLS TO THE CREATED ABILITY BUTTONS
        abilityOneButton.setOnClickListener {
           generateTurnOrder(abilityOneSkill,enemyList)
            println("You did " + abilityOneSkill.damage + "damage!")

        }

        abilityTwoButton.setOnClickListener {
            generateTurnOrder(abilityTwoSkill,enemyList)
            println("You did " + abilityTwoSkill.damage + "damage!")
        }



    }



    private fun generateTurnOrder(chosenSkill: Skills, enemies: List<Enemy>) {
        val characterSpeed = chosenSkill.speed
        val enemySpeeds = enemies.map { it.speed }
        val turnOrder = mutableListOf<String>()

        // Add the character to the turn order list
        turnOrder.add("character")

        // Add each enemy to the turn order list
        for (i in enemies.indices) {
            turnOrder.add("enemy$i")
        }

        // Sort the turn order list based on speed, with lower speeds going first
        turnOrder.sortBy { if (it == "character") characterSpeed else enemySpeeds[it.substring(5).toInt()] }
        println(turnOrder)

        // Execute each turn in the turn order with a delay
        for ((index, turn) in turnOrder.withIndex()) {
            CoroutineScope(Dispatchers.Main).launch {
                delay((index + 1) * 1000L) // delay for 1 second times the index of the turn
                if (turn == "character") {
                    executeCharacterTurn(chosenSkill)
                    checkVictoryAndDefeat(rootView)
                } else {
                    executeEnemyTurn(enemies[turn.substring(5).toInt()],player)
                    checkVictoryAndDefeat(rootView)
                }
            }
        }
    }




    private fun executeCharacterTurn(chosenSkill: Skills){
        var skillUsed = chosenSkill
        animateKnight()
        skillUsed.use(enemy)
        println("after skillUsed.use(enemy) in executeCharacterTurn")

        var damageDealt = (skillUsed.damage - enemy.defense)
        enemyHealthBar.progress -= damageDealt
    }

    private fun executeEnemyTurn(enemy: Enemy, player: Player) {
        // Calculate the damage dealt by the enemy
        val damageDealt = enemy.attack

        // Apply the damage to the player
        player.takeDamage(damageDealt)
        playerHealthBar.progress -= damageDealt

        // Print the results of the turn
        println("${enemy.enemyName} dealt $damageDealt damage to the player!")
    }

    fun checkVictoryAndDefeat(rootView: View) {
        val victoryActivity = VictoryActivity()
        // Check if the enemy is defeated
        if (enemy.health <= 0) {
            //  battlePlayerTextView.text = "${battlePlayerTextView.text}\nYou have defeated the ${enemy.enemyName}!"
            isBattleOver = true


            //Updates player exp and gold
            MainActivity.player.experience += enemy.goldReward
            MainActivity.player.gold += enemy.goldReward

            // Passing values to companion object so that it can be passed to victory activity
            expGained = enemy.experienceReward
            goldGained = enemy.goldReward

            //Sends exp gained to victory screen for display
            victoryActivity.expGained = enemy.experienceReward

            // Use View.postDelayed to delay the transition to the rewards screen
            rootView.postDelayed({
                //Moves to the rewards screen
                val victoryIntent = Intent(context, VictoryActivity::class.java)
                context.startActivity(victoryIntent)
            }, 2000) // Delay for 2 seconds (2000 milliseconds)
            rootView.removeCallbacks(runnable)
        }
        // Check if the player is defeated
        if (player.health <= 0) {
            abilityOneButton.isEnabled = false
            defendButton.isEnabled = false
            rootView.removeCallbacks(runnable)

        }

    }













    private var attackOnCooldown = false
    private val cooldownPeriod: Long = 1000
    //function, isBattleRunning() to keep allowing inputs and cooldowns

    //Change this to Ability 1 (will handle calling abilities in this function)
    //For now it will be coded to be attack
  /*  private fun attack() {
        // code to handle the player's attack
        if (!isBattleOver) {
            animateKnight()
           // battlePlayerTextView.text = "You attack ${enemy.enemyName} for ${player.attack} damage"
            val slashSkill = Skills(AbilityType.SLASH)
            var chosenSkill = slashSkill
           println(generateTurnOrder(chosenSkill,enemyList))
            slashSkill.use(enemy)
            println(enemy.health)
            checkVictoryAndDefeat(rootView)
            //start cooldown
            attackOnCooldown = true
            abilityOneButton.isEnabled = false
            abilityOneButton.text = "Cooldown"
            abilityOneButton.postDelayed({
                attackOnCooldown = false
                abilityOneButton.isEnabled = true
                abilityOneButton.text = "Attack"
            }, cooldownPeriod)
        }
    }

   */
    //change this to Ability2
    private fun defend() {
        // code to use defend ability
        if (!isBattleOver) {


            player.defense += 1
            println(player.defense)
            checkVictoryAndDefeat(rootView)

            //start cooldown
            attackOnCooldown = true
            defendButton.isEnabled = false
            defendButton.text = "Cooldown"
            defendButton.postDelayed({

                player.defense -= 1
                attackOnCooldown = false
                defendButton.isEnabled = true
                defendButton.text = "Defend"
            }, cooldownPeriod)
            println(player.defense)
        }
    }


  /*  private fun enemyAttack() {
        var damageDealt = (enemy.attack - player.defense)
        if (damageDealt >= 0) {
            playerHealthBar.progress -= damageDealt
            battleEnemyTextView.text = "The ${enemy.enemyName} attacks you for ${damageDealt} damage"
        } else {
            "The ${enemy.enemyName} attacks you for 0 damage"
        }
    }

   */


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







