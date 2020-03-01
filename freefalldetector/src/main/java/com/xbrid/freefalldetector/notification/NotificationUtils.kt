package com.xbrid.freefalldetector.notification

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.xbrid.freefalldetector.R


class NotificationUtils(private val mContext: Context) {


    @RequiresApi(Build.VERSION_CODES.O)
    fun showSmallNotification(
        title: String,
        message: String
    ) {
        val notification = NotificationCompat.Builder(mContext, "CHANNEL_ID")
            .setSmallIcon(R.drawable.ic_launcher)
            .setContentTitle(title)
            .setContentText(message)
            .build()
        with(NotificationManagerCompat.from(mContext)) {
            createNotificationChannel(mContext)
            notify(100, notification)
        }
    }


    private fun createNotificationChannel(context: Context) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val name = context.getString(R.string.app_name)
            val descriptionText = "R.string.channel_description"
            val importance = NotificationManager.IMPORTANCE_HIGH
            val channel = NotificationChannel("CHANNEL_ID", name, importance).apply {
                description = descriptionText
            }
            val notificationManager =
                context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(channel)
        }
    }
}