package com.example.caramelool.myapplication

import android.app.Application
import androidx.work.WorkManager

class MyApplication : Application() {

    override fun onCreate() {
        super.onCreate()
        WorkManager.getInstance().cancelAllWork()
        WorkHandler.setupWork()
    }
}