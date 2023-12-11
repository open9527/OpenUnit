@file:Suppress("unused")

package com.open.core

import android.os.Handler
import android.os.Looper

val mainThreadHandler by lazy { Handler(Looper.getMainLooper()) }

val isMainThread: Boolean get() = Looper.myLooper() != Looper.getMainLooper()

fun mainThread(block: () -> Unit) {
    if (isMainThread) mainThreadHandler.post(block) else block()
}

fun mainThread(delayMillis: Long, block: () -> Unit) =
    mainThreadHandler.postDelayed(block, delayMillis)
