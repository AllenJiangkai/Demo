package com.alan.business.util.upload.ld

import android.content.Context
import android.os.Environment
import android.provider.MediaStore
import android.provider.MediaStore.Audio
import com.alibaba.fastjson.JSONObject
import com.mari.uang.util.upload.ld.LDDevicesUtil.getBatteryData
import com.mari.uang.util.upload.ld.LDDevicesUtil.getGeneralData
import com.mari.uang.util.upload.ld.LDDevicesUtil.getHardWareInfo
import com.mari.uang.util.upload.ld.LDDevicesUtil.getNetworkData
import com.mari.uang.util.upload.ld.LDDevicesUtil.getOtherData
import com.mari.uang.util.upload.ld.LDFileUtil.getStorageInfo

/**
 *
 * @ProjectName:    Business
 * @Package:        com.alan.business.util.upload.ld
 * @ClassName:      LunduUtil
 * @Description:     java类作用描述
 * @Author:         jtao
 * @CreateDate:     2021/4/21 2:14 PM
 * @UpdateUser:     更新者
 * @UpdateDate:     2021/4/21 2:14 PM
 * @UpdateRemark:   更新说明
 * @Version:        1.0
 */
object LunduUtil {
    fun getDeviceInfo(context: Context): JSONObject {
        val deviceInfo = JSONObject()
        try {
            deviceInfo["hardware"] = getHardWareInfo(context)
            deviceInfo["storage"] = getStorageInfo(context)
            deviceInfo["general_data"] = getGeneralData(context)
            deviceInfo["other_data"] = getOtherData(context)
            deviceInfo["network"] = getNetworkData(context)
            deviceInfo["battery_status"] = getBatteryData(context)
            deviceInfo["file_data"] = getFileData(context)
        } catch (var2: Exception) {
            var2.printStackTrace()
        }
        return deviceInfo
    }

    private fun getFileData(context: Context): JSONObject? {
        val entity = JSONObject()
        try {
            entity["images_external"] =
                getImagesExternalCount(context)
            entity["images_internal"] =
                getImagesInternalCount(context)
            entity["audio_external"] =
                getAudioExternalCount(context)
            entity["audio_internal"] =
                getAudioInternalCount(context)
            entity["video_external"] =
                getVideoExternalCount(context)
            entity["video_internal"] =
                getVideoInternalCount(context)
            entity["download_files"] =
                getDownloadFilesCount()
        } catch (e: java.lang.Exception) {
        } finally {
            return entity
        }
    }

    fun getDownloadFilesCount(): Int {
        return try {
            Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS)
                .listFiles().size
        } catch (ignored: java.lang.Exception) {
            -1
        }
    }

    fun getVideoExternalCount(context: Context): Int {
        return try {
            var count = 0
            val contentUri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI
            val cursor = context.contentResolver.query(
                contentUri,
                null,
                null,
                null,
                null
            )
            if (cursor != null) {
                count = cursor.count
                cursor.close()
            } else {
                count = -1
            }
            count
        } catch (ignored: java.lang.Exception) {
            -1
        }
    }

    fun getVideoInternalCount(context: Context): Int {
        return try {
            var count = 0
            val contentUri = MediaStore.Video.Media.INTERNAL_CONTENT_URI
            val cursor = context.contentResolver.query(
                contentUri,
                null, null, null, null
            )
            if (cursor != null) {
                count = cursor.count
                cursor.close()
            } else {
                count = -1
            }
            count
        } catch (ignored: java.lang.Exception) {
            -1
        }
    }

    fun getAudioExternalCount(context: Context): Int {
        return try {
            var count = 0
            val contentUri = Audio.Media.EXTERNAL_CONTENT_URI
            val cursor = context.contentResolver.query(
                contentUri,
                null, null, null, null
            )
            if (cursor != null) {
                count = cursor.count
                cursor.close()
            } else {
                count = -1
            }
            count
        } catch (ignored: java.lang.Exception) {
            -1
        }
    }

    fun getAudioInternalCount(context: Context): Int {
        return try {
            var count = 0
            val contentUri = Audio.Media.INTERNAL_CONTENT_URI
            val cursor = context.contentResolver.query(
                contentUri,
                null, null, null, null
            )
            if (cursor != null) {
                count = cursor.count
                cursor.close()
            } else {
                count = -1
            }
            count
        } catch (ignored: java.lang.Exception) {
            -1
        }
    }

    fun getImagesExternalCount(context: Context): Int {
        return try {
            var count = 0
            val contentUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI
            val cursor = context.contentResolver.query(
                contentUri,
                null, null, null, null
            )
            if (cursor != null) {
                count = cursor.count
                cursor.close()
            } else {
                count = -1
            }
            count
        } catch (ignored: Exception) {
            -1
        }
    }

    fun getImagesInternalCount(context: Context): Int {
        return try {
            var count = 0
            val contentUri = MediaStore.Images.Media.INTERNAL_CONTENT_URI
            val cursor =
                context.contentResolver.query(contentUri, null, null, null, null)
            if (cursor != null) {
                count = cursor.count
                cursor.close()
            } else {
                count = -1
            }
            count
        } catch (ignored: java.lang.Exception) {
            -1
        }
    }
}


