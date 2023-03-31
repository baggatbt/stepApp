package com.example.myapplication.ui.main



abstract class Skills(val name: String,
                     val damage: Int,
                     val speed: Int,
                     val staminaCost: Int,
                     val attackFrames: IntArray,
                     val timingWindowStartFrame: Int,
                     val timingWindowEndFrame: Int) {

    abstract fun use(target: GameEntity): Unit
}








/**
 * To create a new skill you can:

val slashSkill = Skills(AbilityType.SLASH)
val blockCard = Skills(AbilityType.BLOCK)

Then, to use a card's ability, you can simply call the use() method:

slashSkill.use()
blockSkill.use()
 */
