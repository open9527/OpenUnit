package com.open.unit.ui.launch

import android.os.Bundle
import android.view.View
import com.open.base.BaseActivity
import com.open.compose.ui.ComposeActivity
import com.open.core.LogUtils
import com.open.core.ViewClickUtils.addClick
import com.open.core.binding.binding
import com.open.pkg.app.PkgRouter
import com.open.pkg.ui.main.MainActivity
import com.open.pkg.ui.media.AlbumActivity
import com.open.pkg.ui.media.RecorderActivity
import com.open.pkg.ui.splash.SplashActivity
import com.open.router.RouterDelegate
import com.open.unit.R
import com.open.unit.databinding.ActivityLaunchBinding
import com.open.unit.utils.EngineManager
import com.open.unit.utils.LauncherIcon
import com.tencent.mmkv.MMKV
import io.flutter.embedding.android.FlutterActivity


open class LaunchActivity : BaseActivity(R.layout.activity_launch) {

    private val binding: ActivityLaunchBinding by binding(this)


    private val dataCache: MMKV by lazy {
        MMKV.mmkvWithID(ICON_CHANGE)
    }


    override fun initView() {

        binding.click = ClickProxy()
        binding.tvStartPkg.addClick({

            PkgRouter.navigation(
                this,
                Bundle().apply {
                    putInt(BUNDLE_TOTAL_KEY, 5)
                },
                SplashActivity::class.java
            )

        }, viewAlpha = true)

        binding.tvStartFlutter.addClick({
            PkgRouter.navigation(
                this,
                Bundle().apply {
                    putString("key", "value")
                },
                FlutterActivity::class.java,
                EngineManager.getEngineIntent(EngineManager.FLUTTER_PAGE_ENGINE_ID, this)
            )

        }, viewAlpha = true)

        binding.tvStartCompose.addClick({
            PkgRouter.navigation(
                this,
                Bundle().apply {
                    putString("key", "value")
                },
                ComposeActivity::class.java
            )

        }, viewAlpha = true)

        binding.tvTest.addClick({
//            switch(dataCache.decodeBool(ICON_CHANGE_KEY, true))
//            navigationSplashActivity()
//            navigationAlbumActivity()
            navigationRecorderActivity()
//            navigationMainActivity()
            LogUtils.d(RouterDelegate.getRoutes())
        }, viewAlpha = true)
    }

    private fun navigationMainActivity() {
        PkgRouter.navigation(
            this,
            Bundle().apply {
                putInt(BUNDLE_TOTAL_KEY, 1)
            },
          MainActivity::class.java
        )
    }
   private fun navigationRecorderActivity() {
        PkgRouter.navigation(
            this,
            Bundle().apply {
            },
            RecorderActivity::class.java
        )
    }

    private fun navigationAlbumActivity() {
        PkgRouter.navigation(
            this,
            Bundle().apply {
                putString(AlbumActivity.QUERY_TYPE, AlbumActivity.QUERY_IMAGE)
            },
            AlbumActivity::class.java
        )
    }

    private fun navigationSplashActivity() {
        PkgRouter.navigation(
            this,
            Bundle().apply {
                putInt(BUNDLE_TOTAL_KEY, 1)
            },
            "page://splash_activity?authorization=true&target=http%3a%2f%2fwww.baidu.com%3fq%3dabc"
        )
    }

    private fun switch(change: Boolean) {
        if (change) {
            LauncherIcon.switchIcon(LaunchNewActivity::class.java, LaunchActivity::class.java)
        } else {
            LauncherIcon.switchIcon(LaunchActivity::class.java, LaunchNewActivity::class.java)
        }
        dataCache.encode(ICON_CHANGE_KEY, !change)
    }


    inner class ClickProxy {
        fun onStartPkg(): (View) -> Unit = {

        }
    }

    companion object {
        public const val BUNDLE_TOTAL_KEY = "bundle_total"

        private const val ICON_CHANGE = "icon_change"
        private const val ICON_CHANGE_KEY = "icon_change_key"
    }
}