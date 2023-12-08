package com.open.pkg.ui.main

import androidx.activity.viewModels
import com.google.android.material.tabs.TabLayoutMediator
import com.open.base.BaseActivity
import com.open.core.binding.binding
import com.open.pkg.R
import com.open.pkg.databinding.MainActivityBinding
import com.open.pkg.ui.article.ArticleFragment
import com.open.pkg.ui.mine.MineFragment
import com.open.pkg.ui.project.ProjectFragment

class MainActivity : BaseActivity(R.layout.main_activity) {
    private val binding: MainActivityBinding by binding(this)

    private val viewModel: MainViewModel by viewModels()


    private val titles by lazy {
        listOf("文章", "项目", "我的")
    }
    private val fragmentList by lazy {
        listOf(ArticleFragment(), ProjectFragment(), MineFragment())
    }

    private val adapter by lazy {
        MainFragmentPagerAdapter(supportFragmentManager, lifecycle)
    }

    override fun initView() {
        adapter.setFragmentList(fragmentList)
        binding.viewPager.adapter = adapter
        TabLayoutMediator(binding.tabLayout, binding.viewPager) { tab, position ->
            tab.text = titles[position]
        }.attach()
    }

}