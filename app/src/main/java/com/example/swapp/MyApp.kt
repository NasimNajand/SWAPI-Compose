package com.example.swapp

import android.app.Application
import android.util.Log
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class MyApp : Application() {
    companion object {
        private const val TAG = "MyApp"
    }
    override fun onCreate() {
        super.onCreate()
        Log.i(TAG, "onCreate: ")
    }
}