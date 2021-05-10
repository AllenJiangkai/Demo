package com.de.danaemas.event

import android.annotation.SuppressLint
import android.location.Location
import com.aitime.android.deviceid.DeviceIdentifier
import com.coupang.common.network.ParameterTool
import com.coupang.common.utils.ContextUtils
import com.coupang.common.utils.spf.SpConfig.location_latitude
import com.coupang.common.utils.spf.SpConfig.location_longitude
import com.de.danaemas.AppApi
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.util.*
import java.util.concurrent.TimeUnit

object ActionUtil {


    /**
     * 埋点数据统计
     * @param actionEnum
     * @param productId
     * @param startTime
     */
    fun actionRecord(actionEnum: ActionEnum, productId: String?, startTime: Long) {
        actionRecord(actionEnum, productId, startTime, System.currentTimeMillis())
    }

    /**
     * 埋点数据统计
     * @param sakuActionEnum
     * @param productId
     * @param startTime
     */
    @SuppressLint("CheckResult")
     fun actionRecord(
        sakuActionEnum: ActionEnum,
        productId: String?,
        startTime: Long,
        endTime: Long
    ) {
        val isUpload = booleanArrayOf(false)
        LocationTool.getInstance().startLocation(object :
            LocationCall {
            override fun error(errorMsg: String?) {}
            override fun location(
                location: Location?,
                addressDetail: String?,
                addressJson: String?
            ) {
                if (!isUpload[0]) {
                    isUpload[0] = true
                    upload(sakuActionEnum, productId, startTime, endTime)
                }
            }
        })
        Observable.timer(3, TimeUnit.SECONDS)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe { s: Long? ->
                if (!isUpload[0]) {
                    isUpload[0] = true
                    upload(sakuActionEnum, productId, startTime, endTime)
                }
            }
    }

    fun upload(
        sakuActionEnum: ActionEnum,
        productId: String?,
        startTime: Long,
        endTime: Long
    ) {
        val map: MutableMap<String, Any?> = HashMap()
        map["sceneType"] = sakuActionEnum.type
        map["productId"] = productId
        map["longitude"] = location_longitude
        map["latitude"] = location_latitude
        map["deviceNo"] = DeviceIdentifier.getUniqueIdentifier(ContextUtils.getApplication())
        map["startTime"] = startTime
        map["endTime"] = endTime

        try {
            CoroutineScope(Dispatchers.Main).launch {
                AppApi.api.uploadActionData(ParameterTool.toRequestBody(map))
            }
        }catch (ex:Exception){
            ex.printStackTrace()
        }

    }


}