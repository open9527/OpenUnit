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
import com.open.pkg.ui.main.MainViewModel
import com.open.recyclerview.adapter.BaseAdapter
import com.open.recyclerview.adapter.diffCallback
import com.open.recyclerview.animations.ItemAnimation
import com.open.recyclerview.decoration.DividerDecoration
import com.open.recyclerview.layoutmanager.WrapContentLinearLayoutManager
import com.open.serialization.JsonClient

class ArticleFragment : BaseFragment(R.layout.article_fragment) {

    private val binding: ArticleFragmentBinding by binding(this)

    private val viewModel: ArticleViewModel by viewModels()

    private val mainViewModel: MainViewModel by applicationViewModels()

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
        rvAdapter.addLifecycleOwner(viewLifecycleOwner)
        binding.rvList.apply {
            layoutManager = WrapContentLinearLayoutManager(context)
//            addItemDecoration(DividerDecoration().inset(20.0f))
//            addItemDecoration(DividerDecoration().stroke(20.0f))
//            addItemDecoration(DividerDecoration().dash(20.0f))
            adapter = rvAdapter
        }
    }

    override fun initData() {
        request(0)
    }

    private fun request(page: Int) {
        WanApiImpl.requestBanner().observe(this) {
            LogUtils.d("Banner:${JsonClient.toJson(it)}")
            if (it.isSuccessful) {
                rvAdapter.submitList(listOf(it.data?.let { bannerList ->
                    ArticleBannerCell(bannerList, viewLifecycleOwner)
                }))
            }

        }

//        WanApiImpl.requestArticleList(page).observe(this) {
//            LogUtils.d("ArticleList:${JsonClient.toJson(it)}")
//        }
    }
}