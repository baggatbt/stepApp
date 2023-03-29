package com.example.myapplication.ui.main

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.animation.ObjectAnimator
import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.graphics.PorterDuff
import android.os.CountDownTimer
import android.os.Handler
import android.os.Looper
import android.view.View
import android.widget.*
import androidx.core.content.ContentProviderCompat.requireContext
import androidx.recyclerview.widget.RecyclerView
import com.example.myapplication.*
import kotlinx.coroutines.*
import java.util.*
import java.util.concurrent.CountDownLatch


class Battle(private val onEnemyHealthChangedListener: OnEnemyHealthChangedListener) {
    companion object {
        var expGained = 0
        var goldGained = 0
    }


    lateinit var abilityOneButton: Button
    lateinit var abilityTwoButton: Button
    lateinit var basicAttackButton: Button


    lateinit var player: Player
    lateinit var context: Context
    lateinit var basicKnight : ImageView

    lateinit var goblinPicture : ImageView
    lateinit var slimePicture : ImageView
    lateinit var rootView: View

    lateinit var playerHealthBar: ProgressBar
    lateinit var turn_order_bar: ProgressBar
    lateinit var enemyImageIcon : ImageView
    lateinit var chosenSkill: Skills
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
    lateinit var attackFrames: IntArray
    lateinit var enemyRecyclerView: RecyclerView
    lateinit var enemyAdapter: EnemyAdapter
    private lateinit var turnOrderIcon1: ImageView
    private lateinit var turnOrderIcon2: ImageView
    private lateinit var turnOrderIcon3: ImageView




    var enemyCanAttackFlag = false
    var runnable: Runnable = Runnable {}
    val enemyList = mutableListOf<Enemy>()
    var turnOrder = mutableListOf<String>()

    //Slash animation variables
    private lateinit var attackAnimation: ImageView
    private lateinit var attackButton: Button

    private var currentFrame = 0

    private var attackTimer: CountDownTimer? = null
    private var lastTapTime = 0L
    var abilityOneExecuted = false
    var abilityTwoExecuted = false
    var currentTime: Long = 0
    var timingWindowOpen = false

    var attackInProgress = false
    var selectedEnemy: Enemy? = null




    //Intializing
    fun start(player: Player, enemies: List<Enemy>, context: Context): Skills {
        this.player = player
        player.currentHealth = player.maxHealth

        //TODO: Implement setPlayerSkills()
        // Set player skills
        abilityOneSkill = Skills(AbilityType.SLASH)
        abilityTwoSkill = Skills(AbilityType.HEAVYSLASH)

        turnOrderIcon1 = (context as Activity).findViewById(R.id.turnOrderIcon1)
        turnOrderIcon2 = (context as Activity).findViewById(R.id.turnOrderIcon2)
        turnOrderIcon3 = (context as Activity).findViewById(R.id.turnOrderIcon3)

        enemyList.clear()
        enemyList.addAll(enemies)

        this.context = context

        // Find views
        attackAnimation = (context as Activity).findViewById(R.id.basicKnight)
        basicAttackButton = (context as Activity).findViewById(R.id.basicAttackButton)
        abilityOneButton = (context as Activity).findViewById(R.id.ability_card_1)
        abilityTwoButton = (context as Activity).findViewById(R.id.ability_card_2)
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
        basicKnight = (context as Activity).findViewById(R.id.basicKnight)
        rootView = (context as Activity).findViewById<View>(R.id.root)
        playerHealthBar = (context as Activity).findViewById(R.id.playerHealthBar)
        playerHealthBar.max = player.maxHealth
        playerHealthBar.progress = player.currentHealth
        chosenSkill = abilityOneSkill


        // Add RecyclerView and adapter references
        enemyRecyclerView = (context as Activity).findViewById(R.id.enemiesRecyclerView)
        val activity = context as BattleActivity
        enemyAdapter = EnemyAdapter(enemyList, activity) // Pass 'activity' as the click listener
        enemyRecyclerView.adapter = enemyAdapter





        generateTurnOrder(chosenSkill,enemyList)


        rootView.setOnClickListener {
            if (timingWindowOpen) {
                // The player touched the screen between frame 2 and 3
                println("Touched during timing window")
            }
        }

        abilityOneButton.setOnClickListener {
            if (selectedEnemy != null) {
                println("Ability 1 button clicked")
                chosenSkill = abilityOneSkill
                generatePlayerTurnOrderIcons(abilityOneSkill)
                setAbilityButtonsEnabled(false)
                attackFrames = abilityOneSkill.attackFrames
                launchAttackTurns()
            } else {
                Toast.makeText(context, "Please select an enemy to attack.", Toast.LENGTH_SHORT).show()
            }
        }


        abilityTwoButton.setOnClickListener {
            setAbilityButtonsEnabled(false)
            chosenSkill = abilityTwoSkill
            generatePlayerTurnOrderIcons(abilityTwoSkill)
            rootView.setOnClickListener {
                timingWindowOpen = true
                executeCharacterTurn(context,abilityTwoSkill)
                Handler(Looper.getMainLooper()).postDelayed({
                    setAbilityButtonsEnabled(true)
                    battleLoop()
                }, nextTurnDelay)
            }
        }
        return chosenSkill // Return the chosen skill
        battleLoop()
    }



    private var currentTurnIndex = 0 // keep track of whose turn it is currently

    val nextTurnDelay = 1000L // set delay before the next turn
    private fun battleLoop() {
        if (player.currentHealth <= 0 || enemyList.all { it.currentHealth <= 0 }) {
            checkVictoryAndDefeat(rootView)
            println("Battle ended")
            return
        }

        // Regenerate turnOrder list before starting the next round
        generateTurnOrder(chosenSkill, enemyList)
        generatePlayerTurnOrderIcons(chosenSkill)

        setAbilityButtonsEnabled(true)
    }




    private fun setAbilityButtonsEnabled(enabled: Boolean) {
        basicAttackButton.isEnabled = enabled
        abilityOneButton.isEnabled = enabled
        abilityTwoButton.isEnabled = enabled
    }


    private fun timingFlash() {
        basicKnight.setColorFilter(Color.WHITE, PorterDuff.Mode.SRC_ATOP)
        basicKnight.postDelayed({ basicKnight.clearColorFilter() }, 50)
    }




    /* private fun handleTimingWindow(chosenSkill: Skills) {
         if (attackInProgress) {
             println("window open")
             currentTime = System.currentTimeMillis()
             val skillStartWindow = chosenSkill.startWindow
             val skillEndWindow = chosenSkill.endWindow

             println("last tap time in handleTiming window $lastTapTime")

             val timeSinceLastTap = currentTime - lastTapTime
             println("current time in handle timing" + currentTime)
             println("time since last tap in handle timing $timeSinceLastTap")
             val correctTiming = timeSinceLastTap in skillStartWindow..skillEndWindow
             println("time since last tap in handle timing $timeSinceLastTap")
             if (correctTiming){
                 println("Success Effect Applied!")
                 timingWindowOpen = false
                 tapAttempt = false

             } else {
                 println("Timing failed")
                 timingWindowOpen = false
                 tapAttempt = false

             }
         }

     }
     /
     */


    var tapAttempt = false
    var battleEnded = false // Add this line at the class level

    private fun launchAttackTurns() {
        setAbilityButtonsEnabled(false)
        println("launch attack turns called")
        println(turnOrder)

        for ((index, turn) in turnOrder.withIndex()) {
            CoroutineScope(Dispatchers.Main).launch {
                delay((index + 1) * 1000L)
                if (!battleEnded) {
                    if (turn == "character") {
                        executeCharacterTurn(context, chosenSkill)
                    } else {
                        val enemy = enemyList[turn.substring(5).toInt()]
                        val enemyAbility = if (enemy.attacksToChargeSpecial == 0) {
                            enemy.abilities.find { it.isSpecial } ?: error("No special ability found")
                        } else {
                            enemy.abilities.first { !it.isSpecial }
                        }
                        executeEnemyTurn(enemy, enemyAbility)
                    }
                    numTurnsTaken++

                    if (numTurnsTaken == turnOrder.size) {
                        if (checkVictoryAndDefeat(rootView) == false) {
                            turnOrder.clear()
                            numTurnsTaken = 0

                            abilityOneExecuted = false
                            battleLoop()
                        }
                    }

                    if (player.currentHealth <= 0 || enemyList.all { it.currentHealth <= 0 }) {
                        endBattle()
                        battleEnded = true
                    }
                }
            }
        }
    }





    private fun executeCharacterTurn(context: Context, skill: Skills) {
        // Check if a target is selected
        if (selectedEnemy == null) {
            Toast.makeText(context, "Please select an enemy to attack.", Toast.LENGTH_SHORT).show()
            return
        }
        remainingTurnOrders.removeAt(0)
        attackInProgress = true // Set the attackInProgress flag to true
        println("Time executeCharacter started, ${System.currentTimeMillis()}")

        animateKnight()
        // Takes in the array containing the frames of the selected ability, as well as the window the correct timing will be in
        startAttackAnimation(attackFrames, abilityOneSkill.timingWindowStartFrame, abilityOneSkill.timingWindowEndFrame)

        val targetEnemy = selectedEnemy!!
        var skillUsed = chosenSkill
        skillUsed.use(targetEnemy)
        println("after skillUsed.use(enemy) in executeCharacterTurn")

        // Enemy takes damage and health bar is updated
        onEnemyHealthChangedListener.onEnemyHealthChanged(targetEnemy)

        if (targetEnemy.currentHealth <= 0) {
            // Update the RecyclerView
            enemyAdapter.notifyItemRemoved(enemyList.indexOf(targetEnemy))
            enemyList.remove(targetEnemy)
        }


        if (player.currentHealth <= 0) {
            return
        }

        if (player.currentHealth <= 0 || enemyList.all { it.currentHealth <= 0 }) {
            endBattle()
            battleEnded = true
            return
        }
        currentTurnIndex++
        battleLoop() // Call battleLoop() after the player's attack
    }





    private fun executeEnemyTurn(enemy: Enemy, enemyAbility: EnemyAbility) {
        remainingTurnOrders.removeAt(0)


        if (enemy.currentHealth <= 0) {
            // Update the RecyclerView
            enemyAdapter.notifyItemRemoved(enemyList.indexOf(enemy))
            enemyList.remove(enemy)
        } else {
            // Calculate the damage dealt by the enemy
            val attack = if (enemy.attacksToChargeSpecial == 0) {
                enemy.abilities.find { it.isSpecial } ?: error("No special ability found")
            } else {
                enemy.abilities.first { !it.isSpecial }
            }

            val damageDealt = attack.damage

            // Apply the damage to the player
            player.takeDamage(damageDealt)
            playerHealthBar.progress -= damageDealt
            checkVictoryAndDefeat(rootView)

            // Decrement the number of turns until the special attack can be used
            if (enemy.attacksToChargeSpecial > 0) {
                enemy.attacksToChargeSpecial--
                println("attacks to charge special ${enemy.attacksToChargeSpecial}")
            }

            // Print the results of the turn
            if  (enemy.attacksToChargeSpecial == 0) {
                println("${enemy.name} used ${attack.name}!")
            } else {
                println("${enemy.name} attacked with its non-special ability!")
            }
            if (enemy.currentHealth <= 0) {
                goblinPicture.visibility = View.GONE
            }
        }
    }








    //TODO: create player icon for turn order bar and finish this function
    private fun generatePlayerTurnOrderIcons(chosenSkill: Skills): List<TurnOrderItem> {
        val characterSpeed = chosenSkill.speed
        return listOf(TurnOrderItem("character", R.drawable.sword3030, characterSpeed))
    }


    private fun clearTurnOrderIcons() {
        turnOrderIcon1.setImageResource(0)
        turnOrderIcon2.setImageResource(0)
        turnOrderIcon3.setImageResource(0)


    }

    private val remainingTurnOrders = mutableListOf<Int>()




    private fun generateTurnOrder(chosenSkill: Skills, enemies: List<Enemy>) {
        remainingTurnOrders.clear()
        remainingTurnOrders.addAll(1..10)

        val characterSpeed = chosenSkill.speed
        val enemySpeeds = mutableListOf<Int>()

        turnOrder = mutableListOf<String>()

        // Add the character to the turn order list
        turnOrder.add("character")

        // Add each enemy to the turn order list and keep track of their speeds
        for (i in enemies.indices) {
            val enemy = enemies[i]
            turnOrderIcon1
            val enemySpeed = if (enemy.attacksToChargeSpecial == 0) {
                enemy.specialAttackSpeed
            } else {
                enemy.speed
            }
            enemySpeeds.add(enemySpeed)
            turnOrder.add("enemy$i")
        }

        // Sort the turn order list based on speed, with lower speeds going first
        turnOrder.sortBy {
            if (it == "character") characterSpeed else enemySpeeds[it.substring(5).toInt()]
        }
    }


    var numTurnsTaken = 0 // Initialize a variable to keep track of the number of turns taken

    private fun endBattle() {
        checkVictoryAndDefeat(rootView)
    }

    private fun checkVictoryAndDefeat(rootView: View): Boolean {
        val victoryActivity = VictoryActivity()
        var isVictory = false

        // Check if all enemies are defeated
        if (enemyList.all { it.currentHealth <= 0 }) {
            //  battlePlayerTextView.text = "${battlePlayerTextView.text}\nYou have defeated the ${enemy.enemyName}!"


            //Updates player exp and gold
            val totalExpReward = enemyList.sumBy { it.experienceReward }
            val totalGoldReward = enemyList.sumBy { it.goldReward }
            MainActivity.player.experience += totalExpReward
            MainActivity.player.gold += totalGoldReward

            // Passing values to companion object so that it can be passed to victory activity
            expGained = totalExpReward
            goldGained = totalGoldReward

            //Sends exp gained to victory screen for display
            victoryActivity.expGained = totalExpReward

            // Use View.postDelayed to delay the transition to the rewards screen
            rootView.postDelayed({
                //Moves to the rewards screen
                val victoryIntent = Intent(context, VictoryActivity::class.java)
                context.startActivity(victoryIntent)
            }, 2000) // Delay for 2 seconds (2000 milliseconds)
            rootView.removeCallbacks(runnable)

            // Set the isVictory flag to true
            isVictory = true
        }

        // Check if the player is defeated
        if (player.currentHealth <= 0) {
            abilityOneButton.isEnabled = false
            val defeatIntent = Intent(context, MainActivity::class.java)
            context.startActivity(defeatIntent)

            rootView.removeCallbacks(runnable)
        }

        // Return the isVictory flag
        return isVictory
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

    /*
    private fun animateGoblin() {
        // Create an ObjectAnimator to animate the x position of the basicKnight image
        val animator = ObjectAnimator.ofFloat(goblinPicture, "x", goblinPicture.x, goblinPicture.x -50f)
        animator.duration = 200
        animator.start()

        // Add a listener to the animator to reverse the animation when it's finished
        animator.addListener(object : AnimatorListenerAdapter() {
            override fun onAnimationEnd(animation: Animator) {
                super.onAnimationEnd(animation)
                val reverseAnimator = ObjectAnimator.ofFloat(goblinPicture, "x", goblinPicture.x, goblinPicture.x + 50f)
                reverseAnimator.duration = 300
                reverseAnimator.start()
            }
        })
    }

    private fun animateSlime() {
        // Create an ObjectAnimator to animate the x position of the basicKnight image
        val animator = ObjectAnimator.ofFloat(slimePicture, "x", slimePicture.x, slimePicture.x -50f)
        animator.duration = 200
        animator.start()

        // Add a listener to the animator to reverse the animation when it's finished
        animator.addListener(object : AnimatorListenerAdapter() {
            override fun onAnimationEnd(animation: Animator) {
                super.onAnimationEnd(animation)
                val reverseAnimator = ObjectAnimator.ofFloat(slimePicture, "x", slimePicture.x, slimePicture.x + 50f)
                reverseAnimator.duration = 300
                reverseAnimator.start()
            }
        })
    }

     */

    private fun startAttackAnimation(attackFrames: IntArray, timingWindowStartFrame: Int, timingWindowEndFrame: Int) {
        //Calculate how long the duration of the animation needs to be
        val frameInterval = 50L
        val totalDuration = attackFrames.size * frameInterval

        attackTimer = object : CountDownTimer(totalDuration, frameInterval) {
            override fun onTick(millisUntilFinished: Long) {
                // Update the animation frame
                currentFrame++
                if (currentFrame >= attackFrames.size) {
                    currentFrame = 0 // reset to the first frame
                }

                // Check if the animation is between the timing window start and end frames
                if (currentFrame in timingWindowStartFrame..timingWindowEndFrame) {
                    timingWindowOpen = true
                    timingFlash()
                } else {
                    timingWindowOpen = false
                }

                attackAnimation.setImageResource(attackFrames[currentFrame])
            }

            override fun onFinish() {
                // Handle the end of the animation if needed
                timingWindowOpen = false
                currentFrame = 0 // Reset to the first frame
                attackAnimation.setImageResource(attackFrames[currentFrame]) // Set the animation to the first frame
            }
        }
        attackTimer?.start()
    }
}




