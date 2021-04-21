package com.coupang.common.pages

import android.content.Context
import android.os.Looper
import android.util.AttributeSet
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.widget.FrameLayout

/**
 * @author Alan_Xiong
 *
 * @desc: 状态pageView的layout，页面状态可按需添加
 * @time 2020/6/1 11:51 AM
 */
class PageStateLayout : FrameLayout {

    private val TAG: String = PageStateLayout::class.java.simpleName
    private var mInflater: LayoutInflater? = null

    private var mLoadingView: View? = null //加载的loading
    private var mEmptyView: View? = null //空数据
    private var mRetryView: View? = null //网路重试
    private var mContentView: View? = null //正常view

    constructor(context: Context) : this(context, null)

    constructor(context: Context, attributeSet: AttributeSet?) : this(context, attributeSet, -1)

    constructor(context: Context, attributeSet: AttributeSet?, defStyleAttr: Int) : super(
        context,
        attributeSet,
        defStyleAttr
    ) {
        mInflater = LayoutInflater.from(context)
    }


    fun isMainThread(): Boolean {
        return Looper.myLooper() == Looper.getMainLooper()
    }

    /**
     * @param enums 需要展示的view枚举
     * 根据view 的类型展示
     * 其他的view做隐藏处理
     */
    private fun showViewByEnums(enums: PageEnums) {
        when (enums) {
            PageEnums.BASE_LOADING -> {
                mLoadingView?.visibility = View.VISIBLE
                if (mRetryView != null) {
                    mRetryView?.visibility = View.GONE
                }
                if (mEmptyView != null) {
                    mEmptyView?.visibility = View.GONE
                }
                if (mContentView != null) {
                    mContentView?.visibility = View.GONE
                }
            }
            PageEnums.BASE_EMPTY -> {
                mEmptyView?.visibility = View.VISIBLE
                if (mLoadingView != null) {
                    mLoadingView?.visibility = View.GONE
                }
                if (mRetryView != null) {
                    mRetryView?.visibility = View.GONE
                }
                if (mContentView != null) {
                    mContentView?.visibility = View.GONE
                }
            }
            PageEnums.BASE_RETRY -> {
                mRetryView?.visibility = View.VISIBLE
                if (mLoadingView != null) {
                    mLoadingView?.visibility = View.GONE
                }
                if (mEmptyView != null) {
                    mEmptyView?.visibility = View.GONE
                }
                if (mContentView != null) {
                    mContentView?.visibility = View.GONE
                }
            }
            PageEnums.BASE_CONTENT -> {
                mContentView?.visibility = View.VISIBLE
                if (mLoadingView != null) {
                    mLoadingView?.visibility = View.GONE
                }
                if (mEmptyView != null) {
                    mEmptyView?.visibility = View.GONE
                }
                if (mRetryView != null) {
                    mRetryView?.visibility = View.GONE
                }
            }
        }
    }


    /**
     * @param enums 根据枚举展示需要的view
     */
    fun showView(enums: PageEnums) {
        if (isMainThread()) {
            showViewByEnums(enums)
        } else {
            post(Runnable {
                kotlin.run {
                    showViewByEnums(enums)
                }
            })
        }
    }

    /**
     * @param layoutId 布局id
     * @param enums 布局枚举类型
     * 设置目标view进行加载
     */
    fun setPageView(layoutId: Int, enums: PageEnums): View? {
        return when (enums) {
            PageEnums.BASE_LOADING -> {
                setLoadingView(mInflater!!.inflate(layoutId, this, false))
            }
            PageEnums.BASE_EMPTY -> {
                setEmptyView(mInflater!!.inflate(layoutId, this, false))
            }
            PageEnums.BASE_CONTENT -> {
                setContentView(mInflater!!.inflate(layoutId, this, false))
            }
            PageEnums.BASE_RETRY -> {
                setRetryView(mInflater!!.inflate(layoutId, this, false))
            }
        }

    }

    /**
     * 设置view 视图
     */
    fun setLoadingView(view: View): View? {
        val loadingView = mLoadingView
        if (loadingView != null) {
            Log.w(TAG, "you have already set a loading view and would be instead of this new one.")
        }
        removeView(loadingView)
        addView(view)
        mLoadingView = view
        return mLoadingView
    }

    fun setEmptyView(view: View): View? {
        val emptyView = mEmptyView
        if (emptyView != null) {
            Log.w(TAG, "you have already set a empty view and would be instead of this new one.")
        }
        removeView(emptyView)
        addView(view)
        mEmptyView = view
        return mEmptyView
    }

    fun setRetryView(view: View): View? {
        val retryView = mRetryView
        if (retryView != null) {
            Log.w(TAG, "you have already set a retry view and would be instead of this new one.")
        }
        removeView(retryView)
        addView(view)
        mRetryView = view
        return mRetryView
    }

    fun setContentView(view: View): View? {
        val contentView = mContentView
        if (contentView != null) {
            Log.w(TAG, "you have already set a retry view and would be instead of this new one.")
        }
        removeView(contentView)
        addView(view)
        mContentView = view
        return mContentView
    }

    fun getRetryView(): View? {
        return mRetryView
    }

    fun getLoadingView(): View? {
        return mLoadingView
    }

    fun getContentView(): View? {
        return mContentView
    }

    fun getEmptyView(): View? {
        return mEmptyView
    }

}