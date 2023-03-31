package com.example.myapplication.jobClasses
import com.example.myapplication.jobSkills.HeavySlash
import com.example.myapplication.jobSkills.Slash
import com.example.myapplication.ui.main.*

class Warrior(name: String, speed: Int, health: Int, level: Int, attack: Int, defense: Int, gold: Int, experience: Int, loadout: Loadout)
    : Player(name, speed, health, level, attack, defense, warrior, gold, experience, loadout) {

    // Initialize Warrior-specific properties here if needed.

    companion object {
        val warrior = PlayerClass(
            name = "Warrior",
            availableSkills = listOf(Slash(), HeavySlash()), // Skill3(), Skill4(), Skill5() ( to be added later)
            unlockedSkills = mutableListOf(Slash(), HeavySlash())
        )
    }
}
