package com.open.pkg.ui.web

import android.annotation.SuppressLint
import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import android.view.View
import android.webkit.WebView
import androidx.activity.OnBackPressedCallback
import androidx.activity.viewModels
import androidx.webkit.WebSettingsCompat
import androidx.webkit.WebViewFeature
import com.open.base.BaseActivity
import com.open.core.ViewClickUtils.addClick
import com.open.core.binding.binding
import com.open.core.copyToClipboard
import com.open.core.toast
import com.open.pkg.R
import com.open.pkg.app.PkgShare
import com.open.pkg.databinding.WebActivityBinding
import com.open.pkg.ui.view.BrowserView
import com.open.pkg.ui.web.dialog.WebMenuDialog

class WebActivity : BaseActivity(R.layout.web_activity) {

    private val binding: WebActivityBinding by binding(this)

    private val viewModel: WebViewModel by viewModels()

    override fun initView() {
        binding.vm = viewModel
        backPressed {
            if (binding.webView.canGoBack()) {
                binding.webView.goBack()
            } else {
                finish()
            }
        }
        intent.extras?.let { bundle ->
//            initWebView(binding.webView, bundle.getString(WEB_URL, ""))
            initWebView(binding.webView, "https://mall-mobile.shmedia.tech/real.html?shop_id=31011201&site_id=310112&target=media&access_id=184&version=2023110101#/")
        }

        binding.ivBack.addClick({
            if (binding.webView.canGoBack()) {
                binding.webView.goBack()
            } else {
                finish()
            }

        })

        binding.ivNext.addClick({
            if (binding.webView.canGoForward()) {
                binding.webView.goForward()
            }
        })
        binding.ivRefresh.addClick({
            binding.webView.reload()
        })

        binding.ivMore.addClick({
            WebMenuDialog.with().apply {
                addListener { string ->
                    when (string) {
                        "投诉" -> {

                        }

                        "听全文" -> {

                        }

                        "字体调整" -> {

                        }

                        "分享" -> {
                            PkgShare.shareText(
                                this@WebActivity,
                                "分享链接:${binding.webView.originalUrl}"
                            )
                        }

                        "刷新" -> {
                            binding.webView.reload()
                        }

                        "夜间模式" -> {
                            if (WebViewFeature.isFeatureSupported(WebViewFeature.FORCE_DARK)) {
                                if (WebSettingsCompat.FORCE_DARK_ON == WebSettingsCompat.getForceDark(
                                        binding.webView.settings
                                    )
                                ) {
                                    WebSettingsCompat.setForceDark(
                                        binding.webView.settings,
                                        WebSettingsCompat.FORCE_DARK_OFF
                                    )
                                } else {
                                    WebSettingsCompat.setForceDark(
                                        binding.webView.settings,
                                        WebSettingsCompat.FORCE_DARK_ON
                                    )
                                }
                            }
                        }

                        "浏览器打开" -> {
                            val intent = Intent(Intent.ACTION_VIEW)
                            intent.addCategory(Intent.CATEGORY_DEFAULT)
                            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                            intent.data = Uri.parse(binding.webView.originalUrl)
                            this@WebActivity.startActivity(intent)

                        }

                        "复制链接" -> {
                            binding.webView.originalUrl?.copyToClipboard()
                            this@WebActivity.toast("$string 成功")
                        }

                        "退出" -> {
                            finish()
                        }

                        else -> {

                        }
                    }
                    dismiss()
                }
                showDialog(this@WebActivity)
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