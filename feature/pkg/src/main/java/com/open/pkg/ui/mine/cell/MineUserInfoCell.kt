package com.open.pkg.ui.mine.cell

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.activity.result.ActivityResultLauncher
import androidx.databinding.DataBindingUtil
import androidx.databinding.ObservableField
import androidx.databinding.ObservableInt
import com.open.core.ViewClickUtils.addClick
import com.open.pkg.R
import com.open.pkg.app.PkgRouter
import com.open.pkg.databinding.MineUserInfoCellBinding
import com.open.pkg.ui.media.AlbumActivity
import com.open.recyclerview.adapter.BaseCell
import com.open.recyclerview.adapter.BaseViewHolder

class MineUserInfoCell(private val launcher: ActivityResultLauncher<Intent>) : BaseCell {
    val valueName = ObservableField("open_9527")
    val valueAvatar = ObservableField("")
    val valueAvatarPlaceholderRes = ObservableInt()

    init {
        valueAvatarPlaceholderRes.set(R.drawable.avatar_placeholder_ic)
    }

    override fun getItemType(): Int = R.layout.mine_user_info_cell

    override fun bindViewHolder(holder: BaseViewHolder) {
        DataBindingUtil.bind<MineUserInfoCellBinding>(holder.itemView)?.let {
            it.cell = this
            onClick(it.root)
        }
    }

    private fun onClick(view: View) {
        view.addClick({
            PkgRouter.navigation(
                view.context,
                Bundle().apply {
                    putString(AlbumActivity.QUERY_TYPE, AlbumActivity.QUERY_IMAGE)
                },
                AlbumActivity::class.java, launcher
            )
        }, viewAlpha = true)

    }
}