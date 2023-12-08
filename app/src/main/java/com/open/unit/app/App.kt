package com.open.unit.app
import com.open.pkg.app.PkgApp
import com.open.unit.utils.EngineManager


class App : PkgApp() {
    override fun onCreate() {
        super.onCreate()
        initFlutter()
    }

    private fun initFlutter() {
        EngineManager.registerEngine(EngineManager.FLUTTER_PAGE_ENGINE_ID, this)
    }
}