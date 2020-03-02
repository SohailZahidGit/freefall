package com.xbrid.freefall

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.xbrid.freefalldetector.db.DatabaseHelper
import com.xbrid.freefalldetector.services.DetectedActivitiesIntentService
import com.xbrid.freefalldetector.utils.Constants
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setupRecycleView()
        startService(Intent(this, DetectedActivitiesIntentService::class.java))
    }

    private fun setupRecycleView() {
        recycler.adapter = DetectionAdaptor(DatabaseHelper(this@MainActivity).getAllEvents())
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
                // we can only up update newly added item only
                recycler.adapter =
                    DetectionAdaptor(DatabaseHelper(this@MainActivity).getAllEvents())
                (recycler.adapter as DetectionAdaptor).notifyDataSetChanged()
            }
        }
    }

    override fun onPause() {
        unregisterReceiver(myReceiver)
        super.onPause()
    }
}

