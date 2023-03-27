package com.example.myapplication

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.animation.ObjectAnimator
import android.content.Context
import android.os.Bundle
import android.text.Layout
import android.view.LayoutInflater
import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.LinearLayout
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.myapplication.ui.main.*
import com.example.myapplication.MainActivity


import kotlin.random.Random


class BattleActivity : AppCompatActivity(), OnEnemyHealthChangedListener {
    private lateinit var abilityOneButton: Button
    private lateinit var basicAttackButton: Button
    private lateinit var enemyAdapter: EnemyAdapter
    private lateinit var enemiesRecyclerView: RecyclerView

    lateinit var battle: Battle
    var player = MainActivity.player

    // Called when the health of an enemy changes
    override fun onEnemyHealthChanged(enemy: Enemy) {
        // Update the RecyclerView to show the new health values
        (enemiesRecyclerView.adapter as? EnemyAdapter)?.notifyDataSetChanged()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_battle)

        // Set up the RecyclerView containing the list of enemies
        setupEnemiesRecyclerView()

        // Initialize and set up the ability buttons and basic attack button
        val abilityCardsLayout = LayoutInflater.from(this).inflate(R.layout.ability_cards, null)
        val basicAttackLayout = LayoutInflater.from(this).inflate(R.layout.basicattackbutton, null)
        val bottomLayout = findViewById<ConstraintLayout>(R.id.root)

        // Add constraints to position the ability cards at the bottom of the root layout
        val params = ConstraintLayout.LayoutParams(
            ConstraintLayout.LayoutParams.MATCH_PARENT,
            ConstraintLayout.LayoutParams.WRAP_CONTENT
        )
        val params2 = ConstraintLayout.LayoutParams(
            ConstraintLayout.LayoutParams.MATCH_PARENT,
            ConstraintLayout.LayoutParams.WRAP_CONTENT
        )
        params.bottomToBottom = ConstraintLayout.LayoutParams.PARENT_ID
        params2.bottomToBottom = ConstraintLayout.LayoutParams.PARENT_ID
        abilityCardsLayout.layoutParams = params
        basicAttackLayout.layoutParams = params2

        bottomLayout.addView(abilityCardsLayout)
        bottomLayout.addView(basicAttackLayout)

        abilityOneButton = abilityCardsLayout.findViewById(R.id.ability_card_1)
        basicAttackButton = basicAttackLayout.findViewById(R.id.basicAttackButton)

        // Initialize the battle and start it with the first enemy in the list
        battle = Battle(this)
        battle.start(player, enemyAdapter.enemies, this) // Pass the list of enemies to the start function
    }

    // Sets up the enemies RecyclerView
    private fun setupEnemiesRecyclerView() {
        enemiesRecyclerView = findViewById(R.id.enemiesRecyclerView)
        enemiesRecyclerView.layoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)

        val enemies = generateEnemyList() // Use the generateEnemyList() function to create a list of random enemies

        val adapter = EnemyAdapter(enemies)
        enemiesRecyclerView.adapter = adapter

        enemyAdapter = adapter
    }

    // Generates a random list of enemies based on the number of enemies specified
    private fun generateEnemyList(): List<Enemy> {
        val numberOfEnemies = 2
        val enemyList = mutableListOf<Enemy>()
        for (i in 1..numberOfEnemies) {
            val enemyType = EnemyType.values().random()
            enemyList.add(Enemy(enemyType))
        }
        return enemyList
    }

    // The rest of the BattleActivity class
}

