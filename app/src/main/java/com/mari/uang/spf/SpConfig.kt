package com.mari.uang.spf

import com.coupang.common.spfs.DelegatePreference
import com.coupang.common.spfs.DelegateSharedPreferences

object SpConfig : DelegateSharedPreferences("spConfig") {

    var isShowPerDialog by DelegatePreference(this, "isShowPermission", false)
    var redDotData by DelegatePreference(this, "RedDotData", "")
    var orderType1 by DelegatePreference(this, "OrderType1", "")
    var orderType2 by DelegatePreference(this, "OrderType2", "")


}