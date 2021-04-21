package com.mari.uang.module.profile

import com.mari.uang.AppApi
import com.coupang.common.base.BaseRepository
import com.coupang.common.network.Result
import com.coupang.common.network.safeApiCall

class ProfileRepository : BaseRepository<Any?>() {

     suspend fun itemList(): Result<ProfileItemInfo> = safeApiCall {
        AppApi.api.personalCenter()
    }
     suspend fun requestRedData(): Result<ArrayList<ProfileRedInfo>> = safeApiCall {
        AppApi.api.redNotice()
    }


}