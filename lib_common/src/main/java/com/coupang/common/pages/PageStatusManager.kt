package com.coupang.common.pages

import android.app.Activity
import android.content.Context
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment

/**
 * @author Alan_Xiong
 *
 * @desc: 页面状态管理器
 * @time 2020/6/1 1:58 PM
 */
class PageStatusManager private constructor() {

    val VIEW_NO_ID = 0
    var BASE_VIEW_LOADING = VIEW_NO_ID
    var BASE_VIEW_EMPTY = VIEW_NO_ID
    var BASE_VIEW_RETRY = VIEW_NO_ID

    private var mPageStateLayout: PageStateLayout? = null

    companion object {
        fun newBuilder(): ViewBuilder {
            return ViewBuilder()
        }
    }

    class ViewBuilder {

        var statusManager = PageStatusManager()

        fun setLoadingViewId(layoutId: Int): ViewBuilder {
            statusManager.BASE_VIEW_LOADING = layoutId
            return this
        }

        fun setEmptyViewId(layoutId: Int): ViewBuilder {
            statusManager.BASE_VIEW_EMPTY = layoutId
            return this
        }

        fun setRetryViewId(layoutId: Int): ViewBuilder {
            statusManager.BASE_VIEW_RETRY = layoutId
            return this
        }

        fun build(): PageStatusManager {
            return statusManager
        }
    }

    /**
     * 管理器注册
     * @param windowOrViewObject 需要替换的视图类型
     * activity/fragment/view
     */
    fun register(windowOrViewObject: Any, mListener: OnPageStatusListener) {
        val mContextParent: ViewGroup
        val mContext: Context
        //判断view类型
        when (windowOrViewObject) {
            is Activity -> {
                mContext = windowOrViewObject
                mContextParent = windowOrViewObject.findViewById(android.R.id.content)
            }
            is Fragment -> {
                mContext = windowOrViewObject.context!!
                mContextParent = windowOrViewObject.view?.parent as ViewGroup
            }
            is View -> {
                mContext = windowOrViewObject.context
                mContextParent = windowOrViewObject.parent as ViewGroup
            }
            else -> {
                throw IllegalArgumentException("the argument's type must be Fragment or Activity: init(context)")
            }
        }

        val childCount = mContextParent.childCount
        var index = 0
        val oldContent: View
        if (windowOrViewObject is View) {
            oldContent = windowOrViewObject
            for (i in 0 until childCount) {
                index = i
                break
            }
        } else {
            oldContent = mContextParent.getChildAt(0)
        }
        //移除需要替换的view
        mContextParent.removeView(oldContent)
        //添加view，保持view的宽高和以前的view一致
        val stateLayout = PageStateLayout(context = mContext)
        val lp = oldContent.layoutParams
        mContextParent.addView(stateLayout, index, lp)
        //添加正常布局
        stateLayout.setContentView(oldContent)
        //添加其他状态
        addLoadingLayout(stateLayout)
        addEmptyLayout(stateLayout)
        addRetryLayout(stateLayout)
        //回调
        mListener.setEmptyEvent(stateLayout.getEmptyView())
        mListener.setEmptyEvent(stateLayout.getLoadingView())
        mListener.setRetryEvent(stateLayout.getRetryView())
        mPageStateLayout = stateLayout
    }

    private fun addLoadingLayout(stateLayout: PageStateLayout) {
        if (BASE_VIEW_LOADING != VIEW_NO_ID) {
            stateLayout.setPageView(BASE_VIEW_LOADING, PageEnums.BASE_LOADING)
        }
    }

    private fun addEmptyLayout(stateLayout: PageStateLayout) {
        if (BASE_VIEW_LOADING != VIEW_NO_ID) {
            stateLayout.setPageView(BASE_VIEW_EMPTY, PageEnums.BASE_EMPTY)
        }
    }

    private fun addRetryLayout(stateLayout: PageStateLayout) {
        if (BASE_VIEW_LOADING != VIEW_NO_ID) {
            stateLayout.setPageView(BASE_VIEW_RETRY, PageEnums.BASE_RETRY)
        }
    }

    fun showLoading() {
        mPageStateLayout?.showView(PageEnums.BASE_LOADING)
    }

    fun showRetry() {
        mPageStateLayout?.showView(PageEnums.BASE_RETRY)
    }

    fun showContent() {
        mPageStateLayout?.showView(PageEnums.BASE_CONTENT)
    }

    fun showEmpty() {
        mPageStateLayout?.showView(PageEnums.BASE_EMPTY)
    }


}