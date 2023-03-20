package com.example.myapplication.ui.main


import android.media.Image
import com.example.myapplication.R
import kotlin.random.Random

enum class EnemyType(var enemyID: Int, var enemyName: String, var level: Int, var attack: Int, var defense: Int,
                     var health: Int, var experienceReward: Int, var goldReward: Int, var speed: Int, var specialAttackSpeed: Int, var attacksToChargeSpecial : Int, var abilities: List<EnemyAbility>)
{
    GOBLIN(1,"Goblin",1,1,0,7,5,3,4,1,2,listOf(EnemyAbility.SWIPE, EnemyAbility.TACKLE)),
    SLIME(2,"Slime",1,1,0,5,5,3,4,2, 2,listOf(EnemyAbility.SLAP, EnemyAbility.SQUISH))

}



class Enemy(private val type: EnemyType) : GameEntity() {
    var enemyID = type.enemyID
    var enemyName = type.enemyName
    var level = type.level
    var attack = type.attack
    var defense = type.defense
    var health = type.health
    var experienceReward = type.experienceReward
    var goldReward = type.goldReward
    var speed = type.speed
    var abilities = type.abilities
    //The number of turns they take before special
    var specialAttackSpeed = type.specialAttackSpeed
    var attacksToChargeSpecial = type.attacksToChargeSpecial
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
    SLAP(5,9,3,true),
    SQUISH(1,1,5,false)
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

