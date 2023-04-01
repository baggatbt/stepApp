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


