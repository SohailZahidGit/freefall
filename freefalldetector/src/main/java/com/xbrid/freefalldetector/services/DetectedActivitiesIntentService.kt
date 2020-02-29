package com.xbrid.freefalldetector.services

import android.app.IntentService
import android.content.Intent
import com.google.android.gms.location.ActivityRecognitionResult
import com.google.android.gms.location.DetectedActivity
import com.xbrid.freefalldetector.sensor.TransitionRecognition
import com.xbrid.freefalldetector.sensor.TransitionRecognitionUtils.toActivityString
import com.xbrid.freefalldetector.sensor.TransitionRecognitionUtils.toTransitionType
import com.xbrid.freefalldetector.utils.Constants.Companion.freeFallDetected
import com.xbrid.freefalldetector.utils.showToast
import java.text.SimpleDateFormat
import java.util.*

class DetectedActivitiesIntentService :
    IntentService(DetectedActivitiesIntentService::class.java.simpleName) {

    override fun onCreate() {
        super.onCreate()
        initTransitionRecognition()
    }

    override fun onHandleIntent(intent: Intent?) {
        val result = ActivityRecognitionResult.extractResult(intent)
        val detectedActivities: ArrayList<DetectedActivity>? =
            result?.probableActivities as? ArrayList<DetectedActivity>
        detectedActivities?.forEach {

            val theActivity = toActivityString(it.type)
            val transType = toTransitionType(it.type)
            showToast(
                "Transition: "
                        + theActivity + " (" + transType + ")" + "   "
                        + SimpleDateFormat("HH:mm:ss", Locale.UK)
                    .format(Date()) + "\n\n"
            )
            // store info
        }
        sendBroadcast(getBroadCastIntent())
    }

    private fun getBroadCastIntent(): Intent? {
        return Intent(freeFallDetected)
    }

    private fun initTransitionRecognition() {
        val mTransitionRecognition = TransitionRecognition(this)
        mTransitionRecognition.startTracking()
    }
}