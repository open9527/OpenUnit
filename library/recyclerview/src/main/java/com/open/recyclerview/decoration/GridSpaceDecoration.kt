package com.open.recyclerview.decoration

import android.graphics.Canvas
import android.graphics.Rect
import android.view.View
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ItemDecoration


/**
 * 需要在RecyclerView 设置margin=space
 */
class GridSpaceDecoration constructor(private val space: Int) : ItemDecoration() {

    override fun onDraw(canvas: Canvas, recyclerView: RecyclerView, state: RecyclerView.State) {}

    override fun onDrawOver(
        canvas: Canvas,
        recyclerView: RecyclerView,
        state: RecyclerView.State
    ) {
    }

    override fun getItemOffsets(
        rect: Rect,
        view: View,
        recyclerView: RecyclerView,
        state: RecyclerView.State
    ) {
        val position: Int = recyclerView.getChildAdapterPosition(view)
        val spanCount: Int = (recyclerView.layoutManager as GridLayoutManager).spanCount

        //所在的列
        val column: Int = position % spanCount

        // column * (列间距 * (1f / 列数))
        rect.left = column * space / spanCount


        // 列间距 - (column + 1) * (列间距 * (1f /列数))
        rect.right = space - (column + 1) * space / spanCount


        if (position >= spanCount) {
            rect.top = space
        }
    }
}