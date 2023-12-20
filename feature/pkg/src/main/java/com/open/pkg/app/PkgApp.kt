package com.open.pkg.app

import android.app.Application
import android.content.Context
import com.open.core.ActivityLifecycle
import com.open.core.AppLifecycle
import com.open.core.ContextHolder
import com.open.core.LogUtils
import com.open.image.initCoil
import com.open.net.NetConfig
import com.open.pkg.net.factory.LiveDataCallAdapterFactory
import com.open.pkg.ui.main.MainActivity
import com.open.pkg.ui.media.AlbumActivity
import com.open.pkg.ui.search.SearchActivity
import com.open.pkg.ui.web.WebActivity
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
        AppLifecycle.initialize(PkgConfig.isDebug())
        ActivityLifecycle.initialize(PkgConfig.isDebug(), context)
        initCoil(context)
        MMKV.initialize(context)
        NetConfig.initialize(
//            debug = PkgConfig.isDebug(),
            debug = PkgConfig.isLog(),
            hostUrl = PkgConfig.getHostUrl(),
            headers = mutableMapOf("xxxKey" to "xxxValue"),
            callAdapterFactory = LiveDataCallAdapterFactory()
        )
        PkgRouter.apply {
            initialize(context)
            addList(
                cla = listOf(
                    MainActivity::class.java,
                    SearchActivity::class.java,
                    WebActivity::class.java,
                    AlbumActivity::class.java,
                )
            )
        }
    }

}