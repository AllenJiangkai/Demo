package com.de.danaemas.module.home.widget

import android.content.Context
import android.view.View
import android.widget.ViewFlipper
import androidx.annotation.AnimRes
import java.util.*

abstract class NoticeAdapter (var mContext: Context?){

    var mViewFlipper: ViewFlipper? = null
    private var mInAnim = 0
    private  var mOutAnim:Int = 0
    private var mFlipInterval = 0
    var mViewList: MutableList<View>? = null


    open fun setViewFlipper(viewFlipper: ViewFlipper?) {
        mViewFlipper = viewFlipper
    }

    abstract fun getCount(): Int

    open fun setAnimation(@AnimRes inAnim: Int, @AnimRes outAnim: Int) {
        mInAnim = inAnim
        mOutAnim = outAnim
    }

    open fun setFlipInterval(flipInterval: Int) {
        mFlipInterval = flipInterval
    }

    open fun getItemView(pos: Int): View? {
        if (mViewList == null) {
            throw NullPointerException("Filp list view is null")
        }
        return mViewList!![pos]
    }

    abstract fun initView(context: Context?, position: Int): View?

    open fun create() {
        if (mViewList == null) {
            mViewList = ArrayList()
        }
        mViewList!!.clear()
        for (i in 0 until getCount()) {
            val view = initView(mContext, i)
            if (view != null) {
                mViewList!!.add(view)
                mViewFlipper!!.addView(view)
            } else {
                throw NullPointerException("Filp item view is null, postion $i")
            }
        }
        //视图进入动画
        mViewFlipper!!.setInAnimation(mContext, mInAnim)
        //视图退出动画
        mViewFlipper!!.setOutAnimation(mContext, mOutAnim)
        //视图的切换间隔
        mViewFlipper!!.flipInterval = mFlipInterval
        //true 自动开始滚动
        mViewFlipper!!.isAutoStart = mViewList != null && mViewList!!.size > 1
    }
}