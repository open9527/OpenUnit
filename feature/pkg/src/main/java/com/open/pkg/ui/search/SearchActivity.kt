package com.open.pkg.ui.search

import android.annotation.SuppressLint
import android.view.KeyEvent
import android.view.MotionEvent
import android.view.inputmethod.EditorInfo
import android.widget.TextView
import android.widget.TextView.OnEditorActionListener
import androidx.activity.viewModels
import com.open.base.BaseActivity
import com.open.core.KeyboardAction
import com.open.core.LogUtils
import com.open.core.binding.binding
import com.open.pkg.R
import com.open.pkg.app.PkgRouter
import com.open.pkg.databinding.SearchActivityBinding
import com.open.pkg.net.api.WanApiImpl
import com.open.pkg.ui.article.cell.ArticleContentCell
import com.open.recyclerview.adapter.BaseAdapter
import com.open.recyclerview.adapter.BaseCell
import com.open.recyclerview.adapter.diffCallback
import com.open.recyclerview.animations.ItemAnimation
import com.open.recyclerview.layoutmanager.WrapContentLinearLayoutManager
import com.open.serialization.JsonClient
import com.scwang.smart.refresh.layout.api.RefreshLayout
import com.scwang.smart.refresh.layout.listener.OnRefreshLoadMoreListener


class SearchActivity : BaseActivity(R.layout.search_activity), KeyboardAction {

    private val binding: SearchActivityBinding by binding(this)

    private val viewModel: SearchViewModel by viewModels()

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

    @SuppressLint("ClickableViewAccessibility")
    override fun initView() {
        binding.vm = viewModel
        binding.rvList.apply {
            layoutManager = WrapContentLinearLayoutManager(context)
            adapter = rvAdapter
        }

        binding.etSearch.isCursorVisible = false
        binding.etSearch.setOnTouchListener { _, event ->
            if (MotionEvent.ACTION_DOWN == event.action) {
                binding.etSearch.isCursorVisible = true
            }
            false
        }
        binding.etSearch.setOnEditorActionListener { textView, imeAction, _ ->
            if (EditorInfo.IME_ACTION_SEARCH == imeAction) {
                viewModel.valueKeyword.set(textView.text.toString())
                hideKeyboard(binding.etSearch)
                request(page = 0)
            }
            false
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
        intent.extras?.let { bundle ->
            LogUtils.d("bundleToJson:${JsonClient.toJson(PkgRouter.bundleToMap(bundle))}")
            viewModel.valueKeyword.set(bundle.getString(BUNDLE_KEY, ""))
        }
        request(page = 0)
    }

    private fun request(page: Int) {
        this.page = page
        if (0 == page) {
            cellList.clear()
        }
        requestSearchArticleList()
    }

    private fun requestSearchArticleList() {
        LogUtils.d("requestSearchArticleList page=${page}")
        WanApiImpl.requestSearchArticleList(
            page,
            viewModel.valueKeyword.get() ?: ""
        )
            .observe(this) { searchArticleListResponse ->
                if (searchArticleListResponse.isSuccessful) {
                    if (page > 0 && searchArticleListResponse.data?.list?.size == 0) {
                        binding.refresh.finishLoadMoreWithNoMoreData()
                    }
                    searchArticleListResponse.data?.list?.forEach { articleVo ->
                        cellList.add(ArticleContentCell(articleVo))
                    }
                    if (0 == page) {
                        rvAdapter.submitList(cellList)
                    }
                    rvAdapter.notifyItemRangeChanged(0, cellList.size, cellList.size)

                }
            }
    }

    companion object {
        const val BUNDLE_KEY: String = "BUNDLE_KEY_SEARCH_ACTIVITY"
    }
}