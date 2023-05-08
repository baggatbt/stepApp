package com.example.myapplication.ui.main

import android.widget.ImageView

interface OnEnemyClickListener {
    fun onEnemyClick(enemy: Enemy, enemyImageView: ImageView)
}
