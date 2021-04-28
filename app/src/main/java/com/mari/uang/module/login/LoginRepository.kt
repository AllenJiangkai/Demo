package com.mari.uang.module.login

import com.coupang.common.base.BaseRepository
import com.coupang.common.network.ParameterTool
import com.coupang.common.network.Result
import com.coupang.common.network.safeApiCall
import com.mari.uang.AppApi
import com.mari.uang.module.login.dto.VerificationCodeInfo
import java.util.*

class LoginRepository : BaseRepository<Any?>() {

    suspend fun sendVerificationCode(phone: String): Result<VerificationCodeInfo> = safeApiCall {
        val map: MutableMap<String, Any> = java.util.HashMap()
        map["register"] = "1"
        map["phone"] = phone
        map["type"] = "find_pwd"
        map["captcha"] = ""
        map["type2"] = "1"
        map["RCaptchaKey"] = "1"
        map["SMSType"] = ""
        AppApi.api.getLoginCode(ParameterTool.toRequestBody(map))
    }

    suspend fun sendVoiceCode(phone: String): Result<VerificationCodeInfo> = safeApiCall {
        val map: MutableMap<String, Any> = HashMap()
        map["register"] = "1"
        map["phone"] = phone
        map["type"] = "find_pwd"
        map["captcha"] = ""
        map["type2"] = "1"
        map["RCaptchaKey"] = "1"
        map["SMSType"] = ""
        AppApi.api.getLoginVoiceCode(ParameterTool.toRequestBody(map))
    }

    suspend fun login(phone: String,code:String): Result<LoginVO> = safeApiCall {
        val map: MutableMap<String, Any> = HashMap()
        map["username"] = phone
        map["smsCode"] = code
        AppApi.api.login(ParameterTool.toRequestBody(map))
    }

}