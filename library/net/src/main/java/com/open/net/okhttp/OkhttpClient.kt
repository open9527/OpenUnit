package com.open.net.okhttp

import com.open.core.LogUtils
import com.open.net.Https.trustSSLCertificate
import com.open.net.NetConfig
import com.open.net.okhttp.interceptor.HeaderInterceptor
import com.open.net.okhttp.interceptor.LogInterceptor
import com.open.net.okhttp.interceptor.CacheInterceptor
import okhttp3.ConnectionPool
import okhttp3.OkHttpClient
import okhttp3.Protocol
import okhttp3.logging.HttpLoggingInterceptor
import java.net.Proxy
import java.util.Collections
import java.util.concurrent.TimeUnit


object OkhttpClient {

    private const val DEFAULT_TIMEOUT: Long = 10
    private const val MAX_IDLE_CONNECTIONS = 10


    val okHttpClient by lazy {
        OkHttpClient.Builder()
            .connectTimeout(DEFAULT_TIMEOUT, TimeUnit.SECONDS)
            .writeTimeout(DEFAULT_TIMEOUT, TimeUnit.SECONDS)
            .readTimeout(DEFAULT_TIMEOUT, TimeUnit.SECONDS)
            .connectionPool(ConnectionPool(MAX_IDLE_CONNECTIONS, DEFAULT_TIMEOUT, TimeUnit.SECONDS))
            .protocols(Collections.singletonList(Protocol.HTTP_1_1))
            .retryOnConnectionFailure(true)
            .trustSSLCertificate()
            .addInterceptor(HeaderInterceptor(NetConfig.getHeaders()))
//            .addInterceptor(LogInterceptor())
            .addInterceptor(HttpLoggingInterceptor { message ->
                LogUtils.d(NetConfig.getDebug(), message)
            }.apply {
                level = HttpLoggingInterceptor.Level.BODY
            })
            .addInterceptor(CacheInterceptor())
            .proxy(Proxy.NO_PROXY)
            .build()
    }

}