package com.example.myapplication.ui.main

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.animation.ObjectAnimator
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.graphics.PorterDuff
import android.os.CountDownTimer
import android.view.View
import android.widget.*
import com.example.myapplication.*
import kotlinx.coroutines.*
import java.util.*


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
    lateinit var turn_order_bar: ProgressBar
    lateinit var enemyImageIcon : ImageView
    lateinit var abilityOneSkill: Skills
    lateinit var abilityTwoSkill: Skills

    lateinit var monsterTurnIcon: ImageView
    lateinit var monsterTurnIcon2: ImageView
    lateinit var monsterTurnIcon3: ImageView
    lateinit var monsterTurnIcon4: ImageView
    lateinit var monsterTurnIcon5: ImageView
    lateinit var monsterTurnIcon6: ImageView
    lateinit var monsterTurnIcon7: ImageView
    lateinit var monsterTurnIcon8: ImageView
    lateinit var monsterTurnIcon9: ImageView
    lateinit var monsterTurnIcon10: ImageView


    var enemyCanAttackFlag = false
    var runnable: Runnable = Runnable {}
    private var isBattleOver = false
    val enemyList = mutableListOf<Enemy>()
    var turnOrder = mutableListOf<String>()

    //Slash animation variables
    private lateinit var attackAnimation: ImageView
    private lateinit var attackButton: Button

    private val attackFrames = arrayOf(
        R.drawable.basicslash1,
        R.drawable.basicslash2,
        R.drawable.basicslash3,
        R.drawable.basicslash4
    )
    private var currentFrame = 0

    private var attackTimer: CountDownTimer? = null
    private var lastTapTime = 0L



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

        //Basic Slash Animation Variables
        attackAnimation = (context as Activity).findViewById(R.id.basicKnight)


        abilityOneButton = (context as Activity).findViewById(R.id.ability_card_1)
        abilityTwoButton = (context as Activity).findViewById(R.id.ability_card_2)

    turn_order_bar = (context as Activity).findViewById<ProgressBar>(R.id.turn_order_bar)
    monsterTurnIcon = (context as Activity).findViewById<ImageView>(R.id.enemyImage1)
    monsterTurnIcon2 = (context as Activity).findViewById<ImageView>(R.id.enemyImage2)
    monsterTurnIcon3 = (context as Activity).findViewById<ImageView>(R.id.enemyImage3)
    monsterTurnIcon4 = (context as Activity).findViewById<ImageView>(R.id.enemyImage4)
    monsterTurnIcon5 = (context as Activity).findViewById<ImageView>(R.id.enemyImage5)
    monsterTurnIcon6 = (context as Activity).findViewById<ImageView>(R.id.enemyImage6)
    monsterTurnIcon7 = (context as Activity).findViewById<ImageView>(R.id.enemyImage7)
    monsterTurnIcon8 = (context as Activity).findViewById<ImageView>(R.id.enemyImage8)
    monsterTurnIcon9 = (context as Activity).findViewById<ImageView>(R.id.enemyImage9)
    monsterTurnIcon10 = (context as Activity).findViewById<ImageView>(R.id.enemyImage10)


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
            abilityOneButton.isEnabled = false
                val currentTime = System.currentTimeMillis()
                val timeSinceLastTap = currentTime - lastTapTime
                lastTapTime = currentTime

                // Check if the tap was within the correct timing window
                val correctTiming = timeSinceLastTap in 250..750

                if (correctTiming) {
                    println("Damage increased by timing success")
                } else {
                    println("Timing failed")
                }

            animateKnight()
            generatePlayerTurnOrder(abilityOneSkill)
            startAttackAnimation()
            generateTurnOrder(abilityOneSkill,enemyList)
            println("You did " + abilityOneSkill.damage + " damage!")

        }

        abilityTwoButton.setOnClickListener {
            generatePlayerTurnOrder(abilityTwoSkill)
            generateTurnOrder(abilityTwoSkill,enemyList)
            println("You did " + abilityTwoSkill.damage + "damage!")
        }

    generateEnemyTurnOrder(enemyList)



    }

    private fun generateEnemyTurnOrder(enemies:List<Enemy>) {
        // Check for each speed value in enemy list
        for (i in enemies.indices) {
            when (enemy.speed) {
                1 -> {monsterTurnIcon.visibility = View.VISIBLE}
                2 -> {monsterTurnIcon2.visibility = View.VISIBLE}
                3 -> {monsterTurnIcon3.visibility = View.VISIBLE}
                4 -> {monsterTurnIcon4.visibility = View.VISIBLE}
                5 -> {monsterTurnIcon5.visibility = View.VISIBLE}
                6 -> {monsterTurnIcon6.visibility = View.VISIBLE}
                7 -> {monsterTurnIcon7.visibility = View.VISIBLE}
                8 -> {monsterTurnIcon8.visibility = View.VISIBLE}
                9 -> {monsterTurnIcon9.visibility = View.VISIBLE}
                10 -> {monsterTurnIcon10.visibility = View.VISIBLE}

            }
        }
    }

    //TODO: create player icon for turn order bar and finish this function
    private fun generatePlayerTurnOrder(chosenSkill: Skills){
        val characterSpeed = chosenSkill.speed
        when (characterSpeed){
            1 -> {monsterTurnIcon.visibility = View.VISIBLE}

        }
        basicKnight.postDelayed({
            basicKnight.setColorFilter(Color.WHITE, PorterDuff.Mode.SRC_ATOP)
            basicKnight.postDelayed({ basicKnight.clearColorFilter() }, 50)
        }, 300)
    }


    private fun generateTurnOrder(chosenSkill: Skills, enemies: List<Enemy>) {
        val characterSpeed = chosenSkill.speed
        val enemySpeeds = enemies.map { it.speed }
        turnOrder = mutableListOf<String>()


        // Add the character to the turn order list
        turnOrder.add("character")

        // Add each enemy to the turn order list and create icons on the turn order bar UI element
        for (i in enemies.indices) {
            turnOrder.add("enemy$i")
            if (enemy.speed == 4) {
                monsterTurnIcon4.visibility = View.VISIBLE
            }
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
//ANIMATION FUNCTIONS
    private fun animateKnight() {
        // Create an ObjectAnimator to animate the x position of the basicKnight image
        val animator = ObjectAnimator.ofFloat(basicKnight, "x", basicKnight.x, basicKnight.x + 50f)
        animator.duration = 200
        animator.start()

        // Add a listener to the animator to reverse the animation when it's finished
        animator.addListener(object : AnimatorListenerAdapter() {
            override fun onAnimationEnd(animation: Animator) {
                super.onAnimationEnd(animation)
                val reverseAnimator = ObjectAnimator.ofFloat(basicKnight, "x", basicKnight.x, basicKnight.x - 50f)
                reverseAnimator.duration = 300
                reverseAnimator.start()
            }
        })
    }

    private fun startAttackAnimation() {
        attackTimer = object : CountDownTimer(200, 50) {
            override fun onTick(millisUntilFinished: Long) {
                // Update the animation frame
                currentFrame++
                if (currentFrame >= attackFrames.size) {
                    currentFrame = 0 // reset to the first frame
                }
                attackAnimation.setImageResource(attackFrames[currentFrame])
            }

            override fun onFinish() {
                // End the attack


            }
        }

        attackTimer?.start()
    }

}











