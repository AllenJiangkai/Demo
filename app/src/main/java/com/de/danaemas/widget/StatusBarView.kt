package com.de.danaemas.widget

import android.content.Context
import android.content.res.Resources
import android.util.AttributeSet
import android.view.View
import android.widget.RelativeLayout
import com.de.danaemas.R
import kotlinx.android.synthetic.main.widget_status_bar.view.*

class StatusBarView : RelativeLayout {

    private lateinit var clickListener: () -> Unit
    fun onClickBackListener(callback: () -> Unit) {
        this.clickListener = callback
    }

    private lateinit var inflateView: View

    constructor(context: Context) : super(context) {
        initView()
    }

    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs) {
        initView()
        initStatusView()
    }

    private fun initView() {
        inflateView = View.inflate(
            context,
            R.layout.widget_status_bar,
            this@StatusBarView
        )
        initStatusView()
    }


    private fun initStatusView() {
        id_status.apply {
            layoutParams.height = getStatusBarHeight()
        }
    }

    private fun getStatusBarHeight(): Int {
        val resources: Resources = context.resources
        val resourceId: Int = resources.getIdentifier("status_bar_height", "dimen", "android")
        return resources.getDimensionPixelSize(resourceId)
    }

    fun setStatusViewBackground(drawable: Int){
        id_status.setBackgroundResource(drawable)
    }
}