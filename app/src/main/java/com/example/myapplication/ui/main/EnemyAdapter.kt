package com.example.myapplication.ui.main

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.ProgressBar
import androidx.recyclerview.widget.RecyclerView
import com.example.myapplication.R

class EnemyAdapter(val enemies: List<Enemy>) :
    RecyclerView.Adapter<EnemyAdapter.EnemyViewHolder>() {

    inner class EnemyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val enemyImageView: ImageView = itemView.findViewById(R.id.enemyImageView)
        val enemyHealthBar: ProgressBar = itemView.findViewById(R.id.enemyHealthBar)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): EnemyViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.enemy_item, parent, false)
        return EnemyViewHolder(view)
    }

    override fun onBindViewHolder(holder: EnemyViewHolder, position: Int) {
        val enemy = enemies[position]
        holder.enemyImageView.setImageResource(enemy.getImageResource())
        holder.enemyHealthBar.max = enemy.maxHealth
        holder.enemyHealthBar.progress = enemy.currentHealth // Assuming you want to start with the max health
    }


    override fun getItemCount() = enemies.size

    private fun getDrawableIdForEnemyType(drawableId: Int): Int {
        return when (drawableId) {
            1 -> R.drawable.goblin
            2 -> R.drawable.idle_slime
            else -> throw IllegalArgumentException("Invalid drawableId: $drawableId")
        }
    }

    fun getEnemy(position: Int): Enemy {
        return enemies[position]
    }
}
