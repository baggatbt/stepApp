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
import android.widget.Toast
import com.example.myapplication.ui.main.EnemyType
import com.example.myapplication.ui.main.Enemy




import kotlin.random.Random


class BattleActivity : AppCompatActivity(), OnEnemyHealthChangedListener, OnEnemyClickListener {

    private lateinit var abilityOneButton: Button
    private lateinit var basicAttackButton: Button
    private lateinit var enemyAdapter: EnemyAdapter
    private lateinit var enemiesRecyclerView: RecyclerView
    private lateinit var turnOrderAdapter: TurnOrderAdapter
    private lateinit var turnOrderRecyclerView: RecyclerView

    lateinit var battle: Battle
    var player = MainActivity.player
    private var selectedEnemy: Enemy? = null

    private fun updateSelectedEnemyUI() {
        // Update the UI to show the selected enemy
        for (i in 0 until enemiesRecyclerView.childCount) {
            val enemyViewHolder = enemiesRecyclerView.findViewHolderForAdapterPosition(i)
            if (enemyViewHolder is EnemyAdapter.EnemyViewHolder) {
                val enemyImageView =
                    enemyViewHolder.itemView.findViewById<ImageView>(R.id.enemyImageView)
                val targetedEnemyIcon =
                    enemyViewHolder.itemView.findViewById<ImageView>(R.id.targetedEnemyIcon) // Change this line
                if (selectedEnemy == enemyViewHolder.enemy) {
                    enemyImageView.translationZ = 10f // Set the translationZ to a higher value
                    targetedEnemyIcon.translationZ =
                        11f // Set the translationZ to an even higher value
                    targetedEnemyIcon.visibility =
                        View.VISIBLE // Make the targetedEnemyIcon visible
                } else {
                    // Remove the border or reset the background color for other enemies
                    enemyImageView.background = null
                    enemyImageView.translationZ = 0f // Reset the translationZ
                    targetedEnemyIcon.translationZ = 0f // Reset the translationZ
                    targetedEnemyIcon.visibility =
                        View.INVISIBLE // Hide the targetedEnemyIcon for other enemies
                }
            }
        }
    }


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

        turnOrderRecyclerView = findViewById(R.id.turnOrderRecyclerView)
        turnOrderAdapter = TurnOrderAdapter(listOf())
        turnOrderRecyclerView.adapter = turnOrderAdapter

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
        val chosenSkill = battle.start(player, enemyAdapter.enemies, this) // Get the chosen skill
        val turnOrderItems = battle.generateTurnOrderItems(chosenSkill, enemyAdapter.enemies)
        turnOrderAdapter.turnOrderItems = turnOrderItems

    }

    // Sets up the enemies RecyclerView
    // Sets up the enemies RecyclerView
    private fun setupEnemiesRecyclerView() {
        enemiesRecyclerView = findViewById(R.id.enemiesRecyclerView)
        enemiesRecyclerView.layoutManager =
            LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)

        val enemies =
            generateEnemyList() // Use the generateEnemyList() function to create a list of random enemies

        val adapter = EnemyAdapter(enemies, this)
        enemiesRecyclerView.adapter = adapter

        enemyAdapter = adapter
    }

    override fun onEnemyClick(enemy: Enemy) {
        selectedEnemy = enemy
        battle.selectedEnemy = enemy // Update the selected enemy in the Battle instance
        updateSelectedEnemyUI()
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
}
