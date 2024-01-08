package com.open.pkg.ui.splash

import android.os.Bundle
import androidx.activity.OnBackPressedCallback
import androidx.activity.viewModels
import androidx.lifecycle.lifecycleScope
import com.open.base.BaseActivity
import com.open.core.CountDown
import com.open.core.LogUtils
import com.open.core.binding.binding
import com.open.pkg.R
import com.open.pkg.app.PkgRouter
import com.open.pkg.databinding.SplashActivityBinding
import com.open.pkg.ui.main.MainActivity
import com.open.serialization.JsonClient

class SplashActivity : BaseActivity(R.layout.splash_activity) {
    private val binding: SplashActivityBinding by binding(this)


    private val viewModel: SplashViewModel by viewModels()

    override fun initView() {
        binding.vm = viewModel
        backPressed {}
    }


    override fun initData() {
        intent.extras?.let { bundle ->
            ontCountDown(bundle.getInt("bundle_total", 3))
//            LogUtils.d("bundle:${PkgRouter.bundleToMap(bundle)}")
            LogUtils.d("bundleToJson:${JsonClient.toJson(PkgRouter.bundleToMap(bundle))}")
        }
    }




    private fun ontCountDown(duration: Int) {
        val countdownJob = CountDown.countDownCoroutine(duration, 1, 1, {
            LogUtils.d("倒计时变更: ${it}s")
            viewModel.valueCount.set(it.toString())
        }, {
            LogUtils.d("倒计时开始")
        }, {
            LogUtils.d("倒计时结束")
            PkgRouter.navigation(
                this,
                Bundle().apply {
                    putString("key", "value")
                },
                MainActivity::class.java
            )
            finish()
        }, lifecycleScope)

    }


    companion object {
    }
}