package com.abdularis.mediapicker.commons

object Utils {
    @JvmStatic
    fun formatSeconds(ms: Int): String {
        val secs = ms / 1000
        val secsLeft = secs % 60
        val min = secs / 60
        return "$min:$secsLeft"
    }
}