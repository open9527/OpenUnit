package com.open.pkg.ui.navi.cell

import android.view.View
import androidx.databinding.DataBindingUtil
import androidx.databinding.ObservableField
import com.open.core.LogUtils
import com.open.core.ViewClickUtils.addClick
import com.open.pkg.R
import com.open.pkg.databinding.NaviTitleCellBinding
import com.open.pkg.net.vo.NavigationVo
import com.open.recyclerview.adapter.BaseCell
import com.open.recyclerview.adapter.BaseViewHolder

class NaviTitleCell(navigationVo: NavigationVo) : BaseCell {

    val valueTitle = ObservableField<String>()

    init {
        valueTitle.set(navigationVo.name)
    }

    override fun getItemType(): Int = R.layout.navi_title_cell

    override fun bindViewHolder(holder: BaseViewHolder) {
        DataBindingUtil.bind<NaviTitleCellBinding>(holder.itemView)?.let {
            it.cell = this
            onClick(it.root)
        }
    }

    private fun onClick(view: View) {
        view.addClick({
            LogUtils.d("onClick")
        }, viewAlpha = true)

    }
}