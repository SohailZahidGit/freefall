package com.xbrid.freefalldetector.services

import android.annotation.SuppressLint
import android.app.IntentService
import android.content.Intent
import android.hardware.Sensor
import android.hardware.SensorManager
import android.os.Handler
import android.os.Message
import com.google.android.gms.location.ActivityRecognitionResult
import com.google.android.gms.location.DetectedActivity
import com.xbrid.freefalldetector.sensor.Accelerometer
import com.xbrid.freefalldetector.sensor.TransitionRecognition
import com.xbrid.freefalldetector.sensor.TransitionRecognitionUtils.toActivityString
import com.xbrid.freefalldetector.sensor.TransitionRecognitionUtils.toTransitionType
import com.xbrid.freefalldetector.utils.Constants
import com.xbrid.freefalldetector.utils.Constants.Companion.freeFallDetected
import com.xbrid.freefalldetector.utils.showToast
import java.text.SimpleDateFormat
import java.util.*


class DetectedActivitiesIntentService :
    IntentService(DetectedActivitiesIntentService::class.java.simpleName) {

    private var mSensorManager: SensorManager? = null
    private var mSensor: Sensor? = null

    override fun onCreate() {
        super.onCreate()

        mSensorManager = getSystemService(SENSOR_SERVICE) as SensorManager
        mSensor = mSensorManager?.getDefaultSensor(Sensor.TYPE_ACCELEROMETER)
        val accelerometer = Accelerometer(mSensorManager!!, mSensor, mHandler)
        accelerometer.startListening()
        //initTransitionRecognition()
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

    @SuppressLint("HandlerLeak")
    private val mHandler: Handler = object : Handler() {
        override fun handleMessage(msg: Message) {
            when (msg.what) {
                Constants.MESSAGE_CHANGED -> {
                    val value: Float = msg.getData().getFloat(Constants.VALUE)
                    //mPlot.addEntry(value)
                    showToast(value.toString())
                }
                Constants.MESSAGE_TOAST ->
                    showToast(msg.data.getString(Constants.TOAST) ?: "")
            }
        }
    }

    private fun getBroadCastIntent(): Intent? {
        return Intent(freeFallDetected)
    }

    private fun initTransitionRecognition() {
        val mTransitionRecognition = TransitionRecognition(this)
        mTransitionRecognition.startTracking()
    }
}