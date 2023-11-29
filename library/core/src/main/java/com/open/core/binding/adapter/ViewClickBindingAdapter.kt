package com.open.core.binding.adapter

import android.view.View
import androidx.databinding.BindingAdapter
import com.open.core.ViewClickUtils.addClick

object ViewClickBindingAdapter {

    @BindingAdapter(
        value = [
            "bindClickViewListener",
            "bindClickDebounce",
            "bindClickDebounceDefault",
            "bindClickViewScale",
            "bindClickViewScaleDefault",
            "bindClickViewScaleDuration",
            "bindClickViewAlpha",
            "bindClickViewAlphaDefault"
        ], requireAll = false
    )
    @JvmStatic
    fun setBindViewClick(
        view: View,
        listener: (View) -> Unit,
        debounce: Boolean = true,
        debounceDefault: Long = 500L,
        viewScale: Boolean = true,
        scaleDefault: Float = 0.9f,
        duration: Long = 120,
        viewAlpha: Boolean = false,
        alphaDefault: Float = 0.7f
    ) {
        view.addClick(
            listener,
            debounce,
            debounceDefault,
            viewScale,
            scaleDefault,
            duration,
            viewAlpha,
            alphaDefault
        )
    }

//    @BindingAdapter(
//        value = [
//            "bindClickViewListener",
//        ], requireAll = false
//    )
//    @JvmStatic
//    fun setBindViewClick(
//        view: View,
//        listener: View.OnClickListener,
//    ) {
//
//        val onClick: (View) -> Unit = {
//
//        }
//
//        view.setOnClickListener(onClick)
//    }
}