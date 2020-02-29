package com.xbrid.freefalldetector.receiver

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import com.xbrid.freefalldetector.services.DetectedActivitiesIntentService

class BootReceiver : BroadcastReceiver() {

    override fun onReceive(context: Context?, intent: Intent?) {
        if (intent?.action.equals("android.intent.action.BOOT_COMPLETED")) {
            val bgService = Intent(context, DetectedActivitiesIntentService::class.java)
            context?.startService(bgService)
        }
    }
}