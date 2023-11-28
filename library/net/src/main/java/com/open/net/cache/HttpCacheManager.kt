package com.open.net.cache

import com.open.core.LogUtils
import com.tencent.mmkv.MMKV
import kotlinx.serialization.Serializable
import okhttp3.Request
import okio.Buffer
import java.io.IOException

object HttpCacheManager {
    private const val MAP_ID: String = "http_cache_data"
    const val HTTP_CACHE_HEADER: String = "http_cache_header"

    enum class HttpCacheType {

        /**
         * 不使用缓存,(可用于清理缓存)
         */

        NO_CACHE,

        /**
         * 不使用缓存,(可用文件上传下载)
         */
        NO_CACHE_FILE,

        /**
         * 只使用缓存
         *
         * 已有缓存的情况下：读取缓存 -> 回调成功
         * 没有缓存的情况下：请求网络 -> 写入缓存 -> 回调成功
         */
        USE_CACHE_ONLY,

        /**
         * 优先使用缓存
         *
         * 已有缓存的情况下：先读缓存 —> 回调成功 —> 请求网络 —> 刷新缓存
         * 没有缓存的情况下：请求网络 -> 写入缓存 -> 回调成功
         */
        USE_CACHE_FIRST,


        /**
         * 只在网络请求失败才去读缓存
         */
        USE_CACHE_AFTER_FAILURE,
    }


    private val httpCache: MMKV by lazy {
        MMKV.mmkvWithID(MAP_ID)
    }


    fun getHttpCache(request: Request): String? {
        val cacheKey: String = generateCacheKey(request)
        LogUtils.d( "getHttpCache cacheKey: $cacheKey")
        val json = httpCache.decodeString(cacheKey, null)
        LogUtils.d("getHttpCache json: $json")
        return json
    }

    fun saveHttpCache(request: Request, json: String?): Boolean {
        val cacheKey: String = generateCacheKey(request)
        LogUtils.d( "saveHttpCache cacheKey: $cacheKey")
        LogUtils.d( "saveHttpCache json: $json")
        return httpCache.encode(cacheKey, json)
    }

    fun cleanHttpCache(request: Request): Boolean {
        val cacheKey = generateCacheKey(request)
        val cacheData = httpCache.decodeString(cacheKey, null)
        LogUtils.d("cleanHttpCache cacheKey: $cacheKey")
        LogUtils.d("cleanHttpCache cacheData: $cacheData")
        if (cacheData != null) {
            httpCache.removeValueForKey(cacheKey)
        }
        return null == cacheData
    }


    private fun generateCacheKey(request: Request): String {
        return "${
            HttpCacheChe(
                url = request.url.toString(),
                method = request.method,
                body = "${bodyToString(request)}"
            )
        }"

    }

    private fun bodyToString(request: Request): String? {
        return try {
            val copy = request.newBuilder().build()
            val buffer = Buffer()
            if (copy.body == null) return ""
            copy.body!!.writeTo(buffer)
            val msg = buffer.readUtf8()
//            LogUtils.d( "bodyToString: $msg")
            msg
        } catch (e: IOException) {
            ""
        }
    }


    @Serializable
    private data class HttpCacheChe(
        val id: String = "9527",
        val method: String?,
        val url: String = "host-url",
        val body: String?
    )
}