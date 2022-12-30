package com.example.myapplication

import android.os.Bundle
import android.view.View
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import com.example.myapplication.MainActivity.Companion.enemy
import com.example.myapplication.MainActivity.Companion.player
import com.example.myapplication.ui.main.Battle
import com.example.myapplication.ui.main.Player
import com.example.myapplication.ui.main.Enemy


class BattleActivity : AppCompatActivity() {

    private lateinit var attackButton: Button
    lateinit var battle: Battle

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_battle)

        attackButton = findViewById(R.id.attackButton)


    }

}


