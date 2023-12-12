package com.open.pkg.ui.article.cell

import android.os.Bundle
import android.view.View
import androidx.databinding.DataBindingUtil
import androidx.databinding.ObservableField
import com.open.core.ViewClickUtils.addClick
import com.open.pkg.R
import com.open.pkg.app.PkgRouter
import com.open.pkg.databinding.ArticleContentCellBinding
import com.open.pkg.net.vo.ArticleVo
import com.open.pkg.ui.main.MainActivity
import com.open.pkg.ui.web.WebActivity
import com.open.recyclerview.adapter.BaseCell
import com.open.recyclerview.adapter.BaseViewHolder

class ArticleContentCell(articleVo: ArticleVo) : BaseCell {
    val valueLink = ObservableField<String>()
    val valueContent = ObservableField<String>()
    val valueAuthor = ObservableField<String>()
    val valueDate = ObservableField<String>()
    val valueChapterName = ObservableField<String>()


    init {
        valueLink.set(articleVo.link)
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
            onClick(it.root)
        }
    }

    private fun onClick(view: View) {
        view.addClick({
            PkgRouter.navigation(
                view.context,
                Bundle().apply {
                    putString(WebActivity.WEB_URL, valueLink.get())
                    putString(WebActivity.WEB_TITLE, valueContent.get())
                },
                WebActivity::class.java
            )

        }, viewAlpha = true)

    }
}