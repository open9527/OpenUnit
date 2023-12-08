package com.open.pkg.app

import com.open.pkg.BuildConfig


object PkgConfig {

    fun isDebug(): Boolean = BuildConfig.DEBUG
    fun isLog(): Boolean = true
    fun getHostUrl(): String = BuildConfig.HOST_URL

    fun getRouterHostUrl(): String = BuildConfig.ROUTER_HOST_URL

}