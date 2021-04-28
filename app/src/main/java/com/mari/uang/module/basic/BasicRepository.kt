package com.mari.uang.module.basic

import com.coupang.common.base.BaseRepository
import com.coupang.common.network.EmptyVO
import com.coupang.common.network.ParameterTool
import com.coupang.common.network.Result
import com.coupang.common.network.safeApiCall
import com.mari.uang.AppApi
import com.mari.uang.module.basic.dto.BasicItemInfoList

/**
 *
 * @ProjectName:    Business
 * @Package:        com.alan.business.module.basic
 * @ClassName:      BasicRepository
 * @Description:     java类作用描述
 * @Author:         jtao
 * @CreateDate:     2021/4/17 2:14 PM
 * @UpdateUser:     更新者
 * @UpdateDate:     2021/4/17 2:14 PM
 * @UpdateRemark:   更新说明
 * @Version:        1.0
 */
class BasicRepository : BaseRepository<Any?>(){
    suspend fun  getBankCardInfo(map :@JvmSuppressWildcards  MutableMap<String, Any>) : Result<BasicItemInfoList> = safeApiCall{
        AppApi.api.getBankCardInfo(map)
    }

    suspend fun  getJobInfo(map : MutableMap<String, Any>) : Result<BasicItemInfoList> = safeApiCall{
        AppApi.api.getJobInfo(ParameterTool.toRequestBody(map))
    }

    suspend fun getPersonalAuthInfo(map : MutableMap<String, Any>) :Result<BasicItemInfoList> = safeApiCall{
        AppApi.api.getPersonalAuthInfo(ParameterTool.toRequestBody(map))
    }

    suspend fun saveBankInfo(map : MutableMap<String, Any>) : Result<EmptyVO> = safeApiCall{
        AppApi.api.saveBankCardInfo(ParameterTool.toRequestBody(map))
    }

    suspend fun saveJobInfo(map : MutableMap<String, Any>) : Result<EmptyVO> = safeApiCall{
        AppApi.api.saveJobInfo(ParameterTool.toRequestBody(map))
    }

    suspend fun savePersonalAuthInfo(map : MutableMap<String, Any>) : Result<EmptyVO> = safeApiCall{
        AppApi.api.savePersonalAuthInfo(ParameterTool.toRequestBody(map))
    }


}