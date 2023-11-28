package com.open.recyclerview.scroller

import android.content.Context
import android.util.DisplayMetrics
import androidx.recyclerview.widget.LinearSmoothScroller
import androidx.recyclerview.widget.LinearSmoothScroller.SNAP_TO_END
import androidx.recyclerview.widget.LinearSmoothScroller.SNAP_TO_START
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.SnapHelper

fun RecyclerView.smoothScrollToStartPosition(position: Int, millisecondsPerInch: Float) =
    smoothScrollToPosition(position, SNAP_TO_START, millisecondsPerInch)

fun RecyclerView.smoothScrollToEndPosition(position: Int, millisecondsPerInch: Float) =
    smoothScrollToPosition(position, SNAP_TO_END, millisecondsPerInch)

fun RecyclerView.smoothScrollToPosition(position: Int, snap: Int, millisecondsPerInch: Float) =
    layoutManager?.let {
        val smoothScroller = LinearSmoothScroller(context, snap, millisecondsPerInch)
        smoothScroller.targetPosition = position
        it.startSmoothScroll(smoothScroller)
    }

fun LinearSmoothScroller(context: Context, snap: Int, millisecondsPerInch: Float = 25f) =
    object : LinearSmoothScroller(context) {
        override fun getVerticalSnapPreference() = snap
        override fun getHorizontalSnapPreference() = snap

        //calculateSpeedPerPixel的方法返回值是滑动每个像素所需要的时间(毫秒),
        //默认是25F / displayMetrics.densityDpi。
        //如果需要调慢一点可将这个值调大一点。可以根据实际体验，设置一个合适的值。
        override fun calculateSpeedPerPixel(displayMetrics: DisplayMetrics): Float {
            return millisecondsPerInch / displayMetrics.densityDpi
        }
    }


