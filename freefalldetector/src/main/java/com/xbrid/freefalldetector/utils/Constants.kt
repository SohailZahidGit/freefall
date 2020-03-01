package com.xbrid.freefalldetector.utils

class Constants {
    companion object {
        // Message types sent from the Accelerometer Handler
        const val MESSAGE_CHANGED = 1
        const val MESSAGE_EMERGENCY = 2
        const val MESSAGE_TOAST = 3
        const val VALUE = "value"
        const val TOAST = "toast"

        const val freeFallDetected = "free.fall.detected"
    }
}