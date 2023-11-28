package com.open.unit.app

import android.app.Application
import android.content.Context
import com.open.core.ContextHolder
import com.open.core.LogUtils
import com.open.image.initCoil
import com.open.net.NetConfig
import com.open.router.OpenRouter
import com.tencent.mmkv.MMKV

class App : Application() {
    override fun attachBaseContext(base: Context?) {
        super.attachBaseContext(base)
        ContextHolder.init(this)
    }

    override fun onCreate() {
        super.onCreate()
        initialize(this)
    }

    private fun initialize(context: Application) {
        initCoil(context)
        LogUtils.init(true)
        MMKV.initialize(context)
        NetConfig.initialize(true, "")
        OpenRouter.init(context)
    }

}