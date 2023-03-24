package com.example.myapplication.ui.main

import android.app.Activity
import android.media.Image
import android.widget.ImageView
import com.example.myapplication.R



enum class AbilityType(
    var damage: Int,
    var speed: Int,
    var staminaCost: Int,
    var attackFrames: IntArray,
    var timingWindowStartFrame: Int,
    var timingWindowEndFrame: Int
) {
    SLASH(2, 2, 3, intArrayOf(R.drawable.basicslash1, R.drawable.basicslash2, R.drawable.basicslash3, R.drawable.basicslash4),
        1, 4),
    HEAVYSLASH(5, 6, 5, intArrayOf(
        //Frames will go here
    ), 2, 3)
}


class Skills(private val type: AbilityType) {
    var speed: Int = type.speed
    var damage: Int = type.damage
    var staminaCost: Int = type.staminaCost
    var attackFrames: IntArray = type.attackFrames
    var timingWindowStartFrame: Int = type.timingWindowStartFrame
    var timingWindowEndFrame: Int = type.timingWindowEndFrame


    //Takes an optional parameter
    fun use(enemy: Enemy? = null, player: Player? = null) {
        when (type) {
            AbilityType.SLASH -> {
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
