package com.open.net.okhttp.interceptor

import com.open.core.LogUtils
import com.open.net.NetConfig
import okhttp3.Interceptor
import okhttp3.Interceptor.*
import okhttp3.Request
import okhttp3.RequestBody
import okhttp3.Response
import okhttp3.ResponseBody
import okio.Buffer
import java.io.IOException
import java.nio.charset.Charset

internal class LogInterceptor : Interceptor {
    @Throws(IOException::class)
    override fun intercept(chain: Chain): Response {
        val request: Request = chain.request()
        val requestBody: RequestBody? = request.body
        if (requestBody != null) {
            val buffer = Buffer()
            requestBody.writeTo(buffer)
        }
        val response: Response = chain.proceed(request)


        val responseBody: ResponseBody? = response.body
        var responseBodyString: String? = null
        if (responseBody != null) {
            val source = responseBody.source()
            source.request(Long.MAX_VALUE)
            val buffer: Buffer = source.buffer
            responseBodyString = buffer.clone().readString(Charset.forName("UTF-8"))
        }
        LogUtils.d(
            NetConfig.getDebug(),
            String.format(
                "[ request ]:url:%s%n[ headers ]:%s%n[ responseBody ]:%s",
                request.url,
                request.headers,
                responseBodyString
            )
        )
        return response
    }

    private companion object {
        private const val TAG: String = "LogInterceptor"
    }

}
