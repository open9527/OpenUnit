package com.open.unit.ui.launch

import android.content.ComponentName
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.view.View
import com.open.base.BaseActivity
import com.open.core.LogUtils
import com.open.core.ViewClickUtils.addClick
import com.open.core.binding.binding
import com.open.net.cache.HttpCacheManager
import com.open.router.OpenRouter
import com.open.router.Postcard
import com.open.unit.R
import com.open.unit.databinding.ActivityLaunchBinding
import com.open.unit.utils.EngineManager
import com.tencent.mmkv.MMKV
import okhttp3.Request
import java.lang.Compiler.enable


open class LaunchActivity : BaseActivity(R.layout.activity_launch) {

    private val binding: ActivityLaunchBinding by binding(this)

    init {
        OpenRouter.register(NATIVE_ACTION_PATH, NATIVE_CLASS_PATH)
        OpenRouter.register(COMPOSE_ACTION_PATH, COMPOSE_CLASS_PATH)
        OpenRouter.register(FLUTTER_ACTION_PATH, FLUTTER_CLASS_PATH)
    }

    private val dataCache: MMKV by lazy {
        MMKV.mmkvWithID(ICON_CHANGE)
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

        binding.tvChangeIcon.addClick({
            changeIcon(dataCache.decodeBool(ICON_CHANGE_KEY, true))
        }, viewAlpha = true)
    }


    private fun changeIcon(change: Boolean) {
//        val intent = Intent(this, LaunchActivity::class.java)
        val pm = packageManager
        if (change) {
            dataCache.encode(ICON_CHANGE_KEY, false)
            pm.setComponentEnabledSetting(
                ComponentName(
                    baseContext,
                    "com.open.unit.ui.launch.LaunchNewActivity"
                ),
                PackageManager.COMPONENT_ENABLED_STATE_ENABLED,
                PackageManager.DONT_KILL_APP
            )

            pm.setComponentEnabledSetting(
                ComponentName(
                    baseContext,
                    "com.open.unit.ui.launch.LaunchActivity"
                ),
                PackageManager.COMPONENT_ENABLED_STATE_DISABLED,
                PackageManager.DONT_KILL_APP
            )
            LogUtils.d("换LaunchNewActivity的图标")
        } else {
            dataCache.encode(ICON_CHANGE_KEY, true)
            pm.setComponentEnabledSetting(
                ComponentName(
                    baseContext,
                    "com.open.unit.ui.launch.LaunchActivity"
                ),
                PackageManager.COMPONENT_ENABLED_STATE_ENABLED,
                PackageManager.DONT_KILL_APP
            )
            pm.setComponentEnabledSetting(
                ComponentName(
                    baseContext,
                    "com.open.unit.ui.launch.LaunchNewActivity"
                ),
                PackageManager.COMPONENT_ENABLED_STATE_DISABLED,
                PackageManager.DONT_KILL_APP
            )
            LogUtils.d("换LaunchActivity的图标")
        }
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

        private const val ICON_CHANGE = "icon_change"
        private const val ICON_CHANGE_KEY = "icon_change_key"
    }
}