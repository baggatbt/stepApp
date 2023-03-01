package com.example.myapplication.ui.main


import android.media.Image
import com.example.myapplication.R

enum class EnemyType(var enemyID: Int, var enemyName: String, var level: Int, var attack: Int, var defense: Int,
                     var health: Int, var experienceReward: Int, var goldReward: Int, var speed: Int)
{
    GOBLIN(1,"Goblin",1,1,0,5,5,3,4)

}

class Enemy(private val type: EnemyType) {
    val enemyID = type.enemyID
    val enemyName = type.enemyName
    var health = type.health

    fun takeDamage(damage: Int) {
        health -= damage
    }

    fun use(enemy: Enemy? = null) {
        when (type) {
            EnemyType.GOBLIN -> {
                // Logic for using the slash ability
                enemy?.takeDamage(1)
            }
        }
    }
}

/*class Goblin (
    attackModifier: Int,
    defenseModifier: Int,
) : Enemy(1,"Goblin", 1, 1 + attackModifier, 0 + defenseModifier, 3, 5, 3,3)



class Slime(
    attackModifier: Int,
    defenseModifier: Int
) : Enemy(2,"Slime", 1, 1 + attackModifier, 0 + defenseModifier, 3, 5, 3,6)

 */