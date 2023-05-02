package com.example.myapplication.ui.main

interface OnHealthChangedListener {
    fun onPlayerHealthChanged(player: Player)
    fun onEnemyHealthChanged(enemy: Enemy)
}
