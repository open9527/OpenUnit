package com.open.pkg.ui.media.cell

import android.view.View
import androidx.databinding.DataBindingUtil
import androidx.databinding.ObservableBoolean
import androidx.databinding.ObservableField
import com.open.core.LogUtils
import com.open.core.ViewClickUtils.addClick
import com.open.pkg.R
import com.open.pkg.databinding.AlbumImageCellBinding
import com.open.pkg.ui.media.vo.MediaBean
import com.open.recyclerview.adapter.BaseCell
import com.open.recyclerview.adapter.BaseViewHolder

class AlbumImageCell(mediaBean: MediaBean,  private val listener: ((String) -> Unit)? = null) :
    BaseCell {
    val valueData = ObservableField<Any>()
    val valueType = ObservableField<String>()
    val valuePlaceholderRes = ObservableField<Int>()
    val valueSelect = ObservableBoolean(false)

    init {
        valueData.set(mediaBean.bitmapThumbnail ?: mediaBean.uri)
        valuePlaceholderRes.set(R.drawable.empty_image)
        valueType.set(mediaBean.type)
    }

    override fun getItemType(): Int = R.layout.album_image_cell

    override fun bindViewHolder(holder: BaseViewHolder) {
        DataBindingUtil.bind<AlbumImageCellBinding>(holder.itemView)?.let {
            it.cell = this
            onClick(it.root)
            onSelectClick(it.cbCheck)
        }
    }

    private fun onClick(view: View) {
        view.addClick({
            LogUtils.d("onClick${valueData.get().toString()}")
            listener?.let {
                it(valueData.get().toString())
            }

        }, viewAlpha = true)

    }

    private fun onSelectClick(view: View) {
        view.addClick({
            updateSelect(!valueSelect.get())
        }, viewAlpha = true)

    }

    private fun updateSelect(select: Boolean) {
        valueSelect.set(select)
    }

}


