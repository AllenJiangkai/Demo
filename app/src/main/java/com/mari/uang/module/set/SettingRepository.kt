package com.mari.uang.module.set

import com.coupang.common.base.BaseRepository
import com.coupang.common.network.EmptyVO
import com.coupang.common.network.Result
import com.coupang.common.network.safeApiCall
import com.mari.uang.AppApi

class SettingRepository : BaseRepository<Any?>() {

     suspend fun logOut(): Result<EmptyVO> = safeApiCall {
        AppApi.api.logOut()
    }


}