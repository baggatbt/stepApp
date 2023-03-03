package com.example.myapplication.ui.main

data class Loadout(
        var skill1: Skills,
        var skill2: Skills,
        var skill3: Skills
)

class Player(
        var level: Int,
        var attack: Int,
        var defense: Int,
        var playerJob: String,
        var gold: Int,
        var health: Int,
        var experience: Int = 0,
        var loadout: Loadout
) {
        fun takeDamage(damage: Int) {
                health -= damage
        }
}

