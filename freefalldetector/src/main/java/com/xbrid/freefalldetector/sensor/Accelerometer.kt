package com.xbrid.freefalldetector.sensor

import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.os.Bundle
import android.os.Handler
import android.os.Message
import android.util.Log
import com.xbrid.freefalldetector.utils.Constants

class Accelerometer(private val mSensorManager: SensorManager, s: Sensor?, h: Handler) :
    SensorEventListener {
    private val mSensor: Sensor?
    private val mHandler: Handler
    private var lastUpdate: Long = -1
    private lateinit var accel_values: FloatArray
    private var last_accel_values: FloatArray? = null
    //    private int fallThreshold = 10;
    private var mAccelCurrent = SensorManager.GRAVITY_EARTH
    private var mAccelLast = SensorManager.GRAVITY_EARTH
    private val mAccel = 0.00f
    private val mWindow: Window

    fun startListening() {
        if (mSensor == null) { // Send a failure message back to the Activity
            val msg: Message = mHandler.obtainMessage(Constants.MESSAGE_TOAST)
            val bundle = Bundle()
            bundle.putString(Constants.TOAST, "Unable to find accelerometer")
            msg.setData(bundle)
            mHandler.sendMessage(msg)
        } else {
            mSensorManager.registerListener(
                this,
                mSensor,
                SensorManager.SENSOR_DELAY_FASTEST
            ) //sampling every 0.2sec => 5Hz
        }
    }

    fun stopListening() {
        mSensorManager.unregisterListener(this)
    }

    override fun onAccuracyChanged(
        sensor: Sensor?,
        accuracy: Int
    ) { /*Safe not to implement*/
    }

    override fun onSensorChanged(event: SensorEvent) {
        val curTime = System.currentTimeMillis()
        if (event.sensor.type == Sensor.TYPE_ACCELEROMETER) { // sampling frequency f= 10Hz.
            if (curTime - lastUpdate > CHECK_INTERVAL) {
                val diffTime = curTime - lastUpdate
                lastUpdate = curTime
                accel_values = event.values.clone()
                if (last_accel_values != null) {
                    mAccelLast = mAccelCurrent
                    mAccelCurrent = Math.sqrt(
                        accel_values[0] * accel_values[0] + accel_values[1] * accel_values[1] + (accel_values[2] * accel_values[2]).toDouble()
                    ).toFloat()
                    // Initial approach
//                    float delta = mAccelCurrent - mAccelLast;
//                    mAccel = mAccel * 0.9f + delta;
// Send the value back to the Activity
                    var msg: Message = mHandler.obtainMessage(Constants.MESSAGE_CHANGED)
                    val bundle = Bundle()
                    bundle.putFloat(Constants.VALUE, mAccelCurrent)
                    msg.setData(bundle)
                    mHandler.sendMessage(msg)
                    mWindow.add(mAccelCurrent)
                    if (mWindow.isFull && mWindow.isFallDetected) {
                        Log.w(TAG, "Fall detected by window class")
                        mWindow.clear()
                        msg = mHandler.obtainMessage(Constants.MESSAGE_EMERGENCY)
                        mHandler.sendMessage(msg)
                    }
                    //                    Inital approach
//                    =====================================
//                    if (mAccel > fallThreshold) {
//
//                        Log.w(TAG, "acceleration greater than threshold");
//                        // Send the value back to the Activity
//                        msg = mHandler.obtainMessage(Constants.MESSAGE_EMERGENCY);
//                        mHandler.sendMessage(msg);
//                    }
                }
                last_accel_values = accel_values.clone()
            }
        }
    }

    companion object {
        const val TAG = "Accelerometer"
        private const val CHECK_INTERVAL = 100 // [msec]
    }

    init {
        mSensor = s
        mHandler = h
        mWindow =
            Window(1000 / CHECK_INTERVAL) //sampling for 1 sec
    }
}