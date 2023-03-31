package com.example.myapplication.ui.main

import com.example.myapplication.jobSkills.HeavySlash
import com.example.myapplication.jobSkills.Slash

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

    fun updateClass(newClass: PlayerClass) {
        playerClass = newClass
        loadout = generateLoadoutForClass(newClass.name)
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



























