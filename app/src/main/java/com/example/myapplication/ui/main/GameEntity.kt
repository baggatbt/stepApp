package com.example.myapplication.ui.main

open class GameEntity(val name: String, var speed: Int, var maxHealth: Int) {

    var currentHealth: Int = maxHealth


    fun isAlive() = currentHealth > 0

    fun takeDamage(damage: Int) {
        currentHealth -= damage
        if (currentHealth < 0) {
            currentHealth = 0
        }
    }
}


