package com.mari.uang.module.basic

import android.text.Editable
import android.text.InputType
import android.text.TextUtils
import android.text.TextWatcher
import android.widget.EditText
import android.widget.TextView
import com.mari.uang.R
import com.mari.uang.config.ConstantConfig.INFO_AUTH_ITEM_TYPE_CITY_SELECT
import com.mari.uang.module.basic.BasicInfoAdapterItem.Companion.TYPE_CITY
import com.mari.uang.module.basic.BasicInfoAdapterItem.Companion.TYPE_ENUM
import com.mari.uang.module.basic.BasicInfoAdapterItem.Companion.TYPE_TEXT
import com.mari.uang.module.basic.BasicInfoAdapterItem.Companion.TYPE_TIPS
import com.mari.uang.module.basic.dto.BasicItemInfo
import com.chad.library.adapter.base.BaseMultiItemQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import com.chad.library.adapter.base.entity.MultiItemEntity

/**
 *
 * @ProjectName:    Business
 * @Package:        com.alan.business.module.basic
 * @ClassName:      AuthBasicAdapter
 * @Description:     java类作用描述
 * @Author:         jtao
 * @CreateDate:     2021/4/17 3:55 PM
 * @UpdateUser:     更新者
 * @UpdateDate:     2021/4/17 3:55 PM
 * @UpdateRemark:   更新说明
 * @Version:        1.0
 */
class AuthBasicAdapter(data: MutableList<MultiItemEntity>? = null) : BaseMultiItemQuickAdapter<MultiItemEntity, BaseViewHolder>(data){

    init {
        addItemType(TYPE_TIPS, R.layout.item_basicinfo_tips)
        addItemType(TYPE_ENUM, R.layout.item_basicinfo_enum)
        addItemType(TYPE_TEXT, R.layout.item_basicinfo_text)
        addItemType(TYPE_CITY, R.layout.item_basicinfo_enum)
    }

    override fun convert(helper: BaseViewHolder?, item: MultiItemEntity?) {

        val homeEntity = item as BasicInfoAdapterItem<*>
        when(homeEntity.itemType){
            TYPE_TIPS -> {
                convertTips(helper!!,homeEntity.data as BasicItemInfo)
            }
            TYPE_ENUM -> {
                convertEnum(helper!!,homeEntity.data  as BasicItemInfo)
            }
            TYPE_TEXT -> {
                convertText(helper!!,homeEntity.data  as BasicItemInfo)
            }
            TYPE_CITY -> {
                convertEnum(helper!!,homeEntity.data  as BasicItemInfo)
            }
        }
    }

    private fun convertTips(helper: BaseViewHolder, item: BasicItemInfo){

    }

    private fun convertEnum(baseViewHolder: BaseViewHolder, infoAuthItemEntity: BasicItemInfo){
        baseViewHolder.setText(R.id.tvDes, infoAuthItemEntity.title)
        val tvContent: TextView = baseViewHolder.getView(R.id.tvContent)
        if (!TextUtils.isEmpty(infoAuthItemEntity.subtitle)) tvContent.hint = (
            infoAuthItemEntity.subtitle
        )

        if (!TextUtils.isEmpty(infoAuthItemEntity.value)) {
            if (infoAuthItemEntity.cate.equals(INFO_AUTH_ITEM_TYPE_CITY_SELECT)) {
                baseViewHolder.setText(
                    R.id.tvContent,
                    infoAuthItemEntity.value?.replace("|", "-")
                )
            } else {
                baseViewHolder.setText(R.id.tvContent, infoAuthItemEntity.value)
            }
        }

    }

    private fun convertText(baseViewHolder: BaseViewHolder, infoAuthItemEntity: BasicItemInfo){
        baseViewHolder.setText(R.id.tvDes, infoAuthItemEntity.title)

        val editText: EditText = baseViewHolder.getView(R.id.etContent)
        if ("1" == infoAuthItemEntity.inputType) {
            editText.inputType = InputType.TYPE_CLASS_NUMBER
        } else {
            editText.inputType = InputType.TYPE_CLASS_TEXT
        }


        if (editText.tag != null && editText.tag is TextWatcher) {
            editText.removeTextChangedListener(editText.tag as TextWatcher)
        }

        if (!TextUtils.isEmpty(infoAuthItemEntity.subtitle)) editText.hint = (
            infoAuthItemEntity.subtitle
        )

        if (!TextUtils.isEmpty(infoAuthItemEntity.value)) editText.setText(
            infoAuthItemEntity.value
        )

        val textWatcher: TextWatcher = object : TextWatcher {
            override fun beforeTextChanged(
                s: CharSequence,
                start: Int,
                count: Int,
                after: Int
            ) {
            }

            override fun onTextChanged(
                s: CharSequence,
                start: Int,
                before: Int,
                count: Int
            ) {
            }

            override fun afterTextChanged(editable: Editable) {
                if (TextUtils.isEmpty(editable.toString())) {
                    infoAuthItemEntity.value = ""
                } else {
                    infoAuthItemEntity.value = editable.toString()
                }
            }
        }

        editText.addTextChangedListener(textWatcher)
        editText.tag = textWatcher
    }
}