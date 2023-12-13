package com.open.pkg.ui.web.dialog

import android.view.View
import androidx.databinding.DataBindingUtil
import androidx.databinding.ObservableField
import com.open.core.ViewClickUtils.addClick
import com.open.core.toast
import com.open.pkg.R
import com.open.pkg.databinding.WebMenuCellBinding
import com.open.recyclerview.adapter.BaseCell
import com.open.recyclerview.adapter.BaseViewHolder

class WebMenuCell(title: String) : BaseCell {
    val valueTitle = ObservableField<String>()

    init {
        valueTitle.set(title)
    }

    override fun getItemType() = R.layout.web_menu_cell

    override fun bindViewHolder(holder: BaseViewHolder) {
        DataBindingUtil.bind<WebMenuCellBinding>(holder.itemView)?.let {
            it.cell = this
            onClick(it.root)
        }
    }

    private fun onClick(view: View) {
        view.addClick({
            it.context.toast("待开发!")
        }, viewAlpha = true)

    }
}