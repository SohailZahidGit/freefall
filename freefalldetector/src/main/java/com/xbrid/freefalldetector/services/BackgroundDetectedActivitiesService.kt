package com.xbrid.freefalldetector.services

import android.app.PendingIntent
import android.app.Service
import android.content.Intent
import android.os.Binder
import android.os.IBinder
import android.widget.Toast
import com.google.android.gms.location.*
import com.xbrid.freefalldetector.receiver.FreeFallDetectReceiver

class BackgroundDetectedActivitiesService : Service() {

    private var mPendingIntent: PendingIntent? = null
    private var mActivityRecognitionClient: ActivityRecognitionClient? = null

    private var mBinder: IBinder = Binder()

    override fun onCreate() {
        super.onCreate()
        mActivityRecognitionClient = ActivityRecognitionClient(this)
        val mIntentService = Intent(this, DetectedActivitiesIntentService::class.java)
        mPendingIntent =
            PendingIntent.getService(this, 1, mIntentService, PendingIntent.FLAG_UPDATE_CURRENT)
        launchTransitionsTracker()
    }

    override fun onBind(intent: Intent): IBinder? {
        return mBinder
    }

    override fun onStartCommand(intent: Intent, flags: Int, startId: Int): Int {
        super.onStartCommand(intent, flags, startId)
        return START_STICKY
    }

    private fun launchTransitionsTracker() {
        val transitions = ArrayList<ActivityTransition>()

        transitions.add(
            ActivityTransition.Builder()
                .setActivityType(DetectedActivity.TILTING)
                .setActivityTransition(ActivityTransition.ACTIVITY_TRANSITION_ENTER)
                .build()
        )

        transitions.add(
            ActivityTransition.Builder()
                .setActivityType(DetectedActivity.TILTING)
                .setActivityTransition(ActivityTransition.ACTIVITY_TRANSITION_EXIT)
                .build()
        )

        transitions.add(
            ActivityTransition.Builder()
                .setActivityType(DetectedActivity.WALKING)
                .setActivityTransition(ActivityTransition.ACTIVITY_TRANSITION_ENTER)
                .build()
        )

        transitions.add(
            ActivityTransition.Builder()
                .setActivityType(DetectedActivity.WALKING)
                .setActivityTransition(ActivityTransition.ACTIVITY_TRANSITION_EXIT)
                .build()
        )


        val request = ActivityTransitionRequest(transitions)
        val activityRecognitionClient = ActivityRecognition.getClient(this)

        val intent = Intent(this, FreeFallDetectReceiver::class.java)
        mPendingIntent = PendingIntent.getBroadcast(this, 0, intent, 0)
        val task =
            activityRecognitionClient.requestActivityTransitionUpdates(request, mPendingIntent)
        task.addOnSuccessListener {
            Toast.makeText(
                applicationContext,
                "Tracker registered successfully!",
                Toast.LENGTH_SHORT
            )
                .show()
        }
        task.addOnFailureListener {
            Toast.makeText(
                applicationContext,
                "Tracker registration failed!", Toast.LENGTH_SHORT
            ).show()
        }
    }

    private fun removeLaunchTransitionsTracker() {
        val task = mActivityRecognitionClient?.removeActivityUpdates(
            mPendingIntent
        )
        task?.addOnSuccessListener {
            Toast.makeText(
                applicationContext,
                "Removed activity updates successfully!",
                Toast.LENGTH_SHORT
            )
                .show()
        }

        task?.addOnFailureListener {
            Toast.makeText(
                applicationContext, "Failed to remove activity updates!",
                Toast.LENGTH_SHORT
            ).show()
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        removeLaunchTransitionsTracker()
    }
}