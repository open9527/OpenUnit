package com.open.unit.ui.launch

import android.os.Bundle
import android.view.View
import com.open.base.BaseActivity
import com.open.core.ViewClickUtils.addClick
import com.open.core.binding.binding
import com.open.router.OpenRouter
import com.open.router.Postcard
import com.open.unit.R
import com.open.unit.databinding.ActivityLaunchBinding
import com.open.unit.utils.EngineManager

class LaunchActivity : BaseActivity(R.layout.activity_launch) {

    private val binding: ActivityLaunchBinding by binding(this)

    init {
        OpenRouter.register(NATIVE_ACTION_PATH, NATIVE_CLASS_PATH)
        OpenRouter.register(COMPOSE_ACTION_PATH, COMPOSE_CLASS_PATH)
        OpenRouter.register(FLUTTER_ACTION_PATH, FLUTTER_CLASS_PATH)
    }

    override fun initView() {
        binding.click = ClickProxy()
        binding.tvStartPkg.addClick({
            OpenRouter.navigation(
                it.context,
                Postcard(NATIVE_ACTION_PATH, Bundle().apply {
                    putString("key", "value")
                })
            )
        }, viewAlpha = true)

        binding.tvStartFlutter.addClick({
            OpenRouter.navigation(
                it.context,
                Postcard(FLUTTER_ACTION_PATH, Bundle().apply {
                    putString("key", "value")
                }), EngineManager.getEngineIntent(EngineManager.FLUTTER_PAGE_ENGINE_ID, this)
            )

        }, viewAlpha = true)

        binding.tvStartCompose.addClick({
            OpenRouter.navigation(
                it.context,
                Postcard(COMPOSE_ACTION_PATH, Bundle().apply {
                    putString("key", "value")
                })
            )
        }, viewAlpha = true)
    }


    inner class ClickProxy {
        fun onStartPkg(): (View) -> Unit = {

        }
    }

    companion object {
        private const val NATIVE_ACTION_PATH = "pkg://splash-activity"
        private const val NATIVE_CLASS_PATH = "com.open.pkg.ui.splash.SplashActivity"

        private const val COMPOSE_ACTION_PATH = "compose://compose-activity"
        private const val COMPOSE_CLASS_PATH = "com.open.compose.ui.ComposeActivity"

        private const val FLUTTER_ACTION_PATH = "flutter://flutter-activity"
        private const val FLUTTER_CLASS_PATH = "io.flutter.embedding.android.FlutterActivity"
    }
}