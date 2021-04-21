package com.mari.uang.module.order

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter

class OrderFragmentAdapter(fm: FragmentManager, private val contents: List<FragmentTabContent>) :
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