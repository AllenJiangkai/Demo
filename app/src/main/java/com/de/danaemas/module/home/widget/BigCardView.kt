package com.de.danaemas.module.home.widget

import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.widget.RelativeLayout
import com.de.danaemas.R
import com.de.danaemas.module.home.dto.CardInfo
import com.de.danaemas.util.TypefaceUtil.setTextTypeface
import kotlinx.android.synthetic.main.widget_big_card_view.view.*

class BigCardView : RelativeLayout {

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
            R.layout.widget_big_card_view,
            this@BigCardView
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