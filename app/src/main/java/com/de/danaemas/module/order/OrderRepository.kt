package com.de.danaemas.module.order

import com.coupang.common.base.BaseRepository
import com.coupang.common.network.ParameterTool
import com.coupang.common.network.Result
import com.coupang.common.network.safeApiCall
import com.de.danaemas.AppApi

class OrderRepository : BaseRepository<Any?>() {

     suspend fun orderList(orderType: Int): Result<OrderList> = safeApiCall {
         val map=HashMap<String, Any>()
         map["orderType"] = orderType
         map["pageNum"] = 1
         map["pageSize"] = 10
        AppApi.api.orderList(ParameterTool.toRequestBody(map))
    }


}