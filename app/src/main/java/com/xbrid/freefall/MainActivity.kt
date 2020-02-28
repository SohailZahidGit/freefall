package com.xbrid.freefall

import android.content.Intent
import android.os.Build
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.xbrid.freefalldetector.services.BackgroundDetectedActivitiesService

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val bgService = Intent(this, BackgroundDetectedActivitiesService::class.java)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            startForegroundService(bgService)
        } else {
            startService(bgService)
        }
    }
}
