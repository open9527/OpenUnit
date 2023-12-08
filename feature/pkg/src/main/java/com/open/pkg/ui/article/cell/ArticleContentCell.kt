package com.open.pkg.ui.article.cell

import androidx.databinding.DataBindingUtil
import androidx.databinding.ObservableField
import com.open.pkg.R
import com.open.pkg.databinding.ArticleContentCellBinding
import com.open.pkg.net.vo.ArticleVo
import com.open.recyclerview.adapter.BaseCell
import com.open.recyclerview.adapter.BaseViewHolder

class ArticleContentCell(articleVo: ArticleVo) : BaseCell {
    val valueContent = ObservableField<String>()
    val valueAuthor = ObservableField<String>()
    val valueDate = ObservableField<String>()
    val valueChapterName = ObservableField<String>()

    init {
        valueContent.set(articleVo.title)
        if (articleVo.author.isEmpty()) {
            valueAuthor.set(articleVo.shareUser)
        } else {
            valueAuthor.set(articleVo.author)
        }

        valueDate.set(articleVo.niceDate)
        valueChapterName.set("${articleVo.superChapterName}·${articleVo.chapterName}")
    }

    override fun getItemType(): Int {
        return R.layout.article_content_cell
    }

    override fun bindViewHolder(holder: BaseViewHolder) {
        DataBindingUtil.bind<ArticleContentCellBinding>(holder.itemView)?.let {
            it.cell = this

        }
    }
}