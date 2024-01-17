package com.open.unit.ui.launch

import android.net.Uri
import android.os.Bundle
import android.view.View
import androidx.activity.result.contract.ActivityResultContracts
import com.open.base.BaseActivity
import com.open.compose.ui.ComposeActivity
import com.open.core.LogUtils
import com.open.core.binding.binding
import com.open.core.toast
import com.open.pkg.app.PkgConfig
import com.open.pkg.app.PkgRouter
import com.open.pkg.app.PkgShare
import com.open.pkg.ui.main.MainActivity
import com.open.pkg.ui.media.AlbumActivity
import com.open.pkg.ui.media.RecorderActivity
import com.open.pkg.ui.mine.MineFragment
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


    private val albumLauncher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == MineFragment.ALBUM_RESULT_CODE) {
                val data = result.data?.extras?.getString(MineFragment.ALBUM_RESULT_URI, "")
                data?.let {
                    share(Uri.parse(it))
                    toast("选择的资源路径:${it}")
                }
            }
        }


    override fun initView() {
        binding.click = ClickProxy()
    }


    private fun share(uri: Uri) {
        PkgShare.shareMedia(this,
            PkgShare.ShareType.SHARE_TYPE_IMAGE,
//            PkgShare.ShareType.SHARE_TYPE_VIDEO,
            uri)
//        PkgShare.shareThirdPartyMedia(
//            this,
//            PkgShare.SharePackage.PACKAGE_WECHAT,
//            PkgShare.ShareType.SHARE_TYPE_IMAGE,
//            uri
//        )
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
            AlbumActivity::class.java, albumLauncher
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


    private fun selectAlbum() {
        PkgRouter.navigation(
            this,
            Bundle().apply {
                putString(AlbumActivity.QUERY_TYPE, AlbumActivity.QUERY_IMAGE)
            },
            AlbumActivity::class.java, albumLauncher
        )


    }

    private fun navigationCompose() {
        PkgRouter.navigation(
            this,
            Bundle().apply {
                putString("key", "value")
            },
            ComposeActivity::class.java
        )
    }

    private fun navigationFlutter() {
        PkgRouter.navigation(
            this,
            Bundle().apply {
                putString("key", "value")
            },
            FlutterActivity::class.java,
            EngineManager.getEngineIntent(EngineManager.FLUTTER_PAGE_ENGINE_ID, this)
        )
    }

    private fun navigationPkg() {
        PkgRouter.navigation(
            this,
            Bundle().apply {
                putInt(BUNDLE_TOTAL_KEY, if (PkgConfig.isDebug()) 1 else 5)
            },
            SplashActivity::class.java
        )
    }

    inner class ClickProxy {
        fun onStartPkg(): (view: View) -> Unit = {
            navigationPkg()
        }

        fun onStartFlutter(): (view: View) -> Unit = {
            navigationFlutter()
        }

        fun onStartCompose(): (view: View) -> Unit = {
            navigationCompose()
        }


        fun onStartTest(): (view: View) -> Unit = {
            navigationAlbumActivity()
        }

        fun onTest(): (view: View) -> Unit = {
            switch(dataCache.decodeBool(ICON_CHANGE_KEY, true))
            navigationSplashActivity()
            navigationAlbumActivity()
            navigationRecorderActivity()
            navigationMainActivity()
            LogUtils.d(RouterDelegate.getRoutes())
        }
    }

    companion object {
        public const val BUNDLE_TOTAL_KEY = "bundle_total"

        private const val ICON_CHANGE = "icon_change"
        private const val ICON_CHANGE_KEY = "icon_change_key"
    }
}