package com.mari.uang

import android.content.Context
import android.os.Process
import android.util.Log
import androidx.multidex.MultiDexApplication
import com.coupang.common.impl.Tools
import com.coupang.common.utils.ContextUtils
import com.mari.uang.util.ToolsManager
import com.tencent.smtt.sdk.QbSdk


/**
 * @author Alan_Xiong
 *
 * @desc: application
 * @time 2020/5/28 4:03 PM
 */
class MyApplication : MultiDexApplication() {

    override fun attachBaseContext(base: Context?) {
        super.attachBaseContext(base)
    }

    override fun onCreate() {
        super.onCreate()
        ContextUtils.init(this)
        Tools.initData(ToolsManager())
        initX5()
        val displayMetrics = applicationContext.resources.displayMetrics
        screenWidth = displayMetrics.widthPixels
        screenHeight = displayMetrics.heightPixels
        baseCox = this
    }

    companion object {
        var screenWidth = 0
        var screenHeight = 0
        private var baseCox : Context? = null
        fun baseCox() : Context? {
            return baseCox
        }
    }
    private fun initX5() {

        //x5内核初始化接口
        QbSdk.setDownloadWithoutWifi(true)
        QbSdk.initX5Environment(applicationContext, object : QbSdk.PreInitCallback {
            override fun onCoreInitFinished() {

            }

            override fun onViewInitFinished(p0: Boolean) {
                //x5內核初始化完成的回调，为true表示x5内核加载成功，否则表示x5内核加载失败，会自动切换到系统内核。
                Log.d("app", " onViewInitFinished is $p0")
            }

        })
    }


    /**
     * 轻量线程异步加载
     */
    private fun initThirdServer() {
        Thread {
            Process.setThreadPriority(Process.THREAD_PRIORITY_BACKGROUND)

        }.start()
    }


}