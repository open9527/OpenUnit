package com.open.unit

import android.os.Bundle
import androidx.activity.ComponentActivity
import com.open.core.LogUtils


class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        LogUtils.init(true)
    }
}

