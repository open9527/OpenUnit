package com.open.pkg.ui.article

import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import com.open.base.BaseFragment
import com.open.core.LogUtils
import com.open.core.ViewClickUtils.addClick
import com.open.core.applicationViewModels
import com.open.core.binding.binding
import com.open.pkg.R
import com.open.pkg.app.PkgRouter
import com.open.pkg.databinding.ArticleFragmentBinding
import com.open.pkg.net.api.WanApiImpl
import com.open.pkg.ui.article.cell.ArticleBannerCell
import com.open.pkg.ui.article.cell.ArticleContentCell
import com.open.pkg.ui.main.MainViewModel
import com.open.pkg.ui.search.SearchActivity
import com.open.pkg.ui.view.TextSwitchBanner
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


    private val rvAdapter by lazy {
        BaseAdapter(diffCallback(), ItemAnimation.create().apply {
            duration(300)
            enabled(true)
            firstOnly(false)
            animation(animationType = ItemAnimation.SCALE_IN)
        })
    }

    override fun initView() {
        binding.rvList.apply {
            layoutManager = WrapContentLinearLayoutManager(context)
            adapter = rvAdapter
        }
        binding.clSearch.addClick({
            PkgRouter.navigation(
                requireContext(),
                Bundle().apply {
                    putString(SearchActivity.BUNDLE_KEY, viewModel.valueContent.get())
                },
                SearchActivity::class.java
            )
        })

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
            requestHotKey()
        }
        requestArticleList()
    }

    private fun initTextSwitchBanner(list: List<String>) {
        TextSwitchBanner.with(requireContext())
            .bindTextSwitcher(binding.textSwitcher)
            .setTextList(list)
            .addListener { _, string, _ ->
                PkgRouter.navigation(
                    requireContext(),
                    Bundle().apply {
                        putString(SearchActivity.BUNDLE_KEY, string)
                    },
                    SearchActivity::class.java
                )
            }
            .addChangeListener { _, string, _ ->
                viewModel.valueContent.set(string)
            }
            .addLifecycleOwner(viewLifecycleOwner)
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
                if (page > 0 && articleListResponse.data?.list?.size == 0) {
                    binding.refresh.finishLoadMoreWithNoMoreData()
                }
                articleListResponse.data?.list?.forEach { articleVo ->
                    cellList.add(ArticleContentCell(articleVo))
                }
                rvAdapter.notifyItemRangeChanged(0, cellList.size, cellList.size)
            }
        }
    }


    private fun requestHotKey() {
        WanApiImpl.requestHotKey().observe(viewLifecycleOwner) { hotKeyResponse ->
//            LogUtils.d("HotKey:${JsonClient.toJson(requestHotKey)}")
            if (hotKeyResponse.isSuccessful) {
                hotKeyResponse.data?.let { hotKeyList ->
                    val list = mutableListOf<String>()
                    hotKeyList.forEach {
                        list.add(it.name)
                    }
                    initTextSwitchBanner(list)
                }
            }

        }
    }

    companion object {
        private const val TAG: String = "ArticleFragment"
        private const val BUNDLE_KEY: String = "BUNDLE_KEY_ARTICLE_FRAGMENT"
        fun newInstance(string: String): ArticleFragment {
            val bundle = Bundle()
            bundle.putString(BUNDLE_KEY, string)
            val fragment = ArticleFragment()
            fragment.arguments = bundle
            return fragment
        }
    }
}
