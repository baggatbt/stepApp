package com.example.myapplication.ui.main

import android.app.Activity
import android.media.Image
import android.widget.ImageView
import android.widget.ProgressBar
import com.example.myapplication.R



enum class AbilityType(var damage: Int, var speed: Int, var staminaCost: Int) {
    SLASH(2,2,3),
    HEAVYSLASH(5,6,5)
}

class Skills(private val type: AbilityType, private val activity: Activity) {
    var speed: Int = type.speed
    var damage: Int = type.damage
    var staminaCost: Int = type.staminaCost

    var lastTapTime = 0L

    fun tapSuccessTiming(startAttack: Int, endAttack: Int, enemy: Enemy?, damageBonus: Int) {
        val currentTime = System.currentTimeMillis()
        val timeSinceLastTap = currentTime - lastTapTime
        println(timeSinceLastTap)
        lastTapTime = currentTime


        // Check if the tap was within the correct timing window
        val correctTiming = timeSinceLastTap in startAttack..endAttack
        val timingWindow = 250..700
        val basicKnightImageView = activity.findViewById<ImageView>(R.id.basicKnight)
        when {
            correctTiming -> {
                enemy?.takeDamage(damageBonus)
                println("success! damage dealt increased")
            }
            timeSinceLastTap in timingWindow -> {
                println("Timing Missed, but within timing window")
                basicKnightImageView.setImageResource(R.drawable.knightflashtiming)
            }
            else -> {
                println("Timing Missed")

                }
            }
        }



    //Takes an optional parameter
    fun use(enemy: Enemy? = null, player: Player? = null) {
        when (type) {
            AbilityType.SLASH -> {
                tapSuccessTiming(250,750,enemy,1,)
                enemy?.takeDamage(AbilityType.SLASH.damage)
            }
            AbilityType.HEAVYSLASH -> {
                //Logic for the heavy slash
                enemy?.takeDamage(AbilityType.HEAVYSLASH.damage)
                }
            }
        }
    }



/**
 * To create a new skill you can:

val slashSkill = Skills(AbilityType.SLASH)
val blockCard = Skills(AbilityType.BLOCK)

Then, to use a card's ability, you can simply call the use() method:

slashSkill.use()
blockSkill.use()
 */
