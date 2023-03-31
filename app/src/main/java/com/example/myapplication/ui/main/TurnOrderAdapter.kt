package com.example.myapplication.ui.main

import android.util.Log
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
    val speed: Int,
    var isVisible: Boolean = true // Add this line
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
        println("on bind is being used")
        Log.d("TurnOrderAdapter", "Binding item at position $position with id ${currentItem.id}")
        holder.turnOrderIcon.setImageResource(currentItem.imageResource)
        holder.turnOrderSpeed.text = currentItem.speed.toString()
        holder.turnOrderIcon.visibility = if (currentItem.isVisible) View.VISIBLE else View.INVISIBLE // Update this line
    }

    fun updateItemVisibility(id: String, isVisible: Boolean) {
        val index = turnOrderItems.indexOfFirst { it.id == id }
        if (index != -1) {
            turnOrderItems[index].isVisible = isVisible
            notifyItemChanged(index)
        }
    }

    override fun getItemCount(): Int {
        return turnOrderItems.size
    }

    fun update(newTurnOrderItems: List<TurnOrderItem>) {
        turnOrderItems = newTurnOrderItems
        notifyDataSetChanged()
    }



}

