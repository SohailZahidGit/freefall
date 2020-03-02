package com.xbrid.freefalldetector.services

import android.annotation.SuppressLint
import android.app.*
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.hardware.Sensor
import android.hardware.SensorManager
import android.os.Build
import android.os.Handler
import android.os.Message
import androidx.annotation.RequiresApi
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationCompat.PRIORITY_HIGH
import com.xbrid.freefalldetector.R
import com.xbrid.freefalldetector.notification.NotificationUtils
import com.xbrid.freefalldetector.sensor.Accelerometer
import com.xbrid.freefalldetector.utils.Constants.Companion.freeFallDetected


class DetectedActivitiesIntentService :
    IntentService(DetectedActivitiesIntentService::class.java.simpleName) {

    val notificationUtils = NotificationUtils(this)
    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        val channelId =
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                createNotificationChannel("CHANNEL_ID", "My Background Service")
            } else {
                // If earlier version channel ID is not used
                // https://developer.android.com/reference/android/support/v4/app/NotificationCompat.Builder.html#NotificationCompat.Builder(android.content.Context)
                ""
            }

        val notificationBuilder = NotificationCompat.Builder(this, channelId)
        val notification = notificationBuilder.setOngoing(true)
            .setSmallIcon(R.drawable.ic_launcher)
            .setContentTitle("Free fall dectector")
            .setPriority(PRIORITY_HIGH)
            .setCategory(Notification.CATEGORY_STATUS)
            .build()
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

    private fun getBroadCastIntent(): Intent? {
        return Intent(freeFallDetected)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun createNotificationChannel(channelId: String, channelName: String): String {
        val chan = NotificationChannel(
            channelId,
            channelName, NotificationManager.IMPORTANCE_NONE
        )
        chan.lightColor = Color.BLUE
        chan.lockscreenVisibility = Notification.VISIBILITY_PRIVATE
        val service = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        service.createNotificationChannel(chan)
        return channelId
    }

}