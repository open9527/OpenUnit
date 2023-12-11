package com.open.core

import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.ProcessLifecycleOwner

object AppLifecycle {
    fun initialize(boolean: Boolean = false) {
        ProcessLifecycleOwner.get().lifecycle.addObserver(object : LifecycleEventObserver {
            override fun onStateChanged(source: LifecycleOwner, event: Lifecycle.Event) {

                when (event) {
                    Lifecycle.Event.ON_CREATE -> {
                        LogUtils.d(boolean, "AppLifecycle  ON_CREATE:${LogUtils.createAppInfo()} ")
                    }

                    Lifecycle.Event.ON_START,
                    Lifecycle.Event.ON_RESUME -> {
                        LogUtils.d(boolean, "AppLifecycle  app切到前台了:$event ")
                    }

                    Lifecycle.Event.ON_PAUSE,
                    Lifecycle.Event.ON_STOP -> {
                        LogUtils.d(boolean, "AppLifecycle  app切到后台了:$event ")
                    }

                    else -> {
                        LogUtils.d(boolean, "AppLifecycle onStateChanged $event ")
                    }
                }
            }

        })
    }

}