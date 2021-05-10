package com.de.danaemas.module.auth

import ai.advance.liveness.lib.LivenessResult
import com.coupang.common.base.BaseRepository
import com.coupang.common.network.EmptyVO
import com.coupang.common.network.ParameterTool.fileCreate
import com.coupang.common.network.ParameterTool.toRequestBody
import com.coupang.common.network.Result
import com.coupang.common.network.safeApiCall
import com.coupang.common.user.UserManager.isLogin
import com.coupang.common.user.UserManager.username
import com.de.danaemas.AppApi
import com.de.danaemas.module.auth.AuthFaceActivity.Companion.TO_IDCARD_REQUEST_CODE
import com.de.danaemas.module.auth.AuthFaceActivity.Companion.TO_LIVENESS_REQUEST_CODE
import com.de.danaemas.module.auth.AuthFaceActivity.Companion.TYPE_UPLOAD_IDCARD
import com.de.danaemas.module.auth.AuthFaceActivity.Companion.TYPE_UPLOAD_LIVENESS
import com.de.danaemas.module.auth.dto.AuthCardInfo
import com.de.danaemas.module.auth.dto.AuthSubmitInfo
import com.de.danaemas.module.auth.dto.CanClickInfo
import com.de.danaemas.module.auth.dto.FaceAuthInfo
import java.io.File
import java.util.*

class AuthRepository : BaseRepository<Any?>() {

     suspend fun getPersonInfo(productId: String): Result<FaceAuthInfo> = safeApiCall {
         val map:@JvmSuppressWildcards  MutableMap<String, Any> = HashMap()
         map["product_id"] = productId
         if (isLogin()) {
             map["mobile"] = username
         }
        AppApi.api.getPersonInfo(map)
    }

    suspend fun uploadImage(filePath: String, type: Int): Result<AuthCardInfo> = safeApiCall {
        val map: @JvmSuppressWildcards MutableMap<String, Any> = HashMap()
        if (type == TYPE_UPLOAD_LIVENESS) {
            map["livenessId"] = LivenessResult.getLivenessId()
            map["score"] = LivenessResult.getLivenessScore()
            map["type"] = TYPE_UPLOAD_LIVENESS
            map["ocrtype"] = TO_LIVENESS_REQUEST_CODE
        } else {
            map["type"] = TYPE_UPLOAD_IDCARD
            map["ocrtype"] = TO_IDCARD_REQUEST_CODE
        }

        AppApi.api.uploadImage(fileCreate(getFileByPath(filePath)), map)
    }

    suspend fun advanceLog(): Result<CanClickInfo> = safeApiCall {
        AppApi.api.advanceLog()
    }


    suspend fun submitAuth(): Result<AuthSubmitInfo> = safeApiCall {
        AppApi.api.submitAuth()
    }

    suspend fun saveBaseInfo(bean: AuthCardInfo, orderNo: String, product_id: String): Result<EmptyVO> = safeApiCall {
        val map: MutableMap<String, Any> = HashMap()
        map["product_id"] = product_id
        map["order_no"] = orderNo
        map["mobile"] = username
        map["name"] = bean.name?:""
        map["id_number"] =bean.idCardNumber?:""
        map["birthday"] = bean.getBirthdayStr()?:""

        AppApi.api.saveBaseInfo(toRequestBody(map))
    }

    fun getFileByPath(filePath: String?): File? {
        return if (isSpace(filePath)) null else File(filePath)
    }


    private fun isSpace(s: String?): Boolean {
        if (s == null) return true
        var i = 0
        val len = s.length
        while (i < len) {
            if (!Character.isWhitespace(s[i])) {
                return false
            }
            ++i
        }
        return true
    }
}