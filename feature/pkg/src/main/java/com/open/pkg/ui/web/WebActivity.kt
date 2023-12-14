package com.open.pkg.ui.web

import android.annotation.SuppressLint
import android.graphics.Bitmap
import android.view.View
import android.webkit.WebView
import androidx.activity.OnBackPressedCallback
import androidx.activity.viewModels
import com.open.base.BaseActivity
import com.open.core.ViewClickUtils.addClick
import com.open.core.binding.binding
import com.open.pkg.R
import com.open.pkg.databinding.WebActivityBinding
import com.open.pkg.ui.view.BrowserView
import com.open.pkg.ui.web.dialog.WebMenuDialog

class WebActivity : BaseActivity(R.layout.web_activity) {

    private val binding: WebActivityBinding by binding(this)

    private val viewModel: WebViewModel by viewModels()

    override fun initView() {
        binding.vm = viewModel
        intent.extras?.let { bundle ->
            initWebView(binding.webView, bundle.getString(WEB_URL, ""))
        }

        binding.tvBack.addClick({
            if (binding.webView.canGoBack()) {
                binding.webView.goBack()
            } else {
                finish()
            }

        })

        binding.tvNext.addClick({
            if (binding.webView.canGoForward()) {
                binding.webView.goForward()
            }
        })
        binding.tvRefresh.addClick({
            binding.webView.reload()
        })

        binding.tvMore.addClick({
            WebMenuDialog.build().showDialog(this)
        })

        onBackPressedDispatcher.addCallback(this, object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                if (binding.webView.canGoBack()) {
                    binding.webView.goBack()
                } else {
                    finish()
                }
            }
        })
    }


    @SuppressLint("SetJavaScriptEnabled")
    private fun initWebView(webView: BrowserView, webUrl: String) {
        webView.apply {
            settings.apply {
                webView.settings.userAgentString.toString()
                builtInZoomControls = true
            }
            setBrowserViewClient(AppBrowserViewClient())
            setBrowserChromeClient(AppBrowserChromeClient(webView))
            setLifecycleOwner(this@WebActivity)
            loadUrl(webUrl)
        }
    }


    private inner class AppBrowserViewClient : BrowserView.BrowserViewClient() {
//
//        override fun shouldOverrideUrlLoading(view: WebView, request: WebResourceRequest): Boolean {
//            LogUtils.d("shouldOverrideUrlLoading requestUrl= ${request.url}")
//            return super.shouldOverrideUrlLoading(view, request)
//        }
//
//        override fun shouldOverrideUrlLoading(view: WebView, url: String): Boolean {
//            LogUtils.d("shouldOverrideUrlLoading url= $url")
//            return super.shouldOverrideUrlLoading(view, url)
//        }


        /**
         * 网页加载错误时回调，这个方法会在 onPageFinished 之前调用
         */
        override fun onReceivedError(
            view: WebView,
            errorCode: Int,
            description: String,
            failingUrl: String
        ) {
            // 处理要用延迟, 因为加载出错之后会先调用 onReceivedError 再调用 onPageFinished

        }

        /**
         * 开始加载网页
         */
        override fun onPageStarted(view: WebView, url: String, favicon: Bitmap?) {
            viewModel.valueProgressBar.set(View.VISIBLE)
        }

        /**
         * 完成加载网页
         */
        override fun onPageFinished(view: WebView, url: String) {
            viewModel.valueProgressBar.set(View.GONE)
        }
    }

    private inner class AppBrowserChromeClient constructor(view: BrowserView) :
        BrowserView.BrowserChromeClient(view) {

        /**
         * 收到网页标题
         */
        override fun onReceivedTitle(view: WebView, title: String?) {
            if (title == null) {
                return
            }
            setTitle(title)
        }

        override fun onReceivedIcon(view: WebView, icon: Bitmap?) {
            if (icon == null) {
                return
            }
//            setRightIcon(BitmapDrawable(resources, icon))
        }

        /**
         * 收到加载进度变化
         */
        override fun onProgressChanged(view: WebView, newProgress: Int) {
            viewModel.valueProgress.set(newProgress)
        }
    }


    companion object {
        const val WEB_URL = "web_url"
        const val WEB_TITLE = "web_title"
    }
}