package com.open.recyclerview.drag

import androidx.recyclerview.widget.RecyclerView

/**
 * Adapter接口，接入拖拽功能需要实现该接口
 */
interface IDragAdapter {

    fun getDragData(): List<Any>

    fun getDragItem(viewHolder: RecyclerView.ViewHolder?): IDragItem?


}