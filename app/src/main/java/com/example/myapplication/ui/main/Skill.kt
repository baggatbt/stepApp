package com.example.myapplication.ui.main

import com.example.myapplication.MainActivity
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

typealias enemyInteraction = (enemy:Enemy) -> Unit
class Skill(var skillName: String, var skillDescription: String, var callback:enemyInteraction) {
    companion object {



    }
}

fun basicAttack(enemy: Enemy) {
    enemy.health  -= MainActivity.player.attack
}

var basicAtk = Skill("Basic Attack","description", ::basicAttack)


typealias playerInteraction = (player: Player) -> Unit;

class PlayerSkill(var skillName: String, var skillDescription: String, var callback:playerInteraction){}

fun defend(player: Player) {
    player.defense++
    GlobalScope.launch {
        delay(500L)
        player.defense--
    }
}
var basicDefense = PlayerSkill("Defend", "description", ::defend);

