package com.mari.uang.widget

import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.widget.RelativeLayout
import com.mari.uang.R
import com.coupang.common.extentions.visible
import kotlinx.android.synthetic.main.widget_common_title_bar.view.*

class TitleBarView : RelativeLayout {

    private lateinit var clickListener: () -> Unit
    fun onClickBackListener(callback: () -> Unit) {
        this.clickListener = callback
    }
    private lateinit var clickRightListener: () -> Unit

    fun onClickRightListener(callback: () -> Unit) {
        this.clickRightListener = callback
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
            R.layout.widget_common_title_bar,
            this@TitleBarView
        )
        rl_back.setOnClickListener {
            if (::clickListener.isInitialized) {
                clickListener()
            }
        }

        ivRight.setOnClickListener {
            if (::clickRightListener.isInitialized) {
                clickRightListener()
            }
        }
    }

    fun setTitle(title: String?) {
        tv_title.text = title?:""
    }
    fun setRightImage(src: Int) {
        ivRight.setImageResource(src)
        ivRight.visible()
    }



}