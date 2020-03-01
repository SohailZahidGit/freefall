package com.xbrid.freefalldetector.receiver

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent

class FreeFallDetectReceiver : BroadcastReceiver() {

    private var mContext: Context? = null
    override fun onReceive(context: Context?, intent: Intent?) {
        mContext = context
    }
}