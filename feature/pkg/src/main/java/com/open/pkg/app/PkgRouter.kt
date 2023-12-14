package com.open.pkg.app

import android.app.Application
import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import com.open.core.LogUtils
import com.open.router.OpenRouter
import com.open.router.Postcard
import java.net.URI
import java.net.URLDecoder
import java.util.Locale


object PkgRouter {
    private const val PKG_HOST = "page://"
    fun getPkgHost(): String = PKG_HOST

    fun initialize(context: Application){
        OpenRouter.init(context)
    }
    fun addList(host: String = PKG_HOST, cla: List<Class<*>>) {
        cla.forEach {
            add(host, it)
        }
    }

    fun navigation(ctx: Context?, bundle: Bundle = Bundle(), cls: Class<*>, intent: Intent) {
        OpenRouter.navigation(
            ctx,
            Postcard(getRouterPath(clazz = cls), bundle), intent
        )
    }

    fun navigation(ctx: Context?, bundle: Bundle = Bundle(), cls: Class<*>) {
        OpenRouter.navigation(
            ctx,
            Postcard(getRouterPath(clazz = cls), bundle)
        )
    }


    fun navigation(ctx: Context?, bundle: Bundle = Bundle(), path: String) {
        val pairPath = decodePath(path)
        OpenRouter.navigation(
            ctx,
            Postcard(pairPath.first,
                bundle.apply {
                    pairPath.second.forEach {
                        putString(it.key, it.value)
                    }
                })
        )
    }

    fun bundleToMap(bundle:Bundle) = bundle.toMap()


    private fun getRouterPath(host: String = PKG_HOST, clazz: Class<*>): String =
        host + encodePath(clazz.simpleName)


    private fun add(host: String = PKG_HOST, clazz: Class<*>) {
        OpenRouter.register(host + encodePath(clazz.simpleName), clazz.name)
    }

    private fun encodePath(path: String): String {
        return path.createToUnderlineCase()
    }

    private fun decodePath(path: String): Pair<String, MutableMap<String, String>> {
        return path.parseQueryCase()
    }

    private fun String.parseQueryCase(): Pair<String, MutableMap<String, String>> {
        val pathString = this.trim()
        if (pathString.isNotEmpty()) {
            try {
                val uri = URI(pathString)
                val routerHost = uri.host
                val routerQuery = uri.rawQuery
                if (routerQuery.isNotEmpty()) {
                    val params = routerQuery.split("&")
                    val mapQuery = mutableMapOf<String, String>()
                    for (param in params) {
                        val pair = param.split("=")
                        val key = URLDecoder.decode(pair[0], "UTF-8")
                        val value = URLDecoder.decode(pair[1], "UTF-8")
                        mapQuery[key] = value
                    }
                    return Pair(PKG_HOST + routerHost, mapQuery)
                }
                return Pair(PKG_HOST + routerHost, mutableMapOf())
            } catch (e: Exception) {
                LogUtils.d("Exception:$e")
            }
        }
        return Pair(pathString, mutableMapOf())
    }

    private fun String.createToUnderlineCase(): String {
        val str = this.trim()
        if (str.isEmpty()) return ""
        val list = mutableListOf<String>()
        var i = 1
        var j = 0
        while (i < str.length) {
            if (str[i] in 'A'..'Z') {
                list.add(str.substring(j, i))
                j = i
            }
            i++
        }
        list.add(str.substring(j))
        return list.joinToString(separator = "_") { it.lowercase(Locale.getDefault()) }
    }

    private fun Bundle.toMap(): Map<String, String?> {
        val map = mutableMapOf<String, String?>()
        for (key in keySet()) {
            map[key] = get(key).toString()
        }
        return map
    }



}