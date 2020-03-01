package com.xbrid.freefalldetector.services

import android.annotation.SuppressLint
import android.app.IntentService
import android.app.Notification
import android.app.Service
import android.content.Intent
import android.hardware.Sensor
import android.hardware.SensorManager
import android.os.Build
import android.os.Handler
import android.os.Message
import androidx.annotation.RequiresApi
import androidx.core.app.NotificationCompat
import com.xbrid.freefalldetector.R
import com.xbrid.freefalldetector.db.DatabaseHelper
import com.xbrid.freefalldetector.notification.NotificationUtils
import com.xbrid.freefalldetector.sensor.Accelerometer
import com.xbrid.freefalldetector.utils.Constants.Companion.freeFallDetected
import com.xbrid.freefalldetector.utils.DetectionEvent


class DetectedActivitiesIntentService :
    IntentService(DetectedActivitiesIntentService::class.java.simpleName) {

    val notificationUtils = NotificationUtils(this)
    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        val notification: Notification = NotificationCompat.Builder(this, "CHANNEL_ID")
            .setContentTitle("Forground Service")
            .setSmallIcon(R.drawable.ic_launcher)
            .build()
        //A notifcation HAS to be passed for the foreground service to be started.
        startForeground(101, notification)
        return Service.START_STICKY
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate() {
        super.onCreate()
        val mSensorManager = getSystemService(SENSOR_SERVICE) as SensorManager
        val mSensor = mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER)
        val accelerometer =
            Accelerometer(this, mSensorManager, mSensor, mHandler)
        accelerometer.startListening()
    }

    override fun onHandleIntent(intent: Intent?) {
    }

    @SuppressLint("HandlerLeak")
    private val mHandler: Handler = object : Handler() {
        @RequiresApi(Build.VERSION_CODES.O)
        override fun handleMessage(msg: Message) {
            notificationUtils.showSmallNotification("Alert", "Free fall detected")
            sendBroadcast(getBroadCastIntent())
        }
    }

    private fun saveRecordIntoDB(duration: String) {
        val db = DatabaseHelper(this)
        val tsLong = System.currentTimeMillis() / 1000
        val ts = tsLong.toString()
        db.addEvent(DetectionEvent(ts, duration))
    }
    private fun getBroadCastIntent(): Intent? {
        return Intent(freeFallDetected)
    }
}