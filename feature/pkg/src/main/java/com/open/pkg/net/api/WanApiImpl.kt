package com.open.pkg.net.api

import androidx.lifecycle.LiveData
import com.open.net.cache.HttpCacheManager
import com.open.net.retrofit.RetrofitClient
import com.open.pkg.net.response.BaseResponse
import com.open.pkg.net.vo.ListData

object WanApiImpl {
    fun requestArticleList(page:Int): LiveData<BaseResponse.ApiResponse<ListData>> {
        return RetrofitClient.retrofitClient<WanApi>().requestArticleList(
            HttpCacheManager.HttpCacheType.USE_CACHE_AFTER_FAILURE.name,
            page
        )
    }

}