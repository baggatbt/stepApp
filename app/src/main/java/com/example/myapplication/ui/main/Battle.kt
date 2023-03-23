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
import android.os.Handler
import android.os.Looper
import android.view.View
import android.widget.*
import com.example.myapplication.*
import kotlinx.coroutines.*
import java.util.*
import java.util.concurrent.CountDownLatch


class Battle {
    companion object {
        var expGained = 0
        var goldGained = 0
    }


    lateinit var abilityOneButton: Button
    lateinit var abilityTwoButton: Button
    lateinit var basicAttackButton: Button

    lateinit var enemy: Enemy
    lateinit var player: Player
    lateinit var context: Context
    lateinit var basicKnight : ImageView

    lateinit var goblinPicture : ImageView
    lateinit var rootView: View

    lateinit var playerHealthBar: ProgressBar
    lateinit var enemyHealthBar: ProgressBar
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
    var abilityOneExecuted = false
    var abilityTwoExecuted = false
    var currentTime: Long = 0
    var timingWindowOpen = false
    var tapTimeoutHandler: Handler? = null
    val tapTimeoutRunnable = Runnable {
        rootView.setOnClickListener(null)

    }
    var attackInProgress = false


    //Intializing
    fun start(player: Player, enemy: Enemy, context: Context) {
        this.player = player

        //TODO: Implement setPlayerSkills()
        // Set player skills
        abilityOneSkill = Skills(AbilityType.SLASH)
        abilityTwoSkill = Skills(AbilityType.HEAVYSLASH)


        this.enemy = enemy
        enemyList.add(enemy)
        println(enemy)
        this.context = context

        // Find views
        attackAnimation = (context as Activity).findViewById(R.id.basicKnight)
        basicAttackButton = (context as Activity).findViewById(R.id.basicAttackButton)
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
        basicKnight = (context as Activity).findViewById(R.id.basicKnight)
        goblinPicture = (context as Activity).findViewById(R.id.goblinImage)
        rootView = (context as Activity).findViewById<View>(R.id.root)
        playerHealthBar = (context as Activity).findViewById(R.id.playerHealthBar)
        playerHealthBar.max = player.health
        playerHealthBar.progress = player.health
        enemyHealthBar =  (context as Activity).findViewById(R.id.enemyHealthBar)
        enemyHealthBar.max = enemy.health
        enemyHealthBar.progress = enemy.health
        chosenSkill = abilityOneSkill
        generateTurnOrder(chosenSkill,enemyList)

        // Set onClickListeners for skill buttons
        /*
        basicAttackButton.setOnClickListener {
            setAbilityButtonsEnabled(false)
            generatePlayerTurnOrder(abilityOneSkill)
            rootView.setOnClickListener {
                timingWindowOpen = true
                lastTapTime = System.currentTimeMillis()
                val damageModifier = setTimingWindow(1.0f, 1.5f) // Set your desired damage boost range
                performPlayerTurn(abilityOneSkill, damageModifier)
                Handler(Looper.getMainLooper()).postDelayed({
                    setAbilityButtonsEnabled(true)
                    battleLoop()
                }, nextTurnDelay)
            }
        }

         */


        abilityOneButton.setOnClickListener {
            chosenSkill = abilityOneSkill
            generateTurnOrder(abilityOneSkill, enemyList)
            setAbilityButtonsEnabled(false)
            // Start the battle loop
            battleLoop()
        }






        abilityTwoButton.setOnClickListener {
            setAbilityButtonsEnabled(false)
            chosenSkill = abilityTwoSkill
            generatePlayerTurnOrder(abilityTwoSkill)
            rootView.setOnClickListener {
                timingWindowOpen = true
                executeCharacterTurn(abilityTwoSkill)
                Handler(Looper.getMainLooper()).postDelayed({
                    setAbilityButtonsEnabled(true)
                    battleLoop()
                }, nextTurnDelay)
            }
        }



        generateEnemyTurnOrder(enemyList)

    }

    fun onRootViewClick(view: View) {
        tapTimeoutHandler?.removeCallbacks(tapTimeoutRunnable)
        Handler(Looper.getMainLooper()).postDelayed({
            handleTimingWindow(chosenSkill)
        }, 100)

        // Remove the listener from the rootView
        rootView.setOnClickListener(null)
    }

    private var currentTurnIndex = 0 // keep track of whose turn it is currently

    val nextTurnDelay = 1000L // set delay before the next turn
    private fun battleLoop() {
        // Check if player or enemy is dead and end the battle if so
        if (player.health <= 0) {
            checkVictoryAndDefeat(rootView)
            return
        }
        if (enemy.health <= 0) {
            checkVictoryAndDefeat(rootView)
            return
        }else {
            launchAttackTurns()
            handleTimingWindow(chosenSkill)
        }

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




    private fun handleTimingWindow(chosenSkill: Skills) {
        if (attackInProgress) {
            println("window open")

            currentTime = System.currentTimeMillis()
            val skillStartWindow = chosenSkill.startWindow
            val skillEndWindow = chosenSkill.endWindow
            val delayTillTimingStartOpens = skillStartWindow

            val handler = Handler(Looper.getMainLooper())
            handler.postDelayed({
                timingFlash()
                println("350ms has passed since someFunction() was called")
            }, delayTillTimingStartOpens)

            val timeSinceLastTap = currentTime - lastTapTime
            println("current time in handle timing" + currentTime)
            println("time since last tap in handle timing $timeSinceLastTap")
            val correctTiming = timeSinceLastTap in skillStartWindow..skillEndWindow
            println("time since last tap in handle timing $timeSinceLastTap")
            if (correctTiming) {

                println("Success Effect Applied!")
                timingWindowOpen = false
            } else {
                println("Timing failed")
                timingWindowOpen = false
            }
        }

    }





    private fun generateEnemyTurnOrder(enemies:List<Enemy>) {
        // Check for each speed value in enemy list
        for (i in enemies.indices) {
            val currentEnemy = enemies[i]
            val enemySpeed = if (currentEnemy.attacksToChargeSpecial == 0) currentEnemy.specialAttackSpeed else currentEnemy.speed
            when (enemySpeed) {
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
            2 -> {monsterTurnIcon2.visibility = View.VISIBLE}
            3 -> {monsterTurnIcon3.visibility = View.VISIBLE}
            4 -> {monsterTurnIcon4.visibility = View.VISIBLE}
            5 -> {monsterTurnIcon5.visibility = View.VISIBLE}
            6 -> {monsterTurnIcon6.visibility = View.VISIBLE}
            7 -> {monsterTurnIcon7.visibility = View.VISIBLE}
            8 -> {monsterTurnIcon8.visibility = View.VISIBLE}
            9 -> {monsterTurnIcon9.visibility = View.VISIBLE}
            10 ->{monsterTurnIcon10.visibility = View.VISIBLE}

        }

    }

    private fun clearTurnOrderIcons(chosenSkill: Skills) {
        when (chosenSkill.speed){
            1 -> {monsterTurnIcon.visibility = View.INVISIBLE}
            2 -> {monsterTurnIcon2.visibility = View.INVISIBLE}
            3 -> {monsterTurnIcon3.visibility = View.INVISIBLE}
            4 -> {monsterTurnIcon4.visibility = View.INVISIBLE}
            5 -> {monsterTurnIcon5.visibility = View.INVISIBLE}
            6 -> {monsterTurnIcon6.visibility = View.INVISIBLE}
            7 -> {monsterTurnIcon7.visibility = View.INVISIBLE}
            8 -> {monsterTurnIcon8.visibility = View.INVISIBLE}
            9 -> {monsterTurnIcon9.visibility = View.INVISIBLE}
            10 ->{monsterTurnIcon10.visibility = View.INVISIBLE}

        }
    }

    var battleEnded = false // Flag to check if the battle has ended
    private fun generateTurnOrder(chosenSkill: Skills, enemies: List<Enemy>) {

        val characterSpeed = chosenSkill.speed
        val enemySpeeds = enemies.map { if (enemy.attacksToChargeSpecial == 0){
            it.specialAttackSpeed
        } else{
            it.speed
        }
        }

        turnOrder = mutableListOf<String>()

        // Add the character to the turn order list
        turnOrder.add("character")

        // Add each enemy to the turn order list and create icons on the turn order bar UI element
        for (i in enemies.indices) {
            turnOrder.add("enemy$i")
        }

        // Sort the turn order list based on speed, with lower speeds going first
        turnOrder.sortBy {
            if (it == "character") characterSpeed else enemySpeeds[it.substring(5).toInt()]
        }
    }

    var numTurnsTaken = 0 // Initialize a variable to keep track of the number of turns taken
    fun launchAttackTurns() {
        // Execute each turn in the turn order with a delay
        for ((index, turn) in turnOrder.withIndex()) {
            CoroutineScope(Dispatchers.Main).launch {
                delay((index + 1) * 1000L) // delay for 1 second times the index of the turn
                if (!battleEnded) { // Only execute the turn if the battle has not ended
                    if (turn == "character") {
                        lastTapTime = System.currentTimeMillis()
                        //TODO: figure out where this lasttaptime needs to be called for timing to work
                        animateKnight()
                        startAttackAnimation()
                        executeCharacterTurn(chosenSkill)
                    } else {
                        val enemy = enemyList[turn.substring(5).toInt()]
                        val enemyAbility = if (enemy.attacksToChargeSpecial == 0) {
                            enemy.abilities.find { it.isSpecial } ?: error("No special ability found")
                        } else {
                            enemy.abilities.first { !it.isSpecial }
                        }
                        executeEnemyTurn(enemyAbility)
                    }

                    numTurnsTaken++ // Increment the number of turns taken

                    // Check if all turns have been taken
                    if (numTurnsTaken == turnOrder.size) {
                        // Reset the turn order and number of turns taken
                        if(checkVictoryAndDefeat(rootView) == false) {
                            turnOrder.clear()
                            numTurnsTaken = 0
                            generateEnemyTurnOrder(enemyList)
                            abilityOneExecuted = false
                            setAbilityButtonsEnabled(true)
                        }
                    }

                    // Check if the battle has ended
                    if (player.health <= 0 || enemyList.all { it.health <= 0 }) {
                        endBattle()
                        battleEnded = true
                    }
                }
            }
        }
    }



    private fun executeCharacterTurn(chosenSkill: Skills){
        timingWindowOpen = true
        attackInProgress = true // Set the attackInProgress flag to true
        rootView.setOnClickListener(::onRootViewClick)
        tapTimeoutHandler = Handler(Looper.getMainLooper())
        tapTimeoutHandler?.postDelayed(tapTimeoutRunnable, 2000) // 2 seconds after launching attack listener dies

        var skillUsed = chosenSkill
        skillUsed.use(enemy)
        println("after skillUsed.use(enemy) in executeCharacterTurn")

        var damageDealt = (skillUsed.damage - enemy.defense)
        enemyHealthBar.progress -= damageDealt
        if (enemy.health <= 0) {
            goblinPicture.visibility = View.GONE

        }
        clearTurnOrderIcons(skillUsed)

        if (player.health <= 0) {
            return
        }

    }

    private fun executeEnemyTurn(enemyAbility: EnemyAbility, ) {

        if (enemy.health <= 0) {
            return
        }

        // Calculate the damage dealt by the enemy
        val attack = if (enemy.attacksToChargeSpecial == 0) {
            enemy.abilities.find { it.isSpecial } ?: error("No special ability found")
        } else {
            enemy.abilities.first { !it.isSpecial }
        }

        val damageDealt = attack.damage

        // Apply the damage to the player
        animateGoblin()
        player.takeDamage(damageDealt)
        playerHealthBar.progress -= damageDealt

        // Decrement the number of turns until the special attack can be used
        if (enemy.attacksToChargeSpecial > 0) {
            enemy.attacksToChargeSpecial--
        }

        // Print the results of the turn
        if  (enemy.attacksToChargeSpecial == 0) {
            println("${enemy.name} used ${attack.name}!")
        } else {
            println("${enemy.name} attacked with its non-special ability!")
        }
        if (enemy.health <= 0) {
            goblinPicture.visibility = View.GONE

        }
    }

    private fun endBattle() {
        checkVictoryAndDefeat(rootView)
    }

    private fun checkVictoryAndDefeat(rootView: View): Boolean {
        val victoryActivity = VictoryActivity()
        var isVictory = false

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

            // Set the isVictory flag to true
            isVictory = true
        }

        // Check if the player is defeated
        if (player.health <= 0) {
            abilityOneButton.isEnabled = false
            isBattleOver = true

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








