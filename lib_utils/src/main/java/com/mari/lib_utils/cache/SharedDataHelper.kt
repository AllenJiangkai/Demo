package com.mari.lib_utils.cache

import android.content.Context
import android.content.SharedPreferences
import android.util.Base64
import java.io.*


/**
 * @author Alan_Xiong
 *
 * @desc: sharePreference 工具
 * @time 2020/5/27 4:16 PM
 */
class SharedDataHelper private constructor() {
    companion object {
        private var mSharedPreferences: SharedPreferences? = null
        private const val SP_NAME = "config"

        /**
         * 存储重要信息到sharedPreferences；
         *
         * @param key
         * @param value
         */
        fun setStringSF(
            context: Context,
            key: String?,
            value: String?
        ) {
            if (mSharedPreferences == null) {
                mSharedPreferences = context.getSharedPreferences(
                    SP_NAME,
                    Context.MODE_PRIVATE
                )
            }
            mSharedPreferences!!.edit().putString(key, value).apply()
        }

        /**
         * 返回存在sharedPreferences的信息
         *
         * @param key
         * @return
         */
        fun getStringSF(context: Context, key: String?): String {
            if (mSharedPreferences == null) {
                mSharedPreferences = context.getSharedPreferences(
                    SP_NAME,
                    Context.MODE_PRIVATE
                )
            }
            return mSharedPreferences!!.getString(key, "").toString()
        }

        /**
         * 存储重要信息到sharedPreferences；
         *
         * @param key
         * @param value
         */
        fun setIntergerSF(
            context: Context,
            key: String?,
            value: Int
        ) {
            if (mSharedPreferences == null) {
                mSharedPreferences = context.getSharedPreferences(
                    SP_NAME,
                    Context.MODE_PRIVATE
                )
            }
            mSharedPreferences!!.edit().putInt(key, value).apply()
        }

        /**
         * 返回存在sharedPreferences的信息
         *
         * @param key
         * @return
         */
        fun getIntergerSF(context: Context, key: String?): Int {
            if (mSharedPreferences == null) {
                mSharedPreferences = context.getSharedPreferences(
                    SP_NAME,
                    Context.MODE_PRIVATE
                )
            }
            return mSharedPreferences!!.getInt(key, -1)
        }

        /**
         * 清除某个内容
         */
        fun removeSF(context: Context, key: String?) {
            if (mSharedPreferences == null) {
                mSharedPreferences = context.getSharedPreferences(
                    SP_NAME,
                    Context.MODE_PRIVATE
                )
            }
            mSharedPreferences!!.edit().remove(key).apply()
        }

        /**
         * clear SharedPreferences
         */
        fun clearShareprefrence(context: Context) {
            if (mSharedPreferences == null) {
                mSharedPreferences = context.getSharedPreferences(
                    SP_NAME,
                    Context.MODE_PRIVATE
                )
            }
            mSharedPreferences!!.edit().clear().apply()
        }

        /**
         * 将对象储存到SharedPreferences
         *
         * @param key
         * @param device
         * @param <T>
        </T> */
        fun <T> saveDeviceData(
            context: Context,
            key: String?,
            device: T
        ): Boolean {
            if (mSharedPreferences == null) {
                mSharedPreferences = context.getSharedPreferences(
                    SP_NAME,
                    Context.MODE_PRIVATE
                )
            }
            val bytes = ByteArrayOutputStream()
            return try { //Device为自定义类
                // 创建对象输出流，并封装字节流
                val oos = ObjectOutputStream(bytes)
                // 将对象写入字节流
                oos.writeObject(device)
                // 将字节流编码成base64的字符串
                val authBase64 = String(
                    Base64.encode(
                        bytes
                            .toByteArray(), Base64.DEFAULT
                    )
                )
                mSharedPreferences!!.edit().putString(key, authBase64)
                    .apply()
                true
            } catch (e: Exception) {
                e.printStackTrace()
                false
            }
        }

        /**
         * 将对象从sharePreference中取出来
         *
         * @param key
         * @param <T>
         * @return
        </T> */
        fun <T> getDeviceData(context: Context, key: String?): T? {
            if (mSharedPreferences == null) {
                mSharedPreferences = context.getSharedPreferences(
                    SP_NAME,
                    Context.MODE_PRIVATE
                )
            }
            var device: T? = null
            val productBase64 =
                mSharedPreferences!!.getString(key, null) ?: return null
            // 读取字节
            val base64 =
                Base64.decode(productBase64.toByteArray(), Base64.DEFAULT)
            // 封装到字节流
            val bais = ByteArrayInputStream(base64)
            try { // 再次封装
                val bis = ObjectInputStream(bais)
                // 读取对象
                device = bis.readObject() as T?
            } catch (e: Exception) {
                e.printStackTrace()
            }
            return device
        }


    }

}