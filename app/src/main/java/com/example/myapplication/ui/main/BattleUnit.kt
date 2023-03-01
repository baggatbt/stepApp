package com.example.myapplication.ui.main

class BattleUnit(private val unit: Any, val speed: Int = 0) {
    init {
        require(unit is Player || unit is Enemy) { "Invalid unit type" }
    }
}

