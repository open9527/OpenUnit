package com.open.pkg.ui.project.content.cell

import androidx.databinding.DataBindingUtil
import androidx.databinding.ObservableField
import com.open.pkg.R
import com.open.pkg.databinding.ArticleContentCellBinding
import com.open.pkg.databinding.ProjectContentCellBinding
import com.open.pkg.net.vo.ProjectVo
import com.open.recyclerview.adapter.BaseCell
import com.open.recyclerview.adapter.BaseViewHolder

class ProjectContentCell(projectVo: ProjectVo) : BaseCell {
    val valueContent = ObservableField<String>()
    val valueAuthor = ObservableField<String>()
    val valueDate = ObservableField<String>()
    val valueChapterName = ObservableField<String>()

    init {
        valueContent.set(projectVo.title)
        if (projectVo.author.isEmpty()) {
            valueAuthor.set(projectVo.shareUser)
        } else {
            valueAuthor.set(projectVo.author)
        }

        valueDate.set(projectVo.niceDate)
        valueChapterName.set("${projectVo.superChapterName}·${projectVo.chapterName}")
    }

    override fun getItemType(): Int {
        return R.layout.project_content_cell
    }

    override fun bindViewHolder(holder: BaseViewHolder) {
        DataBindingUtil.bind<ProjectContentCellBinding>(holder.itemView)?.let {
            it.cell = this

        }
    }
}