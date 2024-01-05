package com.open.pkg.ui.main

import android.widget.ImageView
import android.widget.TextView
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
            Pair("导航", R.drawable.navi_icon),
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
            offscreenPageLimit = tabList.size
            adapter = pageAdapter
        }
        pageAdapter.setDataList(tabList)
        binding.tabLayout.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab?) {
                tab?.customView?.apply {
                    findViewById<ImageView>(R.id.iv_tab_icon).drawable.setTint(
                        ContextCompat.getColor(
                            this@MainActivity,
                            R.color.accent_color
                        )
                    )
                    findViewById<TextView>(R.id.tv_tab_title).apply {
                        setTextColor(
                            ContextCompat.getColor(
                                this@MainActivity,
                                R.color.accent_color
                            )
                        )
                    }
                }

            }

            override fun onTabUnselected(tab: TabLayout.Tab?) {
                tab?.customView?.apply {
                    findViewById<ImageView>(R.id.iv_tab_icon).drawable.setTint(
                        ContextCompat.getColor(
                            this@MainActivity,
                            R.color.panda
                        )
                    )
                    findViewById<TextView>(R.id.tv_tab_title).apply {
                        setTextColor(ContextCompat.getColor(this@MainActivity, R.color.panda))
                    }
                }
            }

            override fun onTabReselected(tab: TabLayout.Tab?) {

            }

        })
        TabLayoutMediator(binding.tabLayout, binding.viewPager) { tab, position ->
            tab.setCustomView(R.layout.main_tab_item)
            tab.customView?.apply {
                findViewById<ImageView>(R.id.iv_tab_icon).apply {
                    setImageDrawable(
                        ContextCompat.getDrawable(
                            this@MainActivity,
                            tabList[position].second
                        )
                    )
                }.drawable.setTint(ContextCompat.getColor(this@MainActivity, R.color.panda))
                findViewById<TextView>(R.id.tv_tab_title).apply {
                    text = tabList[position].first
                    setTextColor(ContextCompat.getColor(this@MainActivity, R.color.panda))
                }
            }
            if (0 == position) {
                tab.select()
            }
        }.attach()
    }

}