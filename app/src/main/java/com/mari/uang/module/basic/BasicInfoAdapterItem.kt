package com.mari.uang.module.basic

import com.chad.library.adapter.base.entity.MultiItemEntity
import java.io.Serializable

class BasicInfoAdapterItem<T>(private val itemType: Int, var data: T) : MultiItemEntity, Serializable {

    override fun getItemType(): Int {
        return itemType
    }

    companion object {
        const val TYPE_TIPS = 1
        const val TYPE_ENUM = 2
        const val TYPE_TEXT = 3
        const val TYPE_CITY = 4
    }


}
