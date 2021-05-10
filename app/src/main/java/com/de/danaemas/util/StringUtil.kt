package com.de.danaemas.util

import android.text.TextUtils
import java.text.DecimalFormat
import java.util.regex.Pattern

/**
 *
 * @ProjectName:    Business
 * @Package:        com.mari.uang.util
 * @ClassName:      StringUtil
 * @Description:     java类作用描述
 * @Author:         jtao
 * @CreateDate:     2021/4/22 4:17 PM
 * @UpdateUser:     更新者
 * @UpdateDate:     2021/4/22 4:17 PM
 * @UpdateRemark:   更新说明
 * @Version:        1.0
 */
object StringUtil {

    // 判断是否是色值
    fun isColor(color: String?): Boolean {
        if (TextUtils.isEmpty(color)) return false
        val p = Pattern.compile("#[0-9a-fA-F]{6,8}$")
        val m = p.matcher(color)
        return m.matches()
    }

    /**
     * 转换为数字
     * @param
     * @return
     */
    fun toNum(str: String?): String? {
        var str = str
        val regEx = "[^0-9]"
        val p = Pattern.compile(regEx)
        val m = p.matcher(str)
        str = m.replaceAll("").trim { it <= ' ' }
        return str
    }

    /**
     * 转换文件大小
     * @param fileS
     * @return
     */
    fun formetFileSize(fileS: Long): String? {
        val df = DecimalFormat("#.00")
        var fileSizeString = ""
        val wrongSize = "0B"
        if (fileS == 0L) {
            return wrongSize
        }
        fileSizeString = if (fileS < 1024) {
            df.format(fileS.toDouble()) + "B"
        } else if (fileS < 1048576) {
            df.format(fileS.toDouble() / 1024) + "KB"
        } else if (fileS < 1073741824) {
            df.format(fileS.toDouble() / 1048576) + "MB"
        } else {
            df.format(fileS.toDouble() / 1073741824) + "GB"
        }
        return fileSizeString
    }

    /**
     * 手机号加密
     * @param phone
     * @return
     */
    fun encryptionPhone(phone: String?): String? {
        if (TextUtils.isEmpty(phone)) return ""
        val sb = StringBuilder(phone!!)
        return sb.replace(3, 7, "****").toString()
    }

}