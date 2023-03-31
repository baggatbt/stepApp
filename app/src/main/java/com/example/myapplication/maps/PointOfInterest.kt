package com.example.myapplication.maps

import android.graphics.PointF
import com.example.myapplication.ui.main.Quest

abstract class PointOfInterest(val location: PointF, var visited: Boolean = false, var requires: List<Int> = emptyList()) {
    abstract fun onClick(): Boolean
}


class BattlePoint(location: PointF, val battleId: Int, requires: List<Int> = emptyList()) : PointOfInterest(location, false, requires) {
    override fun onClick(): Boolean {
        // Handle click event for the battle point
        return true
    }
}

class QuestPoint(location: PointF, val quest: Quest, val questId: Int, requires: List<Int> = emptyList()) : PointOfInterest(location, false, requires) {
    override fun onClick(): Boolean {
        // Handle click event for the quest point
        return true
    }
}
