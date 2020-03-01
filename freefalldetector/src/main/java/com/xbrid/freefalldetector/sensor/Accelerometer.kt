package com.xbrid.freefalldetector.sensor

import android.content.Context
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.os.Bundle
import android.os.Handler
import android.os.Message
import android.util.Log
import com.xbrid.freefalldetector.db.DatabaseHelper
import com.xbrid.freefalldetector.utils.DetectionEvent
import java.sql.Timestamp
import java.time.Duration
import kotlin.math.abs
import kotlin.math.sqrt

class Accelerometer(
    private val context: Context,
    private val mSensorManager: SensorManager,
    private val mSensor: Sensor,
    private val mHandler: Handler
) :
    SensorEventListener {
    private var fallThreshold = 10
    private var mAccelCurrent = SensorManager.GRAVITY_EARTH
    private var mAccelLast = SensorManager.GRAVITY_EARTH
    private var mAccel = 0.00f
    private var duration: Long = System.currentTimeMillis()
    private val DETECTION_CASE_X = 0
    private val DETECTION_CASE_Y = 1
    private val DETECTION_CASE_Z = 2

    fun startListening() {
        mSensorManager.registerListener(
            this,
            mSensor,
            SensorManager.SENSOR_DELAY_FASTEST
        ) //sampling every 0.2sec => 5Hz
    }

    fun stopListening() {
        mSensorManager.unregisterListener(this)
    }

    override fun onAccuracyChanged(
        sensor: Sensor?,
        accuracy: Int
    ) {
    }

    override fun onSensorChanged(event: SensorEvent) {
        duration = System.currentTimeMillis()
        val ax = event.values[0]
        val ay = event.values[1]
        val az = event.values[2]
        mAccelLast = mAccelCurrent
        mAccelCurrent = sqrt((ax * ax + ay * ay + az * az).toDouble()).toFloat()
        val delta = mAccelCurrent - mAccelLast.toDouble()
        mAccel = mAccel * 0.9f + delta.toFloat()
        val isFreeFallDetected: Boolean = compare(ax.toInt(), ay.toInt(), az.toInt())
        handleCases(isFreeFallDetected)
    }

    private fun handleCases(isFreeFallDetected: Boolean) {
        if (isFreeFallDetected) {
            if ((mAccelLast - mAccelCurrent) > fallThreshold) {
//                mHandler.dispatchMessage(Message().apply {
//                    data.apply {
//                        Bundle().apply {
//                            putBoolean("freeFallDetected", true)
//                        }
//                    }
//                })
                mHandler.postDelayed(Runnable {
                    mHandler.dispatchMessage(Message().apply {
                        data.apply {
                            Bundle().apply {
                                putBoolean("freeFallDetected", true)
                            }
                        }
                    })
                }, 1000)
                duration = System.currentTimeMillis() - duration
//                saveRecordIntoDB(duration = duration.toString())
            }
        }
    }

    private fun compare(ax: Int, ay: Int, az: Int): Boolean {
        var ax = ax
        var ay = ay
        var az = az
        ax = abs(ax)
        ay = abs(ay)
        az = abs(az)
        if (ax > ay) {
            if (ax > az) return true
        } else return true
        return false
    }

    private fun saveRecordIntoDB(duration: String) {
        val db = DatabaseHelper(context)
        val tsLong = System.currentTimeMillis() / 1000
        val ts = tsLong.toString()
        db.addEvent(DetectionEvent(ts, duration))
    }
}