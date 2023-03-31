package com.example.myapplication.jobSkills

import com.example.myapplication.R
import com.example.myapplication.ui.main.GameEntity
import com.example.myapplication.ui.main.Skills

class Slash : Skills("Slash", 2, 2, 3,
    intArrayOf(R.drawable.basicslash1, R.drawable.basicslash2, R.drawable.basicslash3, R.drawable.basicslash4),
    1, 4) {

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
