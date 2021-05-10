package com.de.danaemas.util.upload.ld

import android.annotation.SuppressLint
import android.content.BroadcastReceiver
import android.content.Context
import android.content.IntentFilter
import android.content.pm.PackageInfo
import android.content.pm.PackageManager
import android.database.Cursor
import android.hardware.Sensor
import android.hardware.SensorManager
import android.net.ConnectivityManager
import android.net.NetworkInfo
import android.net.Proxy
import android.net.wifi.ScanResult
import android.net.wifi.WifiManager
import android.os.BatteryManager
import android.os.Build
import android.os.Build.VERSION
import android.os.Environment
import android.os.SystemClock
import android.provider.ContactsContract
import android.provider.MediaStore
import android.provider.MediaStore.Audio
import android.provider.Settings.Secure
import android.telephony.*
import android.text.TextUtils
import android.util.DisplayMetrics
import android.view.WindowManager
import com.alibaba.fastjson.JSONArray
import com.alibaba.fastjson.JSONException
import com.alibaba.fastjson.JSONObject
import com.de.danaemas.util.upload.ld.LDAdProvider.getAdvertisingIdInfo
import com.de.danaemas.util.upload.ld.LDComUtil.getNonNullText
import com.de.danaemas.util.upload.ld.LDComUtil.haveSelfPermission
import java.io.*
import java.net.NetworkInterface
import java.util.*

/**
 *
 * @ProjectName:    Business
 * @Package:        com.mari.uang.util.upload.ld
 * @ClassName:      LDDevicesUtil
 * @Description:     java类作用描述
 * @Author:         jtao
 * @CreateDate:     2021/4/21 4:19 PM
 * @UpdateUser:     更新者
 * @UpdateDate:     2021/4/21 4:19 PM
 * @UpdateRemark:   更新说明
 * @Version:        1.0
 */
object LDDevicesUtil {
    private const val NETWORN_NONE = "none"
    private const val NETWORN_WIFI = Context.WIFI_SERVICE
    private const val NETWORN_2G = "2G"
    private const val NETWORN_3G = "3G"
    private const val NETWORN_4G = "4G"
    private const val NETWORN_MOBILE = "other"

    @Throws(JSONException::class)
    fun getHardWareInfo(context: Context): JSONObject? {
        val hardWareData = JSONObject()
        hardWareData["device_name"] = getNonNullText(getDriverBrand())
        hardWareData["sdk_version"] = getNonNullText(getDriverSDKVersion())
        hardWareData["model"] = getNonNullText(getDriverModel())
        hardWareData["physical_size"] =
            getNonNullText(getScreenPhysicalSize(context))
        hardWareData["release"] = getNonNullText(getDriverOsVersion())
        hardWareData["brand"] = getNonNullText(getDriverBrand())
        hardWareData["serial_number"] = getNonNullText(getSerialNumber())

//        hardWareData.put("board", LDCommonUtil.getNonNullText(getBoard()));
//        hardWareData.put("production_date", LDCommonUtil.getNonNullText(getDriverTime()));
//        hardWareData.put("device_height", LDCommonUtil.getNonNullText(getDisplayMetrics(context).heightPixels + ""));
//        hardWareData.put("device_width", LDCommonUtil.getNonNullText(getDisplayMetrics(context).widthPixels + ""));
        return hardWareData
    }

    @Throws(JSONException::class)
    fun getGeneralData(context: Context): JSONObject? {
        val generalData = JSONObject()
        generalData["gaid"] = getNonNullText(getGAID(context))
        generalData["and_id"] = getNonNullText(getAndroidId(context))
        generalData["phone_type"] = getNonNullText(getPhoneType(context).toString())
        generalData["mac"] = getNonNullText(getMacAddress(context))
        generalData["locale_display_language"] =
            getNonNullText(getLocaleDisplayLanguage())
        generalData["locale_iso_3_language"] =
            getNonNullText(getISO3Language(context))
        generalData["locale_iso_3_country"] = getNonNullText(getISO3Country(context))
        generalData["language"] = getNonNullText(getLanguage(context))
        generalData["imei"] = getNonNullText(getDeviceImeIValue(context))
        generalData["phone_number"] = getNonNullText(getCurrentPhoneNum(context))
        generalData["network_operator_name"] =
            getNonNullText(getNetWorkOperatorName(context))
        generalData["network_type"] = getNonNullText(getNetworkState(context))
        generalData["time_zone_id"] = getNonNullText(getCurrentTimeZone())


//        generalData.put("is_using_proxy_port", isUsingProxyPort());
//        generalData.put("is_using_vpn", isUsingVPN());
//        generalData.put("is_usb_debug", isOpenUSBDebug(context));
//        generalData.put("elapsedRealtime", getElapsedRealtime());
//        generalData.put("sensor_list", getSensorList(context));
//        generalData.put("currentSystemTime", System.currentTimeMillis());
//        generalData.put("uptimeMillis", getUpdateMills());
        return generalData
    }

    @Throws(JSONException::class)
    fun getOtherData(context: Context): JSONObject? {
        val otherData = JSONObject()
        otherData["root_jailbreak"] = if (isRoot()) "1" else "0"
        otherData["last_boot_time"] = bootTime().toString() + ""
        otherData["keyboard"] = getKeyboard(context)
        otherData["simulator"] = if (isEmulator(context)) "1" else "0"
        otherData["dbm"] = getNonNullText(getMobileDbm(context))
        return otherData
    }

    @Throws(JSONException::class)
    fun getAppListData(context: Context): JSONArray? {
        var appsJsonArray = getAppList(context)
        if (appsJsonArray == null || appsJsonArray.size <= 0) {
            appsJsonArray = getAppList2(context)
        }
        return appsJsonArray
    }

    fun getNetworkData(context: Context): JSONObject? {
        val network = JSONObject()
        val currentNetwork = JSONObject()
        val configNetwork = JSONArray()
        try {
            val wifiManager =
                context.getSystemService(Context.WIFI_SERVICE) as WifiManager
            if (wifiManager != null && wifiManager.isWifiEnabled) {
                val wifiInfo = wifiManager.connectionInfo
                currentNetwork["ssid"] = wifiInfo.ssid
                currentNetwork["bssid"] = wifiInfo.bssid
                currentNetwork["mac"] = wifiInfo.macAddress

//                currentNetwork.put("name", getWifiName(context));
//                network.put("current_wifi", currentNetwork);
//                network.put("IP", getWifiIP(context));
                val configs =
                    wifiManager.scanResults
                val var6: Iterator<*> = configs.iterator()
                while (var6.hasNext()) {
                    val scanResult =
                        var6.next() as ScanResult
                    val config = JSONObject()
                    config["bssid"] = scanResult.BSSID
                    config["ssid"] = scanResult.SSID
                    config["mac"] = scanResult.BSSID
                    config["name"] = scanResult.SSID
                    configNetwork.add(config)
                }

//                network.put("wifi_count", configs.size() + 1);
                network["configured_wifi"] = configNetwork
            }
        } catch (var9: Exception) {
        }
        return network
    }

    @Throws(JSONException::class)
    fun getBatteryData(context: Context): JSONObject? {
        val jSONObject = JSONObject()
        val manager =
            context.getSystemService(Context.BATTERY_SERVICE) as BatteryManager
        if (manager != null) {
            val dianliang = manager.getIntProperty(4)
            jSONObject["battery_pct"] = dianliang
        }
        val intent = context.registerReceiver(
            null as BroadcastReceiver?,
            IntentFilter("android.intent.action.BATTERY_CHANGED")
        )
        val k = intent!!.getIntExtra("plugged", -1)
        return when (k) {
            1 -> {
                jSONObject["is_usb_charge"] = 0
                jSONObject["is_ac_charge"] = 1
                jSONObject["is_charging"] = 1
                jSONObject
            }
            2 -> {
                jSONObject["is_usb_charge"] = 1
                jSONObject["is_ac_charge"] = 0
                jSONObject["is_charging"] = 1
                jSONObject
            }
            else -> {
                jSONObject["is_usb_charge"] = 0
                jSONObject["is_ac_charge"] = 0
                jSONObject["is_charging"] = 0
                jSONObject
            }
        }
    }

    private fun getGAID(context: Context): String? {
        try {
            val adInfo = getAdvertisingIdInfo(context)
            if (adInfo != null) {
                return adInfo.id
            }
        } catch (var1: Exception) {
            var1.printStackTrace()
        }
        return ""
    }

    private fun getISO3Language(paramContext: Context): String? {
        return paramContext.resources.configuration.locale.isO3Language
    }

    private fun getISO3Country(paramContext: Context): String? {
        return paramContext.resources.configuration.locale.isO3Country
    }

    private fun getLocaleDisplayLanguage(): String? {
        return Locale.getDefault().displayLanguage
    }

    @SuppressLint("MissingPermission")
    private fun getDeviceImeIValue(context: Context): String? {
        if (haveSelfPermission(context, "android.permission.READ_PHONE_STATE")) {
            try {
                return if (VERSION.SDK_INT >= 26) {
                    (context.getSystemService(Context.TELEPHONY_SERVICE) as TelephonyManager).imei
                } else (context.getSystemService(Context.TELEPHONY_SERVICE) as TelephonyManager).deviceId
            } catch (var2: Exception) {
            }
        }
        return ""
    }

    @SuppressLint("MissingPermission")
    private fun getCurrentPhoneNum(context: Context): String? {
        try {
            val tm =
                context.getSystemService(Context.TELEPHONY_SERVICE) as TelephonyManager
            if (tm != null) {
                return tm.line1Number
            }
        } catch (var2: Exception) {
        }
        return ""
    }

    private fun getScreenPhysicalSize(paramContext: Context): String? {
        val display =
            (paramContext.getSystemService(Context.WINDOW_SERVICE) as WindowManager).defaultDisplay
        val displayMetrics = DisplayMetrics()
        display.getMetrics(displayMetrics)
        return java.lang.Double.toString(
            Math.sqrt(
                Math.pow(
                    (displayMetrics.heightPixels.toFloat() / displayMetrics.ydpi).toDouble(),
                    2.0
                ) + Math.pow(
                    (displayMetrics.widthPixels.toFloat() / displayMetrics.xdpi).toDouble(),
                    2.0
                )
            )
        )
    }

    fun getAudioExternalNumber(context: Context): String? {
        var result = 0
        return if (!haveSelfPermission(
                context,
                "android.permission.READ_EXTERNAL_STORAGE"
            )
        ) {
            ""
        } else {
            val cursor: Cursor?
            cursor = context.contentResolver.query(
                Audio.Media.EXTERNAL_CONTENT_URI,
                arrayOf(
                    "date_added",
                    "date_modified",
                    "duration",
                    "mime_type",
                    "is_music",
                    "year",
                    "is_notification",
                    "is_ringtone",
                    "is_alarm"
                ),
                null as String?,
                null as Array<String?>?,
                null as String?
            )
            while (cursor != null && cursor.moveToNext()) {
                ++result
            }
            if (cursor != null && !cursor.isClosed) {
                cursor.close()
            }
            result.toString()
        }
    }

    fun getAudioInternalNumber(context: Context): String? {
        var result = 0
        val cursor: Cursor?
        cursor = context.contentResolver.query(
            Audio.Media.INTERNAL_CONTENT_URI,
            arrayOf(
                "date_added",
                "date_modified",
                "duration",
                "mime_type",
                "is_music",
                "year",
                "is_notification",
                "is_ringtone",
                "is_alarm"
            ),
            null as String?,
            null as Array<String?>?,
            "title_key"
        )
        while (cursor != null && cursor.moveToNext()) {
            ++result
        }
        if (cursor != null && !cursor.isClosed) {
            cursor.close()
        }
        return result.toString()
    }

    fun getImagesExternalNumber(context: Context): String? {
        var result = 0
        return if (!haveSelfPermission(
                context,
                "android.permission.READ_EXTERNAL_STORAGE"
            )
        ) {
            ""
        } else {
            val cursor: Cursor?
            cursor = context.contentResolver.query(
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                arrayOf(
                    "datetaken",
                    "date_added",
                    "date_modified",
                    "height",
                    "width",
                    "latitude",
                    "longitude",
                    "mime_type",
                    "title",
                    "_size"
                ),
                null as String?,
                null as Array<String?>?,
                null as String?
            )
            while (cursor != null && cursor.moveToNext()) {
                ++result
            }
            if (cursor != null && !cursor.isClosed) {
                cursor.close()
            }
            result.toString()
        }
    }

    fun getImagesInternalNumber(context: Context): String? {
        var result = 0
        val cursor: Cursor?
        cursor = context.contentResolver.query(
            MediaStore.Images.Media.INTERNAL_CONTENT_URI,
            arrayOf(
                "datetaken",
                "date_added",
                "date_modified",
                "height",
                "width",
                "latitude",
                "longitude",
                "mime_type",
                "title",
                "_size"
            ),
            null as String?,
            null as Array<String?>?,
            null as String?
        )
        while (cursor != null && cursor.moveToNext()) {
            ++result
        }
        if (cursor != null && !cursor.isClosed) {
            cursor.close()
        }
        return result.toString()
    }

    fun getVideoExternalNumber(context: Context): String? {
        var result = 0
        return if (!haveSelfPermission(
                context,
                "android.permission.READ_EXTERNAL_STORAGE"
            )
        ) {
            ""
        } else {
            val arrayOfString = arrayOf("date_added")
            val cursor: Cursor?
            cursor = context.contentResolver.query(
                MediaStore.Video.Media.EXTERNAL_CONTENT_URI,
                arrayOfString,
                null as String?,
                null as Array<String?>?,
                null as String?
            )
            while (cursor != null && cursor.moveToNext()) {
                ++result
            }
            if (cursor != null && !cursor.isClosed) {
                cursor.close()
            }
            result.toString()
        }
    }

    fun getVideoInternalNumber(context: Context): String? {
        var result = 0
        val arrayOfString = arrayOf("date_added")
        val cursor: Cursor?
        cursor = context.contentResolver.query(
            MediaStore.Video.Media.INTERNAL_CONTENT_URI,
            arrayOfString,
            null as String?,
            null as Array<String?>?,
            null as String?
        )
        while (cursor != null && cursor.moveToNext()) {
            ++result
        }
        if (cursor != null && !cursor.isClosed) {
            cursor.close()
        }
        return result.toString()
    }

    fun getDownloadFileNumber(): String? {
        var result = 0
        val files =
            Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS)
                .listFiles()
        if (files != null) {
            result = files.size
        }
        return result.toString()
    }

    fun getContactsGroupNumber(context: Context): String? {
        var result = 0
        return if (!haveSelfPermission(
                context,
                "android.permission.READ_CONTACTS"
            )
        ) {
            ""
        } else {
            val uri = ContactsContract.Groups.CONTENT_URI
            val contentResolver = context.contentResolver
            val cursor: Cursor?
            cursor = contentResolver.query(
                uri,
                null as Array<String?>?,
                null as String?,
                null as Array<String?>?,
                null as String?
            )
            while (cursor != null && cursor.moveToNext()) {
                ++result
            }
            if (cursor != null && !cursor.isClosed) {
                cursor.close()
            }
            result.toString()
        }
    }

    private fun getPhoneType(context: Context): Int {
        return try {
            val manager =
                context.getSystemService(Context.TELEPHONY_SERVICE) as TelephonyManager
            manager.phoneType
        } catch (var1: Exception) {
            0
        }
    }

    private fun getLanguage(context: Context): String? {
        val locale = context.resources.configuration.locale
        return locale.language
    }

    private fun getNetWorkOperatorName(context: Context): String? {
        val manager =
            context.getSystemService(Context.TELEPHONY_SERVICE) as TelephonyManager
        return manager.networkOperatorName
    }

    private fun isOnline(context: Context): Boolean {
        val manager =
            context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val info = manager.activeNetworkInfo
        return info != null && info.isConnected
    }

    private fun getAppList(context: Context): JSONArray? {
        val packages =
            context.packageManager.getInstalledPackages(0)
        val jsonArray = JSONArray()
        if (packages != null && packages.size > 0) {
            try {
                for (i in packages.indices) {
                    val packageInfo =
                        packages[i] as PackageInfo
                    val name =
                        packageInfo.applicationInfo.loadLabel(context.packageManager)
                            .toString()
                    val jsonObject =
                        JSONObject()
                    jsonObject["app_name"] = name
                    jsonObject["package"] = packageInfo.packageName
                    jsonObject["version_name"] = packageInfo.versionName
                    jsonObject["version_code"] = packageInfo.versionCode
                    jsonObject["in_time"] = packageInfo.firstInstallTime
                    jsonObject["up_time"] = packageInfo.lastUpdateTime
                    jsonObject["flags"] = packageInfo.applicationInfo.flags
                    jsonObject["app_type"] =
                        if (packageInfo.applicationInfo.flags and 1 == 0) "0" else "1"
                    jsonObject["create_time"] = System.currentTimeMillis()
                    jsonArray.add(jsonObject)
                }
            } catch (var7: Exception) {
            }
        }
        return jsonArray
    }

    private fun getAppList2(context: Context): JSONArray? {
        val jsonArray = JSONArray()
        try {
            val pm = context.packageManager
            val process = Runtime.getRuntime().exec("pm list packages")
            val bis =
                BufferedReader(InputStreamReader(process.inputStream))
            var line = ""
            while (bis.readLine().also { line = it } != null) {
                val packageInfo =
                    pm.getPackageInfo(line.replace("package:", ""), PackageManager.GET_GIDS)
                val name =
                    packageInfo.applicationInfo.loadLabel(context.packageManager).toString()
                val jsonObject = JSONObject()
                jsonObject["app_name"] = name
                jsonObject["package"] = packageInfo.packageName
                jsonObject["version_name"] = packageInfo.versionName
                jsonObject["version_code"] = packageInfo.versionCode
                jsonObject["in_time"] = packageInfo.firstInstallTime
                jsonObject["up_time"] = packageInfo.lastUpdateTime
                jsonObject["flags"] = packageInfo.applicationInfo.flags
                jsonObject["app_type"] =
                    if (packageInfo.applicationInfo.flags and 1 == 0) "0" else "1"
                jsonObject["create_time"] = System.currentTimeMillis()
                jsonArray.add(jsonObject)
            }
            bis.close()
        } catch (var9: Exception) {
        }
        return jsonArray
    }

    private fun getKeyboard(paramContext: Context): String? {
        val configuration =
            paramContext.resources.configuration
        val stringBuilder = StringBuilder()
        stringBuilder.append(configuration.keyboard)
        stringBuilder.append("")
        return stringBuilder.toString()
    }

    @SuppressLint("MissingPermission")
    private fun getMobileDbm(context: Context): String? {
        var dbm = -1
        val tm =
            context.getSystemService(Context.TELEPHONY_SERVICE) as TelephonyManager
        try {
            if (haveSelfPermission(
                    context,
                    "android.permission.ACCESS_COARSE_LOCATION"
                )
            ) {
                val cellInfoList = tm.allCellInfo
                if (null != cellInfoList) {
                    val var3: Iterator<*> = cellInfoList.iterator()
                    while (var3.hasNext()) {
                        val cellInfo = var3.next() as CellInfo
                        if (cellInfo is CellInfoGsm) {
                            val cellSignalStrengthGsm =
                                cellInfo.cellSignalStrength
                            dbm = cellSignalStrengthGsm.dbm
                        } else if (cellInfo is CellInfoCdma) {
                            val cellSignalStrengthCdma =
                                cellInfo.cellSignalStrength
                            dbm = cellSignalStrengthCdma.dbm
                        } else if (cellInfo is CellInfoWcdma) {
                            val cellSignalStrengthWcdma =
                                cellInfo.cellSignalStrength
                            dbm = cellSignalStrengthWcdma.dbm
                        } else if (cellInfo is CellInfoLte) {
                            val cellSignalStrengthLte =
                                cellInfo.cellSignalStrength
                            dbm = cellSignalStrengthLte.dbm
                        }
                    }
                }
            }
        } catch (var6: Exception) {
        }
        return dbm.toString()
    }

    private fun getWifiIP(context: Context): String? {
        var ip: String? = null
        try {
            val wifiManager =
                context.getSystemService(Context.WIFI_SERVICE) as WifiManager
            if (wifiManager.isWifiEnabled) {
                val wifiInfo = wifiManager.connectionInfo
                val i = wifiInfo.ipAddress
                ip =
                    (i and 255).toString() + "." + (i shr 8 and 255) + "." + (i shr 16 and 255) + "." + (i shr 24 and 255)
            }
        } catch (var4: Exception) {
        }
        return ip
    }

    private fun getWifiName(context: Context): String? {
        return if (isOnline(context) && getNetworkState(context) == Context.WIFI_SERVICE) {
            val wifiManager = context.applicationContext
                .getSystemService(Context.WIFI_SERVICE) as WifiManager
            val wifiInfo = wifiManager.connectionInfo
            var ssid = wifiInfo.ssid
            if (!TextUtils.isEmpty(ssid) && ssid.contains("\"")) {
                ssid = ssid.replace("\"".toRegex(), "")
            }
            ssid
        } else {
            ""
        }
    }

    private fun getMacAddress(context: Context): String? {
        var mac = getMacAddress1(context)
        if (TextUtils.isEmpty(mac)) {
            mac = getMacFromHardware()
        }
        return mac
    }

    private fun getMacAddress1(context: Context): String? {
        return try {
            val localWifiManager =
                context.getSystemService(Context.WIFI_SERVICE) as WifiManager
            val localWifiInfo = localWifiManager.connectionInfo
            var macAddress = localWifiInfo.macAddress
            if (TextUtils.isEmpty(macAddress) || "02:00:00:00:00:00" == macAddress) {
                macAddress = getMacAddress2(context)
            }
            macAddress
        } catch (var3: Exception) {
            null
        }
    }

    private fun getMacAddress2(context: Context): String? {
        return if (isOnline(context) && getNetworkState(context) == Context.WIFI_SERVICE) {
            var macSerial: String? = null
            var str = ""
            try {
                val pp =
                    Runtime.getRuntime().exec("cat /sys/class/net/wlan0/address ")
                val ir = InputStreamReader(pp.inputStream)
                val input = LineNumberReader(ir)
                while (null != str) {
                    str = input.readLine()
                    if (str != null) {
                        macSerial = str.trim { it <= ' ' }
                        break
                    }
                }
            } catch (var5: Exception) {
            }
            macSerial
        } else {
            ""
        }
    }

    private fun getMacFromHardware(): String? {
        try {
            val all: List<NetworkInterface> =
                Collections.list(NetworkInterface.getNetworkInterfaces())
            val var1: Iterator<*> = all.iterator()
            while (var1.hasNext()) {
                val nif = var1.next() as NetworkInterface
                if (nif.name.equals("wlan0", ignoreCase = true)) {
                    val macBytes = nif.hardwareAddress ?: return null
                    val mac = StringBuilder()
                    val var6 = macBytes.size
                    for (var7 in 0 until var6) {
                        val b = macBytes[var7]
                        mac.append(String.format("%02X:", b))
                    }
                    if (mac.length > 0) {
                        mac.deleteCharAt(mac.length - 1)
                    }
                    return mac.toString()
                }
            }
        } catch (var9: Exception) {
        }
        return null
    }

    fun getRootAuth(): Boolean {
        var process: Process? = null
        var os: DataOutputStream? = null
        var var3: Boolean
        try {
            process = Runtime.getRuntime().exec("su")
            os = DataOutputStream(process.outputStream)
            os.writeBytes("exit\n")
            os.flush()
            val exitValue = process.waitFor()
            if (exitValue != 0) {
                var3 = false
                return var3
            }
            var3 = true
        } catch (var14: Exception) {
            var3 = false
            return var3
        } finally {
            try {
                os?.close()
                process!!.destroy()
            } catch (var13: Exception) {
                var13.printStackTrace()
            }
        }
        return var3
    }

    private fun isRoot(): Boolean {
        var bool = false
        try {
            bool = if (!File("/system/bin/su").exists() && !File("/system/xbin/su").exists()) {
                false
            } else {
                true
            }
        } catch (var2: Exception) {
        }
        return bool
    }

    private fun bootTime(): Long {
        return System.currentTimeMillis() - SystemClock.elapsedRealtimeNanos() / 1000000L
    }

    @SuppressLint("MissingPermission")
    private fun isEmulator(context: Context): Boolean {
        try {
            if (haveSelfPermission(context, "android.permission.READ_PHONE_STATE")) {
                val tm =
                    context.getSystemService(Context.TELEPHONY_SERVICE) as TelephonyManager
                val imei = tm.deviceId
                return if (imei != null && imei == "000000000000000") {
                    true
                } else {
                    Build.MODEL == "sdk" || Build.MODEL == "google_sdk"
                }
            }
        } catch (var2: Exception) {
        }
        return false
    }

    @SuppressLint("HardwareIds")
    private fun getAndroidId(context: Context): String? {
        return try {
            Secure.getString(
                context.applicationContext.contentResolver,
                "android_id"
            )
        } catch (var2: Exception) {
            var2.printStackTrace()
            null
        }
    }

    private fun getNetworkState(context: Context): String {
        val connManager =
            context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        return if (null == connManager) {
            "none"
        } else {
            val activeNetInfo = connManager.activeNetworkInfo
            if (activeNetInfo != null && activeNetInfo.isAvailable) {
                val wifiInfo = connManager.getNetworkInfo(1)
                if (null != wifiInfo) {
                    val state = wifiInfo.state
                    if (null != state && (state == NetworkInfo.State.CONNECTED || state == NetworkInfo.State.CONNECTING)) {
                        return Context.WIFI_SERVICE
                    }
                }
                val networkInfo = connManager.getNetworkInfo(0)
                if (null != networkInfo) {
                    val state = networkInfo.state
                    val strSubTypeName = networkInfo.subtypeName
                    if (null != state && (state == NetworkInfo.State.CONNECTED || state == NetworkInfo.State.CONNECTING)) {
                        return when (activeNetInfo.subtype) {
                            1, 2, 4, 7, 11 -> "2G"
                            3, 5, 6, 8, 9, 10, 12, 14, 15 -> "3G"
                            13 -> "4G"
                            else -> {
                                if (!strSubTypeName.equals(
                                        "TD-SCDMA",
                                        ignoreCase = true
                                    ) && !strSubTypeName.equals(
                                        "WCDMA",
                                        ignoreCase = true
                                    ) && !strSubTypeName.equals("CDMA2000", ignoreCase = true)
                                ) {
                                    "other"
                                } else "3G"
                            }
                        }
                    }
                }
                "none"
            } else {
                "none"
            }
        }
    }

    private fun getCurrentTimeZone(): String? {
        val tz = TimeZone.getDefault()
        return tz.getDisplayName(false, 0)
    }

    private fun getDriverBrand(): String? {
        return try {
            Build.BRAND
        } catch (var1: Exception) {
            ""
        }
    }

    fun getDriverTime(): String? {
        return try {
            val l = Build.TIME
            val stringBuilder = StringBuilder()
            stringBuilder.append(l)
            stringBuilder.append("")
            stringBuilder.toString()
        } catch (var3: Exception) {
            ""
        }
    }

    private fun getDriverSDKVersion(): String? {
        return try {
            VERSION.SDK_INT.toString() + ""
        } catch (var1: Exception) {
            ""
        }
    }

    private fun getDriverModel(): String? {
        return try {
            Build.MODEL
        } catch (var1: Exception) {
            ""
        }
    }

    private fun getDriverOsVersion(): String? {
        return try {
            VERSION.RELEASE
        } catch (var1: Exception) {
            ""
        }
    }

    fun getBoard(): String? {
        return try {
            Build.BOARD
        } catch (var1: Exception) {
            ""
        }
    }

    private fun getSerialNumber(): String? {
        return try {
            val clazz = Class.forName("android.os.SystemProperties")
            clazz.getMethod(
                "get",
                String::class.java
            ).invoke(clazz, "ro.serialno") as String
        } catch (var1: Exception) {
            ""
        }
    }

    fun getDisplayMetrics(context: Context): DisplayMetrics? {
        return context.resources.displayMetrics
    }

    fun getElapsedRealtime(): Long {
        return SystemClock.elapsedRealtime()
    }

    fun getUpdateMills(): Long {
        return SystemClock.uptimeMillis()
    }

    fun isUsingVPN(): Boolean {
        return if (VERSION.SDK_INT > 14) {
            val defaultHost = Proxy.getDefaultHost()
            !TextUtils.isEmpty(defaultHost)
        } else {
            false
        }
    }

    fun isOpenUSBDebug(context: Context): Boolean {
        return Secure.getInt(context.contentResolver, "adb_enabled", 0) > 0
    }

    fun isUsingProxyPort(): Boolean {
        return if (VERSION.SDK_INT > 14) {
            val defaultPort = Proxy.getDefaultPort()
            defaultPort != -1
        } else {
            false
        }
    }

    fun getSensorList(context: Context): JSONArray? {
        val jsonArray = JSONArray()
        val sensorManager =
            context.getSystemService(Context.SENSOR_SERVICE) as SensorManager
        try {
            val sensors =
                sensorManager.getSensorList(-1)
            val var4: Iterator<*> = sensors.iterator()
            while (var4.hasNext()) {
                val item = var4.next() as Sensor
                val jsonObject = JSONObject()
                jsonObject["type"] = item.type.toString()
                jsonObject["name"] = item.name
                jsonObject["version"] = item.version.toString()
                jsonObject["vendor"] = item.vendor
                jsonObject["maxRange"] = item.maximumRange.toString()
                jsonObject["minDelay"] = item.minDelay.toString()
                jsonObject["power"] = item.power.toString()
                jsonObject["resolution"] = item.resolution.toString()
                jsonArray.add(jsonObject)
            }
        } catch (var7: Exception) {
            var7.printStackTrace()
        }
        return jsonArray
    }
}