package com.example.myapplication.ui.main

import android.app.Application

class MyApplication : Application() {
    lateinit var stepsManager: StepsManager
        private set

    override fun onCreate() {
        super.onCreate()
        stepsManager = StepsManager(this)
        instance = this
    }

    companion object {
        lateinit var instance: MyApplication
            private set
    }
}
