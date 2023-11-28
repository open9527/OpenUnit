package com.open.recyclerview.layoutmanager

import android.content.Context
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView

fun RecyclerView.linear(
    context: Context?,
    orientation: Int = RecyclerView.VERTICAL,
    reverseLayout: Boolean = false
) =
    WrapContentLinearLayoutManager(context, orientation, reverseLayout)

fun RecyclerView.grid(
    context: Context?,
    spanCount: Int,
    orientation: Int = RecyclerView.VERTICAL,
    reverseLayout: Boolean = false
) =
    WrapContentGridLayoutManager(context, spanCount, orientation, reverseLayout)

fun RecyclerView.staggered(
    context: Context?,
    spanCount: Int,
    orientation: Int = RecyclerView.VERTICAL
) =
    WrapContentStaggeredGridLayoutManager(spanCount, orientation)


private fun setSpanSizeLookup() {
    var spanSizeLookup = object : GridLayoutManager.SpanSizeLookup() {
        override fun getSpanSize(position: Int): Int {
            return when (position) {
                0 -> 2
                else -> 1
            }
        }
    }
}

