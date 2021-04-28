package com.mari.uang.module.order

import android.widget.ImageView
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import com.coupang.common.utils.GlideLoadUtils
import com.mari.uang.R

class OrderListAdapter(layoutResId: Int) : BaseQuickAdapter<OrderInfo, BaseViewHolder>(layoutResId) {

    override fun convert(helper: BaseViewHolder, item:OrderInfo) {
        helper.apply {
            addOnClickListener(R.id.rl_main)
            GlideLoadUtils.loadImage(mContext,item.productLogo,getView<ImageView>(R.id.image))
            setText(R.id.tv_product_name,item.productName)
            setText(R.id.tv_right,item.buttonText)
            setText(R.id.tv_amount_value,item.orderAmount)
            setText(R.id.tv_term_value,item.term)
            setText(R.id.tv_time_value,item.loanTime)
            setText(R.id.tv_repay_time_value,item.repayTime)
        }
    }

}