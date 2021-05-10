package com.de.danaemas.module.basic

import android.text.TextUtils
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.chad.library.adapter.base.entity.MultiItemEntity
import com.coupang.common.base.BaseViewModel
import com.coupang.common.network.Result
import com.coupang.common.user.UserManager
import com.coupang.common.utils.strings
import com.de.danaemas.R
import com.de.danaemas.config.ConstantConfig.INFO_AUTH_ITEM_TYPE_CITY_SELECT
import com.de.danaemas.config.ConstantConfig.INFO_AUTH_ITEM_TYPE_ENUM
import com.de.danaemas.config.ConstantConfig.INFO_AUTH_ITEM_TYPE_TIP
import com.de.danaemas.config.ConstantConfig.INFO_AUTH_ITEM_TYPE_TXT
import com.de.danaemas.event.ActionEnum
import com.de.danaemas.event.ActionUtil
import com.de.danaemas.module.basic.dto.BasicItemInfo
import com.de.danaemas.module.basic.dto.BasicItemInfoList
import com.de.danaemas.module.contact.dto.NameTypeInfo
import kotlinx.coroutines.launch
import java.util.*
import kotlin.collections.ArrayList

/**
 *
 * @ProjectName:    Business
 * @Package:        com.alan.business.module.basic
 * @ClassName:      BasicInfoModel
 * @Description:     java类作用描述
 * @Author:         jtao
 * @CreateDate:     2021/4/17 2:08 PM
 * @UpdateUser:     更新者
 * @UpdateDate:     2021/4/17 2:08 PM
 * @UpdateRemark:   更新说明
 * @Version:        1.0
 */
class BasicInfoModel : BaseViewModel<BasicRepository>() {

    val dataList = MutableLiveData<ArrayList<MultiItemEntity>>()
    val saveResult = MutableLiveData<Boolean>()

    override fun createRepository(): BasicRepository {
        return BasicRepository()
    }


    fun getData(taskType: String?, productId: String?) {
        val map:  MutableMap<String, Any> = HashMap()
        map["mobile"] = UserManager.username
        showLoading()
        viewModelScope.launch {
            when (taskType) {
                "bank" -> {
                    map.clear()
                    map["productId"] = productId ?: ""
                    var result = repository.getBankCardInfo(map)
                    if (result is Result.Success) {
                        dataList.value = convertUI(result.data)
                    }
                }
                "job" -> {
                    map["product_id"] = productId ?: ""
                    var result = repository.getJobInfo(map)
                    if (result is Result.Success) {
                        dataList.value = convertUI(result.data)
                    }
                }
                "personal" -> {
                    map["product_id"] = productId ?: ""
                    var result = repository.getPersonalAuthInfo(map)
                    if (result is Result.Success) {
                        dataList.value = convertUI(result.data)
                    }
                }
            }
            hideLoading()
        }
    }


    fun saveInfoData(
        dataList: ArrayList<BasicItemInfo>?,
        taskType: String?,
        productId: String?,
        startTime: Long?
    ) {
        val map = dataListToValueMap(dataList)
        for (key in map!!.keys) {
            if (TextUtils.isEmpty(map[key].toString())) {
                showToast.value = strings(R.string.toast_incomplete_information)
                return
            }
        }
        map["product_id"] = productId ?: ""
        viewModelScope.launch {

            when (taskType) {
                "bank" -> {
                    var result = repository.saveBankInfo(map)
                    if (result is Result.Success) {
                        saveResult.value = true
                    }else if(result is Result.GeneralError){
                        showToast.value=result.message
                    }
                    ActionUtil.actionRecord(ActionEnum.BankCard, productId, startTime?:0)
                }
                "job" -> {
                    var result = repository.saveJobInfo(map)
                    if (result is Result.Success) {
                        saveResult.value = true
                    }else if(result is Result.GeneralError){
                        showToast.value=result.message
                    }
                    ActionUtil.actionRecord(ActionEnum.Job, productId, startTime?:0)
                }
                "personal" -> {
                    var result = repository.savePersonalAuthInfo(map)
                    if (result is Result.Success) {
                        saveResult.value = true
                    }else if(result is Result.GeneralError){

                        showToast.value=result.message
                    }
                    ActionUtil.actionRecord(ActionEnum.Personal, productId, startTime?:0)
                }
            }
            hideLoading()
        }
    }

    private fun convertUI(list: BasicItemInfoList): ArrayList<MultiItemEntity> {
        val result = ArrayList<MultiItemEntity>()
        list.items?.let {
            it.forEach { item ->
                when (item.cate) {
                    INFO_AUTH_ITEM_TYPE_ENUM -> {
                        result.add(
                            BasicInfoAdapterItem(
                                BasicInfoAdapterItem.TYPE_CITY,
                                item
                            )
                        )
                    }
                    INFO_AUTH_ITEM_TYPE_CITY_SELECT -> {
                        result.add(
                            BasicInfoAdapterItem(
                                BasicInfoAdapterItem.TYPE_CITY,
                                item
                            )
                        )
                    }
                    INFO_AUTH_ITEM_TYPE_TIP -> {
                        result.add(
                            BasicInfoAdapterItem(
                                BasicInfoAdapterItem.TYPE_TIPS,
                                item
                            )
                        )
                    }
                    INFO_AUTH_ITEM_TYPE_TXT -> {
                        result.add(
                            BasicInfoAdapterItem(
                                BasicInfoAdapterItem.TYPE_TEXT,
                                item
                            )
                        )
                    }
                }
            }
        }
        return result
    }

    private fun dataListToValueMap(dataList: List<BasicItemInfo>?): MutableMap<String, Any>? {
        val map: MutableMap<String, Any> =
            HashMap()
        if (dataList == null) return map
        for (i in dataList.indices) {
            val bean: BasicItemInfo = dataList[i]
            if (bean.cate.equals("tip")) continue
            if (bean.cate.equals("enum")) {
                map[bean.code!!] = nameToType(bean.note,bean.value)
            } else {
                if (bean.cate.equals(INFO_AUTH_ITEM_TYPE_CITY_SELECT)) {
                    var value=bean.value?.replace("-", "|")
                    map[bean.code?:""] =  value?:""
                } else {
                    map[bean.code?:""] = bean.value?:""
                }
            }
        }
        return map
    }

    private fun nameToType(notes: ArrayList<NameTypeInfo>?, name: String?) : String {
        var type = ""
        notes?.let { it ->
            it.forEach{
                if (it.name == name){
                    type = it.type?:""
                }
            }
        }
        return type
    }
}