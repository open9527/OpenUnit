package com.open.pkg.ui.article

import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.open.base.BaseFragment
import com.open.core.LogUtils
import com.open.core.applicationViewModels
import com.open.core.binding.binding
import com.open.pkg.R
import com.open.pkg.databinding.ArticleFragmentBinding
import com.open.pkg.net.api.WanApiImpl
import com.open.pkg.ui.article.cell.ArticleBannerCell
import com.open.pkg.ui.article.cell.ArticleContentCell
import com.open.pkg.ui.main.MainViewModel
import com.open.recyclerview.adapter.BaseAdapter
import com.open.recyclerview.adapter.BaseCell
import com.open.recyclerview.adapter.diffCallback
import com.open.recyclerview.animations.ItemAnimation
import com.open.recyclerview.layoutmanager.WrapContentLinearLayoutManager
import com.scwang.smart.refresh.layout.api.RefreshLayout
import com.scwang.smart.refresh.layout.listener.OnRefreshLoadMoreListener


class ArticleFragment : BaseFragment(R.layout.article_fragment) {

    private val binding: ArticleFragmentBinding by binding(this)

    private val viewModel: ArticleViewModel by viewModels()

    private val mainViewModel: MainViewModel by applicationViewModels()

    private var page: Int = 0
    private var cellList: MutableList<BaseCell> = mutableListOf()
    fun newInstance(): Fragment {
        val args = Bundle()
        val fragment = ArticleFragment()
        fragment.arguments = args
        return fragment
    }

    private val rvAdapter by lazy {
        BaseAdapter(diffCallback(), ItemAnimation.create().apply {
            duration(300)
            enabled(true)
            animation(animationType = ItemAnimation.FADE_IN)
        })
    }

    override fun initView() {
        binding.rvList.apply {
            layoutManager = WrapContentLinearLayoutManager(context)
//            addItemDecoration(DividerDecoration().inset(20.0f))
//            addItemDecoration(DividerDecoration().stroke(20.0f))
//            addItemDecoration(DividerDecoration().dash(20.0f))
            adapter = rvAdapter
        }

        binding.refresh.apply {
            setOnRefreshLoadMoreListener(object : OnRefreshLoadMoreListener {
                override fun onRefresh(refreshLayout: RefreshLayout) {
                    refreshLayout.resetNoMoreData()
                    request(page = 0)
                    refreshLayout.finishRefresh()
                }

                override fun onLoadMore(refreshLayout: RefreshLayout) {
                    request(page = ++page)
                    refreshLayout.finishLoadMore()
                }
            })
        }
    }

    override fun initData() {
        request(page = 0)
    }

    private fun request(page: Int) {
        this.page = page
        if (0 == page) {
            cellList.clear()
            requestBanner()
        }
        requestArticleList()
    }

    private fun requestBanner() {
        WanApiImpl.requestBanner().observe(viewLifecycleOwner) { bannerResponse ->
//            LogUtils.d("Banner:${JsonClient.toJson(bannerResponse)}")
            if (bannerResponse.isSuccessful) {
                bannerResponse.data?.let { bannerList ->
                    cellList.add(0, ArticleBannerCell(bannerList, viewLifecycleOwner))
                    rvAdapter.submitList(cellList)
                }
            }

        }
    }

    private fun requestArticleList() {
        LogUtils.d("requestArticleList page=${page}")
        WanApiImpl.requestArticleList(page).observe(viewLifecycleOwner) { articleListResponse ->
            if (articleListResponse.isSuccessful) {
                articleListResponse.data?.list?.forEach { articleVo ->
                    cellList.add(ArticleContentCell(articleVo))
                }
                rvAdapter.notifyItemRangeChanged(0, cellList.size, cellList.size)
            }
        }
    }

}
