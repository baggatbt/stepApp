package com.example.myapplication.ui.main

abstract class Skills(
    val name: String,
    val damage: Int,
    val speed: Int,
    val attacksInChain: Int,
    val attackFrameSequences: List<IntArray>,
    val timingWindowStartFrame: Int,
    val timingWindowEndFrame: Int
) {
    abstract fun use(target: GameEntity): Unit
}
