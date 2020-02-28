package com.xbrid.freefalldetector.receiver

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import com.google.android.gms.location.ActivityTransitionEvent
import com.google.android.gms.location.ActivityTransitionResult
import com.google.android.gms.location.DetectedActivity

class FreeFallDetectReceiver : BroadcastReceiver() {

    private var mContext: Context? = null

    override fun onReceive(context: Context?, intent: Intent?) {
        mContext = context
        if (ActivityTransitionResult.hasResult(intent)) {
            val result = ActivityTransitionResult.extractResult(intent)
            processTransitionResult(result)
        }
    }

    private fun processTransitionResult(result: ActivityTransitionResult?) {
        result?.transitionEvents?.forEach { event -> onDetectedTransitionEvent(event) }
    }

    private fun onDetectedTransitionEvent(activity: ActivityTransitionEvent) {
        when (activity.activityType) {
            DetectedActivity.ON_BICYCLE,
            DetectedActivity.RUNNING,
            DetectedActivity.TILTING -> {
                // Make whatever you want with the activity
            }
            else -> {
            }
        }
    }
}