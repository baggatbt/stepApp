package com.example.myapplication.ui.main

import android.app.Activity
import android.media.Image
import android.widget.ImageView
import com.example.myapplication.R



enum class AbilityType(var damage: Int, var speed: Int, var staminaCost: Int, var startWindow: Long, var endWindow: Int) {
    SLASH(2,2,3,250,500),
    HEAVYSLASH(5,6,5,350,500)
}

class Skills(private val type: AbilityType) {
    var speed: Int = type.speed
    var damage: Int = type.damage
    var staminaCost: Int = type.staminaCost
    var startWindow: Long = type.startWindow
    var endWindow: Int = type.endWindow


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
