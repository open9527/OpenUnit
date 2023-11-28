package com.open.core

import android.os.Build
import android.util.Log
import org.jetbrains.annotations.NonNls


object LogUtils {
    private const val MAX_TAG_LENGTH: Int = 23
    private var isDebug = false
    fun init(debug: Boolean) {
        this.isDebug = debug
    }

    fun d(debug: Boolean, @NonNls message: String) {
        printLog(debug, null, message)
    }

    fun d(tag: String, @NonNls message: String) {
        printLog(isDebug, tag, message)
    }

    fun d(@NonNls message: String) {
        printLog(isDebug, null, message)
    }

    private fun printLog(debug: Boolean = isDebug, tag: String?, message: String) {
        if (debug) {
            val stackTrace = Thread.currentThread().stackTrace
            if (stackTrace.isEmpty()) {
                Log.d(tag ?: createStackElementTag(Thread.currentThread().stackTrace[4]), message)
            } else {
                Log.d(tag ?: createStackElementTag(Exception().stackTrace[2]), message)
            }
        }
    }

    private fun createStackElementTag(element: StackTraceElement): String {
        val tag: String = "(" + element.fileName + ":" + element.lineNumber + ")"
        // 日志 TAG 长度限制已经在 Android 8.0 被移除
        if (tag.length <= MAX_TAG_LENGTH || Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            return tag
        }
        return tag.substring(0, MAX_TAG_LENGTH)
    }
}
