package com.open.pkg.app

import com.open.router.OpenRouter

object RouterConfig {
    private const val PKG_HOST = "pkg://"
    private fun add(host: String = PKG_HOST, clazz: Class<*>) {
        OpenRouter.register(host + clazz.simpleName, clazz.name)
    }

    fun addList(host: String = PKG_HOST, cla: List<Class<*>>) {
        cla.forEach {
            add(host,  it)
        }
    }

    fun getPkgHost(): String = PKG_HOST

    fun getRouterPath(host: String = PKG_HOST, clazz: Class<*>): String = host + clazz.simpleName

}