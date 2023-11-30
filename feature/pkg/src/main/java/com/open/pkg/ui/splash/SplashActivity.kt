package com.open.pkg.ui.splash

import com.open.base.BaseActivity
import com.open.core.LogUtils
import com.open.core.binding.binding
import com.open.net.NetConfig
import com.open.pkg.R
import com.open.pkg.databinding.ActivitySplashBinding
import com.open.pkg.net.api.WanApiImpl
import com.open.serialization.JsonClient

class SplashActivity : BaseActivity(R.layout.activity_splash) {
    private val binding: ActivitySplashBinding by binding(this)
    override fun initData() {
        NetConfig.addHeader("Splash", "Splash")
        request(0)
    }

    private fun request(page: Int) {
        WanApiImpl.requestArticleList(page).observe(this) {
            LogUtils.d(JsonClient.toJson(it))
        }
    }
}