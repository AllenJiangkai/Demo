package com.mari.uang.util

import android.content.Context
import com.coupang.common.impl.ToolsImpl

/**
 *
 * @ProjectName:    Business
 * @Package:        com.mari.uang.util
 * @ClassName:      ToolsManager
 * @Description:     java类作用描述
 * @Author:         jtao
 * @CreateDate:     2021/4/24 2:21 PM
 * @UpdateUser:     更新者
 * @UpdateDate:     2021/4/24 2:21 PM
 * @UpdateRemark:   更新说明
 * @Version:        1.0
 */

class ToolsManager :ToolsImpl{
    override fun getDeviceId(context: Context): String{
        return MyDeviceUtil.getDeviceId(context)?:""
    }

    override fun getDeviceName(context: Context): String {
        return MyDeviceUtil.getDeviceName()?:""
    }

    override fun getOsVersion(context: Context): String {
        return MyDeviceUtil.getOsVersion()?:""
    }
}