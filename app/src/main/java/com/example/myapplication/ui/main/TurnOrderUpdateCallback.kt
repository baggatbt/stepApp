package com.example.myapplication.ui.main

interface TurnOrderUpdateCallback {
    fun onTurnOrderUpdated(turnOrderItems: List<TurnOrderItem>)
}
