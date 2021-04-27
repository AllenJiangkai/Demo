package com.mari.uang.util

import android.content.Context
import android.util.Log
import com.appsflyer.AppsFlyerConversionListener
import com.appsflyer.AppsFlyerLib
import com.coupang.common.user.UserManager.isLogin
import com.coupang.common.utils.ContextUtils
import com.mari.uang.BuildConfig
import java.util.*

object EventUtil {


    //埋点事件统计
    fun event(context: Context?, action: String) {
        if (isLogin()) return
        AppsFlyerLib.getInstance().trackEvent(context, action, HashMap())
    }

    fun initAF() {
        val conversionListener: AppsFlyerConversionListener = object : AppsFlyerConversionListener {
            override fun onConversionDataSuccess(map: Map<String, Any>) {
                for (attrName in map.keys) {
                    Log.d("AFTool", "attribute: " + attrName + " = " + map[attrName])
                }
            }

            override fun onConversionDataFail(errorMessage: String) {
                Log.e("AFTool", "AF onConversionDataFail = $errorMessage")
            }

            override fun onAppOpenAttribution(conversionData: Map<String, String>) {
                Log.e("AFTool", "AF Attribution")
                for (attrName in conversionData.keys) {
                    Log.e(
                        "AFTool",
                        "AF Attribution = " + attrName + " : " + conversionData[attrName]
                    )
                }
            }

            override fun onAttributionFailure(errorMessage: String) {
                Log.e("AFTool", "AF AttributionFailure = $errorMessage")
            }
        }
        AppsFlyerLib.getInstance()
            .init(BuildConfig.AF_KEY, conversionListener, ContextUtils.getApplication())
        AppsFlyerLib.getInstance().startTracking(ContextUtils.getApplication())
        AppsFlyerLib.getInstance().setDebugLog(true)
    }



}