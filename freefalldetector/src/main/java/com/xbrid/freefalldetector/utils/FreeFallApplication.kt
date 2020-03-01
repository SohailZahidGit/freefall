package com.xbrid.freefalldetector.utils

import android.app.Application
import com.xbrid.freefalldetector.db.DatabaseHelper

class FreeFallApplication : Application() {

    override fun onCreate() {
        super.onCreate()
        DatabaseHelper(this)
    }

}