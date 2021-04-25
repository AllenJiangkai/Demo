package com.mari.uang.module.home.widget

import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.widget.RelativeLayout
import com.mari.uang.R
import com.mari.uang.module.home.dto.CardInfo
import com.mari.uang.util.TypefaceUtil.setTextTypeface
import kotlinx.android.synthetic.main.widget_small_card_view.view.*

class SmallCardView : RelativeLayout {

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
        inflateView = View.inflate(
            context,
            R.layout.widget_small_card_view,
            this@SmallCardView
        )
//        bt_submit.setOnClickListener {
//            if (::clickListener.isInitialized) {
//                clickListener()
//            }
//        }
    }

    fun initData(bean : CardInfo){
        setTextTypeface(tv_price)
        bean.apply {
            tv_price.text=amountRange
            tv_price_desc.text=amountRangeDes
            tv_term.text=termInfo
            tv_term_desc.text=termInfoDes
            tv_rate.text=loanRate
            tv_rate_desc.text=loanRateDes
            bt_submit.text= buttonText
        }

    }

}