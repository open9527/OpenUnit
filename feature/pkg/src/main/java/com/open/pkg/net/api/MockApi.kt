package com.open.pkg.net.api

import androidx.lifecycle.LiveData
import com.open.net.cache.HttpCacheManager
import com.open.pkg.net.response.BaseResponse
import com.open.pkg.net.vo.BannerVo
import retrofit2.http.GET
import retrofit2.http.Header

interface MockApi {
    companion object {
        private const val XXX = "mock/api/xxx"
    }
    @GET(XXX)
    fun requestXXX(
        @Header(HttpCacheManager.HTTP_CACHE_HEADER) cache: String = HttpCacheManager.HttpCacheType.NO_CACHE.name,
    ): LiveData<BaseResponse.ApiResponse<BannerVo>>
}