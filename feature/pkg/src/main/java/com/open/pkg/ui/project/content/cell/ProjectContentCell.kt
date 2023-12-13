package com.open.pkg.ui.project.content.cell

import android.os.Bundle
import android.view.View
import androidx.databinding.DataBindingUtil
import androidx.databinding.ObservableField
import com.open.core.ViewClickUtils.addClick
import com.open.pkg.R
import com.open.pkg.app.PkgRouter
import com.open.pkg.databinding.ArticleContentCellBinding
import com.open.pkg.databinding.ProjectContentCellBinding
import com.open.pkg.net.vo.ProjectVo
import com.open.pkg.ui.splash.SplashActivity
import com.open.pkg.ui.web.WebActivity
import com.open.recyclerview.adapter.BaseCell
import com.open.recyclerview.adapter.BaseViewHolder

class ProjectContentCell(projectVo: ProjectVo) : BaseCell {
    val valueLink = ObservableField<String>()
    val valueTitle = ObservableField<String>()
    val valueContent = ObservableField<String>()
    val valuePic = ObservableField<String>()
    val valueAuthor = ObservableField<String>()
    val valueDate = ObservableField<String>()
    val valueChapterName = ObservableField<String>()

    init {
        if (projectVo.link.isEmpty()) {
            valueLink.set(projectVo.projectLink)
        } else {
            valueLink.set(projectVo.link)
        }
        valueTitle.set(projectVo.title)
        valueContent.set(projectVo.desc)
        valuePic.set(projectVo.envelopePic)
        if (projectVo.author.isEmpty()) {
            valueAuthor.set(projectVo.shareUser)
        } else {
            valueAuthor.set(projectVo.author)
        }

        valueDate.set(projectVo.niceDate)
        valueChapterName.set("${projectVo.superChapterName}·${projectVo.chapterName}")
    }

    override fun getItemType() = R.layout.project_content_cell

    override fun bindViewHolder(holder: BaseViewHolder) {
        DataBindingUtil.bind<ProjectContentCellBinding>(holder.itemView)?.let {
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