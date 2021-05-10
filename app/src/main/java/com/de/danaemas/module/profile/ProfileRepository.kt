package com.de.danaemas.module.profile

import com.coupang.common.base.BaseRepository
import com.coupang.common.network.Result
import com.coupang.common.network.safeApiCall
import com.de.danaemas.AppApi

class ProfileRepository : BaseRepository<Any?>() {

     suspend fun itemList(): Result<ProfileItemInfo> = safeApiCall {
        AppApi.api.personalCenter()
    }
     suspend fun requestRedData(): Result<ArrayList<ProfileRedInfo>> = safeApiCall {
        AppApi.api.redNotice()
    }


}