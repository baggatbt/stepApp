package com.example.myapplication.ui.main

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.ProgressBar
import androidx.recyclerview.widget.RecyclerView
import com.example.myapplication.R


class EnemyAdapter(
    val enemies: List<Enemy>,
    private val recyclerView: RecyclerView,
    private val onEnemyClickListener: OnEnemyClickListener
) : RecyclerView.Adapter<EnemyAdapter.EnemyViewHolder>() {

    inner class EnemyViewHolder(itemView: View, val enemyImageView: ImageView) : RecyclerView.ViewHolder(itemView), View.OnClickListener {
        val enemyHealthBar: ProgressBar = itemView.findViewById(R.id.enemyHealthBar)
        var enemy: Enemy? = null

        init {
            itemView.setOnClickListener(this)
        }

        override fun onClick(v: View?) {
            enemy?.let { onEnemyClickListener.onEnemyClick(it, enemyImageView) }
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): EnemyViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.enemy_item, parent, false)
        val enemyImageView = view.findViewById<ImageView>(R.id.enemyImageView)
        return EnemyViewHolder(view, enemyImageView)
    }



    override fun onBindViewHolder(holder: EnemyViewHolder, position: Int) {
        val enemy = enemies[position]
        holder.itemView.setOnClickListener {
            onEnemyClickListener.onEnemyClick(enemy, holder.enemyImageView)
        }

        holder.enemy = enemy
        holder.enemyImageView.setImageResource(enemy.getImageResource())
        holder.enemyHealthBar.max = enemy.maxHealth
        holder.enemyHealthBar.progress = enemy.currentHealth
    }




    override fun getItemCount() = enemies.size

    private fun getDrawableIdForEnemyType(drawableId: Int): Int {
        return when (drawableId) {
            1 -> R.drawable.goblin_attack00
            2 -> R.drawable.slime_motion1
            else -> throw IllegalArgumentException("Invalid drawableId: $drawableId")
        }
    }

    fun getEnemy(position: Int): Enemy {
        return enemies[position]
    }

    fun getEnemyImageView(position: Int): ImageView {
        return recyclerView.findViewHolderForAdapterPosition(position)?.itemView?.findViewById(R.id.enemyImageView)
            ?: throw IllegalStateException("No ViewHolder found for the given position.")
    }


}
