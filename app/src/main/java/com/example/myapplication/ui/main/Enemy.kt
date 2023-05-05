package com.example.myapplication.ui.main


import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
import android.media.Image
import com.example.myapplication.R
import kotlin.random.Random

data class EnemyAttackAnimation(
    val moveFrames: IntArray,
    val attackFrames: IntArray, // Add this line
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
    val moveAnimation: EnemyAttackAnimation

)   {


    GOBLIN(1, "Goblin", 1, 2, 0, 6, 5, 3, 3, 1, 2, listOf(EnemyAbility.SWIPE, EnemyAbility.TACKLE),1,EnemyAttackAnimation(
        moveFrames = intArrayOf(R.drawable.goblin_running00,R.drawable.goblin_running01,R.drawable.goblin_attack02,
        R.drawable.goblin_attack03, R.drawable.goblin_running04, R.drawable.goblin_running05, R.drawable.goblin_running06, R.drawable.goblin_running07),
        attackFrames = intArrayOf(R.drawable.goblin_attack00,R.drawable.goblin_attack01,R.drawable.goblin_attack02, R.drawable.goblin_attack03, R.drawable.goblin_attack04
        ,R.drawable.goblin_attack05, R.drawable.goblin_attack06, R.drawable.goblin_attack07), // TODO: Add actual attack frame resources
        timingWindowStartFrame = 5,
        timingWindowEndFrame = 6),
    ),
    SLIME(2, "Slime", 1, 2, 0, 7, 5, 3, 4, 9, 2, listOf(EnemyAbility.SLAP, EnemyAbility.SQUISH),2,EnemyAttackAnimation(
        moveFrames = intArrayOf(R.drawable.slime_motion1,R.drawable.slime_motion1,R.drawable.slime_motion1),
        attackFrames = intArrayOf(R.drawable.slime_attack_1,R.drawable.slime_attack_2,R.drawable.slime_attack_3), // Add actual attack frame resources
        timingWindowStartFrame = 0,
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
    val moveAnimation = type.moveAnimation //
    val attackFrames = type.moveAnimation.attackFrames // Update this line


    fun getImageResource(): Int {
        return when (drawableId) {
            1 -> R.drawable.goblin_attack00
            2 -> R.drawable.idle_slime
            else -> throw IllegalArgumentException("Invalid drawableId: $drawableId")
        }
    }

    fun attack() {
        // Implement the logic for the enemy's attack here
    }
}

//isSpecial denotes whether or not the ability is the enemies special attack, usable when charged.
//isSpecial denotes whether or not the ability is the enemies special attack, usable when charged.
enum class EnemyAbility(
    var damage: Int,
    var speed: Int,
    var staminaCost: Int,
    var isSpecial: Boolean,
    var parryWindowOpen: Int, // Add this line
    var parryWindowClose: Int  // Add this line
) {
    //Goblin
    SWIPE(2, 4, 3, true, 2, 3),
    TACKLE(2, 4, 3, false, 2, 3),
    //Slime
    SLAP(1, 5, 3, false, 2, 2), //TODO: For now both abilities are same for reducing art work
    SQUISH(1, 5, 3, true, 2, 3)
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