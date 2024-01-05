package com.open.recyclerview.layoutmanager

import android.content.Context
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView

fun RecyclerView.linear(
    context: Context?,
    orientation: Int = RecyclerView.VERTICAL,
    reverseLayout: Boolean = false
): RecyclerView {
    layoutManager = WrapContentLinearLayoutManager(context, orientation, reverseLayout)
    return this
}


fun RecyclerView.grid(
    context: Context?,
    spanCount: Int,
    orientation: Int = RecyclerView.VERTICAL,
    reverseLayout: Boolean = false,
    spanSize: GridLayoutManager.SpanSizeLookup = object : GridLayoutManager.SpanSizeLookup() {
        override fun getSpanSize(position: Int): Int {
            return 1
        }
    },
): RecyclerView {
    layoutManager =
        WrapContentGridLayoutManager(context, spanCount, orientation, reverseLayout).apply {
            spanSizeLookup = spanSize
        }
    return this
}


fun RecyclerView.staggered(
    context: Context?,
    spanCount: Int,
    orientation: Int = RecyclerView.VERTICAL
): RecyclerView {
    layoutManager = WrapContentStaggeredGridLayoutManager(spanCount, orientation)
    return this
}


private fun setSpanSizeLookup(spanCount: Int) {
    var spanSizeLookup = object : GridLayoutManager.SpanSizeLookup() {
        override fun getSpanSize(position: Int): Int {
            return spanCount
        }
    }
}

