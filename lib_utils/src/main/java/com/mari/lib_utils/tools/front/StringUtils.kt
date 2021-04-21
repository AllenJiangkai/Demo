package com.mari.lib_utils.tools.front

import android.text.TextUtils
import java.io.UnsupportedEncodingException
import java.net.URLEncoder
import java.util.regex.Pattern

/**
 * package: com.easytools.tools.StringUtils
 * author: gyc
 * description:字符串相关的工具类
 * time: create at 2016/10/15 13:47
 */
object StringUtils {
    /**
     * 判断字符串是否为null或长度为0（不包含空格，即如果有空格，则返回false）
     *
     * @param str 待校验字符串
     * @return `true`:空<br></br> `false`: 不为空
     */
    private fun isEmpty(str: CharSequence?): Boolean {
        return str == null || str.length == 0
    }

    /**
     * 判断字符串是否为null或全为空格（如果有空格，则返回true）
     *
     * @param str 待校验字符串
     * @return `true`: null或全空格<br></br> `false`: 不为null且不全空格
     */
    fun isSpace(str: String?): Boolean {
        return str == null || str.trim { it <= ' ' }.isEmpty()
    }

    /**
     * null对象转为长度为0的空字符串
     *
     * @param str 待转对象
     * @return s为null转为长度为0字符串，否则不改变
     */
    fun nullStrToEmpty(str: Any?): String {
        return if (str == null) "" else if (str is String) (str as String?)!! else str.toString()
    }

    /**
     * 返回字符串长度
     *
     * @param str 字符串
     * @return null返回0，其他返回自身长度
     */
    fun length(str: CharSequence?): Int {
        return str?.length ?: 0
    }

    /**
     * 字符串比较
     *
     * @param actual   待比较字符串
     * @param expected 比较字符串
     * @return 是否相同
     */
    fun isEquals(actual: String?, expected: String?): Boolean {
        return TextUtils.equals(actual, expected)
    }

    /**
     * 首字母大写
     * 如：
     * capitalizeFirstLetter(null)     =   null;
     * capitalizeFirstLetter("")       =   "";
     * capitalizeFirstLetter("2ab")    =   "2ab"
     * capitalizeFirstLetter("a")      =   "A"
     * capitalizeFirstLetter("ab")     =   "Ab"
     * capitalizeFirstLetter("Abc")    =   "Abc"
     *
     * @param str 待转字符串
     * @return 首字母大写字符串
     */
    fun capitalizeFirstLetter(str: String): String {
        if (isEmpty(str)) {
            return str
        }
        val c = str[0]
        return if (!Character.isLetter(c) || Character.isUpperCase(c)) str else StringBuilder(
            str.length
        )
            .append(Character.toUpperCase(c)).append(str.substring(1)).toString()
    }

    /**
     * 首字母小写
     *
     * @param str 待转字符串
     * @return 首字母小写字符串
     */
    fun lowerFirstLetter(str: String): String {
        if (isEmpty(str)) {
            return str
        }
        val c = str[0]
        return if (!Character.isLetter(c) || Character.isLowerCase(c)) str else StringBuilder(
            str.length
        )
            .append(Character.toLowerCase(c)).append(str.substring(1)).toString()
    }

    /**
     * 反转字符串
     *
     * @param str 待反转字符串
     * @return 反转字符串
     */
    fun reverse(str: String): String {
        val len = length(str)
        if (len <= 1) return str
        val mid = len shr 1 //移位操作，此为右移1位，相当于除以2
        val chars = str.toCharArray()
        var c: Char
        for (i in 0 until mid) {
            c = chars[i]
            chars[i] = chars[len - i - 1]
            chars[len - i - 1] = c
        }
        return String(chars)
    }

    /**
     * 去掉字符串里的回车（\n）、水平制表符(\t)、空格(\s)和换行（\r）
     *
     * @param str
     * @return
     */
    fun replaceBlank(str: String?): String {
        var dest = ""
        if (str != null) {
            val p = Pattern.compile("\\s*|\t|\r|\n")
            val m = p.matcher(str)
            dest = m.replaceAll("")
        }
        return dest
    }

    /**
     * 使用utf8编码
     * 如：
     * utf8Encode(null)         =   null
     * utf8Encode("")           =   "";
     * utf8Encode("aa")         =   "aa";
     * utf8Encode("啊啊啊啊")   = "%E5%95%8A%E5%95%8A%E5%95%8A%E5%95%8A";
     *
     * @param str 字符串
     * @return utf8编码
     */
    fun utf8Encode(str: String): String {
        return if (!isEmpty(str) && str.toByteArray().size != str.length) {
            try {
                URLEncoder.encode(str, "UTF-8")
            } catch (e: UnsupportedEncodingException) {
                throw RuntimeException("UnsupportedEncodingException occurred. ", e)
            }
        } else str
    }

    /**
     * 使用utf-8编码, 转码异常返回默认值
     *
     * @param str          字符串
     * @param defultReturn utf8编码
     * @return
     */
    fun utf8Encode(str: String, defultReturn: String): String {
        return if (!isEmpty(str) && str.toByteArray().size != str.length) {
            try {
                URLEncoder.encode(str, "UTF-8")
            } catch (e: UnsupportedEncodingException) {
                defultReturn
            }
        } else str
    }


    /**
     * 单个汉字转成ASCII码
     *
     * @param str 单个汉字字符串
     * @return 如果字符串长度是1返回的是对应的ascii码，否则返回-1
     */
    private fun oneCn2ASCII(str: String): Int {
        if (str.length != 1) return -1
        var ascii = 0
        try {
            val bytes = str.toByteArray(charset("GB2312"))
            ascii = if (bytes.size == 1) {
                bytes[0].toInt()
            } else if (bytes.size == 2) {
                val highByte = 256 + bytes[0]
                val lowByte = 256 + bytes[1]
                256 * highByte + lowByte - 256 * 256
            } else {
                throw IllegalArgumentException("Illegal resource string")
            }
        } catch (e: UnsupportedEncodingException) {
            e.printStackTrace()
        }
        return ascii
    }



    /**
     * 判断字符串是否只包含unicode数字。
     *
     *
     *
     * `null`将返回`false`，空字符串`""`将返回`true`。
     *
     * <pre>
     * StringUtils.isNumeric(null)   = false
     * StringUtils.isNumeric("")     = true
     * StringUtils.isNumeric("  ")   = false
     * StringUtils.isNumeric("123")  = true
     * StringUtils.isNumeric("12 3") = false
     * StringUtils.isNumeric("ab2c") = false
     * StringUtils.isNumeric("12-3") = false
     * StringUtils.isNumeric("12.3") = false
    </pre> *
     *
     * @param str 要检查的字符串
     * @return 如果字符串非`null`并且全由unicode数字组成，则返回`true`
     */
    fun isNumeric(str: String?): Boolean {
        if (str == null) {
            return false
        }
        val length = str.length
        for (i in 0 until length) {
            if (!Character.isDigit(str[i])) {
                return false
            }
        }
        return true
    }
}