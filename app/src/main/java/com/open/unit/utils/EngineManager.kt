package com.open.unit.utils

import android.content.Context
import android.content.Intent
import io.flutter.embedding.android.FlutterActivity
import io.flutter.embedding.engine.FlutterEngine
import io.flutter.embedding.engine.FlutterEngineCache
import io.flutter.embedding.engine.dart.DartExecutor

object EngineManager {
    const val FLUTTER_PAGE_ENGINE_ID = "flutter_page_engine_id"

    fun registerEngine(engineId: String, context: Context) {
        val flutterEngine = FlutterEngine(context)
//        flutterEngine.navigationChannel.setInitialRoute(routerName)
        flutterEngine.dartExecutor.executeDartEntrypoint(DartExecutor.DartEntrypoint.createDefault())
        FlutterEngineCache.getInstance().put(engineId, flutterEngine)
    }

    fun getEngineIntent(engineId: String, context: Context): Intent {
        return FlutterActivity.withCachedEngine(engineId).build(context)
    }
}