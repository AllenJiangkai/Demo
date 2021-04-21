package com.mari.uang.module.product

import android.widget.ImageView
import com.mari.uang.R
import com.mari.uang.module.product.dto.VerifyInfo
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import com.coupang.common.utils.GlideLoadUtils.loadImage

class ProductAdapter(layoutResId: Int) : BaseQuickAdapter<VerifyInfo, BaseViewHolder>(layoutResId) {

    override fun convert(helper: BaseViewHolder, item: VerifyInfo) {
        helper.apply {
           loadImage(mContext, item.log, getView<ImageView>(R.id.img_icon))
            if(!item.title.isNullOrEmpty()){
                setText(R.id.tv_product, item.title?.replace(" ", "\n"))
            }
            getView<ImageView>(R.id.img_status).isSelected=isPast(item.status)

        }

    }

    private fun isPast(status:String?): Boolean {
        return status != null && status == "1"
    }
}