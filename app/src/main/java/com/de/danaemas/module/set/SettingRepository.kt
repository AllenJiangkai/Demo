package com.de.danaemas.module.set

import com.coupang.common.base.BaseRepository
import com.coupang.common.network.EmptyVO
import com.coupang.common.network.Result
import com.coupang.common.network.safeApiCall
import com.de.danaemas.AppApi

class SettingRepository : BaseRepository<Any?>() {

     suspend fun logOut(): Result<EmptyVO> = safeApiCall {
        AppApi.api.logOut()
    }


}