package com.open.pkg.ui.splash

import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.open.base.BaseActivity
import com.open.core.CountDown
import com.open.core.LogUtils
import com.open.core.binding.binding
import com.open.pkg.R
import com.open.pkg.databinding.ActivitySplashBinding
import com.open.pkg.net.api.WanApiImpl
import com.open.serialization.JsonClient
import kotlinx.coroutines.ExperimentalCoroutinesApi

class SplashActivity : BaseActivity(R.layout.activity_splash) {
    private val binding: ActivitySplashBinding by binding(this)

    private val viewModel by lazy {
        ViewModelProvider(this)[SplashViewModel::class.java]
    }
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
            LogUtils.d("onFinish")
            finish()
        }, lifecycleScope)
    }

    private fun request(page: Int) {
        WanApiImpl.requestBanner().observe(this) {
            LogUtils.d("Banner:${JsonClient.toJson(it)}")
        }

        WanApiImpl.requestArticleList(page).observe(this) {
            LogUtils.d("ArticleList:${JsonClient.toJson(it)}")
        }
    }
}