package com.example.myapplication.ui.main

import com.example.myapplication.MainActivity
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch


class Skill() {
    companion object {

        fun basicAttack(enemy: Enemy){
            enemy.health  -= MainActivity.player.attack
        }

        fun defend(player: Player) {
            player.defense++
            GlobalScope.launch {
                delay(500L)
                player.defense--
            }
        }
    }

}