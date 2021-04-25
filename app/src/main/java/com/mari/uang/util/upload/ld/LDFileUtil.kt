package com.mari.uang.util.upload.ld

import android.app.ActivityManager
import android.content.Context
import android.database.Cursor
import android.graphics.BitmapFactory
import android.media.ExifInterface
import android.os.Environment
import android.os.StatFs
import android.os.storage.StorageManager
import android.provider.MediaStore
import android.text.TextUtils
import com.alibaba.fastjson.JSONArray
import com.alibaba.fastjson.JSONException
import com.alibaba.fastjson.JSONObject
import java.lang.ref.WeakReference
import java.lang.reflect.InvocationTargetException
import java.text.SimpleDateFormat
import java.util.*
import com.mari.uang.util.upload.ld.LDComUtil.getNonNullText
import com.mari.uang.util.upload.ld.LDComUtil.haveSelfPermission

/**
 *
 * @ProjectName:    Business
 * @Package:        com.mari.uang.util.upload.ld
 * @ClassName:      LDFileUtil
 * @Description:     java类作用描述
 * @Author:         jtao
 * @CreateDate:     2021/4/21 4:07 PM
 * @UpdateUser:     更新者
 * @UpdateDate:     2021/4/21 4:07 PM
 * @UpdateRemark:   更新说明
 * @Version:        1.0
 */
object LDFileUtil {
    private const val ERROR = -1
    private var sPhotosMsgCache: WeakReference<JSONObject?>? =
        null

    private fun externalMemoryAvailable(): Boolean {
        return Environment.getExternalStorageState() == "mounted"
    }

    @Throws(JSONException::class)
    fun getStorageInfo(context: Context): JSONObject? {
        val storageInfo = JSONObject()
        storageInfo["ram_total_size"] = getNonNullText(getRamTotalSize(context))
        storageInfo["ram_usable_size"] = getNonNullText(getRamAvailSize(context))
        storageInfo["internal_storage_usable"] = getAvailableInternalMemorySize()
        storageInfo["internal_storage_total"] = getTotalInternalMemorySize()
        storageInfo["memory_card_size"] = getTotalExternalMemorySize()
        storageInfo["memory_card_usable_size"] = getAvailableExternalMemorySize()
        storageInfo["memory_card_size_use"] =
            getTotalExternalMemorySize() - getAvailableExternalMemorySize()
        storageInfo["contain_sd"] = if (hasSDCard(context, false)) "1" else "0"
        storageInfo["extra_sd"] = if (hasSDCard(context, true)) "1" else "0"
        return storageInfo
    }

    private fun getRamTotalSize(paramContext: Context): String? {
        val activityManager =
            paramContext.getSystemService(Context.ACTIVITY_SERVICE) as ActivityManager
        val memoryInfo = ActivityManager.MemoryInfo()
        activityManager.getMemoryInfo(memoryInfo)
        val stringBuilder = StringBuilder()
        stringBuilder.append(memoryInfo.totalMem)
        stringBuilder.append("")
        return stringBuilder.toString()
    }

    private fun getRamAvailSize(paramContext: Context): String? {
        val activityManager =
            paramContext.getSystemService(Context.ACTIVITY_SERVICE) as ActivityManager
        val memoryInfo = ActivityManager.MemoryInfo()
        activityManager.getMemoryInfo(memoryInfo)
        val stringBuilder = StringBuilder()
        stringBuilder.append(memoryInfo.availMem)
        stringBuilder.append("")
        return stringBuilder.toString()
    }

    private fun getAvailableInternalMemorySize(): Long {
        val path = Environment.getDataDirectory()
        val stat = StatFs(path.path)
        val blockSize = stat.blockSize.toLong()
        val availableBlocks = stat.availableBlocks.toLong()
        return availableBlocks * blockSize
    }

    private fun getTotalInternalMemorySize(): Long {
        val path = Environment.getDataDirectory()
        val stat = StatFs(path.path)
        val blockSize = stat.blockSize.toLong()
        val totalBlocks = stat.blockCount.toLong()
        return totalBlocks * blockSize
    }

    private fun getAvailableExternalMemorySize(): Long {
        return if (externalMemoryAvailable()) {
            val path = Environment.getExternalStorageDirectory()
            val stat = StatFs(path.path)
            val blockSize = stat.blockSize.toLong()
            val availableBlocks = stat.availableBlocks.toLong()
            availableBlocks * blockSize
        } else {
            -1L
        }
    }

    private fun getTotalExternalMemorySize(): Long {
        return if (externalMemoryAvailable()) {
            val path = Environment.getExternalStorageDirectory()
            val stat = StatFs(path.path)
            val blockSize = stat.blockSize.toLong()
            val totalBlocks = stat.blockCount.toLong()
            totalBlocks * blockSize
        } else {
            -1L
        }
    }

    private fun hasSDCard(
        mContext: Context,
        canRemovable: Boolean
    ): Boolean {
        val mStorageManager =
            mContext.getSystemService(Context.STORAGE_SERVICE) as StorageManager
        var storageVolumeClazz: Class<*>? = null
        try {
            storageVolumeClazz = Class.forName("android.os.storage.StorageVolume")
            val getVolumeList =
                mStorageManager.javaClass.getMethod("getVolumeList")
            val isRemovable = storageVolumeClazz.getMethod("isRemovable")
            val result = getVolumeList.invoke(mStorageManager)
            val length = java.lang.reflect.Array.getLength(result)
            for (i in 0 until length) {
                val storageVolumeElement = java.lang.reflect.Array.get(result, i)
                val removable =
                    isRemovable.invoke(storageVolumeElement) as Boolean
                if (removable == canRemovable) {
                    return true
                }
            }
        } catch (var11: ClassNotFoundException) {
            var11.printStackTrace()
        } catch (var12: InvocationTargetException) {
            var12.printStackTrace()
        } catch (var13: NoSuchMethodException) {
            var13.printStackTrace()
        } catch (var14: IllegalAccessException) {
            var14.printStackTrace()
        }
        return false
    }

    fun getImagesMsg(context: Context): JSONObject? {
        return if (sPhotosMsgCache != null && !TextUtils.isEmpty(
                sPhotosMsgCache!!.get().toString()
            )
        ) {
            sPhotosMsgCache!!.get()
        } else if (!haveSelfPermission(
                context,
                "android.permission.READ_EXTERNAL_STORAGE"
            )
        ) {
            getDefaultPhotosMsg()
        } else {
            var photoCursor = context.contentResolver.query(
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                null as Array<String?>?,
                null as String?,
                null as Array<String?>?,
                null as String?
            )
            var isInternalUrl = false
            if (photoCursor == null) {
                isInternalUrl = true
                photoCursor = context.contentResolver.query(
                    MediaStore.Images.Media.INTERNAL_CONTENT_URI,
                    null as Array<String?>?,
                    null as String?,
                    null as Array<String?>?,
                    null as String?
                )
            }
            if (photoCursor == null) {
                getDefaultPhotosMsg()
            } else {
                val albsRoot = JSONObject()
                val albsData = JSONObject()
                var imgDataList = JSONArray()
                val sdf =
                    SimpleDateFormat("yyyy/MM/dd HH:mm:ss", Locale.getDefault())
                var i = 0
                while (i < 500 && photoCursor.moveToNext()) {
                    imgDataList = queryImagesMsg(photoCursor, imgDataList, sdf)
                    ++i
                }
                if (!isInternalUrl && imgDataList.size < 500) {
                    releaseCursor(photoCursor)
                    appendImagesMsg(context, albsRoot, albsData, imgDataList)
                } else {
                    putImagesMsg(albsRoot, albsData, imgDataList)
                    releaseCursor(photoCursor)
                    sPhotosMsgCache!!.get()
                }
            }
        }
    }

    private fun releaseCursor(photoCursor: Cursor?) {
        if (photoCursor != null && !photoCursor.isClosed) {
            photoCursor.close()
        }
    }

    fun appendImagesMsg(
        context: Context,
        albs: JSONObject,
        jsonObject: JSONObject,
        dataList: JSONArray
    ): JSONObject? {
        var dataList = dataList
        val internalCursor = context.contentResolver.query(
            MediaStore.Images.Media.INTERNAL_CONTENT_URI,
            null as Array<String?>?,
            null as String?,
            null as Array<String?>?,
            null as String?
        )
        return if (internalCursor == null) {
            putImagesMsg(albs, jsonObject, dataList)
            sPhotosMsgCache!!.get()
        } else {
            val num = 500 - dataList.size
            val sdf =
                SimpleDateFormat("yyyy/MM/dd HH:mm:ss", Locale.getDefault())
            var i = 0
            while (i < num && internalCursor.moveToNext()) {
                dataList = queryImagesMsg(internalCursor, dataList, sdf)
                ++i
            }
            putImagesMsg(albs, jsonObject, dataList)
            releaseCursor(internalCursor)
            sPhotosMsgCache!!.get()
        }
    }

    private fun putImagesMsg(
        albs: JSONObject,
        jsonObject: JSONObject,
        dataList: JSONArray
    ) {
        try {
            jsonObject["dataList"] = dataList
            albs["albs"] = jsonObject
            sPhotosMsgCache = WeakReference<JSONObject?>(albs)
        } catch (var4: JSONException) {
            var4.printStackTrace()
            sPhotosMsgCache = WeakReference<JSONObject?>(getDefaultPhotosMsg())
        }
    }

    private fun queryImagesMsg(
        internalCursor: Cursor,
        dataList: JSONArray,
        sdf: SimpleDateFormat
    ): JSONArray {
        val photoDate =
            internalCursor.getLong(internalCursor.getColumnIndexOrThrow("datetaken"))
        val photoName =
            internalCursor.getString(internalCursor.getColumnIndex("_display_name"))
        val options = BitmapFactory.Options()
        options.inJustDecodeBounds = true
        val photoPath =
            internalCursor.getString(internalCursor.getColumnIndex("_data"))
        val saveTime =
            internalCursor.getString(internalCursor.getColumnIndex("date_modified"))
        BitmapFactory.decodeFile(photoPath, options)
        val photoH = options.outHeight.toString()
        val photoW = options.outWidth.toString()
        val data = JSONObject()
        try {
            val exifInterface = ExifInterface(photoPath)
            var takeTime = ""
            takeTime = try {
                sdf.parse(exifInterface.getAttribute("DateTime")).toString()
            } catch (var15: Exception) {
                ""
            }

            val latLongResult = FloatArray(2)
            exifInterface.getLatLong(latLongResult)
            data["name"] = photoName
            data["author"] = getNonNullText(exifInterface.getAttribute("Artist"))
            data["height"] = photoH
            data["width"] = photoW
            data["latitude"] = latLongResult[0].toDouble()
            data["longitude"] = latLongResult[1].toDouble()
            data["date"] = sdf.format(photoDate)
            data["createTime"] = sdf.format(System.currentTimeMillis())
            data["model"] = getNonNullText(exifInterface.getAttribute("Model"))
            data["take_time"] = takeTime
            data["save_time"] = saveTime
            data["orientation"] =
                getNonNullText(exifInterface.getAttribute("Orientation"))
            data["x_resolution"] =
                getNonNullText(exifInterface.getAttribute("XResolution"))
            data["y_resolution"] =
                getNonNullText(exifInterface.getAttribute("YResolution"))
            data["gps_altitude"] =
                getNonNullText(exifInterface.getAttribute("GPSAltitude"))
            data["gps_processing_method"] =
                getNonNullText(exifInterface.getAttribute("GPSProcessingMethod"))
            data["lens_make"] = getNonNullText(exifInterface.getAttribute("Make"))
            data["lens_model"] = getNonNullText(exifInterface.getAttribute("Model"))
            data["focal_length"] =
                getNonNullText(exifInterface.getAttribute("FocalLength"))
            data["flash"] = getNonNullText(exifInterface.getAttribute("Flash"))
            data["software"] = getNonNullText(exifInterface.getAttribute("Software"))
            dataList.add(data)
        } catch (var16: Exception) {
        }
        return dataList
    }

    fun getDefaultPhotosMsg(): JSONObject? {
        return JSONObject()
    }
}