package com.open.core.binding.adapter

import android.annotation.SuppressLint
import android.view.View
import android.view.ViewGroup
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
    @BindingAdapter(value = ["bindViewBgDrawableRes"], requireAll = false)
    @JvmStatic
    fun setBindingViewBgResource(view: View, @DrawableRes drawableRes: Int) {
        if (drawableRes > 0) {
            view.setBackgroundResource(drawableRes)
        }
    }

    @BindingAdapter(value = ["bindingViewHeight"], requireAll = false)
    fun setBindingViewHeight(view: View?, viewHeight: Int) {
        if (view == null) return
        val layoutParams = view.layoutParams
        layoutParams.height = viewHeight
        view.layoutParams = layoutParams
    }

    @BindingAdapter(value = ["bindingViewWidth"], requireAll = false)
    fun setBindingViewWidth(view: View?, viewWidth: Int) {
        if (view == null) return
        val layoutParams = view.layoutParams
        layoutParams.width = viewWidth
        view.layoutParams = layoutParams
    }

    @BindingAdapter(value = ["bindingViewMarginLeft", "bindingViewMarginRight"], requireAll = false)
    fun setBindingViewMargin(view: View?, leftMargin: Int, rightMargin: Int) {
        if (view == null) return
        val marginLayoutParams = view.layoutParams as ViewGroup.MarginLayoutParams
        marginLayoutParams.leftMargin = leftMargin
        marginLayoutParams.rightMargin = rightMargin
        view.layoutParams = marginLayoutParams
    }

}