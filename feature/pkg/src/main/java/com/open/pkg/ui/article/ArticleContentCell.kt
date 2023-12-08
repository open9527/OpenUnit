package com.open.pkg.ui.article

import androidx.databinding.DataBindingUtil
import com.open.pkg.R
import com.open.pkg.databinding.ArticleContentCellBinding
import com.open.recyclerview.adapter.BaseCell
import com.open.recyclerview.adapter.BaseViewHolder

class ArticleContentCell : BaseCell {
    override fun getItemType(): Int {
        return R.layout.article_content_cell
    }

    override fun bindViewHolder(holder: BaseViewHolder) {
        DataBindingUtil.bind<ArticleContentCellBinding>(holder.itemView)?.let {
            it.cell = this

        }
    }
}