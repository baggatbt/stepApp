package com.example.myapplication.ui.main

data class Loadout(
        var skill1: Skills,
        var skill2: Skills,
        var skill3: Skills
)

open class Player(name: String, speed: Int, health: Int,
                  var level: Int,
                  var attack: Int,
                  var defense: Int,
                  var playerJob: String,
                  var gold: Int,
                  var experience: Int = 0,
                  var loadout: Loadout) : GameEntity(name, speed, health) {



}












