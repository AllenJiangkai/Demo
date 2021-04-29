package com.mari.uang.widget

import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.widget.RelativeLayout
import com.mari.uang.R
import com.mari.uang.util.GlideLoadUtils
import kotlinx.android.synthetic.main.widget_home_title_bar.view.*

class HomeTitleBarView : RelativeLayout {

    private lateinit var clickListener: () -> Unit
    fun onClickRightListener(callback: () -> Unit) {
        this.clickListener = callback
    }


    private lateinit var inflateView: View

    constructor(context: Context) : super(context) {
        initView()
    }

    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs) {
        initView()
    }

    private fun initView() {
        inflateView = View.inflate(
            context,
            R.layout.widget_home_title_bar,
            this@HomeTitleBarView
        )
        img_right.setOnClickListener {
            if (::clickListener.isInitialized) {
                clickListener()
            }
        }
    }

    fun initImage(url: String?) {
        GlideLoadUtils.loadImage(context,url,img_right)
    }


}