package com.open.net.okhttp.interceptor

import com.open.core.LogUtils
import com.open.net.NetConfig
import com.open.net.cache.HttpCacheManager
import okhttp3.Interceptor
import okhttp3.Protocol
import okhttp3.Request
import okhttp3.Response
import okhttp3.ResponseBody.Companion.toResponseBody
import java.net.HttpURLConnection

internal class CacheInterceptor() : Interceptor {

    override fun intercept(chain: Interceptor.Chain): Response {
        val request = chain.request()
        LogUtils.d(NetConfig.getDebug(), "intercept request:$request")

        val headers = request.headers

        return when (headers[HttpCacheManager.HTTP_CACHE_HEADER]) {
            HttpCacheManager.HttpCacheType.NO_CACHE.name ->
                noCache(chain)

            HttpCacheManager.HttpCacheType.NO_CACHE_FILE.name ->
                noCache(chain)

            HttpCacheManager.HttpCacheType.USE_CACHE_ONLY.name ->
                useCacheOnly(chain)

            HttpCacheManager.HttpCacheType.USE_CACHE_FIRST.name ->
                useCacheFirst(chain)

            HttpCacheManager.HttpCacheType.USE_CACHE_AFTER_FAILURE.name ->
                useCacheAfterFailure(chain)

            else -> noCache(chain)
        }
    }


    //1.NO_CACHE 不使用缓存
    private fun noCache(chain: Interceptor.Chain): Response {
        val request = chain.request()
        //清除历史缓存
        HttpCacheManager.cleanHttpCache(request)
        return chain.proceed(request)
    }

    //2.USE_CACHE_ONLY 只使用缓存
    private fun useCacheOnly(chain: Interceptor.Chain): Response {
        val request = chain.request()
        LogUtils.d(NetConfig.getDebug(),  "useCacheOnly request:$request")

        //先读取缓存
        val cacheData = getCacheData(request)
        if (cacheData != null) {
            return cacheData
        }
        //获取网络数据 并保存
        val response = chain.proceed(request)
        val responseBody = response.body ?: return response
        val data = responseBody.bytes()
        saveCacheData(request, response, data)

        return Response.Builder()
            .request(chain.request())
            .protocol(Protocol.HTTP_2)
            .code(HttpURLConnection.HTTP_GATEWAY_TIMEOUT)
            .message(NO_CACHE)
            .body(data.toResponseBody(responseBody.contentType()))
            .sentRequestAtMillis(-1L)
            .receivedResponseAtMillis(System.currentTimeMillis())
            .build()

    }

    //3.USE_CACHE_FIRST 优先使用缓存
    private fun useCacheFirst(chain: Interceptor.Chain): Response {
        val request = chain.request()
        LogUtils.d(NetConfig.getDebug(),  "useCacheFirst request:$request")

        //先读取缓存
        val cacheData = getCacheData(request)
        if (cacheData != null) {
            return cacheData
        }
        //获取网络数据 并保存
        val response = chain.proceed(request)
        val responseBody = response.body ?: return response
        val data = responseBody.bytes()
        saveCacheData(request, response, data)
        return response.newBuilder()
            .body(data.toResponseBody(responseBody.contentType()))
            .build()
    }

    //4.USE_CACHE_AFTER_FAILURE 网络失败使用缓存
    private fun useCacheAfterFailure(chain: Interceptor.Chain): Response {
        val request = chain.request()
        LogUtils.d(NetConfig.getDebug(),  "useCacheAfterFailure request:$request")
        try {
            val response = chain.proceed(request)
            if (response.code != 200) {
                val cacheData = getCacheData(request)
                if (cacheData != null) {
                    return cacheData
                }
            }
            val responseBody = response.body ?: return response
            val data = responseBody.bytes()
            saveCacheData(request, response, data)
            return response.newBuilder()
                .body(data.toResponseBody(responseBody.contentType()))
                .build()
        } catch (e: Exception) {
            LogUtils.d(NetConfig.getDebug(),  "Exception e:$e")
            val cacheData = getCacheData(request)
            if (cacheData != null) {
                return cacheData
            }
        }
        return chain.proceed(request)

    }

    private fun getCacheData(request: Request): Response? {
        val cache: String? = HttpCacheManager.getHttpCache(request)
        if (cache != null) {
            return Response.Builder()
                .code(200)
                .body(cache.toResponseBody())
                .request(request)
                .message(CACHE)
                .protocol(Protocol.HTTP_2)
                .build()
        }
        return null
    }


    private fun saveCacheData(request: Request, response: Response, data: ByteArray) {
        if (response.code == 200) {
            val dataString = String(data)
            HttpCacheManager.saveHttpCache(
                request,
                dataString
            )
        }
    }

    private companion object {
        private const val TAG: String = "CacheInterceptor"
        private const val NO_CACHE: String = "NO CACHED DATA FOR DISK"
        private const val CACHE: String = "FROM DISK CACHE"
    }
}