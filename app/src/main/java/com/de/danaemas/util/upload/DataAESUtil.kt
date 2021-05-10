package com.de.danaemas.util.upload

import android.util.Base64
import okhttp3.internal.and
import java.util.*
import javax.crypto.Cipher
import javax.crypto.spec.IvParameterSpec
import javax.crypto.spec.SecretKeySpec

/**
 *
 * @ProjectName:    Business
 * @Package:        com.alan.business.util.upload
 * @ClassName:      DataAESUtil
 * @Description:     java类作用描述
 * @Author:         jtao
 * @CreateDate:     2021/4/20 4:48 PM
 * @UpdateUser:     更新者
 * @UpdateDate:     2021/4/20 4:48 PM
 * @UpdateRemark:   更新说明
 * @Version:        1.0
 */
object DataAESUtil {

    private const val iv = "0123456789ABCDEF"

    //AES是加密方式 CBC是工作模式 PKCS5Padding是填充模式
    private const val CBC_PKCS7PAD = "AES/CBC/PKCS7PADDING"

    //AES 加密
    private const val AES = "AES"

    private const val key = "5029c32577b080a2"


    fun encrypt(cleartext: String?): String? {
        return encrypt(key, cleartext!!)
    }
    
    fun decrypt(encrypted: String?): String? {
        return DataAESUtil.decrypt(
            key,
            encrypted
        )
    }
    
    /**
     * 加密：对字符串进行加密，并返回十六进制字符串(hex)
     *
     * @param cleartext 需要加密的字符串
     * @return 加密后的十六进制字符串(hex)
     */
    fun encrypt(key: String, cleartext: String): String? {
        try {
            val ivParameterSpec =
                IvParameterSpec(
                    iv.toByteArray(charset("UTF-8"))
                )
            val skeySpec = SecretKeySpec(
                key.toByteArray(charset("UTF-8")),
                AES
            )
            val cipher =
                Cipher.getInstance(CBC_PKCS7PAD)
            cipher.init(Cipher.ENCRYPT_MODE, skeySpec, ivParameterSpec)
            val encrypted = cipher.doFinal(cleartext.toByteArray())
            val encode =
                Base64.encode(encrypted, Base64.NO_WRAP)
            return String(encode)
            //     return byte2HexStr(encrypted);
        } catch (ex: Exception) {
            ex.printStackTrace()
        }
        return null
    }


    /**
     * 解密：对加密后的十六进制字符串(hex)进行解密，并返回字符串
     *
     * @param encrypted 需要解密的，加密后的十六进制字符串
     * @return 解密后的字符串
     */
    fun decrypt(key: String, encrypted: String?): String? {
        try {
            val ivParameterSpec =
                IvParameterSpec(
                    iv.toByteArray(charset("UTF-8"))
                )
            val skeySpec = SecretKeySpec(
                key.toByteArray(charset("UTF-8")),
                AES
            )
            val cipher =
                Cipher.getInstance(CBC_PKCS7PAD)
            cipher.init(Cipher.DECRYPT_MODE, skeySpec, ivParameterSpec)
            val decode =
                Base64.decode(encrypted, Base64.NO_WRAP)

            //  byte[] bytes = hexStr2Bytes(encryptedStr);
            val original = cipher.doFinal(decode)
            return String(original)
        } catch (ex: Exception) {
            ex.printStackTrace()
        }
        return null
    }

    /**
     * 十六进制字符串转换为byte[]
     *
     * @param hexStr 需要转换为byte[]的字符串
     * @return 转换后的byte[]
     */
    fun hexStr2Bytes(hexStr: String): ByteArray? {


        /*对输入值进行规范化整理*/
        var hexStr = hexStr
        hexStr = hexStr.trim { it <= ' ' }.replace(" ", "").toUpperCase(Locale.US)
        //处理值初始化
        var m = 0
        var n = 0
        val iLen = hexStr.length / 2 //计算长度
        val ret = ByteArray(iLen) //分配存储空间
        for (i in 0 until iLen) {
            m = i * 2 + 1
            n = m + 1
            ret[i] = (Integer.decode(
                "0x" + hexStr.substring(i * 2, m) + hexStr.substring(
                    m,
                    n
                )
            ) and 0xFF).toByte()
        }
        return ret
    }


    /**
     * byte[]转换为十六进制字符串
     *
     * @param bytes 需要转换为字符串的byte[]
     * @return 转换后的十六进制字符串
     */
    fun byte2HexStr(bytes: ByteArray): String? {
        var hs = ""
        var stmp = ""
        for (n in bytes.indices) {
            stmp = Integer.toHexString(bytes[n] and 0XFF)
            hs = if (stmp.length == 1) hs + "0" + stmp else hs + stmp
        }
        return hs
    }
    
}

