package com.open.pkg.app

import android.app.Application
import android.content.Context
import com.open.core.ContextHolder
import com.open.core.LogUtils
import com.open.image.initCoil
import com.open.net.NetConfig
import com.open.pkg.net.factory.LiveDataCallAdapterFactory
import com.open.router.OpenRouter
import com.tencent.mmkv.MMKV

open class PkgApp : Application() {
    override fun attachBaseContext(base: Context?) {
        super.attachBaseContext(base)
        ContextHolder.init(this)
    }

    override fun onCreate() {
        super.onCreate()
        initialize(this)
    }

    private fun initialize(context: Application) {
        LogUtils.init(PkgConfig.isDebug())
        initCoil(context)
        MMKV.initialize(context)
        NetConfig.initialize(
//            debug = PkgConfig.isDebug(),
            debug = PkgConfig.isLog(),
            hostUrl = PkgConfig.getHostUrl(),
            headers = mutableMapOf("headerKey" to "headerValue"),
//            okHttpClient = OkhttpClient.okHttpClient,
            callAdapterFactory = LiveDataCallAdapterFactory()
        )
        OpenRouter.init(context)
        LogUtils.d("initialize")
    }

}