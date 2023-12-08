package com.open.pkg.ui.splash

import android.os.Bundle
import androidx.activity.viewModels
import androidx.lifecycle.lifecycleScope
import com.open.base.BaseActivity
import com.open.core.CountDown
import com.open.core.LogUtils
import com.open.core.binding.binding
import com.open.pkg.R
import com.open.pkg.app.RouterConfig
import com.open.pkg.databinding.SplashActivityBinding
import com.open.pkg.ui.main.MainActivity
import com.open.router.OpenRouter
import com.open.router.Postcard
import kotlinx.coroutines.ExperimentalCoroutinesApi

class SplashActivity : BaseActivity(R.layout.splash_activity) {
    private val binding: SplashActivityBinding by binding(this)


    private val viewModel: SplashViewModel by viewModels()

    override fun initView() {
        binding.vm = viewModel
    }

    override fun initData() {
        ontCountDown()
    }


    @OptIn(ExperimentalCoroutinesApi::class)
    private fun ontCountDown() {
        val countdownJob = CountDown.countDownCoroutines(3, {
            viewModel.valueCount.set(it.toString())
            LogUtils.d("onTick: ${it}s后重发")
        }, {
            OpenRouter.navigation(
                this,
                Postcard(
                    RouterConfig.getRouterPath(clazz = MainActivity::class.java),
                    Bundle().apply {
                        putString("key", "value")
                    })
            )
        }, lifecycleScope)
    }



    companion object {
        private const val SPLASH_ACTION_PATH = "pkg://splash-activity"
    }
}