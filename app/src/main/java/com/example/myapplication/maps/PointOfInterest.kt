package com.example.myapplication.maps

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.graphics.PointF
import com.example.myapplication.BattleActivity
import com.example.myapplication.ui.main.Quest

abstract class PointOfInterest(val location: PointF, var visited: Boolean = false, var requires: List<Int> = emptyList()) {
    abstract fun onClick()
    abstract fun startAction()
}

class BattlePoint(
    private val context: Context,
    location: PointF,
    val battleId: Int,
    val enemies: List<String>,
    requires: List<Int> = emptyList()
) : PointOfInterest(location, false, requires) {
    override fun onClick() {
        startAction()
    }

    override fun startAction() {
        val intent = Intent(context, BattleActivity::class.java)
        (context as Activity).startActivity(intent)
    }
}


class QuestPoint(
    private val context: Context,
    location: PointF,
    val quest: Quest,
    val questId: Int,
    requires: List<Int> = emptyList()
) : PointOfInterest(location, false, requires) {
    override fun onClick() {
        startAction()
    }

    override fun startAction() {
        // Handle starting a quest for the quest point
    }
}

class ActivityPoint(private val context: Context, location: PointF, val activityId: Int, val activityName: String, requires: List<Int> = emptyList()) : PointOfInterest(location, false, requires) {
    override fun onClick() {
        startAction()
    }

    override fun startAction() {
        val intent = Intent(context, TownOneActivity::class.java)
        (context as Activity).startActivity(intent)
    }
}