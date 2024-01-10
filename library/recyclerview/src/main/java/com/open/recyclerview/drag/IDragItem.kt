package com.open.recyclerview.drag

import androidx.recyclerview.widget.RecyclerView

interface IDragItem {

    /**
     * 是否支持拖拽
     */
    fun canDrag(viewHolder: RecyclerView.ViewHolder): Boolean

    /**
     * 是否支持合并
     */
    fun canMerge(selected: RecyclerView.ViewHolder): Boolean

    /**
     * 是否可以接受合并
     */
    fun acceptMerge(target: RecyclerView.ViewHolder): Boolean

    /**
     * 显示合并预览效果，比如满足合并条件时高亮，加强交互体验
     * @param holder viewHolder
     * @param show 是否显示合并预览/高亮
     */
    fun showMergePreview(holder: RecyclerView.ViewHolder?, show: Boolean)

    /**
     * 显示拖拽状态
     * @param holder viewHolder
     * @param isCurrentlyActive 是否被拖拽
     */
    fun showDragState(holder: RecyclerView.ViewHolder?, isCurrentlyActive: Boolean)

}