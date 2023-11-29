package com.open.core.binding.adapter

import android.annotation.SuppressLint
import android.view.View
import androidx.annotation.ColorRes
import androidx.annotation.DrawableRes
import androidx.core.content.ContextCompat
import androidx.databinding.BindingAdapter

object ViewBindingAdapter {

    @SuppressLint("ResourceType")
    @BindingAdapter(value = ["bindViewBgColor"], requireAll = false)
    @JvmStatic
    fun setBindingViewBgColor(view: View, @ColorRes color: Int) {
        if (color > 0) {
            view.setBackgroundColor(ContextCompat.getColor(view.context, color))
        }
    }

    @SuppressLint("ResourceType")
    @BindingAdapter(value = ["bindViewBgColor"], requireAll = false)
    @JvmStatic
    fun setBindingViewBgResource(view: View, @DrawableRes drawableRes: Int) {
        if (drawableRes > 0) {
            view.setBackgroundResource(drawableRes)
        }
    }

}