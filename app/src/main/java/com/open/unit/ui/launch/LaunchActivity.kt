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

class LaunchActivity : BaseActivity(R.layout.activity_launch) {

    private val binding: ActivityLaunchBinding by binding(this)

    init {
        OpenRouter.register(ACTION_PATH, CLASS_PATH)
    }

    override fun initView() {

        binding.click = ClickProxy()
        binding.tvStartPkg.addClick({
            OpenRouter.navigation(
                it.context,
                Postcard(ACTION_PATH, Bundle().apply {
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
        private const val ACTION_PATH = "pkg://splash-activity"
        private const val CLASS_PATH = "com.open.pkg.ui.splash.SplashActivity"
    }
}