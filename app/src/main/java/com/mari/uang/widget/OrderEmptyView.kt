package com.mari.uang.widget

import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.widget.RelativeLayout
import com.mari.uang.R
import kotlinx.android.synthetic.main.widget_order_empty.view.*

class OrderEmptyView : RelativeLayout {

    private lateinit var clickListener: () -> Unit
    fun onClickButtonListener(callback: () -> Unit) {
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
        inflateView = View.inflate(context, R.layout.widget_order_empty, this@OrderEmptyView)
        bt_submit.setOnClickListener {
            if(::clickListener.isInitialized){
                clickListener()
            }
        }
    }


}