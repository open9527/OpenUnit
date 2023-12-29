package com.open.pkg.ui.main

import androidx.activity.viewModels
import androidx.core.content.ContextCompat
import com.google.android.material.tabs.TabLayout
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
    private val icons by lazy {
        listOf(R.drawable.home_icon, R.drawable.project_icon, R.drawable.mine_icon)
    }
    private val fragmentList by lazy {
        listOf(
            ArticleFragment.newInstance("文章"),
            ProjectFragment.newInstance("项目"),
            MineFragment.newInstance("我的")
        )
    }

    private val pageAdapter by lazy {
        MainFragmentPagerAdapter(supportFragmentManager, lifecycle)
    }

    override fun initView() {
        pageAdapter.setFragmentList(fragmentList)
        binding.viewPager.apply {
            isUserInputEnabled = false
            adapter = pageAdapter
        }
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
            tab.icon = ContextCompat.getDrawable(this, icons[position])
            tab.text = titles[position]
        }.attach()
        binding.tabLayout.getTabAt(0)?.select()
    }

}