package com.coupang.common.network

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.net.ConnectivityManager
import android.telephony.TelephonyManager
import androidx.core.app.ActivityCompat

class NetUtil {
    /**
     * 获取当前网络连接的类型
     *
     * @return int
     */
    fun getNetType(context: Context): NetType {
        val connectivityManager = context
            .getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
            ?: return NetType.NONE
        val networkInfo = connectivityManager.activeNetworkInfo ?: return NetType.NONE
        val nType = networkInfo.type
        if (nType == ConnectivityManager.TYPE_MOBILE) {
            return getMobileNetType(context)
        } else if (nType == ConnectivityManager.TYPE_WIFI) {
            return NetType.WIFI
        }
        return NetType.NONE
    }

    enum class NetType {
        //未知网络
        NET_UNKNOW,  //4G
        NET_4G,  //3G
        NET_3G,  //2G
        NET_2G,  //wifi
        WIFI,  //没有网络
        NONE,
        NET_5G
    }

    companion object {
        private fun getMobileNetType(context: Context): NetType {
            val mTelephonyManager = (context
                .getSystemService(Context.TELEPHONY_SERVICE) as TelephonyManager)
            if (ActivityCompat.checkSelfPermission(
                    context,
                    Manifest.permission.READ_PHONE_STATE
                ) != PackageManager.PERMISSION_GRANTED
            ) {
                // here to request the missing permissions, and then overriding
                //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                //                                          int[] grantResults)
                // to handle the case where the user grants the permission. See the documentation
                // for ActivityCompat#requestPermissions for more details.
            }
            val networkType = mTelephonyManager.networkType
            return getNetworkClass(networkType)
        }
        private fun getNetworkClass(networkType: Int): NetType {
            return when (networkType) {
                TelephonyManager.NETWORK_TYPE_GPRS, TelephonyManager.NETWORK_TYPE_EDGE, TelephonyManager.NETWORK_TYPE_CDMA, TelephonyManager.NETWORK_TYPE_1xRTT, TelephonyManager.NETWORK_TYPE_IDEN -> NetType.NET_2G
                TelephonyManager.NETWORK_TYPE_UMTS, TelephonyManager.NETWORK_TYPE_EVDO_0, TelephonyManager.NETWORK_TYPE_EVDO_A, TelephonyManager.NETWORK_TYPE_HSDPA, TelephonyManager.NETWORK_TYPE_HSUPA, TelephonyManager.NETWORK_TYPE_HSPA, TelephonyManager.NETWORK_TYPE_EVDO_B, TelephonyManager.NETWORK_TYPE_EHRPD, TelephonyManager.NETWORK_TYPE_HSPAP -> NetType.NET_3G
                TelephonyManager.NETWORK_TYPE_LTE -> NetType.NET_4G
                TelephonyManager.NETWORK_TYPE_NR-> NetType.NET_5G
                else -> NetType.NET_UNKNOW
            }
        }
    }
}