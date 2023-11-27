package com.open.router

import android.app.Activity
import android.content.Context
import android.os.Bundle

class Postcard(
    val path: String,
    var bundle: Bundle = Bundle(),
    var destination: Class<*>? = null,
    var type: RouteType? = null,
    var provider: IProvider? = null,
) {

    fun with(bundle: Bundle): Postcard = apply {
        this.bundle = bundle
    }

    fun withString(key: String, value: String): Postcard = apply {
        bundle.putString(key, value)
    }

    fun withInt(key: String, value: Int): Postcard = apply {
        bundle.putInt(key, value)
    }

    fun withLong(key: String, value: Long): Postcard = apply {
        bundle.putLong(key, value)
    }

    fun withFloat(key: String, value: Float): Postcard = apply {
        bundle.putFloat(key, value)
    }

    fun withDouble(key: String, value: Double): Postcard = apply {
        bundle.putDouble(key, value)
    }

    fun withChar(key: String, value: Char): Postcard = apply {
        bundle.putChar(key, value)
    }

    fun withBoolean(key: String, value: Boolean): Postcard = apply {
        bundle.putBoolean(key, value)
    }

    fun withByte(key: String, value: Byte): Postcard = apply {
        bundle.putByte(key, value)
    }

    fun withCharSequence(key: String, value: CharSequence): Postcard = apply {
        bundle.putCharSequence(key, value)
    }


    fun navigation(context: Context): Any? {
        return OpenRouter.navigation(context, this, -1)
    }

    fun navigation(activity: Activity, requestCode: Int): Any? {
        return OpenRouter.navigation(activity, this, requestCode)
    }

}