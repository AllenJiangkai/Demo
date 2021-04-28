package com.mari.uang.module.profile

import android.widget.ImageView
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import com.coupang.common.utils.GlideLoadUtils
import com.mari.uang.R

class ProfileItemAdapter(layoutResId: Int) : BaseQuickAdapter<ItemInfo, BaseViewHolder>(layoutResId) {

    override fun convert(helper: BaseViewHolder, item:ItemInfo) {
        item.also {
            GlideLoadUtils.loadImage(mContext, it.iconUrl, helper.getView<ImageView>(R.id.img_icon))
            helper.setText(R.id.tv_name,it.title)
        }
    }


}