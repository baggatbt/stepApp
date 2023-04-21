package com.example.myapplication.jobSkills

import com.example.myapplication.R
import com.example.myapplication.ui.main.GameEntity
import com.example.myapplication.ui.main.Skills

class Slash : Skills("Slash", 2, 2, 3,
    intArrayOf(R.drawable.adventurer_attack00,R.drawable.adventurer_attack01,R.drawable.adventurer_attack03,R.drawable.adventurer_attack04,R.drawable.adventurer_attack05),
    0, 5) {

    override fun use(target: GameEntity) {
        target.takeDamage(damage)
    }
}

class HeavySlash : Skills("Heavy Slash", 5, 6, 5, intArrayOf(
    //Frames will go here
), 2, 3) {

    override fun use(target: GameEntity) {
        target.takeDamage(damage)
    }
}
