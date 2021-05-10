package com.de.danaemas.module.home

import com.coupang.common.base.BaseRepository
import com.coupang.common.network.ParameterTool.toRequestBody
import com.coupang.common.network.Result
import com.coupang.common.network.safeApiCall
import com.de.danaemas.AppApi
import com.de.danaemas.module.home.dto.HomeInfo
import com.de.danaemas.module.home.dto.ProductDialogInfo

class HomeRepository : BaseRepository<Any?>() {

     suspend fun requestHomeList(): Result<HomeInfo> = safeApiCall {
        AppApi.api.getHomeData()
    }
     suspend fun requestProduct(map :MutableMap<String,Any>): Result<ProductDialogInfo> = safeApiCall {
        AppApi.api.productApply(toRequestBody(map))
    }


}