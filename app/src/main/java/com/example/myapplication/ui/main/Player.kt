package com.example.myapplication.ui.main

class Player constructor(
    val userName: String,
    var level: Int,
    var gold: Int,
    var attack: Int,
    var defense: Int,
    var health: Int,
    var experience: Int
) : java.io.Serializable {
        //The players stats and inventory go here
}