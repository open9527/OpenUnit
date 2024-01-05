package com.open.pkg.net.vo

import kotlinx.serialization.Serializable


@Serializable
data class NavigationVo(
    val cid: String,
    val name: String,
    val articles: List<ArticleVo>,
)

