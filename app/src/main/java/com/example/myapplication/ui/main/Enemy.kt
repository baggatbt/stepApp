package com.example.myapplication.ui.main



open class Enemy(
    open var name: String,
    var level: Int,
    var attack: Int,
    var defense: Int,
    var health: Int,
    var experienceReward: Int,
    var goldReward: Int
)

class Goblin(
    attackModifier: Int,
    defenseModifier: Int
) : Enemy("Goblin", 1, 1 + attackModifier, 0 + defenseModifier, 3, 5, 3)

class Slime(
    attackModifier: Int,
    defenseModifier: Int
) : Enemy("Slime", 1, 1 + attackModifier, 0 + defenseModifier, 3, 5, 3)