package com.open.core

import android.annotation.SuppressLint
import android.os.Build
import android.util.Log
import org.jetbrains.annotations.NonNls
import java.text.SimpleDateFormat
import java.util.Date


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
//            val stackTrace = Thread.currentThread().stackTrace //stackTrace[4]
            val stackTrace = Exception().stackTrace//stackTrace[2]

            var msg: String = message
            val segmentSize = 3 * 1024
            val length = msg.length.toLong()
            if (length >= segmentSize) {
                while (msg.length > segmentSize) { // 循环分段打印日志
                    val logContent = msg.substring(0, segmentSize)
                    msg = msg.replace(logContent, "")
                    Log.d(tag ?: createStackElementTag(stackTrace[2]), logContent)
                }
                Log.d(tag ?: createStackElementTag(stackTrace[2]), msg)
            } else {
                Log.d(tag ?: createStackElementTag(stackTrace[2]), msg)
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

    @SuppressLint("SimpleDateFormat")
     fun createAppInfo(): String =
        """
            #################### APP INFO ####################
            *********************************************************
            App Start Time     : ${SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(Date())}
            Device Manufacturer: ${Build.MANUFACTURER}
            Device Model       : ${Build.MODEL}
            Android Version    : ${Build.VERSION.RELEASE}
            Android SDK        : ${Build.VERSION.SDK_INT}
            App PackageName    : ${AppUtils.getAppPackageName()}
            App VersionName    : ${AppUtils.getAppVersionName()}
            App VersionCode    : ${AppUtils.getAppVersionCode()}
            *********************************************************
            """.trimIndent()
}
