import android.graphics.PointF
import com.example.myapplication.ui.main.Quest

sealed class PointOfInterest(val location: PointF, var visited: Boolean = false) {
    abstract fun onClick(): Boolean
}

class BattlePoint(location: PointF, val battleId: Int) : PointOfInterest(location) {
    override fun onClick(): Boolean {
        // Handle click event for the battle point
        return true
    }
}

class QuestPoint(location: PointF, val quest: Quest) : PointOfInterest(location) {
    override fun onClick(): Boolean {
        // Handle click event for the quest point
        return true
    }
}
