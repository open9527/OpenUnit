package com.open.pkg.ui.navi.cell

import android.os.Bundle
import android.view.View
import androidx.databinding.DataBindingUtil
import androidx.databinding.ObservableField
import com.open.core.LogUtils
import com.open.core.ViewClickUtils.addClick
import com.open.pkg.R
import com.open.pkg.app.PkgRouter
import com.open.pkg.databinding.NaviContentCellBinding
import com.open.pkg.net.vo.ArticleVo
import com.open.pkg.ui.web.WebActivity
import com.open.recyclerview.adapter.BaseCell
import com.open.recyclerview.adapter.BaseViewHolder

class NaviContentCell(articleVo: ArticleVo) : BaseCell {
    private val valueLink = ObservableField<String>()
    val valueTitle = ObservableField<String>()

    init {
        valueLink.set(articleVo.link)
        valueTitle.set(articleVo.title)
    }

    override fun getItemType(): Int = R.layout.navi_content_cell

    override fun bindViewHolder(holder: BaseViewHolder) {
        DataBindingUtil.bind<NaviContentCellBinding>(holder.itemView)?.let {
            it.cell = this
            onClick(it.root)
        }
    }

    private fun onClick(view: View) {
        view.addClick({
            PkgRouter.navigation(
                view.context,
                Bundle().apply {
                    putString(WebActivity.WEB_URL, valueLink.get())
                    putString(WebActivity.WEB_TITLE, valueTitle.get())
                },
                WebActivity::class.java
            )
        }, viewAlpha = true)

    }
}