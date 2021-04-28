package com.mari.uang.module.main

import android.content.Intent
import androidx.viewpager.widget.ViewPager
import com.coupang.common.base.BaseSimpleActivity
import com.coupang.common.user.UserManager.isLogin
import com.coupang.common.utils.setStatusBarTextColor
import com.coupang.common.utils.strings
import com.mari.uang.R
import com.mari.uang.module.home.HomeFragment
import com.mari.uang.module.login.LoginActivity
import com.mari.uang.module.profile.ProfileFragment
import kotlinx.android.synthetic.main.activity_main.*
import net.lucode.hackware.magicindicator.ViewPagerHelper
import net.lucode.hackware.magicindicator.buildins.commonnavigator.CommonNavigator

class MainActivity : BaseSimpleActivity() {

    private val navigator by lazy { getBottomIndicatorNavigator() }
    private val tabContentList: List<TabContent> by lazy {
        listOf(
            TabContent(
                strings(R.string.first_fragment_label),
                R.drawable.selector_tab_icon_first,
                HomeFragment.newInstance(),
                false
            ), TabContent(
                strings(R.string.second_fragment_label),
                R.drawable.selector_tab_icon_second,
                ProfileFragment.newInstance(),
                false
            )
        )
    }


    private fun initIntent() {
    }

    override fun onNewIntent(intent: Intent?) {
        super.onNewIntent(intent)
        var index = intent?.getIntExtra("index", 0) ?: 0
        switchTab(index)
    }

    override fun onResume() {
        super.onResume()
        setStatusBarTextColor(window, false)
    }

    override fun getLayoutId(): Int {
        return R.layout.activity_main
    }

    override fun initView() {
        initIntent()
//
//        tv_test.setOnClickListener {
//            viewModel.login()
//        }

        initTabView()
    }

    override fun initData() {

    }


    private fun switchTab(tabIndex: Int) {
        if (tabContentList.isEmpty()) {
            return
        }
        if (tabIndex >= 0 && tabIndex < tabContentList.size) {
            tabContainerView.setCurrentItem(tabIndex, true)
        }

    }

    override fun registerObserver() {

//        registerLiveDataCommonObserver(viewModel)

    }

    private fun initTabView() {
        tabContainerView.supportScroll = false
        tabContainerView.offscreenPageLimit = 2
        tabContainerView.adapter = TabContentPagerAdapter(supportFragmentManager, tabContentList)
        bottomIndicator.navigator = navigator
        ViewPagerHelper.bind(bottomIndicator, tabContainerView)
        tabContainerView.addOnPageChangeListener(object : ViewPager.SimpleOnPageChangeListener() {
            override fun onPageSelected(position: Int) {
                setStatusBarTextColor(window, tabContentList[position].isLightStatusBar)
            }
        })
    }

    private fun getBottomIndicatorNavigator() = CommonNavigator(this).also { navigator ->
        navigator.isAdjustMode = true
        navigator.adapter = TabIndicatorNavigatorAdapter(tabContentList).also { indicatorAdapter ->
            indicatorAdapter.tabIndicatorItemClickCallback =
                object : TabIndicatorItemClickCallback {
                    override fun clickIndicatorItem(index: Int) {
                        if (index == 1 && !isLogin()) {
                            goLoginActivity()
                            return
                        }
                        this@MainActivity.tabContainerView.setCurrentItem(index, false)
                    }
                }
        }

    }

    private fun goLoginActivity() {
        val intent = Intent(
            this@MainActivity,
            LoginActivity::class.java
        )
        startActivity(intent)
    }


}
