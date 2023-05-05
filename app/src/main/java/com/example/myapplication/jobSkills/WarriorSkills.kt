package com.example.myapplication.jobSkills

import com.example.myapplication.R
import com.example.myapplication.ui.main.GameEntity
import com.example.myapplication.ui.main.Skills

class Slash : Skills(
    name = "Slash",
    damage = 1,
    speed = 2,
    attacksInChain = 3,
    attackFrameSequences = listOf(
        intArrayOf(
            R.drawable.adventurer_attack00,
            R.drawable.adventurer_attack01,
            R.drawable.adventurer_attack03,
            R.drawable.adventurer_attack04,
            R.drawable.adventurer_attack05
        ),
        intArrayOf(
            R.drawable.adventurer_attack2_00,
            R.drawable.adventurer_attack2_01,
            R.drawable.adventurer_attack2_03,
            R.drawable.adventurer_attack2_04,
            R.drawable.adventurer_attack2_05
        ),
        intArrayOf(
            R.drawable.adventurer_attack3_00,
            R.drawable.adventurer_attack3_01,
            R.drawable.adventurer_attack3_03,
            R.drawable.adventurer_attack3_04,
            R.drawable.adventurer_attack3_05
        )

    ),
    timingWindowStartFrame = 0,
    timingWindowEndFrame = 5
) {
    override fun use(target: GameEntity) {
        target.takeDamage(damage)
    }
}


class HeavySlash : Skills(
    name = "Heavy Slash",
    damage = 5,
    speed = 6,
    attacksInChain = 5,
    attackFrameSequences = listOf(
        intArrayOf(
            // Frames will go here
        )
    ),
    timingWindowStartFrame = 2,
    timingWindowEndFrame = 3
) {
    override fun use(target: GameEntity) {
        target.takeDamage(damage)
    }
}
