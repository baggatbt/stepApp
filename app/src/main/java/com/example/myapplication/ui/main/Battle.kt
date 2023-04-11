package com.example.myapplication.ui.main

import android.animation.*
import android.app.Activity
import android.content.Context
import android.content.Context.MODE_PRIVATE
import android.content.Intent
import android.content.SharedPreferences
import android.graphics.Color
import android.graphics.PorterDuff
import android.graphics.drawable.AnimationDrawable
import android.graphics.drawable.BitmapDrawable
import android.media.MediaPlayer
import android.os.CountDownTimer
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.View
import android.widget.*
import androidx.recyclerview.widget.RecyclerView
import com.example.myapplication.*
import kotlinx.coroutines.*
import java.util.*
import android.widget.ImageView
import androidx.core.content.ContextCompat
import com.example.myapplication.jobSkills.HeavySlash
import com.example.myapplication.jobSkills.Slash
import kotlin.math.pow
import kotlin.math.sqrt


class Battle(private val damageBubbleCallback: DamageBubbleCallback,private val onEnemyHealthChangedListener: OnEnemyHealthChangedListener, private val listener: TurnOrderUpdateListener,private val turnOrderUpdateCallback: TurnOrderUpdateCallback) {
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
    lateinit var turnOrderRecyclerView: RecyclerView
    lateinit var enemyAdapter: EnemyAdapter
    lateinit var currentFrameView: TextView
    lateinit var mediaPlayer : MediaPlayer




    var enemyCanAttackFlag = false
    var runnable: Runnable = Runnable {}
    val enemyList = mutableListOf<Enemy>()
    var turnOrder = mutableListOf<String>()

    //Slash animation variables
    private lateinit var attackAnimation: ImageView
    private lateinit var attackButton: Button
    private lateinit var sharedPreferences: SharedPreferences

    private var currentFrame = 0

    private var attackTimer: CountDownTimer? = null
    private var walkTimer: CountDownTimer? = null

    private var lastTapTime = 0L
    var abilityOneExecuted = false
    var abilityTwoExecuted = false
    var currentTime: Long = 0
    var timingWindowOpen = false

    var attackInProgress = false
    var selectedEnemy: Enemy? = null
    var totalExpReward = 0
    var totalGoldReward = 0






    //Intializing
    fun start(player: Player, enemies: List<Enemy>, context: Context) {
        this.player = player
        player.currentHealth = player.maxHealth
        sharedPreferences = context.getSharedPreferences("myPrefs", Context.MODE_PRIVATE)


        //TODO: Implement setPlayerSkills()
        // Set player skills
        abilityOneSkill = Slash()
        abilityTwoSkill = HeavySlash()


        enemyList.clear()
        enemyList.addAll(enemies)
        // Calculate the total gold and exp rewards
        totalGoldReward = enemyList.sumOf { it.goldReward }
        totalExpReward = enemyList.sumOf { it.experienceReward }
        println("Total gold reward: $totalGoldReward")
        println("Total exp reward: $totalExpReward")

        this.context = context

        // Find views
        turnOrderRecyclerView = (context as Activity).findViewById(R.id.turnOrderRecyclerView)
        currentFrameView = (context as Activity).findViewById(R.id.currentFrameDebug)
        attackAnimation = (context as Activity).findViewById(R.id.basicKnight)
        basicAttackButton = (context as Activity).findViewById(R.id.basicAttackButton)
        abilityOneButton = (context as Activity).findViewById(R.id.ability_card_1)
        abilityTwoButton = (context as Activity).findViewById(R.id.ability_card_2)
        basicKnight = (context as Activity).findViewById(R.id.basicKnight)
        rootView = (context as Activity).findViewById<View>(R.id.root)
        playerHealthBar = (context as Activity).findViewById(R.id.playerHealthBar)
        playerHealthBar.max = player.maxHealth
        playerHealthBar.progress = player.currentHealth
        chosenSkill = abilityOneSkill
        val mediaPlayer = createMediaPlayer(context)



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
            //    playSwordSlashSound() //TODO GET A SOUND

            }
        }

        abilityOneButton.setOnClickListener {
            if (selectedEnemy != null) {
                println("Ability 1 button clicked")
                chosenSkill = abilityOneSkill
                generateTurnOrder(chosenSkill, enemyList) // Add this line
                setAbilityButtonsEnabled(false)
                attackFrames = abilityOneSkill.attackFrames
                launchAttackTurns()
            } else {
                Toast.makeText(context, "Please select an enemy to attack.", Toast.LENGTH_SHORT).show()
            }

            /*
            mediaPlayer.start()
            println("Sound played")

             */
        }

        abilityTwoButton.setOnClickListener {
            setAbilityButtonsEnabled(false)
            chosenSkill = abilityTwoSkill
            generateTurnOrder(chosenSkill, enemyList) // Add this line
            rootView.setOnClickListener {
                timingWindowOpen = true
                executeCharacterTurn(context, abilityTwoSkill)
                Handler(Looper.getMainLooper()).postDelayed({
                    setAbilityButtonsEnabled(true)
                    battleLoop()
                }, nextTurnDelay)
            }
        }


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

        listener.updateTurnOrder() // Call the updateTurnOrder() method in BattleActivity/ Reset the current turn index
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


    var battleEnded = false // Add this line at the class level

    private fun launchAttackTurns() {
        setAbilityButtonsEnabled(false)
        println("launch attack turns called")
        println(turnOrder)
        val delayBetweenAttacks = 2000L // Adjust this value as needed

        for ((index, turn) in turnOrder.withIndex()) {
            CoroutineScope(Dispatchers.Main).launch {
                val currentDelay = (index + 1) * 1000L
                val extraDelay = if (turn != "character") delayBetweenAttacks else 0L
                delay(currentDelay + extraDelay)

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



    interface DamageBubbleCallback {
        fun onDamageDealt(damage: Int, enemyImageView: ImageView)
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

        // Start the walking animation
        startWalkingAnimation(walkingFrames, 500)

        // Knight walks towards the enemy
        animateKnight(moveDistance = 300f) {
            // onAnimationEnd callback - stop the walking animation and start the attack animation
            stopWalkingAnimation()
            startAttackAnimation(attackFrames, abilityOneSkill.timingWindowStartFrame, abilityOneSkill.timingWindowEndFrame) {
                // onAnimationEnd callback - knight walks back to the original position
                startWalkingAnimation(walkingFrames, 500) // Start the walking animation again before moving back
                animateKnight(moveDistance = -300f) {
                    // onAnimationEnd callback - stop the walking animation
                    stopWalkingAnimation()
                    attackInProgress = false // Set the attackInProgress flag to false
                }

            }
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
        battleLoop()
    }

   var walkingFrames = intArrayOf(R.drawable.knight2walk, R.drawable.knight2walk2, R.drawable.knight2walk3, R.drawable.knight2walk4)

    private fun startWalkingAnimation(walkingFrames: IntArray, loopDuration: Long) {
        // Calculate how long the duration of the animation needs to be
        val frameInterval = 100L

        val walkTimer = object : CountDownTimer(Long.MAX_VALUE, frameInterval) {
            var currentFrame = 0

            override fun onTick(millisUntilFinished: Long) {
                // Update the animation frame
                currentFrame++
                if (currentFrame >= walkingFrames.size) {
                    currentFrame = 0 // reset to the first frame
                }

                basicKnight.setImageResource(walkingFrames[currentFrame])
            }

            override fun onFinish() {
                // This should not be called, as we set the duration to Long.MAX_VALUE
            }
        }
        walkTimer.start()

        // Set up another timer to stop the walkTimer after loopDuration
        val stopTimer = object : CountDownTimer(loopDuration, loopDuration) {
            override fun onTick(millisUntilFinished: Long) {
                // No action required
            }

            override fun onFinish() {
                walkTimer.cancel()
                currentFrame = 0 // Reset to the first frame
                basicKnight.setImageResource(walkingFrames[currentFrame]) // Set the animation to the first frame
            }
        }
        stopTimer.start()
    }

    private fun stopWalkingAnimation() {
        walkTimer?.cancel()
        currentFrame = 0 // Reset to the first frame
        basicKnight.setImageResource(walkingFrames[currentFrame]) // Set the animation to the first frame
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

            // Find the enemy ImageView
            val enemyImageView = rootView.findViewById<ImageView>(R.id.enemyImageView)

            // Calculate the distance the enemy should move towards the player
            val overlapFactor = 0.7F // Change this value between 0 and 1 to control how much the sprites should overlap
            val moveDistance = calculateDistance(enemyImageView, basicKnight) - (enemyImageView.width * overlapFactor)


            // Create an ObjectAnimator to animate the enemy's translation
            var enemyMoveAnimator = ObjectAnimator.ofFloat(enemyImageView, "translationX", 0f, moveDistance)
            enemyMoveAnimator.duration = 2000 // Set the duration of the animation (in milliseconds)

            // Set up a listener for the animation to start parry window and apply damage after the animation is complete
            enemyMoveAnimator.addListener(object : Animator.AnimatorListener {
                override fun onAnimationStart(animation: Animator?) {

                    // Create an AnimationDrawable object for the attack animation
                    val attackAnimation = AnimationDrawable()
                    attackAnimation.isOneShot = true
                    enemy.attackAnimation.attackFrames.forEach { frame ->
                        val drawable = ContextCompat.getDrawable(rootView.context, frame)
                        if (drawable is BitmapDrawable) {
                            attackAnimation.addFrame(drawable, 500)
                        } else {
                            throw IllegalArgumentException("Invalid frame drawable: $frame")
                        }
                    }

                    // Set and start the attack animation
                    enemyImageView.setImageDrawable(attackAnimation)
                    attackAnimation.start()
                }

                override fun onAnimationEnd(animation: Animator?) {
                    // Open the parry window and apply damage after the enemy has moved towards the player
                    openParryWindowAndApplyDamage(damageDealt)

                    // Animate the enemy back to its original position after the attack animation is complete
                    enemyMoveAnimator = ObjectAnimator.ofFloat(enemyImageView, "translationX", moveDistance, 0f)
                    enemyMoveAnimator.duration = 500 // Set the duration of the animation (in milliseconds)
                    enemyMoveAnimator.start()

                    // Reset the enemy image to the idle state
                    enemyImageView.setImageResource(enemy.getImageResource())
                }

                override fun onAnimationCancel(animation: Animator?) {
                    // No action required
                }

                override fun onAnimationRepeat(animation: Animator?) {
                    // No action required
                }
            })

            // Start the enemy move animation
            enemyMoveAnimator.start()
        }
    }




    private fun openParryWindowAndApplyDamage(damageDealt: Int) {
        // Prepare for parry window
        val parryWindowDuration = 2000L // Replace with the desired parry window duration
        basicKnight.setOnClickListener {
            player.isParrying = true
            println("Player attempted to parry!")
        }

        // Open the parry window
        println("Parry window is open.")
        val parryWindowTimer = object : CountDownTimer(parryWindowDuration, parryWindowDuration) {
            override fun onTick(millisUntilFinished: Long) {
                // No action required
            }

            override fun onFinish() {
                // Close the parry window
                println("Parry window is closed.")
                basicKnight.setOnClickListener(null)
                // Apply the damage to the player
                player.takeDamage(damageDealt)
                if (player.isParrying) {
                    println("Parry succeeded! Damage reduced.")
                } else {
                    println("Parry failed! Full damage taken.")
                }
                playerHealthBar.progress -= damageDealt
                player.isParrying = false // Reset the parrying status
                checkVictoryAndDefeat(rootView)
            }
        }
        parryWindowTimer.start()
    }







    interface TurnOrderUpdateCallback {
        fun onTurnOrderUpdated(turnOrderItems: List<TurnOrderItem>)
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

        // Create a list of TurnOrderItem objects
        val turnOrderItems = turnOrder.map { order ->
            if (order == "character") {
                TurnOrderItem(order, R.drawable.sword3030, characterSpeed,isVisible = false) // Replace with the player's drawable resource
            } else {
                val enemyIndex = order.substring(5).toInt()
                TurnOrderItem(order, R.drawable.sword3030, enemySpeeds[enemyIndex]) // Replace with the common enemy's drawable resource
            }
        }
        Log.d("Battle", "Updating turn order list: $turnOrderItems")

        // Update the RecyclerView's adapter with the new data
        val turnOrderAdapter = (turnOrderRecyclerView.adapter as TurnOrderAdapter)
        turnOrderUpdateCallback.onTurnOrderUpdated(turnOrderItems)
        turnOrderAdapter.update(turnOrderItems)
    }





    var numTurnsTaken = 0 // Initialize a variable to keep track of the number of turns taken

    private fun endBattle() {
        checkVictoryAndDefeat(rootView)
    }

    // Function to check if all enemies are defeated and end the battle if necessary
    private fun checkVictoryAndDefeat(rootView: View): Boolean {
        val victoryActivity = VictoryActivity()
        var isVictory = false

        // Check if all enemies are defeated
        if (enemyList.all { it.currentHealth <= 0 }) {

            // Update the player's experience and gold
            player.experience += totalExpReward
            player.gold += totalGoldReward
            sharedPreferences.edit().putInt("playerExp", player.experience).apply()
            sharedPreferences.edit().putInt("playerGold", player.gold).apply()

            // Pass the rewards to the victory activity
            expGained = totalExpReward
            goldGained = totalGoldReward
            victoryActivity.expGained = totalExpReward
            victoryActivity.goldGained = totalGoldReward

            // At the end of the battle, save the updated experience value to SharedPreferences


            // Use View.postDelayed to delay the transition to the rewards screen
            rootView.postDelayed({
                // Move to the rewards screen
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
    private fun animateKnight(moveDistance: Float, onAnimationEnd: () -> Unit) {
        // Create an ObjectAnimator to animate the x position of the basicKnight image
        val animator = ObjectAnimator.ofFloat(basicKnight, "x", basicKnight.x, basicKnight.x + moveDistance)
        animator.duration = 500
        animator.start()

        // Add a listener to the animator to reverse the animation when it's finished
        animator.addListener(object : AnimatorListenerAdapter() {
            override fun onAnimationEnd(animation: Animator) {
                super.onAnimationEnd(animation)
                onAnimationEnd() // Call the onAnimationEnd callback


            }
        })
    }






    private fun animateTimingCircle(attackDuration: Long, timingWindowStartFrame: Int, timingWindowEndFrame: Int, frameInterval: Long) {
        val timingWindowStartTime = timingWindowStartFrame * frameInterval
        val timingWindowEndTime = timingWindowEndFrame * frameInterval
        val timingWindowDuration = timingWindowEndTime - timingWindowStartTime

        val timingCircleView = rootView.findViewById<TimingCircleView>(R.id.timingCircleView)

        timingCircleView.postDelayed({
            val animator = ValueAnimator.ofFloat(timingCircleView.width.toFloat() / 2, 0f).apply {
                duration = timingWindowDuration
                addUpdateListener {
                    val radius = it.animatedValue as Float
                    timingCircleView.setRadius(radius)
                }
            }
            animator.start()
        }, timingWindowStartTime)
    }



    private fun startAttackAnimation(attackFrames: IntArray, timingWindowStartFrame: Int, timingWindowEndFrame: Int, onAnimationEnd: () -> Unit) {
        // Calculate how long the duration of the animation needs to be
        val frameInterval = 200L
        val totalDuration = attackFrames.size * frameInterval

        // Call the animateTimingCircle function with the totalDuration, timingWindowStartFrame, and timingWindowEndFrame
        animateTimingCircle(totalDuration, timingWindowStartFrame, timingWindowEndFrame, frameInterval)

        attackTimer = object : CountDownTimer(totalDuration, frameInterval) {
            var damageApplied = false

            override fun onTick(millisUntilFinished: Long) {
                // Update the animation frame
                currentFrame++
                if (currentFrame >= attackFrames.size) {
                    currentFrame = 0 // reset to the first frame
                }
                currentFrameView.setText(currentFrame.toString())

                // Check if the animation is between the timing window start and end frames
                if (currentFrame in timingWindowStartFrame..timingWindowEndFrame) {
                    timingWindowOpen = true

                    timingFlash()

                    // Apply damage when the timing window starts, and ensure it's applied only once
                    if (!damageApplied) {
                        damageApplied = true

                        // Put the damage dealing code here
                        val targetEnemy = selectedEnemy!!
                        val skillUsed = chosenSkill
                        skillUsed.use(targetEnemy)
                        val enemyImageView = rootView.findViewById<ImageView>(R.id.enemyImageView)
                        // Call the onDamageDealt method when the character deals damage
                        damageBubbleCallback.onDamageDealt(skillUsed.damage, enemyImageView)

                        println("after skillUsed.use(enemy) in executeCharacterTurn")

                        // Enemy takes damage and health bar is updated
                        onEnemyHealthChangedListener.onEnemyHealthChanged(targetEnemy)

                        if (targetEnemy.currentHealth <= 0) {
                            // Update the RecyclerView
                            enemyAdapter.notifyItemRemoved(enemyList.indexOf(targetEnemy))
                            enemyList.remove(targetEnemy)
                        }
                    }
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

                // Add a delay to wait for the attack animation to complete
                attackAnimation.postDelayed({
                    onAnimationEnd() // Call the onAnimationEnd callback
                }, totalDuration)
            }
        }
        attackTimer?.start()

        // Start playing the sound
        val mediaPlayer = createMediaPlayer(context)
        mediaPlayer.start()
        println("Sound played")
    }



    private fun createMediaPlayer(context: Context): MediaPlayer {
        val mediaPlayer = MediaPlayer.create(context, R.raw.swordslash)
        mediaPlayer.setOnCompletionListener {
            it.reset()
            it.release()
        }
        return mediaPlayer
    }


    //Gets the distance between top right of player image, and top left of enemy image
    fun calculateDistance(playerImageView: ImageView, enemyImageView: ImageView): Float {
        val playerWidth = playerImageView.width
        val enemyWidth = enemyImageView.width

        val enemyWidthFactor = 0.5 // Change this value between 0 and 1 to control the portion of enemy width to consider
        val adjustedEnemyWidth = enemyWidth * enemyWidthFactor

        // Set an attack distance as a percentage of the sum of player and adjusted enemy widths
        val attackDistancePercentage = 0.1 // Change this value between 0 and 1 to control the attack distance
        val attackDistance = ((playerWidth + adjustedEnemyWidth) * attackDistancePercentage).toFloat()

        return attackDistance
    }










}





