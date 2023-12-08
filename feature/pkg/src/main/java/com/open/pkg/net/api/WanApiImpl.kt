package com.open.pkg.net.api

import androidx.lifecycle.LiveData
import com.open.net.cache.HttpCacheManager
import com.open.net.retrofit.RetrofitClient
import com.open.pkg.net.response.BaseResponse
import com.open.pkg.net.vo.ArticleListData
import com.open.pkg.net.vo.BannerVo
import com.open.pkg.net.vo.HotKeyVo
import com.open.pkg.net.vo.ProjectClassificationVo
import com.open.pkg.net.vo.ProjectListData

object WanApiImpl {

    fun requestHotKey(): LiveData<BaseResponse.ApiResponse<List<HotKeyVo>>> {
        return RetrofitClient.retrofitClient<WanApi>().requestHotKey(
            HttpCacheManager.HttpCacheType.USE_CACHE_AFTER_FAILURE.name
        )
    }

    fun requestSearchArticleList(
        page: Int,
        keyword: String
    ): LiveData<BaseResponse.ApiResponse<ArticleListData>> {
        return RetrofitClient.retrofitClient<WanApi>().requestSearchArticleList(
            HttpCacheManager.HttpCacheType.USE_CACHE_AFTER_FAILURE.name,
            page
        )
    }


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


    fun requestProject(): LiveData<BaseResponse.ApiResponse<List<ProjectClassificationVo>>> {
        return RetrofitClient.retrofitClient<WanApi>().requestProject(
            HttpCacheManager.HttpCacheType.USE_CACHE_AFTER_FAILURE.name
        )
    }

    fun requestProjectList(
        page: Int,
        id: Int
    ): LiveData<BaseResponse.ApiResponse<ProjectListData>> {
        return RetrofitClient.retrofitClient<WanApi>().requestProjectList(
            HttpCacheManager.HttpCacheType.USE_CACHE_AFTER_FAILURE.name,
            page
        )
    }
}