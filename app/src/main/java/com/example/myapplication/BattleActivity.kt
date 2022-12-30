package com.example.myapplication

import android.os.Bundle
import android.view.View
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity

import com.example.myapplication.ui.main.Battle
import com.example.myapplication.ui.main.Player
import com.example.myapplication.ui.main.Goblin


class BattleActivity : AppCompatActivity() {

    private lateinit var attackButton: Button
    lateinit var battle: Battle

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_battle)

        attackButton = findViewById(R.id.attackButton)

        //Create the enemy based on parameters passed from the quest button
        var enemy = Goblin(1,1,0,3,5,3)


    }

    fun playerAttack(view: View) {}

}


