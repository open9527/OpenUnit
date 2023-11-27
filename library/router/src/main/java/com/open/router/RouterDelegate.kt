package com.open.router

import android.app.Activity
import android.app.Application
import android.util.Log
import androidx.fragment.app.Fragment

object RouterDelegate {
    private const val TAG: String = "RouterDelegate"
    private val routes = HashMap<String, RouteMeta>()
    private val providers = HashMap<Class<*>, IProvider>()


    fun add(path: String, clazzName: String) {
        val clazz = Class.forName(clazzName)
        val type = when {
            Activity::class.java.isAssignableFrom(clazz) -> RouteType.ACTIVITY
            Fragment::class.java.isAssignableFrom(clazz) -> RouteType.FRAGMENT
            IProvider::class.java.isAssignableFrom(clazz) -> RouteType.PROVIDER
            else -> RouteType.UNKNOWN
        }
        routes[path] = RouteMeta(clazz, type)

    }

    fun remove(path: String) {
        routes.remove(path)
    }

    @Suppress("UNCHECKED_CAST")
    fun completion(application: Application, postcard: Postcard) {
        val routeMeta = routes[postcard.path]
//            ?: throw IllegalStateException("There is no route match the path [${postcard.path}]")
        if (routeMeta == null) {
            Log.d("RouterDelegate", "There is no route match the path [${postcard.path}]")
            return
        }


        postcard.destination = routeMeta.destination
        postcard.type = routeMeta.type
        if (routeMeta.type == RouteType.PROVIDER) {
            val providerClazz = routeMeta.destination as Class<IProvider>
            var instance = providers[providerClazz]
            if (instance == null) {
                try {
                    val provider = providerClazz.getConstructor().newInstance()
                    provider.init(application)
                    providers[providerClazz] = provider
                    instance = provider
                } catch (e: Exception) {
                    throw IllegalStateException("Init provider failed!")
                }
            }
            postcard.provider = instance
        }
    }
}