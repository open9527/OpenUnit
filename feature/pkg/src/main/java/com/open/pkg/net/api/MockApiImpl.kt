package com.open.pkg.net.api

import androidx.lifecycle.LiveData
import com.open.net.cache.HttpCacheManager
import com.open.net.retrofit.RetrofitClient
import com.open.pkg.net.response.BaseResponse
import com.open.pkg.net.vo.ListData

object MockApiImpl {
    fun requestHomeArticleList(page:Int): LiveData<BaseResponse.ApiResponse<ListData>> {
        return RetrofitClient.retrofitClient<MockApi>().requestHomeArticleList(
            HttpCacheManager.HttpCacheType.USE_CACHE_AFTER_FAILURE.name,
            page
        )
    }

}