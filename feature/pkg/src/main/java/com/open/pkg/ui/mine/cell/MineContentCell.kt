package com.open.pkg.ui.mine.cell

import androidx.databinding.DataBindingUtil
import androidx.databinding.ObservableField
import com.open.pkg.R
import com.open.pkg.databinding.MineContentCellBinding
import com.open.recyclerview.adapter.BaseCell
import com.open.recyclerview.adapter.BaseViewHolder

class MineContentCell(title: String) : BaseCell {
    val valueTitle = ObservableField("open_9527")

    init {
        valueTitle.set(title)
    }

    override fun getItemType(): Int = R.layout.mine_content_cell

    override fun bindViewHolder(holder: BaseViewHolder) {
        DataBindingUtil.bind<MineContentCellBinding>(holder.itemView)?.let {
            it.cell = this
        }
    }
}