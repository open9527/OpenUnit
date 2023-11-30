package com.open.pkg.net.vo

import com.open.net.okhttp.request.JsonRequestBody
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import okhttp3.FormBody
import okhttp3.RequestBody

@Serializable
data class ArticleVo(
    val title: String,
    val link: String,
    val superChapterName: String,
)

@Serializable
data class ListData(
    val curPage: Int,
    val pageCount: Int,
    val size: Int,
    @SerialName("datas")
    val list: List<ArticleVo>
)

fun createJsonBody(articleVo: ArticleVo): RequestBody {
    return JsonRequestBody(articleVo)
}

fun createJsonBody(title: String, link: String, superChapterName: String): RequestBody =
    FormBody.Builder().apply {
        add("title", title)
        add("link", link)
        add("superChapterName", superChapterName)
    }.build()
