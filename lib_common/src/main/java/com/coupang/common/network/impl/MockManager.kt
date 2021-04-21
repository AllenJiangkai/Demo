package com.coupang.common.network.impl

/**
 * @author Allen
 * @date 2020-12-28.
 */
interface MockManager {
    fun isOpenMockEnvironment():Boolean
    fun getMockDataByUrl(url:String):String
    fun getMockStatusByUrl(url:String):Boolean
}