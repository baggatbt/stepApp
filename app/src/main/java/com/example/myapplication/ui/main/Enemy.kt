package com.example.myapplication.ui.main


import android.media.Image
import com.example.myapplication.R
import kotlin.random.Random

enum class EnemyType(
    val enemyID: Int,
    val enemyName: String,
    val level: Int,
    val attack: Int,
    val defense: Int,
    val health: Int,
    val experienceReward: Int,
    val goldReward: Int,
    val speed: Int,
    val specialAttackSpeed: Int,
    val attacksToChargeSpecial: Int,
    val abilities: List<EnemyAbility>
) {
    GOBLIN(1, "Goblin", 1, 2, 0, 6, 5, 3, 3, 1, 2, listOf(EnemyAbility.SWIPE, EnemyAbility.TACKLE)),
    SLIME(2, "Slime", 1, 2, 0, 6, 5, 3, 4, 9, 2, listOf(EnemyAbility.SLAP, EnemyAbility.SQUISH))
}

class Enemy(private val type: EnemyType) : GameEntity(type.enemyName, type.speed, type.health) {

    val enemyID = type.enemyID
    val level = type.level
    val attack = type.attack
    val defense = type.defense
    val experienceReward = type.experienceReward
    val goldReward = type.goldReward
    val abilities = type.abilities
    val specialAttackSpeed = type.specialAttackSpeed
    var attacksToChargeSpecial = type.attacksToChargeSpecial

    fun attack() {
        // Implement the logic for the enemy's attack here
    }

    fun takeDamage(damage: Int) {
        health -= damage
    }
}



//isSpecial denotes whether or not the ability is the enemies special attack, usable when charged.
enum class EnemyAbility(var damage: Int, var speed: Int, var staminaCost: Int, var isSpecial: Boolean) {
    //Goblin
    SWIPE(4,1,3, true),
    TACKLE(2,4,3,false),
    //Slime
    SLAP(2,5,3,false),
    SQUISH(5,9,5,true)
}

class EnemySkills(private val type: EnemyAbility) {
    var speed: Int = type.speed
    var damage: Int = type.damage
    var staminaCost: Int = type.staminaCost

    //Takes an optional parameter
    fun useEnemySkill(enemy: Enemy? = null, player: Player? = null) {

        when (type) {
            EnemyAbility.TACKLE -> {

                //do damage
            }
            EnemyAbility.SWIPE -> {

                //do damage
            }
            EnemyAbility.SLAP -> {

                //do damage
            }
            EnemyAbility.SQUISH -> {

               // do damage
            }
        }
    }
}

