package com.open.core.binding.adapter

import android.view.View
import androidx.databinding.BindingAdapter
import com.open.core.ViewClickUtils.addClick

object ViewClickBindingAdapter {

    @BindingAdapter(
        value = [
            "bindViewClickListener",
            "bindViewClickDebounce",
            "bindViewClickDebounceDefault",
            "bindViewClickScale",
            "bindViewClickScaleDefault",
            "bindViewClickScaleDuration",
            "bindViewClickAlpha",
            "bindViewClickAlphaDefault"
        ], requireAll = false
    )
    @JvmStatic
    fun View.setBindViewClick(
        listener: (view: View) -> Unit,
        debounce: Boolean? = true,
        debounceDefault: Long? = 500L,
        viewScale: Boolean? = true,
        scaleDefault: Float? = 0.9f,
        duration: Long? = 120L,
        viewAlpha: Boolean? = false,
        alphaDefault: Float? = 0.7f
    ) {
        addClick(
            listener = listener,
            debounce = debounce ?: true,
            debounceDefault = debounceDefault ?: 500L,
            viewScale = viewScale ?: true,
            scaleDefault = scaleDefault ?: 0.9f,
            duration = duration ?: 120L,
            viewAlpha = viewAlpha ?: true,
            alphaDefault = alphaDefault ?: 0.7f
        )

    }
}