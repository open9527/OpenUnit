package com.open.pkg.net.api

import androidx.lifecycle.LiveData
import com.open.net.cache.HttpCacheManager
import com.open.net.retrofit.RetrofitClient
import com.open.pkg.net.response.BaseResponse
import com.open.pkg.net.vo.ArticleListData
import com.open.pkg.net.vo.BannerVo

object WanApiImpl {

    fun requestBanner(): LiveData<BaseResponse.ApiResponse<List<BannerVo>>> {
        return RetrofitClient.retrofitClient<WanApi>().requestBanner(
            HttpCacheManager.HttpCacheType.USE_CACHE_AFTER_FAILURE.name
        )
    }

    fun requestArticleList(page: Int): LiveData<BaseResponse.ApiResponse<ArticleListData>> {
        return RetrofitClient.retrofitClient<WanApi>().requestArticleList(
            HttpCacheManager.HttpCacheType.USE_CACHE_AFTER_FAILURE.name,
            page
        )
    }

}