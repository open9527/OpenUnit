package com.open.core

import android.content.Context
import android.os.Build
import android.view.WindowManager
import android.view.WindowMetrics
import androidx.annotation.RequiresApi

object ScreenUtils {

@RequiresApi(Build.VERSION_CODES.R)
fun getScreenWidth(context: Context): Int {
        val windowManager = context.getSystemService(WindowManager::class.java)
        val windowMetrics: WindowMetrics = windowManager.currentWindowMetrics
        val windowBounds = windowMetrics.bounds
        return windowBounds.width()
    }

    @RequiresApi(Build.VERSION_CODES.R)
    fun getScreenHeight(context: Context): Int {
        val windowManager = context.getSystemService(WindowManager::class.java)
        val windowMetrics: WindowMetrics = windowManager.currentWindowMetrics
        val windowBounds = windowMetrics.bounds

        return windowBounds.height()
    }
}