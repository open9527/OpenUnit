package com.open.pkg.net.vo

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
//@Parcelize
data class ProjectClassificationVo(
    val id: String,
    val name: String,
)

@Serializable
data class ProjectListData(
    val curPage: Int,
    val pageCount: Int,
    val size: Int,
    @SerialName("datas")
    val list: List<ProjectVo>
)

@Serializable
data class ProjectVo(
    val title: String,
    val desc: String,
    val link: String,
    val projectLink: String,
    val envelopePic: String,
    val niceDate: String,
    val author: String,
    val shareUser: String,
    val superChapterName: String,
    val chapterName: String,
)

