package com.mari.uang.module.product

import android.text.TextUtils
import com.coupang.common.base.BaseRepository
import com.coupang.common.network.ParameterTool
import com.coupang.common.network.Result
import com.coupang.common.network.safeApiCall
import com.mari.uang.AppApi
import com.mari.uang.module.product.dto.AgreementInfo
import com.mari.uang.module.product.dto.ProductDetailsInfo
import com.mari.uang.module.product.dto.ProductUrlInfo
import com.mari.uang.module.product.dto.SendProductUrlInfo
import java.util.*

class ProductRepository : BaseRepository<Any?>() {

    suspend fun requestProductDetail(
        productId: String?, moduleId: String?,
        positionId: String?,
        position: String?
    ): Result<ProductDetailsInfo> = safeApiCall {
        val map: MutableMap<String, Any> = HashMap()
        map["product_id"] = productId?:""
        if (moduleId!=null&&!TextUtils.isEmpty(moduleId)){
            map["module_id"] = moduleId
        }
        if (positionId!=null&&!TextUtils.isEmpty(positionId)){
            map["position_id"] = positionId
        }
        if (position!=null&&!TextUtils.isEmpty(position)){
            map["position"] = position
        }
        AppApi.api.productDetail(ParameterTool.toRequestBody(map))
    }

    suspend fun requestProductUrlInfo(bean: AgreementInfo): Result<ProductUrlInfo> = safeApiCall {
        val map:@JvmSuppressWildcards MutableMap<String, Any> = HashMap()
        map["productId"] = bean.productId?:""
        map["scene"] = bean.scene?:""
        map["orderId"] = bean.orderId?:""
        map["position"] = bean.position?:""
        AppApi.api.contractJump(map)
    }
    suspend fun sendProduct(bean: ProductDetailsInfo): Result<SendProductUrlInfo> = safeApiCall {
        val map: MutableMap<String, Any> = HashMap()
        map["order_no"] = bean.productDetail?.orderNo?:""
        map["amount"] =  bean.productDetail?.amount?:""
        map["term"] = bean.productDetail?.term?:""
        map["term_type"] = bean.productDetail?.term_type?:""
        AppApi.api.productPush(ParameterTool.toRequestBody(map))
    }





}