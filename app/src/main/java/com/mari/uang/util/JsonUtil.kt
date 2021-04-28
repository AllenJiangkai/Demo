package com.mari.uang.util

import android.content.Context
import com.alibaba.fastjson.JSONObject
import com.mari.uang.module.basic.citydialog.MUCityModel
import java.io.BufferedReader
import java.io.IOException
import java.io.InputStreamReader

/**
 *
 * @ProjectName:    Business
 * @Package:        com.alan.business.util
 * @ClassName:      JsonUtil
 * @Description:     java类作用描述
 * @Author:         jtao
 * @CreateDate:     2021/4/17 6:07 PM
 * @UpdateUser:     更新者
 * @UpdateDate:     2021/4/17 6:07 PM
 * @UpdateRemark:   更新说明
 * @Version:        1.0
 */
object JsonUtil {
    
    fun getCityList(context: Context?): MutableList<MUCityModel>? {
        val jsonStr = context?.let {
            getJson(
                it,
                "cityData.json"
            )
        }
        return JSONObject.parseArray(jsonStr, MUCityModel::class.java)
    }

    fun getJson(context: Context, fileName: String?): String? {
        val stringBuilder = StringBuilder()
        try {
            val assetManager = context.applicationContext.assets
            val bf =
                BufferedReader(InputStreamReader(assetManager.open(fileName!!)))
            var line: String?
            while (bf.readLine().also { line = it } != null) {
                stringBuilder.append(line)
            }
        } catch (e: IOException) {
            e.printStackTrace()
        }
        return stringBuilder.toString()
    }

    fun toJsonString(obj: Any?): String? {
        return if (obj == null) "" else JSONObject.toJSONString(obj)
    }
}