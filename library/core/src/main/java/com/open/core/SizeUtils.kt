package com.open.core


object SizeUtils {
    fun dp2px(dp: Int): Float {
        val scale = ContextHolder.get().resources.displayMetrics.density
        return (dp * scale + 0.5f)
    }

    fun px2dp(px: Int): Float {
        val scale = ContextHolder.get().resources.displayMetrics.density
        return (px / scale + 0.5f)
    }
}