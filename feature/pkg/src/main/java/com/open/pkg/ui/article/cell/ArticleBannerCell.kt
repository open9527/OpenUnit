package com.open.pkg.ui.article.cell

import android.content.Context
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.LifecycleOwner
import com.open.core.ContextHolder
import com.open.core.ResourcesAction
import com.open.image.loadRoundedCorners
import com.open.pkg.R
import com.open.pkg.databinding.ArticleBannerCellBinding
import com.open.pkg.databinding.ArticleBannerItemBinding
import com.open.pkg.net.vo.BannerVo
import com.open.recyclerview.adapter.BaseCell
import com.open.recyclerview.adapter.BaseViewHolder
import com.zhpan.bannerview.BannerViewPager
import com.zhpan.bannerview.BaseBannerAdapter
import com.zhpan.bannerview.constants.PageStyle
import com.zhpan.bannerview.utils.BannerUtils
import com.zhpan.indicator.enums.IndicatorSlideMode

class ArticleBannerCell(
    private val bannerList: List<BannerVo>,
    private var lifecycleOwner: LifecycleOwner
) : BaseCell, ResourcesAction {

    private val bannerAdapter by lazy {
        BannerViewAdapter()
    }


    private lateinit var mBannerPager: BannerViewPager<BannerVo>
    override fun getItemType(): Int {
        return R.layout.article_banner_cell
    }

    override fun bindViewHolder(holder: BaseViewHolder) {
        DataBindingUtil.bind<ArticleBannerCellBinding>(holder.itemView)?.let {
            it.cell = this
        }
        mBannerPager = holder.itemView.findViewById(R.id.banner)
        mBannerPager.apply {
            adapter = bannerAdapter
            setScrollDuration(2000)
            setIndicatorSlideMode(IndicatorSlideMode.SMOOTH)
            setRevealWidth(BannerUtils.dp2px(8f), BannerUtils.dp2px(8f))
            setPageStyle(
                PageStyle.MULTI_PAGE_SCALE, 0.75f
            )
            setIndicatorSliderColor(getColor(R.color.panda), getColor(R.color.accent_color))
            registerLifecycleObserver(lifecycleOwner.lifecycle)
        }.create()
        mBannerPager.refreshData(bannerList)
    }

    override fun getContext(): Context {
        return ContextHolder.get()
    }


}

class BannerViewAdapter : BaseBannerAdapter<BannerVo>() {
    override fun bindData(
        holder: com.zhpan.bannerview.BaseViewHolder<BannerVo>,
        bannerVo: BannerVo,
        position: Int,
        pageSize: Int
    ) {
        DataBindingUtil.bind<ArticleBannerItemBinding>(holder.itemView)?.let {
            it.ivPic.loadRoundedCorners(bannerVo.imagePath, 10f)
            it.tvTitle.text = bannerVo.title
        }
    }

    override fun getLayoutId(viewType: Int): Int {
        return R.layout.article_banner_item
    }
}