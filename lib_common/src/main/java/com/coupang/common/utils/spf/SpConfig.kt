package com.coupang.common.utils.spf

import com.coupang.common.spfs.DelegatePreference
import com.coupang.common.spfs.DelegateSharedPreferences

object SpConfig : DelegateSharedPreferences("spConfig") {

    var isShowPerDialog by DelegatePreference(this, "isShowPermission", false)
    var redDotData by DelegatePreference(this, "RedDotData", "")
    var orderType1 by DelegatePreference(this, "OrderType1", "")
    var orderType2 by DelegatePreference(this, "OrderType2", "")
    var gps_adid by DelegatePreference(this, "GPS_ADID", "")
    var location_longitude by DelegatePreference(this,"LOCATION_LONGITUDE","")
    var location_latitude by DelegatePreference(this,"LOCATION_LATITUDE","")
    var googleReferrerId by DelegatePreference(this,"GOOGLE_REFERRER","")
    var faceBookReferrerId by DelegatePreference(this,"FACEBOOK_REFERRER","")
    var nfur by DelegatePreference(this,"NFUR",false)
    var pushData by DelegatePreference(this,"PUSH_DATA","")
}