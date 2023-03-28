package com.example.myapplication.ui.main

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.myapplication.R

data class TurnOrderItem(
    val id: String,
    val imageResource: Int,
    val speed: Int
)



class TurnOrderAdapter(var turnOrderItems: List<TurnOrderItem>) : RecyclerView.Adapter<TurnOrderAdapter.ViewHolder>() {

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val turnOrderIcon: ImageView = itemView.findViewById(R.id.turnOrderIcon)
        val turnOrderSpeed: TextView = itemView.findViewById(R.id.turnOrderSpeed)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.turn_order_item, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val currentItem = turnOrderItems[position]

        holder.turnOrderIcon.setImageResource(currentItem.imageResource)
        holder.turnOrderSpeed.text = currentItem.speed.toString()
    }

    override fun getItemCount(): Int {
        return turnOrderItems.size
    }
}
