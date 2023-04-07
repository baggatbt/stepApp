package com.example.myapplication.ui.main

import com.example.myapplication.jobSkills.HeavySlash
import com.example.myapplication.jobSkills.Slash
import kotlin.math.max

data class Loadout(
        var skill1: Skills,
        var skill2: Skills,
        var skill3: Skills
)

// Player.kt

data class PlayerClass(
    val name: String,
    val availableSkills: List<Skills>,
    val unlockedSkills: MutableList<Skills>
)

open class Player(
    name: String,
    speed: Int,
    health: Int,
    var level: Int,
    var attack: Int,
    var defense: Int,
    var playerClass: PlayerClass,
    var gold: Int,
    var experience: Int = 0,
    var loadout: Loadout
) : GameEntity(name, speed, health) {
    var isParrying = false

    fun updateClass(newClass: PlayerClass) {
        playerClass = newClass
        loadout = generateLoadoutForClass(newClass.name)
    }

    override fun takeDamage(damage: Int) {
        val actualDamage = if (isParrying) (damage / 2) else damage // Replace 2 with the desired damage reduction factor
        currentHealth = max(0, currentHealth - actualDamage)
        if (isParrying){
            println("PARRIED SON")
        }
    }



    companion object {

        fun generateLoadoutForClass(playerClass: String): Loadout {
            return when (playerClass) {
                "Warrior" -> Loadout(skill1 = Slash(), skill2 = HeavySlash(), skill3 = Slash())
                // Add cases for other classes here
                else -> Loadout(skill1 = Slash(), skill2 = HeavySlash(), skill3 = Slash()) // Default case
            }
        }
    }
}



























