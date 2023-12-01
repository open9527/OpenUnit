package com.open.pkg.net.api

import androidx.lifecycle.LiveData
import com.open.net.cache.HttpCacheManager
import com.open.net.retrofit.RetrofitClient
import com.open.pkg.net.response.BaseResponse
import com.open.pkg.net.vo.BannerVo

object MockApiImpl {
    fun requestXXX(page:Int): LiveData<BaseResponse.ApiResponse<BannerVo>> {
        return RetrofitClient.retrofitClient<MockApi>().requestXXX(
            HttpCacheManager.HttpCacheType.USE_CACHE_AFTER_FAILURE.name
        )
    }

}