package com.example.myapplication

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.animation.ObjectAnimator
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import com.example.myapplication.ui.main.*
import com.example.myapplication.MainActivity

import kotlin.random.Random


class BattleActivity : AppCompatActivity() {
    private lateinit var attackButton: Button
    private lateinit var enemy: Enemy
    lateinit var battle: Battle
    var player = MainActivity.player

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
            setContentView(R.layout.activity_battle)

        var goblinImageView: ImageView = findViewById(R.id.goblinImage)
        var slimeImageView: ImageView = findViewById(R.id.slimeImage)


        attackButton = findViewById(R.id.attackButton)

        // Initialize the player
        player = Player(player.level,player.gold,player.attack,player.defense,player.health,player.experience,player.skills)

        //Initialize the battle
        battle = Battle()

        // Create a list of enemies
        val enemies = listOf(Goblin(0, 0), Slime(0, 0))

        // Shuffle the list and select the first element as the enemy
        enemy = enemies.shuffled().first()

        if (enemy.name == "Goblin"){
            goblinImageView.visibility = View.VISIBLE
        }

        else if ( enemy.name == "Slime"){
            slimeImageView.visibility = View.VISIBLE
        }

        println("You've been attacked by a ${enemy.name}")
    }

    private fun animateKnight() {
        // Create an ObjectAnimator to animate the x position of the basicKnight image
        var basicKnight: ImageView = findViewById(R.id.basicKnight)
        val animator = ObjectAnimator.ofFloat(basicKnight, "x", basicKnight.x, basicKnight.x + 50f)
        animator.duration = 500
        animator.start()

        // Add a listener to the animator to reverse the animation when it's finished
        animator.addListener(object : AnimatorListenerAdapter() {
            override fun onAnimationEnd(animation: Animator) {
                super.onAnimationEnd(animation)
                val reverseAnimator = ObjectAnimator.ofFloat(basicKnight, "x", basicKnight.x, basicKnight.x - 50f)
                reverseAnimator.duration = 500
                reverseAnimator.start()
            }
        })
    }

    fun playerAttack(view: View) {
        animateKnight()

        val context = view.context
        battle.start(player, enemy, context, "attack")


    }

    fun playerDefend(view: View) {
        val context = view.context
        battle.start(player, enemy, context, "defend")
    }
}


