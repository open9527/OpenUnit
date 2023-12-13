package com.open.pkg.ui.web

import android.annotation.SuppressLint
import android.view.View
import android.webkit.WebChromeClient
import android.webkit.WebResourceRequest
import android.webkit.WebResourceResponse
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.activity.OnBackPressedCallback
import androidx.activity.viewModels
import com.open.base.BaseActivity
import com.open.core.ViewClickUtils.addClick
import com.open.core.binding.binding
import com.open.pkg.R
import com.open.pkg.databinding.WebActivityBinding
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
    private fun initWebView(webView: WebView, webUrl: String) {
        webView.settings.userAgentString =
            webView.settings.userAgentString.toString()
        webView.settings.allowFileAccess = true
        webView.loadUrl(webUrl)
        webView.settings.javaScriptEnabled = true
        webView.settings.builtInZoomControls = true


        webView.webViewClient = object : WebViewClient() {
            override fun shouldInterceptRequest(
                view: WebView,
                request: WebResourceRequest
            ): WebResourceResponse? {
                return super.shouldInterceptRequest(view, request)
            }

            override fun shouldOverrideUrlLoading(
                view: WebView?,
                request: WebResourceRequest?
            ): Boolean {
                return super.shouldOverrideUrlLoading(view, request)
            }

            override fun onPageFinished(view: WebView, url: String) {
                viewModel.valueProgressBar.set(View.GONE)
            }

            override fun onReceivedError(
                view: WebView,
                errorCode: Int,
                description: String,
                failingUrl: String
            ) {
                // 页面加载失败
            }
        }
        webView.webChromeClient = object : WebChromeClient() {
            override fun onProgressChanged(view: WebView?, newProgress: Int) {
                super.onProgressChanged(view, newProgress)
                if (newProgress == 100) {
                    viewModel.valueProgressBar.set(View.GONE)
                } else {
                    viewModel.valueProgressBar.set(View.VISIBLE)
                    viewModel.valueProgress.set(newProgress)
                }

            }
        }
    }

    companion object {
        const val WEB_URL = "web_url"
        const val WEB_TITLE = "web_title"
    }
}