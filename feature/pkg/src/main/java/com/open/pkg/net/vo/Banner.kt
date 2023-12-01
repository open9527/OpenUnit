package com.open.pkg.net.vo

import kotlinx.serialization.Serializable

@Serializable
data class BannerVo(
    val title: String,
    val imagePath: String,
    val url: String,
)

