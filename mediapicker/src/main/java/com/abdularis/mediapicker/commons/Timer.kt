package com.abdularis.mediapicker.commons

import android.os.CountDownTimer
import kotlin.math.max

class Timer(expirationTimeInSecs: Long,
            private val onTimerListener: OnTimerListener) {

    private var countDownTimer: CountDownTimer? = null

    init {
        resetWith(expirationTimeInSecs)
    }

    fun start() {
        countDownTimer?.start()
    }

    fun resetWith(expirationTimeInSecs: Long) {
        countDownTimer?.cancel()
        val countDown = max(expirationTimeInSecs - System.currentTimeMillis() / 1000, 0)
        if (countDown <= 0) {
            onTimerListener.onFinish()
        } else {
            countDownTimer = object : CountDownTimer(countDown * 1000, 1000) {
                override fun onTick(millisUntilFinished: Long) {
                    onTimerListener.onTick(millisUntilFinished)
                }

                override fun onFinish() {
                    onTimerListener.onFinish()
                }
            }
        }
    }

    interface OnTimerListener {
        fun onTick(millisUntilFinished: Long)
        fun onFinish()
    }
}