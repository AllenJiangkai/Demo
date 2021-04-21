package com.coupang.common.user

import com.coupang.common.network.DTO

class UserInfo : DTO {
   var uid: String? = null
   var username: String? = null
   var realname: String? = null
   var token: String? = null
   var sessionid: String? = null
   var isOld: String? = null
   var smsMaxId: Long = 0
}