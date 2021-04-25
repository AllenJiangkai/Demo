package com.coupang.common.impl

import android.content.Context

/**
 *
 * @ProjectName:    Business
 * @Package:        com.coupang.common.impl
 * @ClassName:      ToolsImpl
 * @Description:     java类作用描述
 * @Author:         jtao
 * @CreateDate:     2021/4/24 2:17 PM
 * @UpdateUser:     更新者
 * @UpdateDate:     2021/4/24 2:17 PM
 * @UpdateRemark:   更新说明
 * @Version:        1.0
 */
interface ToolsImpl {
    fun getDeviceId(context: Context):String
    fun getDeviceName(context: Context):String
    fun getOsVersion(context: Context):String

}

object  Tools  {
    var tools :ToolsImpl?=null
    fun initData(  tools :ToolsImpl ) {
       this.tools=tools
    }


}