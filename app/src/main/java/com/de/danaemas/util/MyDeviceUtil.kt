package com.de.danaemas.util

import android.Manifest
import android.app.ActivityManager
import android.content.Context
import android.content.Intent
import android.content.pm.PackageInfo
import android.content.pm.PackageManager
import android.location.Geocoder
import android.location.Location
import android.location.LocationManager
import android.net.Uri
import android.net.wifi.WifiManager
import android.os.BatteryManager
import android.os.Build
import android.os.Environment
import android.os.StatFs
import android.provider.Settings
import android.provider.Settings.SettingNotFoundException
import android.telephony.TelephonyManager
import android.text.TextUtils
import androidx.core.app.ActivityCompat
import com.de.danaemas.MyApplication
import java.io.File
import java.io.IOException
import java.net.InetAddress
import java.net.NetworkInterface
import java.net.SocketException
import java.text.SimpleDateFormat
import java.util.*

/**
 *
 * @ProjectName:    Business
 * @Package:        com.mari.uang.util.upload
 * @ClassName:      MyDeviceUtil
 * @Description:     java类作用描述
 * @Author:         jtao
 * @CreateDate:     2021/4/22 3:00 PM
 * @UpdateUser:     更新者
 * @UpdateDate:     2021/4/22 3:00 PM
 * @UpdateRemark:   更新说明
 * @Version:        1.0
 */
object MyDeviceUtil {
    //获取当前app的版本名称
    fun getAppVersion(context: Context): String? {
        return getAppVersion(context, true)
    }

    fun getAppVersion(
        context: Context,
        isApi: Boolean
    ): String? {
        var appVersion = ""
        try {
            val packageManager = context.packageManager
            val packInfo: PackageInfo
            packInfo = packageManager.getPackageInfo(context.packageName, 0)
            appVersion = packInfo.versionName
        } catch (e: PackageManager.NameNotFoundException) {
            e.printStackTrace()
        }
        return appVersion
    }

    //获取当前app的版本号
    fun getAppVersionCode(context: Context): Int {
        return try {
            val packageManager = context.packageManager
            val packInfo: PackageInfo
            packInfo = packageManager.getPackageInfo(context.packageName, 0)
            packInfo.versionCode
        } catch (e: PackageManager.NameNotFoundException) {
            e.printStackTrace()
            0
        }
    }

    fun getInstalledTime(context: Context): String? {
        var installedTime = ""
        val packageManager = context.packageManager
        try {
            val packageInfo =
                packageManager.getPackageInfo(context.packageName, 0)
            val sdf = SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
            val firstInstallTime = packageInfo.firstInstallTime //应用第一次安装的时间
            installedTime = sdf.format(firstInstallTime)
        } catch (e: PackageManager.NameNotFoundException) {
            e.printStackTrace()
        }
        return installedTime
    }

    fun getDeviceId(context: Context): String? {
        try {
            val tm =
                context.getSystemService(Context.TELEPHONY_SERVICE) as TelephonyManager
            if (ActivityCompat.checkSelfPermission(
                    context,
                    Manifest.permission.READ_PHONE_STATE
                ) == PackageManager.PERMISSION_GRANTED
            ) {
                return if (tm == null || tm.deviceId == null) {
                    val androidId = Settings.Secure.getString(
                        context.contentResolver,
                        Settings.Secure.ANDROID_ID
                    )
                    if (androidId != null) {
                        androidId
                    } else {
                        var serial: String? = null
                        val m_szDevIDShort =
                            "35" + Build.BOARD.length % 10 + Build.BRAND.length % 10 + Build.CPU_ABI.length % 10 + Build.DEVICE.length % 10 + Build.DISPLAY.length % 10 + Build.HOST.length % 10 + Build.ID.length % 10 + Build.MANUFACTURER.length % 10 + Build.MODEL.length % 10 + Build.PRODUCT.length % 10 + Build.TAGS.length % 10 + Build.TYPE.length % 10 + Build.USER.length % 10 //13 位
                        try {
                            serial = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                                Build.getSerial()
                            } else {
                                Build.SERIAL
                            }
                            //API>=9 使用serial号
                            return UUID(
                                m_szDevIDShort.hashCode().toLong(),
                                serial.hashCode().toLong()
                            )
                                .toString()
                        } catch (exception: Exception) {
                            //serial需要一个初始化
                            serial = "serial" // 随便一个初始化
                        }
                        //使用硬件信息拼凑出来的15位号码
                        UUID(m_szDevIDShort.hashCode().toLong(), serial.hashCode().toLong())
                            .toString()
                    }
                } else {
                    tm.deviceId
                }
            }
        } catch (e: Exception) {
            val androidId = Settings.Secure.getString(
                context.contentResolver,
                Settings.Secure.ANDROID_ID
            )
            return if (androidId != null) {
                androidId
            } else {
                var serial: String? = null
                val m_szDevIDShort =
                    "35" + Build.BOARD.length % 10 + Build.BRAND.length % 10 + Build.CPU_ABI.length % 10 + Build.DEVICE.length % 10 + Build.DISPLAY.length % 10 + Build.HOST.length % 10 + Build.ID.length % 10 + Build.MANUFACTURER.length % 10 + Build.MODEL.length % 10 + Build.PRODUCT.length % 10 + Build.TAGS.length % 10 + Build.TYPE.length % 10 + Build.USER.length % 10 //13 位
                try {
                    serial = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                        Build.getSerial()
                    } else {
                        Build.SERIAL
                    }
                    //API>=9 使用serial号
                    return UUID(
                        m_szDevIDShort.hashCode().toLong(),
                        serial.hashCode().toLong()
                    ).toString()
                } catch (exception: Exception) {
                    //serial需要一个初始化
                    serial = "serial" // 随便一个初始化
                }
                //使用硬件信息拼凑出来的15位号码
                UUID(m_szDevIDShort.hashCode().toLong(), serial.hashCode().toLong()).toString()
            }
        }
        return null
    }

    fun getDeviceName(): String? {
        return try {
            Build.MODEL
        } catch (e: Exception) {
            e.printStackTrace()
            ""
        }
    }

    fun getOsVersion(): String? {
        return try {
            Build.VERSION.RELEASE
        } catch (e: Exception) {
            e.printStackTrace()
            ""
        }
    }

    fun getLocationInfo(context: Context): Location? {
        try {
            val lm =
                context.getSystemService(Context.LOCATION_SERVICE) as LocationManager
            if (ActivityCompat.checkSelfPermission(
                    context,
                    Manifest.permission.ACCESS_FINE_LOCATION
                ) != PackageManager.PERMISSION_DENIED && ActivityCompat.checkSelfPermission(
                    context,
                    Manifest.permission.ACCESS_COARSE_LOCATION
                ) != PackageManager.PERMISSION_DENIED
            ) {
                return if (lm.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
                    val location =
                        lm.getLastKnownLocation(LocationManager.GPS_PROVIDER)
                    location ?: getLngAndLatWithNetwork(
                        context
                    )
                } else {
                    //                    lm.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 1000, 0, locationListener);
                    lm.getLastKnownLocation(LocationManager.NETWORK_PROVIDER)
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return null
    }

    fun getLngAndLatWithNetwork(context: Context): Location? {
        var location: Location? = null
        try {
            val lm =
                context.getSystemService(Context.LOCATION_SERVICE) as LocationManager
            if (ActivityCompat.checkSelfPermission(
                    context,
                    Manifest.permission.ACCESS_FINE_LOCATION
                ) == PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                    context,
                    Manifest.permission.ACCESS_COARSE_LOCATION
                ) == PackageManager.PERMISSION_GRANTED
            ) {
//                lm.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 1000, 0, locationListener);
                location = lm.getLastKnownLocation(LocationManager.NETWORK_PROVIDER)
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return location
    }

    fun getAddress(
        context: Context?,
        location: Location?
    ): String? {
        if (location == null) {
            return null
        }
        val latitude = location.latitude
        val longitude = location.longitude
        val geocoder = Geocoder(context, Locale.getDefault())
        try {
            val addresses =
                geocoder.getFromLocation(
                    latitude,
                    longitude, 1
                )
            if (addresses.size > 0) {
                val address = addresses[0]
                var baseAddress = ""
                var endAddress = ""
                if (address != null && address.maxAddressLineIndex > 2) {
                    baseAddress = address.getAddressLine(0)
                    endAddress = address.getAddressLine(1)
                }
                return baseAddress + endAddress
            }
        } catch (e: IOException) {
            e.printStackTrace()
        }
        return "获取失败"
    }

    fun getIPAddress(): String? {
        return getIPAddress(true)
    }

    fun getIPAddress(useIPv4: Boolean): String? {
        try {
            val nis =
                NetworkInterface.getNetworkInterfaces()
            val adds =
                LinkedList<InetAddress>()
            while (nis.hasMoreElements()) {
                val ni = nis.nextElement()
                // To prevent phone of xiaomi return "10.0.2.15"
                if (!ni.isUp || ni.isLoopback) continue
                val addresses = ni.inetAddresses
                while (addresses.hasMoreElements()) {
                    adds.addFirst(addresses.nextElement())
                }
            }
            for (add in adds) {
                if (!add.isLoopbackAddress) {
                    val hostAddress = add.hostAddress
                    val isIPv4 = hostAddress.indexOf(':') < 0
                    if (useIPv4) {
                        if (isIPv4) return hostAddress
                    } else {
                        if (!isIPv4) {
                            val index = hostAddress.indexOf('%')
                            return if (index < 0) hostAddress.toUpperCase() else hostAddress.substring(
                                0,
                                index
                            ).toUpperCase()
                        }
                    }
                }
            }
        } catch (e: SocketException) {
            e.printStackTrace()
        }
        return ""
    }

    fun isDeviceRooted(): Boolean {
        val su = "su"
        val locations = arrayOf(
            "/system/bin/", "/system/xbin/", "/sbin/", "/system/sd/xbin/",
            "/system/bin/failsafe/", "/data/local/xbin/", "/data/local/bin/", "/data/local/"
        )
        for (location in locations) {
            if (File(location + su).exists()) {
                return true
            }
        }
        return false
    }

    fun isSimulator(context: Context): Boolean {
        val url = "tel:" + "123456"
        val intent = Intent()
        intent.data = Uri.parse(url)
        intent.action = Intent.ACTION_DIAL
        // 是否可以处理跳转到拨号的 Intent
        val canResolveIntent =
            intent.resolveActivity(context.packageManager) != null
        return (Build.FINGERPRINT.startsWith("generic")
                || Build.FINGERPRINT.toLowerCase().contains("vbox")
                || Build.FINGERPRINT.toLowerCase().contains("test-keys")
                || Build.MODEL.contains("google_sdk")
                || Build.MODEL.contains("Emulator")
                || Build.MODEL.contains("MuMu")
                || Build.MODEL.contains("virtual")
                || Build.MODEL.contains("Android SDK built for x86")
                || Build.SERIAL.equals("android", ignoreCase = true)
                || Build.MANUFACTURER.contains("Genymotion")
                || Build.BRAND.startsWith("generic") && Build.DEVICE.startsWith("generic")
                || "google_sdk" == Build.PRODUCT || (context.getSystemService(Context.TELEPHONY_SERVICE) as TelephonyManager).networkOperatorName
            .toLowerCase() == "android" || !canResolveIntent)
    }

    fun getIMSI(context: Context): String? {
        try {
            val tm =
                context.getSystemService(Context.TELEPHONY_SERVICE) as TelephonyManager
            if (ActivityCompat.checkSelfPermission(
                    context,
                    Manifest.permission.READ_PHONE_STATE
                ) == PackageManager.PERMISSION_GRANTED
            ) {
                return tm.subscriberId
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return null
    }

    fun getMacAddress(context: Context): String? {
        return getMacAddress(
            context, null
        )
    }

    fun getMacAddress(
        context: Context,
        vararg excepts: String?
    ): String? {
        var macAddress =
            getMacAddressByWifiInfo(context)
        if (isAddressNotInExcepts(
                macAddress,
                *excepts
            )
        ) {
            return macAddress
        }
        macAddress =
            getMacAddressByNetworkInterface()
        if (isAddressNotInExcepts(
                macAddress,
                *excepts
            )
        ) {
            return macAddress
        }
        macAddress =
            getMacAddressByInetAddress()
        if (isAddressNotInExcepts(
                macAddress,
                *excepts
            )
        ) {
            return macAddress
        }
        // root的情况       macAddress = getMacAddressByFile();
        return if (isAddressNotInExcepts(
                macAddress,
                *excepts
            )
        ) {
            macAddress
        } else ""
    }

    fun getInetAddress(): InetAddress? {
        try {
            val nis =
                NetworkInterface.getNetworkInterfaces()
            while (nis.hasMoreElements()) {
                val ni = nis.nextElement()
                // To prevent phone of xiaomi return "10.0.2.15"
                if (!ni.isUp) continue
                val addresses = ni.inetAddresses
                while (addresses.hasMoreElements()) {
                    val inetAddress = addresses.nextElement()
                    if (!inetAddress.isLoopbackAddress) {
                        val hostAddress = inetAddress.hostAddress
                        if (hostAddress.indexOf(':') < 0) return inetAddress
                    }
                }
            }
        } catch (e: SocketException) {
            e.printStackTrace()
        }
        return null
    }

    fun getMacAddressByInetAddress(): String {
        try {
            val inetAddress =
                getInetAddress()
            if (inetAddress != null) {
                val ni =
                    NetworkInterface.getByInetAddress(inetAddress)
                if (ni != null) {
                    val macBytes = ni.hardwareAddress
                    if (macBytes != null && macBytes.size > 0) {
                        val sb = StringBuilder()
                        for (b in macBytes) {
                            sb.append(String.format("%02x:", b))
                        }
                        return sb.substring(0, sb.length - 1)
                    }
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return "02:00:00:00:00:00"
    }

    fun isAddressNotInExcepts(
        address: String,
        vararg excepts: String?
    ): Boolean {
        if (excepts == null || excepts.size == 0) {
            return "02:00:00:00:00:00" != address
        }
        for (filter in excepts) {
            if (address == filter) {
                return false
            }
        }
        return true
    }

    fun getMacAddressByNetworkInterface(): String {
        try {
            val nis =
                NetworkInterface.getNetworkInterfaces()
            while (nis.hasMoreElements()) {
                val ni = nis.nextElement()
                if (ni == null || !ni.name.equals("wlan0", ignoreCase = true)) continue
                val macBytes = ni.hardwareAddress
                if (macBytes != null && macBytes.size > 0) {
                    val sb = StringBuilder()
                    for (b in macBytes) {
                        sb.append(String.format("%02x:", b))
                    }
                    return sb.substring(0, sb.length - 1)
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return "02:00:00:00:00:00"
    }

    fun getMacAddressByWifiInfo(context: Context): String {
        try {
            val wifi =
                context.getSystemService(Context.WIFI_SERVICE) as WifiManager
            if (wifi != null) {
                val info = wifi.connectionInfo
                if (info != null) return info.macAddress
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return "02:00:00:00:00:00"
    }


    fun getElectricQuantity(context: Context): String? {
        val manager =
            context.getSystemService(Context.BATTERY_SERVICE) as BatteryManager
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            manager.getIntProperty(BatteryManager.BATTERY_PROPERTY_CAPACITY).toString() + ""
        } else ""
    }

    fun getScreenDensityDpi(context: Context?): String? {
        return MyApplication.screenWidth.toString() + "x" + MyApplication.screenHeight.toShort()
    }

    fun getBrand(): String? {
        return Build.BRAND
    }

    fun getIMEI(context: Context): String? {
        try {
            val tm =
                context.getSystemService(Context.TELEPHONY_SERVICE) as TelephonyManager
            if (ActivityCompat.checkSelfPermission(
                    context,
                    Manifest.permission.READ_PHONE_STATE
                ) == PackageManager.PERMISSION_GRANTED
            ) {
                return tm.deviceId
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return null
    }

    fun getRamTotalSize(context: Context): Long {
        var systemTotalMemorySize: Long = 0
        try {
            //获得ActivityManager服务->获得MemoryInfo对象
            val mActivityManager =
                context.getSystemService(Context.ACTIVITY_SERVICE) as ActivityManager
            val memoryInfo = ActivityManager.MemoryInfo()
            //获得系统可用内存，保存在MemoryInfo对象上
            mActivityManager.getMemoryInfo(memoryInfo)
            val memSize = memoryInfo.totalMem
            systemTotalMemorySize = memSize
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return systemTotalMemorySize
    }

    fun getRomTotalSize(): Long {
        var romTotalSize: Long = 0
        try {
            val path = Environment.getDataDirectory()
            val stat = StatFs(path.path)
            val blockSize = stat.blockSize.toLong()
            val totalBlocks = stat.blockCount.toLong()
            romTotalSize = blockSize * totalBlocks
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return romTotalSize
    }

    fun getSimOperatorByMnc(context: Context): String? {
        val tm =
            context.getSystemService(Context.TELEPHONY_SERVICE) as TelephonyManager
        val operator = tm.simOperator ?: return ""
        return when (operator) {
            "46000", "46002", "46007", "46020" -> "中国移动"
            "46001", "46006", "46009" -> "中国联通"
            "46003", "46005", "46011" -> "中国电信"
            else -> operator
        }
    }

    fun getRomAvailableSize(): Long {
        var romAvailableSize: Long = 0
        try {
            val path = Environment.getDataDirectory()
            val stat = StatFs(path.path)
            val blockSize = stat.blockSize.toLong()
            val availableBlocks = stat.availableBlocks.toLong()
            romAvailableSize = blockSize * availableBlocks
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return romAvailableSize
    }

    fun getSDTotalSize(): Long {
        var sdTotalSize: Long = 0
        try {
            if (!hasSDCard()) {
//                Toast.makeText(context, "内存卡不存在", Toast.LENGTH_SHORT).show();
                return sdTotalSize
            }
            val path = Environment.getExternalStorageDirectory()
            val stat = StatFs(path.path)
            val blockSize = stat.blockSize.toLong()
            val totalBlocks = stat.blockCount.toLong()
            sdTotalSize = blockSize * totalBlocks
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return sdTotalSize
    }

    /**
     * 内存卡是否存在
     *
     * @return
     */
    fun hasSDCard(): Boolean {
        return Environment.getExternalStorageState() == Environment.MEDIA_MOUNTED && !Environment.isExternalStorageRemovable()
    }

    fun getSDAvailableSize(): Long {
        var sdAvailableSize: Long = 0
        try {
            if (!hasSDCard()) {
                return sdAvailableSize
                //                Toast.makeText(context, "内存卡不存在", Toast.LENGTH_SHORT).show();
            }
            val path = Environment.getExternalStorageDirectory()
            val stat = StatFs(path.path)
            val blockSize = stat.blockSize.toLong()
            val availableBlocks = stat.availableBlocks.toLong()
            sdAvailableSize = blockSize * availableBlocks
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return sdAvailableSize
    }

    fun isLocationEnabled(context: Context): Boolean {
        var locationMode = 0
        val locationProviders: String
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            locationMode = try {
                Settings.Secure.getInt(
                    context.contentResolver,
                    Settings.Secure.LOCATION_MODE
                )
            } catch (e: SettingNotFoundException) {
                e.printStackTrace()
                return false
            }
            locationMode != Settings.Secure.LOCATION_MODE_OFF
        } else {
            locationProviders = Settings.Secure.getString(
                context.contentResolver,
                Settings.Secure.LOCATION_PROVIDERS_ALLOWED
            )
            !TextUtils.isEmpty(locationProviders)
        }
    }
}