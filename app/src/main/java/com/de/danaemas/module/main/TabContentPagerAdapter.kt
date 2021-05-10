package com.de.danaemas.module.main

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter

class TabContentPagerAdapter(fm: FragmentManager, private val contents: List<TabContent>) :
        FragmentPagerAdapter(fm) {

    override fun getItem(position: Int): Fragment {
        return contents[position].fragment
    }

    override fun getCount(): Int {
        return contents.size
    }

    override fun getPageTitle(position: Int): CharSequence? {
        return contents[position].tabName
    }
}