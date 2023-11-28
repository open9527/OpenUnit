package com.open.recyclerview.scroller

import androidx.recyclerview.widget.RecyclerView

class RecycleViewScrollListener : RecyclerView.OnScrollListener() {

    private var mDistance: Int = 100
    private var mScrollDx: Int = 0
    private var iScrollChangeAlpha: IScrollChangeAlpha? = null

    override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
        super.onScrolled(recyclerView, dx, dy)
        mScrollDx += dy
        val alpha = if (mScrollDx <= 0) {
            0f
        } else if (mScrollDx in 1..mDistance) {
            //设置透明度，当滑动距离与最大距离相等时，透明度值为
            //滑动距离越小，透明度值越小
            mScrollDx.toFloat() / mDistance
        } else {
            1f
        }
        iScrollChangeAlpha?.onScrimsStateChangeAlpha(recyclerView, mScrollDx >= mDistance, alpha)
    }

    public fun addListener(distance: Int = 100, iScrollChangeAlpha: IScrollChangeAlpha?) {
        this.iScrollChangeAlpha = iScrollChangeAlpha
    }
}

interface IScrollChangeAlpha {
    fun onScrimsStateChangeAlpha(recyclerView: RecyclerView, shown: Boolean, alpha: Float)
}