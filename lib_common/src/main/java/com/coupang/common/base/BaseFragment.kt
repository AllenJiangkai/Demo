package com.coupang.common.base

import android.view.Menu
import android.view.MenuInflater
import androidx.fragment.app.Fragment

/**
 * @author Allen
 * @date 2020-05-27.
 * description：
 */
abstract class BaseFragment : Fragment() {

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        menu.clear()
    }
}