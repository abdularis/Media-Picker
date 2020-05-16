package com.abdularis.mediapickersample

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.abdularis.mediapicker.MediaPickerActivity
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        textView.setOnClickListener {
            startActivityForResult(Intent(this@MainActivity, MediaPickerActivity::class.java), 1)
        }
    }
}
