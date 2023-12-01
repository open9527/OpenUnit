package com.open.pkg.net.api

import androidx.lifecycle.LiveData
import com.open.net.cache.HttpCacheManager
import com.open.pkg.net.response.BaseResponse
import com.open.pkg.net.vo.ArticleListData
import com.open.pkg.net.vo.BannerVo
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Path

interface WanApi {
    companion object {
        private const val BANNER = "banner/json"
        private const val ARTICLE_LIST = "article/list/{page}/json"
    }

    @GET(BANNER)
    fun requestBanner(
        @Header(HttpCacheManager.HTTP_CACHE_HEADER) cache: String = HttpCacheManager.HttpCacheType.NO_CACHE.name,
    ): LiveData<BaseResponse.ApiResponse<List<BannerVo>>>

    @GET(ARTICLE_LIST)
    fun requestArticleList(
        @Header(HttpCacheManager.HTTP_CACHE_HEADER) cache: String = HttpCacheManager.HttpCacheType.NO_CACHE.name,
        @Path("page") page: Int = 0,
    ): LiveData<BaseResponse.ApiResponse<ArticleListData>>


}