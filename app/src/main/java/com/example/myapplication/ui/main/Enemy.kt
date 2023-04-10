package com.example.myapplication.ui.main


import android.media.Image
import com.example.myapplication.R
import kotlin.random.Random

data class EnemyAttackAnimation(
    val attackFrames: IntArray,
    val timingWindowStartFrame: Int,
    val timingWindowEndFrame: Int
)

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
    val abilities: List<EnemyAbility>,
    val drawableId: Int,
    val attackAnimation: EnemyAttackAnimation // Add this line
)   {
    GOBLIN(1, "Goblin", 1, 2, 0, 6, 5, 3, 3, 1, 2, listOf(EnemyAbility.SWIPE, EnemyAbility.TACKLE),1,EnemyAttackAnimation(
        attackFrames = intArrayOf(R.drawable.goblin, R.drawable.goblin),
        timingWindowStartFrame = 2,
        timingWindowEndFrame = 3),
    ),
    SLIME(2, "Slime", 1, 2, 0, 6, 5, 3, 4, 9, 2, listOf(EnemyAbility.SLAP, EnemyAbility.SQUISH),2,EnemyAttackAnimation(
        attackFrames = intArrayOf(R.drawable.idle_slime, R.drawable.slime_motion1,R.drawable.slime_motion2,R.drawable.slime_motion3), //TODO: These need actual animation frames
        timingWindowStartFrame = 2,
        timingWindowEndFrame = 3))
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
    var drawableId = type.drawableId
    val attackAnimation = type.attackAnimation // Add this line

    fun getImageResource(): Int {
        return when (drawableId) {
            1 -> R.drawable.goblin
            2 -> R.drawable.idle_slime
            else -> throw IllegalArgumentException("Invalid drawableId: $drawableId")
        }
    }

    fun attack() {
        // Implement the logic for the enemy's attack here
    }
}

//isSpecial denotes whether or not the ability is the enemies special attack, usable when charged.
enum class EnemyAbility(var damage: Int, var speed: Int, var staminaCost: Int, var isSpecial: Boolean) {
    //Goblin
    SWIPE(2, 4, 3, true),
    TACKLE(2, 4, 3, false),
    //Slime
    SLAP(2, 5, 3, false), //TODO: For now both abilities are same for reducing art work
    SQUISH(2, 5, 3, true)
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