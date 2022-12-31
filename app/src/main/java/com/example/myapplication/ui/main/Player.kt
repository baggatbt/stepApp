package com.example.myapplication.ui.main

class Player constructor(
    var level: Int,
    var gold: Int,
    var attack: Int,
    var defense: Int,
    var health: Int,
    var experience: Int,
    val skills: MutableMap<String, Skill> = mutableMapOf(
        "basic attack" to Skill.basicAttack,
        "fireball" to Skill.fireball,
        "heal" to Skill.heal
)
)

class Skill(val name: String, val attack: Int, val defense: Int, val health: Int) {
    companion object {
        val basicAttack = Skill("basic attack", 1, 0, 0)
        val fireball = Skill("fireball", 5, 0, 0)
        val heal = Skill("heal", 0, 0, 5)
    }
}


