package com.xbrid.freefall

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.xbrid.freefalldetector.services.DetectedActivitiesIntentService
import com.xbrid.freefalldetector.utils.Constants
import com.xbrid.freefalldetector.utils.showToast

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        startService(Intent(this, DetectedActivitiesIntentService::class.java))
    }

    override fun onResume() {
        super.onResume()
        registerReceiver(myReceiver, IntentFilter(Constants.freeFallDetected))
    }

    private val myReceiver: BroadcastReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context?, intent: Intent) {
            if (intent.action.equals(
                    Constants.freeFallDetected,
                    ignoreCase = true
                )
            ) {
                showToast("onReceive")
            }
        }
    }

    override fun onPause() {
        unregisterReceiver(myReceiver)
        super.onPause()
    }
}

