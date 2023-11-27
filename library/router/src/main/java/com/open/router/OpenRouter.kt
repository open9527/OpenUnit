package com.open.router

import android.app.Activity
import android.app.Application
import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment

object OpenRouter {


    private lateinit var application: Application

    fun init(application: Application) {
        this.application = application
    }

    fun register(path: String, clazzName: String) {
        RouterDelegate.add(path, clazzName)
    }


    fun unregister(path: String) {
        RouterDelegate.remove(path)
    }




    fun navigation(ctx: Context?, postcard: Postcard, requestCode: Int = -1): Any? {
        val context = ctx ?: application
        RouterDelegate.completion(application, postcard)
//        logInfo()
        return when (postcard.type) {
            RouteType.ACTIVITY -> {
                val intent = Intent(context, postcard.destination).putExtras(postcard.bundle)
                if (context !is Activity) {
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK)
                }
                if (requestCode >= 0) {
                    if (context is Activity) {
                        context.startActivityForResult(intent, requestCode)
                    }
                } else {
                    context.startActivity(intent)
                }
                null
            }

            RouteType.FRAGMENT -> {
                val fragmentMeta: Class<*>? = postcard.destination
                try {
                    val instance = fragmentMeta?.getConstructor()?.newInstance()
                    if (instance is Fragment) instance.arguments = postcard.bundle
                    instance
                } catch (e: Exception) {
                    null
                }
            }

            RouteType.PROVIDER -> postcard.provider
            else -> null
        }
    }

    fun build(path: String, bundle: Bundle = Bundle()): Postcard {
        return Postcard(path, bundle)
    }
}