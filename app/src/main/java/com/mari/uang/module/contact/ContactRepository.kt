package com.mari.uang.module.contact

import com.coupang.common.base.BaseRepository
import com.coupang.common.network.EmptyVO
import com.coupang.common.network.ParameterTool.toRequestBody
import com.coupang.common.network.Result
import com.coupang.common.network.safeApiCall
import com.coupang.common.user.UserManager.isLogin
import com.coupang.common.user.UserManager.username
import com.mari.uang.AppApi
import com.mari.uang.module.contact.dto.ContactInfo
import okhttp3.RequestBody
import java.util.*

class ContactRepository : BaseRepository<Any?>() {

     suspend fun requestContactInfo(productId: String): Result<ContactInfo> = safeApiCall {
         val map: MutableMap<String, Any> = HashMap()
         map["product_id"] = productId
         if (isLogin()) {
             map["mobile"] = username
         }
        AppApi.api.extInfo(toRequestBody(map))
    }
     suspend fun saveContactInfo(body: RequestBody): Result<EmptyVO> = safeApiCall {
        AppApi.api.saveContactInfo(body)
    }




}