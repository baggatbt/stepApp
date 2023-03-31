package com.example.myapplication

import BattlePoint
import PointOfInterest
import QuestPoint
import android.content.Context
import android.content.Intent
import android.graphics.PointF
import android.os.Build
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import com.example.myapplication.*
import com.example.myapplication.MainActivity.Companion.currentSteps
import com.example.myapplication.MainActivity.Companion.player
import com.example.myapplication.maps.CustomMapView
import com.example.myapplication.ui.main.Quest

class MapOneActivity : AppCompatActivity() {

    companion object {
        const val BATTLE_REQUEST_CODE = 1001
    }


    private lateinit var mapView: CustomMapView
    // ...

    @RequiresApi(Build.VERSION_CODES.R)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_townone)
        mapView = findViewById(R.id.map_view)

        mapView.pointsOfInterest = listOf(
            BattlePoint(
                battleId = 1,
                location = PointF(300f, 600f)
            ),
            QuestPoint(
                location = PointF(800f, 400f),
                quest = Quest("First Quest", 10, experienceGained = 100, goldGained = 1)
            )
        )

        mapView.setOnPointClickListener { point ->
            onPointClicked(point)
        }
    }


    private fun onPointClicked(point: PointOfInterest) {
        when (point) {
            is BattlePoint -> startBattle(point)
            is QuestPoint -> startQuest(point)
            else -> throw IllegalStateException("Unsupported point type: ${point::class.java}")
        }
    }



    fun backToMap(view: View?) {
        val intent = Intent(this@MapOneActivity, MapActivity::class.java)
        startActivity(intent)
        MainActivity.currentSteps = currentSteps

    }

private fun startBattle(battlePoint: BattlePoint) {
    val intent = Intent(this@MapOneActivity, BattleActivity::class.java)
    startActivityForResult(intent, BATTLE_REQUEST_CODE)
}


private fun startQuest(questPoint: QuestPoint) {
    // Use the quest from the quest point and update the player's progress and steps
    val quest = questPoint.quest
    if (currentSteps - quest.stepCost >= 0) {
        currentSteps -= quest.stepCost
        player.experience += quest.experienceGained
        checkIfPlayerLevelUp()
        player.gold += quest.goldGained
        questPoint.visited = true
        mapView.invalidate()

        Toast.makeText(
            this,
            "You've gained ${quest.experienceGained} exp! and ${quest.goldGained} gold",
            Toast.LENGTH_LONG
        ).show()
    }
}


    fun saveData() {

        // Shared Preferences will allow saving
        // and retrieve data in the form of key,value pair.
        // In this function save data
        val sharedPreferences = getSharedPreferences("myPrefs", Context.MODE_PRIVATE)
        val editor = sharedPreferences.edit()
        editor.putInt("key1", MainActivity.currentSteps)
        editor.putInt("playerLevel", MainActivity.player.level)
        editor.putInt("playerExp", MainActivity.player.experience)
        editor.putInt("playerGold", MainActivity.player.gold)
        editor.apply()

    }

    fun calculateExpToLevel(currentLevel: Int): Int {
      return currentLevel * 10 + Math.pow(currentLevel.toDouble(), 1.5).toInt()
    }
    fun checkIfPlayerLevelUp() {
        // Calculate the amount of experience required to reach the next level
        val expToLevel = calculateExpToLevel(player.level)

        // Check if the player has earned enough experience to level up
        if (player.experience > expToLevel) {
            // If the player has earned enough experience, increment their level
            player.level += 1

            // Display a message to the user
            Toast.makeText(this, "You are now level " + player.level, Toast.LENGTH_SHORT).show()

            // Check again if the player has leveled up (to handle if they level up more than once)
            checkIfPlayerLevelUp()
        }
    }

    override fun onPause(){
        super.onPause()
        println("paused")
        println(currentSteps)
        saveData()
    }

    override fun onDestroy() {
        super.onDestroy()
        println(currentSteps)
        saveData()
    }





}