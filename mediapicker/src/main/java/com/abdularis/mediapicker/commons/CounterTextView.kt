package com.abdularis.mediapicker.commons

import android.content.Context
import android.util.AttributeSet
import android.view.View
import androidx.appcompat.widget.AppCompatTextView
import com.abdularis.mediapicker.R
import com.abdularis.mediapicker.commons.Timer.OnTimerListener

class CounterTextView : AppCompatTextView {
    interface OnTimeIsOverListener {
        fun timeRecordVideoIsOver()
    }

    private var timer: Timer? = null
    private var hideOnFinish = false
    private var countTime = 0

    public var onTimeIsOver: OnTimeIsOverListener? = null

    constructor(context: Context) : super(context) {
        init(context, null)
    }

    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs) {
        init(context, attrs)
    }

    fun start(expirationTimeInSecs: Long) {
        if (timer == null) {
            timer = Timer(
                expirationTimeInSecs,
                TextViewTimerListener()
            )
        } else {
            timer?.resetWith(expirationTimeInSecs)
        }
        visibility = View.VISIBLE
        text = getFormattedCountDown(0)
        timer?.start()
    }

    fun stop() {
        visibility = View.GONE
        countTime = 0
        timer?.resetWith(0)
        text = getFormattedCountDown(0)
    }

    private fun init(context: Context, attrs: AttributeSet?) {
        if (attrs == null) {
            hideOnFinish = false
        } else {
            val arr =
                context.obtainStyledAttributes(attrs, R.styleable.CounterTextView, 0, 0)
            hideOnFinish = arr.getBoolean(R.styleable.CounterTextView_hideOnFinish, false)
            arr.recycle()
        }
        text = getFormattedCountDown(0)
    }

    private fun getFormattedCountDown(countDown: Int): String {
        var count = countDown
        var minutes = 0
        if (count == 60) {
            minutes = 1
            count -= 60
        }
        return String.format(textLocale, "%02d:%02d", minutes, count)
    }

    private inner class TextViewTimerListener : OnTimerListener {
        override fun onTick(millisUntilFinished: Long) {
            ++countTime
            text = getFormattedCountDown(countTime)
            if (countTime == 60) {
                stop()
                onTimeIsOver!!.timeRecordVideoIsOver()
            }
        }

        override fun onFinish() {
            if (hideOnFinish) {
                visibility = View.GONE
            }
        }
    }
}