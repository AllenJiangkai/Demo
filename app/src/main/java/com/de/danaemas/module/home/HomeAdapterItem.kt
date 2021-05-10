package com.de.danaemas.module.home

import com.chad.library.adapter.base.entity.MultiItemEntity
import java.io.Serializable

class HomeAdapterItem<T>(private val itemType: Int, var data: T) : MultiItemEntity, Serializable {

    override fun getItemType(): Int {
        return itemType
    }

    companion object {
        const val TYPE_RECOMMEND_BANNER = 1
        const val TYPE_RECOMMEND_BIG_CARD = 2
        const val TYPE_RECOMMEND_SMALL_CARD = 3
        const val TYPE_RECOMMEND_PRODUCT_CARD = 4
        const val TYPE_RECOMMEND_NOTICE = 5
        const val TYPE_RECOMMEND_REPAY_NOTICE = 6
    }

}
