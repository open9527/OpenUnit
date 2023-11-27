package com.open.core

import android.annotation.SuppressLint
import android.content.Context

@SuppressLint("StaticFieldLeak")
object ContextHolder {
    private lateinit var context: Context

    fun init(context: Context) {
        ContextHolder.context = context
    }
    fun get(): Context = context
}