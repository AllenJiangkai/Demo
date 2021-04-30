package com.coupang.common.user

import com.coupang.common.spfs.DelegatePreference
import com.coupang.common.spfs.DelegateSharedPreferences

object UserManager : DelegateSharedPreferences("userInfo") {

    var uid by DelegatePreference(this, "uid", "")
    var username by DelegatePreference(this, "username", "")
    var realname by DelegatePreference(this, "realname", "")
    var token by DelegatePreference(this, "token", "")
    var sessionid by DelegatePreference(this, "sessionid", "")
    var isOld by DelegatePreference(this, "isOld", "")
    var smsMaxId by DelegatePreference(this, "smsMaxId", 0L)
    var phone by DelegatePreference(this, "phone", "")


    fun saveUserInfo(userInfo: UserInfo){
        uid =userInfo.uid?:""
        username =userInfo.username?:""
        realname =userInfo.realname?:""
        token =userInfo.token?:""
        sessionid =userInfo.sessionid?:""
        isOld =userInfo.isOld?:""
        smsMaxId =userInfo.smsMaxId?:0
    }

    fun cleanUserInfo(){
        uid =""
        username =""
        realname =""
        token =""
        sessionid =""
        isOld =""
        smsMaxId =0
    }

    fun isLogin(): Boolean {
        return uid.isNotEmpty()
    }
}