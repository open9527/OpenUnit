package com.open.pkg.ui.mine.cell

import androidx.databinding.DataBindingUtil
import androidx.databinding.ObservableField
import androidx.databinding.ObservableInt
import com.open.core.ResourcesAction
import com.open.pkg.R
import com.open.pkg.databinding.MineUserInfoCellBinding
import com.open.recyclerview.adapter.BaseCell
import com.open.recyclerview.adapter.BaseViewHolder

class MineUserInfoCell : BaseCell {
    val valueName = ObservableField("open_9527")
    val valueAvatar = ObservableField("")
    val valueAvatarPlaceholderRes = ObservableInt()

    init {
        valueAvatarPlaceholderRes.set(R.drawable.empty_circle_image)
    }

    override fun getItemType(): Int = R.layout.mine_user_info_cell

    override fun bindViewHolder(holder: BaseViewHolder) {
        DataBindingUtil.bind<MineUserInfoCellBinding>(holder.itemView)?.let {
            it.cell = this
        }
    }
}