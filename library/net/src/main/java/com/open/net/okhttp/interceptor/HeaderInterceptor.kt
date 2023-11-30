package com.open.net.okhttp.interceptor

import okhttp3.Interceptor
import okhttp3.Response

class HeaderInterceptor(private val headersMap: MutableMap<String, String>) : Interceptor {

    override fun intercept(chain: Interceptor.Chain): Response {
        val builder = chain.request().newBuilder()
        headersMap.forEach {
            builder.addHeader(it.key, it.value)
        }
        return chain.proceed(builder.build())
    }

    private companion object {
        private const val TAG: String = "HeaderInterceptor"
    }
}
