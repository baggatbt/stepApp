package com.example.myapplication.ui.main

open class GameEntity(val name: String, var speed: Int, var health: Int) {



    fun isAlive() = health > 0
}


