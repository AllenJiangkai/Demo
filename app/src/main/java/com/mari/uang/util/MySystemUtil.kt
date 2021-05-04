package com.mari.uang.util

import android.app.ActivityManager
import android.content.Context

/**
 *
 * @ProjectName:    Business
 * @Package:        com.mari.uang.util
 * @ClassName:      MySystemUtil
 * @Description:     java类作用描述
 * @Author:         jtao
 * @CreateDate:     2021/5/2 12:18 PM
 * @UpdateUser:     更新者
 * @UpdateDate:     2021/5/2 12:18 PM
 * @UpdateRemark:   更新说明
 * @Version:        1.0
 */
object MySystemUtil {

    /**
     * 获取进程名称
     * @param cxt
     * @param pid
     * @return
     */
    fun getCurrentProcessName(cxt: Context, pid: Int): String? {
        val am =
            cxt.getSystemService(Context.ACTIVITY_SERVICE) as ActivityManager
        val runningApps =
            am.runningAppProcesses ?: return null
        for (procInfo in runningApps) {
            if (procInfo.pid == pid) {
                return procInfo.processName
            }
        }
        return null
    }

}