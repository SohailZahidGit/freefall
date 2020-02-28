package com.xbrid.freefalldetector.sensor

import java.util.*

class Window {
    private val DEFAULT_SIZE = 10
    private val THRESHOLD = 8f
    private val SIZE: Int
    var samples: LinkedList<Float>

    constructor() {
        SIZE = DEFAULT_SIZE
        samples = LinkedList<Float>()
    }

    constructor(size: Int) {
        SIZE = size
        samples = LinkedList<Float>()
    }

    fun add(value: Float) {
        if (!isFull) {
            samples.add(value)
            //add value
        } else {
            samples.removeFirst()
            samples.add(value)
        }
    }

    fun clear() {
        samples.clear()
    }

    val isFull: Boolean
        get() = samples.size > SIZE

    // check if min value detected earlier than max
    val isFallDetected: Boolean
        get() {
            val max: Float = Collections.max(samples)
            val min: Float = Collections.min(samples)
            val diff = Math.abs(max - min)
            // check if min value detected earlier than max
            val isFall: Boolean = samples.indexOf(max) > samples.indexOf(min)
            return diff > THRESHOLD && isFall
        }

    companion object {
        const val TAG = "Window.java"
    }
}
