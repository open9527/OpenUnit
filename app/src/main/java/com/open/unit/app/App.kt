package com.open.unit.app

import com.open.compose.ui.ComposeActivity
import com.open.pkg.app.PkgApp
import com.open.pkg.app.PkgRouter
import com.open.pkg.ui.splash.SplashActivity
import com.open.unit.utils.EngineManager
import io.flutter.embedding.android.FlutterActivity


class App : PkgApp() {
    override fun onCreate() {
        super.onCreate()
        initFlutter()
        PkgRouter.addList(
            cla = listOf(
                SplashActivity::class.java,
                ComposeActivity::class.java,
                FlutterActivity::class.java
            )
        )

    }

    private fun initFlutter() {
        EngineManager.registerEngine(EngineManager.FLUTTER_PAGE_ENGINE_ID, this)
    }
}