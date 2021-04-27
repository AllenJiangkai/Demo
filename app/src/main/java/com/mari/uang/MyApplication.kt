package com.mari.uang

import android.content.Context
import android.os.Process
import android.util.Log
import androidx.multidex.MultiDexApplication
import com.coupang.common.impl.Tools
import com.coupang.common.utils.ContextUtils
import com.mari.uang.util.ToolsManager


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


    /**
     * 轻量线程异步加载
     */
    private fun initThirdServer() {
        Thread {
            Process.setThreadPriority(Process.THREAD_PRIORITY_BACKGROUND)

        }.start()
    }


}