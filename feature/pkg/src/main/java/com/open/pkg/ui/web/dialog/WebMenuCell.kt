package com.open.pkg.ui.web.dialog

import android.view.View
import androidx.databinding.DataBindingUtil
import androidx.databinding.ObservableField
import com.open.core.ViewClickUtils.addClick
import com.open.pkg.R
import com.open.pkg.databinding.WebMenuCellBinding
import com.open.recyclerview.adapter.BaseCell
import com.open.recyclerview.adapter.BaseViewHolder

class WebMenuCell(title: String, private val listener: ((String) -> Unit)? = null) : BaseCell {
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
            listener?.let {
                valueTitle.get()?.let { it1 -> it(it1) }
            }
        }, viewAlpha = true)

    }
}