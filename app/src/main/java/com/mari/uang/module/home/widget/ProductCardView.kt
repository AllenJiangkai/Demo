package com.mari.uang.module.home.widget

import android.content.Context
import android.text.TextUtils
import android.util.AttributeSet
import android.view.View
import android.widget.RelativeLayout
import com.mari.uang.util.GlideLoadUtils.loadImage
import com.mari.uang.R
import com.mari.uang.module.home.dto.ProductInfo
import com.mari.uang.util.TypefaceUtil.setTextTypeface
import kotlinx.android.synthetic.main.widget_product_card_view.view.*

class ProductCardView : RelativeLayout {

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
            R.layout.widget_product_card_view,
            this@ProductCardView
        )

//        setOnClickListener {
//            if (::clickListener.isInitialized) {
//                clickListener()
//            }
//        }
    }

    fun initData(bean: ProductInfo){
        setTextTypeface(tv_price)
        bean.apply {
            loadImage(context, productLogo, img_icon)
            tv_product_name.text=productName
            bt_right.text=buttonText
            tv_price.text=amountRange
            tv_rate.text=loanRateDes
            tv_amount.text=amountRangeDes
            tv_product.text=productDesc
            tv_tips.text=tagsFormat(productTags)
            if (!TextUtils.isEmpty(buttoncolor)) {
                when (buttoncolor) {
                    "yellow" -> bt_right.setBackgroundResource(R.drawable.bg_product_button_main)
                    "red" -> bt_right.setBackgroundResource(R.drawable.bg_product_button_red)
                    "grey" -> bt_right.setBackgroundResource(R.drawable.bg_product_button_ccc)
                }
            }
        }
    }

    private fun tagsFormat(productTags: List<String>?): String? {
        var formatStr = ""
        if (productTags == null || productTags.isEmpty()) {
            return formatStr
        }
        for (i in productTags.indices) {
            formatStr += productTags[i] + if (i == productTags.size - 1) "" else " / "
        }
        return formatStr
    }

}