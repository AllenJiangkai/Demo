package com.de.danaemas.module.auth.dto

import android.text.TextUtils
import com.coupang.common.network.DTO

class AuthCardInfo :DTO{

    var name: String? = null
    var race: String? = null
    var gender: String? = null
    var birthday: String? = null
    var idCardNumber: String? = null
    var address: String? = null
    var isIdCardImageFront: Boolean? = null
    var day: String? = null
    var month: String? = null
    var year: String? = null

    var type:Int=0
    var imagePath:String=""

    fun getBirthdayStr(): String? {
        return if (!TextUtils.isEmpty(day) && !TextUtils.isEmpty(month) && !TextUtils.isEmpty(year)) "$day-$month-$year" else ""
    }

}