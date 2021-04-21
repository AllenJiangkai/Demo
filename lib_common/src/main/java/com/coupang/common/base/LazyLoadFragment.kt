package com.coupang.common.base

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.LayoutRes

/**
 * @author Allen
 * @date 2020-05-27.
 * description：
 */
abstract class LazyLoadFragment : BaseFragment() {

    private var isVisibleOfCurrentFragment = true
    private var isPrepareView = false
    private var isInitData = false

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(getLayoutId(), null)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        isPrepareView = true
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        initUIViews()
        lazyInitData()
    }

    override fun setUserVisibleHint(isVisibleToUser: Boolean) {
        super.setUserVisibleHint(isVisibleToUser)
        isVisibleOfCurrentFragment = isVisibleToUser

        if (!isVisibleToUser) {
            currentFragmentPause()
        }else{
            currentFragmentVisibleToUser()
        }
        lazyInitData()
    }

    override fun onResume() {
        super.onResume()
        if (!isInitData) {
            lazyInitData()
        }
    }

    override fun onPause() {
        super.onPause()
        if (isInitData&&isNeedOnPauseReloadData()) {
            isInitData = false
        }
        currentFragmentPause()
    }

    private fun lazyInitData() {

        if (isPrepareView && isVisibleOfCurrentFragment && (!isInitData || isNeedReloadData())) {
            isInitData = true
            loadData()

        }
    }

    /**
     * @return fragment再次可见时，是否重新请求数据，默认为False则只请求一次数据
     * */
    open fun isNeedReloadData() = false
    open fun isNeedOnPauseReloadData() = true

    @LayoutRes
    abstract fun getLayoutId(): Int

    abstract fun initUIViews()

    abstract fun loadData()


    open fun currentFragmentPause() {

    }

     open fun currentFragmentVisibleToUser() {}


}