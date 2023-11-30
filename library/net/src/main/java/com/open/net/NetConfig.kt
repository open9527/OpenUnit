package com.open.net

import com.open.net.factory.LiveDataCallAdapterFactory
import okhttp3.OkHttpClient
import retrofit2.CallAdapter


object NetConfig {
    private var isDebug: Boolean = false
    private var hostUrl: String = ""
    private var headers: MutableMap<String, String> = mutableMapOf()
    private var callAdapterFactory: CallAdapter.Factory = LiveDataCallAdapterFactory()
    private var okHttpClient: OkHttpClient? = null
    fun initialize(
        debug: Boolean = false,
        hostUrl: String = "no host",
        headers: MutableMap<String, String> = mutableMapOf(),
        okHttpClient: OkHttpClient? = null,
        callAdapterFactory: CallAdapter.Factory = LiveDataCallAdapterFactory(),
    ) {
        this.isDebug = debug
        this.hostUrl = hostUrl
        this.headers = headers
        this.okHttpClient = okHttpClient
        this.callAdapterFactory = callAdapterFactory
    }

    fun getDebug(): Boolean = isDebug

    fun getHostUrl(): String = hostUrl

    fun addHeader(key: String, value: String) {
        this.headers[key] = value
    }

    fun setHeaders(headers: MutableMap<String, String>) {
        this.headers = headers
    }

    fun getHeaders(): MutableMap<String, String> = headers

    fun setOkHttpClient(okHttpClient: OkHttpClient) {
        this.okHttpClient = okHttpClient
    }

    fun getOkHttpClient(): OkHttpClient? = okHttpClient
    fun setCallAdapterFactory(callAdapterFactory: CallAdapter.Factory) {
        this.callAdapterFactory = callAdapterFactory
    }

    fun getCallAdapterFactory(): CallAdapter.Factory = callAdapterFactory

}