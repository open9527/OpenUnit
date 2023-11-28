package com.open.recyclerview.decoration

import android.graphics.*
import android.view.View
import androidx.recyclerview.widget.RecyclerView
import kotlin.math.roundToInt


class DividerDecoration(
    private val orientation: Int = RecyclerView.VERTICAL,
    private val isLastVisible: Boolean = false,
    private val isInside: Boolean = false
) : RecyclerView.ItemDecoration() {

    private val paint: Paint = Paint(Paint.ANTI_ALIAS_FLAG)

    private var insetStart: Float = 0f
    private var insetEnd: Float = 0f


    fun stroke(size: Float = 1f, color: Int = Color.LTGRAY): DividerDecoration {
        paint.strokeWidth = size
        paint.color = color
        return this
    }

    fun dash(dashWidth: Float, dashGap: Float = dashWidth): DividerDecoration {
        paint.pathEffect = DashPathEffect(floatArrayOf(dashWidth, dashGap), 0f)
        return this
    }

    fun inset(start: Float, end: Float = start): DividerDecoration {
        insetStart = start
        insetEnd = end
        return this
    }

    private val bounds = Rect()

    init {
        paint.strokeJoin = Paint.Join.ROUND
        paint.style = Paint.Style.STROKE
        paint.color = Color.LTGRAY
        paint.strokeWidth = 1f
    }

    override fun onDraw(canvas: Canvas, parent: RecyclerView, state: RecyclerView.State) {
        if (parent.layoutManager == null || parent.adapter == null) {
            return
        }

        canvas.save()

        for (i in 0 until parent.childCount) {
            val child = parent.getChildAt(i)

            val position = parent.getChildAdapterPosition(child)
            val itemCount = parent.adapter!!.itemCount
            if (!isLastVisible && position >= itemCount - 1) {
                continue
            }
            parent.getDecoratedBoundsWithMargins(child, bounds)

            if (orientation == RecyclerView.VERTICAL) {
                val bottom = bounds.bottom + child.translationY
                val top = bottom - paint.strokeWidth
                canvas.drawLine(insetStart, top, parent.width - insetEnd, bottom, paint)
            } else {
                val right = bounds.right + child.translationX
                val left = right - paint.strokeWidth
                canvas.drawLine(left, insetStart, right, parent.height - insetEnd, paint)
            }

        }
        canvas.restore()
    }

    override fun getItemOffsets(
        outRect: Rect,
        view: View,
        parent: RecyclerView,
        state: RecyclerView.State
    ) {

        if (isInside) {
            outRect.set(0, 0, 0, 0)
            return
        }
        if (orientation == RecyclerView.VERTICAL) {
            outRect.set(0, 0, 0, paint.strokeWidth.roundToInt())
        } else {
            outRect.set(0, 0, paint.strokeWidth.roundToInt(), 0)
        }
    }


}