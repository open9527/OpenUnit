package com.open.recyclerview.adapter

import android.view.View
import androidx.recyclerview.widget.RecyclerView

class BaseViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)


//DataBindingUtil.bind<ViewDataBinding>(holder.itemView)?.let {
//    it.cell = this
//    onClick(it.root)
//}