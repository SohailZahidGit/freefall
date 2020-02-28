package com.xbrid.freefalldetector.sensor

class Constants {
    companion object {
        // Message types sent from the Accelerometer Handler
        const val MESSAGE_CHANGED = 1
        const val MESSAGE_EMERGENCY = 2
        const val MESSAGE_TOAST = 3
        // Key names received from the Accelerometer Handler
        const val VALUE = "value"
        const val TOAST = "toast"
        // Shared preferences keys
        const val MyPREFERENCES = "MyPrefs"
        const val Code = "codeKey"
        const val Phone = "phoneKey"
        const val History = "historyKey"


        const val freeFallDetected = "free.fall.detected"
        const val DETECTION_INTERVAL_IN_MILLISECONDS = 500L
    }
}