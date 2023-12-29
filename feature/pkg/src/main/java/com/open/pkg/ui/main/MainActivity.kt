package com.open.pkg.ui.main

import androidx.activity.viewModels
import androidx.core.content.ContextCompat
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import com.open.base.BaseActivity
import com.open.core.binding.binding
import com.open.pkg.R
import com.open.pkg.databinding.MainActivityBinding


class MainActivity : BaseActivity(R.layout.main_activity) {
    private val binding: MainActivityBinding by binding(this)
    private val viewModel: MainViewModel by viewModels()

    private val tabList by lazy {
        listOf(
            Pair("文章", R.drawable.home_icon),
            Pair("项目", R.drawable.project_icon),
            Pair("我的", R.drawable.mine_icon)
        )
    }

    private val pageAdapter by lazy {
        MainFragmentPagerAdapter(supportFragmentManager, lifecycle)
    }

    override fun initView() {
        binding.viewPager.apply {
            isUserInputEnabled = false
            adapter = pageAdapter
        }
        pageAdapter.setDataList(tabList)
        binding.tabLayout.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab?) {
                tab?.icon?.apply {
                    setTint(ContextCompat.getColor(this@MainActivity, R.color.accent_color))
                }
            }

            override fun onTabUnselected(tab: TabLayout.Tab?) {
                tab?.icon?.apply {
                    setTint(ContextCompat.getColor(this@MainActivity, R.color.panda))
                }
            }

            override fun onTabReselected(tab: TabLayout.Tab?) {

            }

        })
        TabLayoutMediator(binding.tabLayout, binding.viewPager) { tab, position ->
            tab.icon = ContextCompat.getDrawable(this, tabList[position].second)
            tab.text = tabList[position].first
        }.attach()
        binding.tabLayout.getTabAt(0)?.select()
    }

}