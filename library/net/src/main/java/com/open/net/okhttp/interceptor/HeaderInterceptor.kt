package com.open.net.okhttp.interceptor

import okhttp3.Interceptor
import okhttp3.Response

class HeaderInterceptor : Interceptor {
    private var headersMap: Map<String, String>? = null

    override fun intercept(chain: Interceptor.Chain): Response {
        val builder = chain.request().newBuilder()
        if (headersMap != null) {
            headersMap?.forEach {
                builder.addHeader(it.key, it.value)
            }
        }
        return chain.proceed(builder.build())
    }


    fun addHeaders(map: Map<String, String>) {
        headersMap = map
    }

    private companion object {
        private const val TAG: String = "HeaderInterceptor"
    }
}
