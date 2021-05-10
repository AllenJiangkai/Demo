package com.de.danaemas.module.order

import android.content.Context
import com.coupang.common.base.BaseSimpleActivity
import com.coupang.common.utils.dip2pxOfFloat
import com.coupang.common.utils.setStatusBarTextColor
import com.coupang.common.utils.strings
import com.de.danaemas.R
import kotlinx.android.synthetic.main.activity_order.*
import net.lucode.hackware.magicindicator.ViewPagerHelper
import net.lucode.hackware.magicindicator.buildins.commonnavigator.CommonNavigator
import net.lucode.hackware.magicindicator.buildins.commonnavigator.abs.CommonNavigatorAdapter
import net.lucode.hackware.magicindicator.buildins.commonnavigator.abs.IPagerIndicator
import net.lucode.hackware.magicindicator.buildins.commonnavigator.abs.IPagerTitleView
import net.lucode.hackware.magicindicator.buildins.commonnavigator.indicators.LinePagerIndicator

class OrderActivity : BaseSimpleActivity() {

    companion object {
        const val BELUM_SELESAI = "1"
        const val DANA_CAIR = "2"
        const val SUDAH_LUNAS = "3"
    }

    private val fragmentList: List<FragmentTabContent> by lazy {
        listOf(
            FragmentTabContent(strings(R.string.order_status_semua), OrderFragment.newInstance(4)),
            FragmentTabContent(strings(R.string.order_status_belum), OrderFragment.newInstance(7)),
            FragmentTabContent(strings(R.string.order_status_dana), OrderFragment.newInstance(6)),
            FragmentTabContent(strings(R.string.order_status_sudah), OrderFragment.newInstance(5))
        )
    }

    override fun getLayoutId(): Int {
        return R.layout.activity_order
    }

    override fun onResume() {
        super.onResume()
        setStatusBarTextColor(window, true)
    }

    override fun initView() {
        title_bar.apply {
            setTitle(strings(R.string.title_order))
            onClickBackListener {
                finish()
            }

        }
        initTabLayout()
    }

    private fun initTabLayout() {
        tabContainerView.adapter = OrderFragmentAdapter(supportFragmentManager, fragmentList)
        indicator.navigator = getIndicatorNavigator()
        tabContainerView.offscreenPageLimit = 4
        ViewPagerHelper.bind(indicator, tabContainerView)
        var index = intent.getStringExtra("tab").toInt()
        tabContainerView.currentItem = index
    }

    private fun getIndicatorNavigator() = CommonNavigator(this).also { navigator ->
        navigator.isAdjustMode = false

        navigator.adapter = object : CommonNavigatorAdapter() {

            override fun getTitleView(context: Context?, index: Int): IPagerTitleView {
                val simplePagerTitleView = ColorFlipPagerTitleView(context)
                simplePagerTitleView.text = fragmentList[index].tabName
                simplePagerTitleView.textSize = dip2pxOfFloat(6f)
                simplePagerTitleView.normalColor = getColor(R.color.color_333333)
                simplePagerTitleView.selectedColor = getColor(R.color.main_color)
                simplePagerTitleView.setOnClickListener {
                    tabContainerView.currentItem = index
                }
                return simplePagerTitleView
            }

            override fun getCount(): Int {
                return fragmentList.size
            }

            override fun getIndicator(context: Context?): IPagerIndicator? {
                val indicator = LinePagerIndicator(context)
                indicator.mode = LinePagerIndicator.MODE_MATCH_EDGE
                indicator.lineHeight = dip2pxOfFloat(5f)
//                indicator.lineWidth = dip2pxOfFloat(15f)
//                indicator.roundRadius = dip2pxOfFloat(1.5f)
//                indicator.startInterpolator = AccelerateInterpolator()
////                indicator.yOffset = dip2pxOfFloat(4f)
//                indicator.endInterpolator = DecelerateInterpolator(2.0f)
                indicator.setColors(getColor(R.color.main_color))
                return indicator
            }

            override fun getTitleWeight(context: Context?, index: Int): Float {
                return super.getTitleWeight(context, index)
            }

        }
    }

    override fun registerObserver() {

    }

    override fun initData() {

    }


}