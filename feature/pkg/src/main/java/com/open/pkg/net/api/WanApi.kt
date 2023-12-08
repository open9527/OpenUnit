package com.open.pkg.net.api

import androidx.lifecycle.LiveData
import com.open.net.cache.HttpCacheManager
import com.open.pkg.net.response.BaseResponse
import com.open.pkg.net.vo.ArticleListData
import com.open.pkg.net.vo.BannerVo
import com.open.pkg.net.vo.HotKeyVo
import com.open.pkg.net.vo.ProjectClassificationVo
import com.open.pkg.net.vo.ProjectListData
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Path

interface WanApi {
    companion object {

        private const val HOTKEY = "hotkey/json"
        private const val BANNER = "banner/json"
        private const val ARTICLE_LIST = "article/list/{page}/json"
        private const val SEARCH_ARTICLE_LIST = "article/query/{page}/json?k={keyword}"

        private const val PROJECT = "project/tree/json"
        private const val PROJECT_LIST = "project/list/{page}/json?cid={id}"
    }

    @GET(HOTKEY)
    fun requestHotKey(
        @Header(HttpCacheManager.HTTP_CACHE_HEADER) cache: String = HttpCacheManager.HttpCacheType.NO_CACHE.name,
    ): LiveData<BaseResponse.ApiResponse<List<HotKeyVo>>>


    @GET(SEARCH_ARTICLE_LIST)
    fun requestSearchArticleList(
        @Header(HttpCacheManager.HTTP_CACHE_HEADER) cache: String = HttpCacheManager.HttpCacheType.NO_CACHE.name,
        @Path("page") page: Int = 0,
        @Path("keyword") keyword: String = "",
    ): LiveData<BaseResponse.ApiResponse<ArticleListData>>

    @GET(BANNER)
    fun requestBanner(
        @Header(HttpCacheManager.HTTP_CACHE_HEADER) cache: String = HttpCacheManager.HttpCacheType.NO_CACHE.name,
    ): LiveData<BaseResponse.ApiResponse<List<BannerVo>>>

    @GET(ARTICLE_LIST)
    fun requestArticleList(
        @Header(HttpCacheManager.HTTP_CACHE_HEADER) cache: String = HttpCacheManager.HttpCacheType.NO_CACHE.name,
        @Path("page") page: Int = 0,
    ): LiveData<BaseResponse.ApiResponse<ArticleListData>>

    @GET(PROJECT)
    fun requestProject(
        @Header(HttpCacheManager.HTTP_CACHE_HEADER) cache: String = HttpCacheManager.HttpCacheType.NO_CACHE.name,
    ): LiveData<BaseResponse.ApiResponse<List<ProjectClassificationVo>>>


    @GET(PROJECT_LIST)
    fun requestProjectList(
        @Header(HttpCacheManager.HTTP_CACHE_HEADER) cache: String = HttpCacheManager.HttpCacheType.NO_CACHE.name,
        @Path("page") page: Int = 0,
        @Path("id") id: Int = 0,
    ): LiveData<BaseResponse.ApiResponse<ProjectListData>>

}