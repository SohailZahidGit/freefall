package com.xbrid.freefalldetector.receiver

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.os.Build
import com.xbrid.freefalldetector.services.BackgroundDetectedActivitiesService

class BootReceiver : BroadcastReceiver() {

    override fun onReceive(context: Context?, intent: Intent?) {
        val bgService = Intent(context, BackgroundDetectedActivitiesService::class.java)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            context?.startForegroundService(bgService)
        } else {
            context?.startService(bgService)
        }
    }
}