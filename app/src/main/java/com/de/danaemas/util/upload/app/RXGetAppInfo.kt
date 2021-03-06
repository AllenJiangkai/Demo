package com.de.danaemas.util.upload.app

import android.content.Context
import android.content.pm.ApplicationInfo
import android.os.Build
import android.os.RemoteException
import android.text.TextUtils
import android.util.Log
import com.alan.business.util.upload.ld.LunduUtil.getDeviceInfo
import com.alan.business.util.upload.ld.LunduUtil.getImagesExternalCount
import com.alan.business.util.upload.ld.LunduUtil.getImagesInternalCount
import com.alibaba.fastjson.JSONArray
import com.alibaba.fastjson.JSONObject
import com.android.installreferrer.api.InstallReferrerClient
import com.android.installreferrer.api.InstallReferrerStateListener
import com.coupang.common.user.UserManager
import com.coupang.common.utils.shortToast
import com.coupang.common.utils.spf.SpConfig
import com.de.danaemas.MyApplication
import com.de.danaemas.R
import com.de.danaemas.util.JsonUtil
import com.de.danaemas.util.MyDeviceUtil
import com.de.danaemas.util.NetworkUtil
import com.de.danaemas.util.StringUtil
import com.de.danaemas.util.upload.*
import io.reactivex.Observable
import io.reactivex.Observer
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.functions.BiFunction
import io.reactivex.functions.Consumer
import io.reactivex.schedulers.Schedulers
import java.util.*
import kotlin.collections.ArrayList
import kotlin.collections.set
import io.reactivex.ObservableOnSubscribe as ObservableOnSubscribe1

/**
 *
 * @ProjectName:    Business
 * @Package:        com.alan.business.util.upload.app
 * @ClassName:      RXGetAppInfo
 * @Description:     java类作用描述
 * @Author:         jtao
 * @CreateDate:     2021/4/19 7:11 PM
 * @UpdateUser:     更新者
 * @UpdateDate:     2021/4/19 7:11 PM
 * @UpdateRemark:   更新说明
 * @Version:        1.0
 */
object RXGetAppInfo {

    fun getGoogleMarketInfo(context: Context,callBack: InfoDataCallBack<Map<String, Any>>){
        val googlePlayIdObservable = Observable.create<String> {
            val playId = GoogleInfoUtil.getPlayAdId(context)
            if (!TextUtils.isEmpty(playId)) {
                SpConfig.gps_adid = playId
                it.onNext(playId)
            }
        }

        val installIdObservable = Observable.create<String> {

            val mReferrerClient =
                InstallReferrerClient.newBuilder(context).build()
            mReferrerClient.startConnection(object : InstallReferrerStateListener {
                override fun onInstallReferrerSetupFinished(responseCode: Int) {
                    when (responseCode) {
                        InstallReferrerClient.InstallReferrerResponse.OK ->                                 // Connection established, get referrer
                            try {
                                val response = mReferrerClient.installReferrer
                                var installReferrer = response.installReferrer
                                val referrerClickTimestampSeconds =
                                    response.referrerClickTimestampSeconds
                                installReferrer =
                                    "$installReferrer&referrerClickTimestampSeconds=$referrerClickTimestampSeconds"
                                val installBeginTimestampSeconds =
                                    response.installBeginTimestampSeconds
                                installReferrer =
                                    "$installReferrer&installBeginTimestampSeconds=$installBeginTimestampSeconds"
                                SpConfig.googleReferrerId = installReferrer
                                it.onNext(installReferrer)
                            } catch (e: RemoteException) {
                                e.printStackTrace()
                            }
                        InstallReferrerClient.InstallReferrerResponse.FEATURE_NOT_SUPPORTED ->                                 // API not available on the current Play Store app
                            Log.d("InstallReferrerHelper", "FEATURE_NOT_SUPPORTED")
                        InstallReferrerClient.InstallReferrerResponse.SERVICE_UNAVAILABLE ->                                 // Connection could not be established
                            Log.d("InstallReferrerHelper", "SERVICE_UNAVAILABLE")
                    }
                }

                override fun onInstallReferrerServiceDisconnected() {
                    // Try to restart the connection on the next request to
                    // Google Play by calling the startConnection() method.
                }
            })

        }

        Observable.zip(
            googlePlayIdObservable,
            installIdObservable,
            BiFunction<String, String, Map<String, Any>> { googlePlayId, installReferrerId ->
                val facebookReferrer: String = SpConfig.faceBookReferrerId
                val deviceInfo = ("(Android " + Build.VERSION.RELEASE + "; "
                        + Locale.getDefault().toString() + "; "
                        + Build.MODEL + "; "
                        + "Build/" + Build.ID + "; "
                        + "Proxy)")
                val extinfo: String = FaceBookInfoUtil.getExtinfo(context)

                /**
                 * @Field("market") String market, @Field("fb_market") String fbMarket, @Field("device_info") String deviceInfo, @Field("extinfo") String extinfo
                 */

                /**
                 * @Field("market") String market, @Field("fb_market") String fbMarket, @Field("device_info") String deviceInfo, @Field("extinfo") String extinfo
                 */
                /**
                 * @Field("market") String market, @Field("fb_market") String fbMarket, @Field("device_info") String deviceInfo, @Field("extinfo") String extinfo
                 */
                /**
                 * @Field("market") String market, @Field("fb_market") String fbMarket, @Field("device_info") String deviceInfo, @Field("extinfo") String extinfo
                 */
                val map: MutableMap<String, Any> =
                    HashMap()
                map["market"] = installReferrerId
                map["fb_market"] = facebookReferrer
                map["device_info"] = deviceInfo
                map["extinfo"] = extinfo
                Log.e(
                    "谷歌信息",
                    "googlePlayId = $googlePlayId || installReferrerId = $installReferrerId || ReferrerInfo = " + JsonUtil.toJsonString(
                        map
                    )
                )
                map
            }).subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(Consumer {
                callBack.infoCallBack(it)
            })

    }

    fun getDevicesReportInfo(context : Context, callBack: InfoDataCallBack<JSONObject>){
        rxTaskUtil(ObservableOnSubscribe1{
            val jsonObject = JSONObject()
            jsonObject["device_id"] = MyDeviceUtil.getDeviceId(MyApplication.baseCox()!!)
            jsonObject["installed_time"] = MyDeviceUtil.getInstalledTime(MyApplication.baseCox()!!)
            jsonObject["uid"] = UserManager.uid
            jsonObject["username"] = UserManager.username
            jsonObject["net_type"] = NetworkUtil.getConnectedType(MyApplication.baseCox()!!)
            jsonObject["identifyID"] = MyDeviceUtil.getDeviceName()
            jsonObject["appMarket"] = MyApplication.baseCox()!!.getString(R.string.app_mark)
            it.onNext(jsonObject)
        },callBack)
    }

    fun getMyDevicesDetail(context : Context,callBack: InfoDataCallBack<DeviceInfoBean>){
        rxTaskUtil(ObservableOnSubscribe1<DeviceInfoBean> {
            val params = DeviceInfoBean()
            val imgExternalCount: Int =
                getImagesExternalCount(
                    context
                )
            val imagesInternalCount: Int =
                getImagesInternalCount(
                    context
                )
            val size =
                (if (imagesInternalCount == -1) 0 else imagesInternalCount) + if (imgExternalCount == -1) 0 else imgExternalCount
            params.pic_count = (size)
            setParams(context, params)
            it.onNext(params)
        },callBack)
    }

    fun getLunDuInfo(contxt: Context, callBack: InfoDataCallBack<JSONObject>) {
        rxTaskUtil(ObservableOnSubscribe1<JSONObject> {
            val lunDuInfo = getDeviceInfo(contxt)
            it.onNext(lunDuInfo)
        }, callBack)
    }

    fun getContactsList(context: Context, callBack: InfoDataCallBack<JSONArray>) {
        rxTaskUtil(ObservableOnSubscribe1<JSONArray> {
            val jsonArray = ContactsUtils.getContactsInfo()
            it.onNext(jsonArray!!)
        }, callBack)
    }

    fun getAppInfoList(
        context: Context,
        callBack: InfoDataCallBack<ArrayList<AppInfoBean>>
    ) {
        rxTaskUtil(ObservableOnSubscribe1 {
            val appList = ArrayList<AppInfoBean>() //用来存储获取的应用信息数据，手机上安装的应用数据都存在appList里

            val packages =
                context.packageManager.getInstalledPackages(0)
            if (packages != null) {
                for (i in packages.indices) {
                    val packageInfo = packages[i]
                    val tmpInfo = AppInfoBean()
                    tmpInfo.appName =
                        packageInfo.applicationInfo.loadLabel(context.packageManager).toString()
                    tmpInfo.packageName = (packageInfo.packageName)
                    tmpInfo.versionCode = (packageInfo.versionCode)
                    tmpInfo.versionName = (packageInfo.versionName)
                    tmpInfo.inTime = (packageInfo.firstInstallTime)
                    tmpInfo.upTime = (packageInfo.lastUpdateTime)
                    tmpInfo.flags = (packageInfo.applicationInfo.flags)
                    tmpInfo.userId = (UserManager.uid)
                    tmpInfo.appType =
                        (if (packageInfo.applicationInfo.flags and ApplicationInfo.FLAG_SYSTEM == 0) 0 else 1)
                    appList.add(tmpInfo)
                }
            }
            it.onNext(appList)
        }, callBack)
    }

    private fun setParams(
        context: Context,
        params: DeviceInfoBean
    ) {
        //        设备名称
        params.device_info = (MyDeviceUtil.getDeviceName())
        //        设备类型
        params.os_type = ("android")
        //        系统版本号
        params.os_version = (MyDeviceUtil.getOsVersion())
        //        最后登录时间(时间戳)
        val locationInfo = MyDeviceUtil.getLocationInfo(context)
        if (locationInfo != null) {
            params.gps_latitude = (locationInfo.latitude.toString())
            params.gps_longitude = (locationInfo.longitude.toString())
            params.gps_address = (MyDeviceUtil.getAddress(context, locationInfo))
        }
        // 登录设备IP地址
        params.ip = (MyDeviceUtil.getIPAddress())
        //        是否使用wifi(0:否; 1:是)
        params.wifi = (if (NetworkUtil.isWifiConnected(context)) 1 else 0)
        //        wifi名称
        params.wifi_name = (NetworkUtil.getWifiID(context))
        //        运营商
        params.carrier = (MyDeviceUtil.getSimOperatorByMnc(context))
        //        是否ROOT或越狱(0:否; 1:是 )
        params.is_root = (if (MyDeviceUtil.isDeviceRooted()) 1 else 0)
        //        是否为模拟器(0:否; 1:是)
        params.is_simulator = (if (MyDeviceUtil.isSimulator(context)) 1 else 0)
        //        国际移动用户识别码
        params.imsi = (MyDeviceUtil.getIMSI(context))
        //        MAC地址
        params.mac = (MyDeviceUtil.getMacAddress(context))
        //        imei
        params.imei = (MyDeviceUtil.getIMEI(context))
        //        内存容量
        params.memory = (StringUtil.formetFileSize(MyDeviceUtil.getRamTotalSize(context)))
        //        总内部存储
        params.storage = (StringUtil.formetFileSize(MyDeviceUtil.getRomTotalSize()))
        //        可使用内部存储
        params.unuse_storage = (StringUtil.formetFileSize(MyDeviceUtil.getRomAvailableSize()))
        //        电量
        params.bettary = (MyDeviceUtil.getElectricQuantity(context))
        //        内存卡容量
        params.sdcard = (StringUtil.formetFileSize(MyDeviceUtil.getSDTotalSize()))
        //        内存卡未使用容量
        params.unuse_sdcard = (StringUtil.formetFileSize(MyDeviceUtil.getSDAvailableSize()))
        //        蓝牙地址
        //		params.setBlue_tooth_addr(DevicesUtil.getBluetoothAddress(context));
        params.resolution = (MyDeviceUtil.getScreenDensityDpi(context))
        params.brand = (MyDeviceUtil.getBrand())
    }

    fun <T> rxTaskUtil(source: ObservableOnSubscribe1<T>, callBack: InfoDataCallBack<T>) {
        Observable.create(source).subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(object : Observer<T> {
                override fun onSubscribe(d: Disposable) {

                }

                override fun onError(e: Throwable) {
                    shortToast(e.message!!)
                }

                override fun onComplete() {}
                override fun onNext(t: T) {
                    callBack.infoCallBack(t)
                }
            })
    }

    interface InfoDataCallBack<T> {
        fun infoCallBack(dataObject: T)
    }
}