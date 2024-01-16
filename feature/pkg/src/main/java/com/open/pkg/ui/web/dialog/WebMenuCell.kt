package com.open.pkg.ui.web.dialog

import android.content.Context
import android.graphics.drawable.Drawable
import android.view.View
import androidx.databinding.DataBindingUtil
import androidx.databinding.ObservableField
import com.open.core.ContextHolder
import com.open.core.ResourcesAction
import com.open.core.ViewClickUtils.addClick
import com.open.pkg.R
import com.open.pkg.databinding.WebMenuCellBinding
import com.open.recyclerview.adapter.BaseCell
import com.open.recyclerview.adapter.BaseViewHolder

class WebMenuCell(menu: Pair<String,Int>, private val listener: ((String) -> Unit)? = null) : BaseCell ,
    ResourcesAction {
    val valueTitle = ObservableField<String>()
    val valueDrawable = ObservableField<Drawable>()

    init {
        valueTitle.set(menu.first)
        valueDrawable.set(getDrawable(menu.second))
    }

    override fun getItemType() = R.layout.web_menu_cell

    override fun bindViewHolder(holder: BaseViewHolder) {
        DataBindingUtil.bind<WebMenuCellBinding>(holder.itemView)?.let {
            it.cell = this
            onClick(it.root)
        }
    }
    override fun getContext(): Context {
        return ContextHolder.get()
    }
    private fun onClick(view: View) {

        view.addClick({
            listener?.let {
                valueTitle.get()?.let { it1 -> it(it1) }
            }
        }, viewAlpha = true)

    }
}