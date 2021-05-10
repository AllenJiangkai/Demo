package com.de.danaemas.module.home.widget.dialog

import com.chad.library.adapter.base.entity.MultiItemEntity
import java.io.Serializable

class DialogAdapterItem<T>(private val itemType: Int, var data: T) : MultiItemEntity, Serializable {

    override fun getItemType(): Int {
        return itemType
    }

    companion object {
        const val MESSAGE = 0
        const val SINGLE_BTN = 1
        const val MULTIPLE_BTN = 2
        const val INFO = 3
        const val IMG = 4
    }



}
