package com.open.recyclerview.adapter

interface BaseCell {
    fun getItemType(): Int
    fun bindViewHolder(holder: BaseViewHolder)
}