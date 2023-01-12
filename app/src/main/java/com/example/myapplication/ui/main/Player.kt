package com.example.myapplication.ui.main
import com.example.myapplication.MainActivity.Companion.player
import com.example.myapplication.ui.main.Job
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch


class Player(var level:Int, var attack:Int, var defense:Int, var playerJob:String, var gold: Int, var health:Int, var experience:Int = 0) {
    var playerSkills = listOf<Skill>()
    fun setJob(newJob: String) {
        playerJob = newJob
        val job = Job()
        if(newJob == "Knight"){
            job.knight()
            playerSkills = job.skillList
        }
    }
}



