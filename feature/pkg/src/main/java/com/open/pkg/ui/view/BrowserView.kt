package com.open.pkg.ui.view

import android.annotation.TargetApi
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.net.http.SslError
import android.os.Build
import android.text.TextUtils
import android.util.AttributeSet
import android.webkit.*
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.LifecycleOwner
import com.open.core.LogUtils
import java.util.*


@Suppress("SetJavaScriptEnabled")
class BrowserView @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null,
    defStyleAttr: Int = android.R.attr.webViewStyle, defStyleRes: Int = 0
) :
    NestedScrollWebView(context, attrs, defStyleAttr, defStyleRes),
    LifecycleEventObserver {

    companion object {
        init {
            // WebView 调试模式开关
//            setWebContentsDebuggingEnabled(PkgConfig.isDebug())
        }
    }

    init {
        val settings: WebSettings = settings
        // 允许文件访问
        settings.allowFileAccess = true
        // 允许网页定位
        settings.setGeolocationEnabled(true)
        // 允许保存密码
        //settings.setSavePassword(true);
        // 开启 JavaScript
        settings.javaScriptEnabled = true
        // 允许网页弹对话框
        settings.javaScriptCanOpenWindowsAutomatically = true
        // 加快网页加载完成的速度，等页面完成再加载图片
        settings.loadsImagesAutomatically = true
        // 本地 DOM 存储（解决加载某些网页出现白板现象）
        settings.domStorageEnabled = true
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            // 解决 Android 5.0 上 WebView 默认不允许加载 Http 与 Https 混合内容
            settings.mixedContentMode = WebSettings.MIXED_CONTENT_ALWAYS_ALLOW
        }

        // 不显示滚动条
        isVerticalScrollBarEnabled = false
        isHorizontalScrollBarEnabled = false
    }

    /**
     * 获取当前的 url
     *
     * @return      返回原始的 url，因为有些url是被WebView解码过的
     */
    override fun getUrl(): String? {
        // 避免开始时同时加载两个地址而导致的崩溃
        return super.getOriginalUrl() ?: return super.getUrl()
    }

    /**
     * 设置 WebView 生命管控（自动回调生命周期方法）
     */
    fun setLifecycleOwner(owner: LifecycleOwner) {
        owner.lifecycle.addObserver(this)
    }

    /**
     * [LifecycleEventObserver]
     */
    override fun onStateChanged(source: LifecycleOwner, event: Lifecycle.Event) {
        when (event) {
            Lifecycle.Event.ON_RESUME -> onResume()
            Lifecycle.Event.ON_STOP -> onPause()
            Lifecycle.Event.ON_DESTROY -> onDestroy()
            else -> {}
        }
    }

    /**
     * 销毁 WebView
     */
    private fun onDestroy() {
        // 停止加载网页
        stopLoading()
        // 清除历史记录
        clearHistory()
        // 取消监听引用
        setBrowserChromeClient(null)
        setBrowserViewClient(null)
        // 移除WebView所有的View对象
        removeAllViews()
        // 销毁此的WebView的内部状态
        destroy()
    }


    fun setBrowserViewClient(client: BrowserViewClient?) {
        if (client == null) {
            super.setWebViewClient(WebViewClient())
            return
        }
        super.setWebViewClient(client)
    }


    fun setBrowserChromeClient(client: BrowserChromeClient?) {
        super.setWebChromeClient(client)
    }

    open class BrowserViewClient : WebViewClient() {

        /**
         * 网站证书校验错误
         */
        override fun onReceivedSslError(view: WebView, handler: SslErrorHandler, error: SslError) {
            // 如何处理应用中的 WebView SSL 错误处理程序提醒：https://support.google.com/faqs/answer/7071387?hl=zh-Hans
            //handler.proceed(),  handler.cancel()
            LogUtils.d("onReceivedSslError")
        }

        /**
         * 同名 API 兼容
         */
        @TargetApi(Build.VERSION_CODES.M)
        override fun onReceivedError(
            view: WebView,
            request: WebResourceRequest,
            error: WebResourceError
        ) {
            if (request.isForMainFrame) {
                onReceivedError(
                    view,
                    error.errorCode,
                    error.description.toString(),
                    request.url.toString()
                )
            }
        }

        /**
         * 加载错误
         */
        override fun onReceivedError(
            view: WebView,
            errorCode: Int,
            description: String,
            failingUrl: String
        ) {
            super.onReceivedError(view, errorCode, description, failingUrl)
        }

        /**
         * 同名 API 兼容
         */
        @TargetApi(Build.VERSION_CODES.N)
        override fun shouldOverrideUrlLoading(view: WebView, request: WebResourceRequest): Boolean {
            return shouldOverrideUrlLoading(view, request.url.toString())
        }

        /**
         * 跳转到其他链接
         */
        override fun shouldOverrideUrlLoading(view: WebView, url: String): Boolean {
            LogUtils.d("shouldOverrideUrlLoading：%s", url)
            val scheme: String = Uri.parse(url).scheme ?: return false
            when (scheme) {
                "http", "https" -> view.loadUrl(url)
                "tel" -> dialing(view, url)
            }
            return true
        }

        /**
         * 跳转到拨号界面
         */
        private fun dialing(view: WebView, url: String) {
            val context: Context = view.context
            //触发拨号
            val intent = Intent(Intent.ACTION_DIAL)
            intent.data = Uri.parse(url)
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            context.startActivity(intent)
        }
    }

    open class BrowserChromeClient constructor(private val webView: BrowserView) :
        WebChromeClient() {

        /**
         * 网页弹出警告框
         */
        override fun onJsAlert(
            view: WebView,
            url: String,
            message: String,
            result: JsResult
        ): Boolean {
            LogUtils.d("onJsAlert")
            // result.confirm()

            return true
        }

        /**
         * 网页弹出确定取消框
         */
        override fun onJsConfirm(
            view: WebView,
            url: String,
            message: String,
            result: JsResult
        ): Boolean {
            LogUtils.d("onJsConfirm")
            // result.confirm()  result.cancel()
            return true
        }

        /**
         * 网页弹出输入框
         */
        override fun onJsPrompt(
            view: WebView,
            url: String,
            message: String,
            defaultValue: String,
            result: JsPromptResult
        ): Boolean {
            LogUtils.d("onJsPrompt")
            // result.confirm(content)   result.cancel()
            return true
        }

        /**
         * 网页请求定位功能
         * 测试地址：https://map.baidu.com/
         */
        override fun onGeolocationPermissionsShowPrompt(
            origin: String,
            callback: GeolocationPermissions.Callback
        ) {
            LogUtils.d("onGeolocationPermissionsShowPrompt")
            //定位请求  callback.invoke(origin, 权限状态, true)
        }

        /**
         * 网页弹出选择文件请求
         * 测试地址：https://app.xunjiepdf.com/jpg2pdf/、http://www.script-tutorials.com/demos/199/index.html
         *
         * @param callback              文件选择回调
         * @param params                文件选择参数
         */
        override fun onShowFileChooser(
            webView: WebView,
            callback: ValueCallback<Array<Uri>>,
            params: FileChooserParams
        ): Boolean {
            LogUtils.d("onShowFileChooser")
            //选择文件,先要获取权限
            //已获取权限 openSystemFileChooser(context, params, callback)
            //未获取 callback.onReceiveValue(null)
            return true
        }

        /**
         * 打开系统文件选择器
         */
        private fun openSystemFileChooser(
            context: Context,
            params: FileChooserParams,
            callback: ValueCallback<Array<Uri>>
        ) {
            val intent: Intent = params.createIntent()
            val mimeTypes: Array<String>? = params.acceptTypes
            val multipleSelect: Boolean = params.mode == FileChooserParams.MODE_OPEN_MULTIPLE
            if (!mimeTypes.isNullOrEmpty() && !TextUtils.isEmpty(mimeTypes[0])) {
                // 要过滤的文件类型
                intent.putExtra(Intent.EXTRA_MIME_TYPES, mimeTypes)
                if (mimeTypes.size == 1) {
                    when (mimeTypes[0]) {
                        "image/*" -> {
                            // 选择图片
                            //val uris: MutableList<Uri> = ArrayList(data.size)
                            //callback.onReceiveValue(uris.toTypedArray())
                            //取消
                            callback.onReceiveValue(null)
                            return
                        }

                        "video/*" -> {
                            // 选择图片
                            //val uris: MutableList<Uri> = ArrayList(data.size)
                            //callback.onReceiveValue(uris.toTypedArray())
                            //取消
                            callback.onReceiveValue(null)
                            return
                        }
                    }
                }
            }

            // 是否是多选模式
            intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, multipleSelect)
            //val uris: MutableList<Uri> = ArrayList(data.size)
            //callback.onReceiveValue(uris.toTypedArray())
            //未选择 需要回调,否则后续无法选择
            callback.onReceiveValue(null)
        }
    }
}