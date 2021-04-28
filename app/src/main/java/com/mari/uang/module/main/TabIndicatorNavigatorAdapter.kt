package com.mari.uang.module.main

import android.content.Context
import android.graphics.Typeface
import android.view.LayoutInflater
import android.widget.ImageView
import android.widget.TextView
import com.coupang.common.utils.colors
import com.mari.uang.R
import net.lucode.hackware.magicindicator.buildins.commonnavigator.abs.CommonNavigatorAdapter
import net.lucode.hackware.magicindicator.buildins.commonnavigator.abs.IPagerIndicator
import net.lucode.hackware.magicindicator.buildins.commonnavigator.abs.IPagerTitleView
import net.lucode.hackware.magicindicator.buildins.commonnavigator.titles.CommonPagerTitleView

class TabIndicatorNavigatorAdapter(private val tabContentList: List<TabContent>) :
    CommonNavigatorAdapter() {

    var tabIndicatorItemClickCallback: TabIndicatorItemClickCallback? = null

    override fun getTitleView(context: Context, index: Int): IPagerTitleView {
        val pagerTitleView = CommonPagerTitleView(context)
        val customLayout = LayoutInflater.from(context).inflate(R.layout.item_pager_title, null)
        val titleImg = customLayout.findViewById(R.id.title_img) as ImageView
        val titleText = customLayout.findViewById(R.id.title_text) as TextView
        titleImg.setImageResource(tabContentList[index].tabIcon)
        titleText.text = tabContentList[index].tabName

//        if (index == 1) {
//            redPoint.visibility = if (true) {
//                View.VISIBLE
//            } else {
//                View.GONE
//            }
//        }
        pagerTitleView.setContentView(customLayout)
        pagerTitleView.onPagerTitleChangeListener =
            object : CommonPagerTitleView.OnPagerTitleChangeListener {

                override fun onSelected(index: Int, totalCount: Int) {
                    titleImg.isSelected = true
                    titleText.setTextColor(colors(R.color.main_color))
                    titleText.typeface = Typeface.defaultFromStyle(Typeface.BOLD)
                }

                override fun onDeselected(index: Int, totalCount: Int) {
                    titleImg.isSelected = false
                    titleText.setTextColor(colors(R.color.main_color_false))
                    titleText.typeface = Typeface.defaultFromStyle(Typeface.NORMAL)
                }

                override fun onLeave(
                    index: Int,
                    totalCount: Int,
                    leavePercent: Float,
                    leftToRight: Boolean
                ) {
                }

                override fun onEnter(
                    index: Int,
                    totalCount: Int,
                    enterPercent: Float,
                    leftToRight: Boolean
                ) {
                }
            }
        pagerTitleView.setOnClickListener {
            tabIndicatorItemClickCallback?.clickIndicatorItem(index)
        }
        return pagerTitleView
    }

    override fun getCount(): Int {
        return tabContentList.size
    }

    override fun getIndicator(context: Context?): IPagerIndicator? {
        return null
    }
}

interface TabIndicatorItemClickCallback {
    fun clickIndicatorItem(index: Int)
}