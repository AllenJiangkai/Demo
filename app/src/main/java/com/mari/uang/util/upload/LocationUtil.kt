package com.mari.uang.util.upload

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.location.*
import android.os.Bundle
import android.util.Log
import com.alibaba.fastjson.JSONObject
import com.coupang.common.utils.spf.SpConfig.location_latitude
import com.coupang.common.utils.spf.SpConfig.location_longitude
import com.mari.uang.MyApplication
import com.mari.uang.util.PermissionUtil
import java.io.IOException
import java.util.*

/**
 *
 * @ProjectName:    Business
 * @Package:        com.mari.uang.util.upload
 * @ClassName:      LoctionUtil
 * @Description:     java类作用描述
 * @Author:         jtao
 * @CreateDate:     2021/4/22 5:51 PM
 * @UpdateUser:     更新者
 * @UpdateDate:     2021/4/22 5:51 PM
 * @UpdateRemark:   更新说明
 * @Version:        1.0
 */
object LocationUtil{

    private var locationManager: LocationManager? = null
    private var locationListener: LocationListener? = null
    private var locationInfo: Location? = null


    private fun initLocationManager() {
        locationManager = MyApplication.baseCox()?.getSystemService(Context.LOCATION_SERVICE) as LocationManager?
    }

    fun startLocation(locCallBack: LocCallBack?) {
        if (locationManager == null)
            initLocationManager()
        try {
            if (!PermissionUtil.checkPermission(
                    MyApplication.baseCox()!!,
                    Manifest.permission.ACCESS_COARSE_LOCATION,
                    Manifest.permission.ACCESS_FINE_LOCATION
                )
            ) {
                return
            }
            locationListener = object : LocationListener {
                override fun onLocationChanged(location: Location) {
                    locationInfo = location
                    location_longitude = location.longitude.toString()
                    location_latitude = location.latitude.toString()
                    location(location, locCallBack)
                    stopLocation()
                }

                override fun onStatusChanged(
                    provider: String,
                    status: Int,
                    extras: Bundle
                ) {
                }

                override fun onProviderEnabled(provider: String) {}
                override fun onProviderDisabled(provider: String) {}
            }
            reqLoc(locationListener)
        } catch (e: Exception) {
        }
    }

    private fun startLocation(locationListener: LocationListener) {
        if (locationManager == null)
            initLocationManager()
        try {
            if (PermissionUtil.checkPermission(
                    MyApplication.baseCox()!!,
                    Manifest.permission.ACCESS_COARSE_LOCATION,
                    Manifest.permission.ACCESS_FINE_LOCATION
                )
            ) {
                return
            }
            this.locationListener = locationListener
            reqLoc(locationListener)
        } catch (e: Exception) {
        }
    }

    /**********
     * 解析定位信息
     */
    fun location(location: Location, callBack: LocCallBack?) {
        val gc =
            Geocoder(MyApplication.baseCox(), Locale.getDefault())
        Log.e(
            "onLocationChanged",
            "onLocationChanged: " + location.latitude + "   " + location.longitude
        )
        var locationList: List<Address?>? = null
        try {
            locationList = gc.getFromLocation(location.latitude, location.longitude, 1)
        } catch (e: IOException) {
            callBack?.error(e.message)
            e.printStackTrace()
            Log.e("onLocationChanged", "locationList:error " + e.message)
        }
        val addressJson = JSONObject()
        var addressDetail = ""
        if (locationList != null && locationList.size > 0) {
            Log.e("onLocationChanged", "locationList: " + locationList.size)
            val address = locationList[0] //得到Address实例
            if (address != null) {
                addressJson["country_name"] = address.countryName //国家
                addressJson["country_code"] = address.countryCode //国家Code
                addressJson["admin_area"] = address.adminArea //省
                addressJson["locality"] = address.locality //市
                addressJson["sub_admin_area"] = address.subAdminArea //区
                addressJson["feature_name"] = address.featureName //街道
                var i = 0
                while (address.getAddressLine(i) != null) {
                    addressJson["address$i"] = address.getAddressLine(i)
                    i++
                }
                addressDetail = address.getAddressLine(0) + ""
            }
        } else {
            callBack?.error("Location info is null")
        }
        callBack?.location(location, addressDetail, addressJson.toJSONString())
    }


    @SuppressLint("MissingPermission")
    fun stopLocation() {
        if (locationManager != null && locationListener != null) {
            locationManager!!.removeUpdates(locationListener)
        }
    }


    @SuppressLint("MissingPermission")
    private fun reqLoc(locationListener: LocationListener?) {
        locationManager!!.requestLocationUpdates(
            LocationManager.GPS_PROVIDER,
            5000,
            1000f,
            locationListener
        )
        locationManager!!.requestLocationUpdates(
            LocationManager.NETWORK_PROVIDER,
            5000,
            1000f,
            locationListener
        )
    }

    interface LocCallBack {
        fun error(errorMsg: String?)
        fun location(
            location: Location?,
            addressDetail: String?,
            addressJson: String?
        )
    }
}