package com.de.danaemas.module.product

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.coupang.common.base.BaseViewModel
import com.coupang.common.network.Result
import com.de.danaemas.module.product.dto.AgreementInfo
import com.de.danaemas.module.product.dto.ProductDetailsInfo
import com.de.danaemas.module.product.dto.SendProductUrlInfo
import kotlinx.coroutines.launch

class ProductModel : BaseViewModel<ProductRepository>() {

    val networkError = MutableLiveData<Boolean>()
    val productDetailsInfo = MutableLiveData<ProductDetailsInfo>()
//    val productInfo = MutableLiveData<ProductInfo>()
//    val tag1 = MutableLiveData<TextTagInfo>()
//    val tag2 = MutableLiveData<TextTagInfo>()
    val productUrl = MutableLiveData<String>()
    val sendEvent = MutableLiveData<SendProductUrlInfo>()

    override fun createRepository(): ProductRepository {
        return ProductRepository()
    }


    fun requestProductDetail(productId:String?,moduleId:String?,positionId:String?,position:String?) {
        showLoading()
        viewModelScope.launch {
            val result = repository.requestProductDetail(productId,moduleId,positionId,position)
            if (result is Result.Success) {
                productDetailsInfo.value=result.data
//                productInfo.value=result.data.productDetail
//                tag1.value=result.data.productDetail?.columnText?.tag1
//                tag2.value=result.data.productDetail?.columnText?.tag2
            }else {
                networkError.value=true
            }
            hideLoading()
        }
    }
    fun requestProductAgreement(bean :AgreementInfo) {
        showLoading()
        viewModelScope.launch {
            val result = repository.requestProductUrlInfo(bean)
            if (result is Result.Success) {
                productUrl.value=result.data.contract_url
            }else {
                networkError.value=true
            }
            hideLoading()
        }
    }

    fun sendProduct(bean:ProductDetailsInfo){
        showLoading()
        viewModelScope.launch {
            val result = repository.sendProduct(bean)
            if (result is Result.Success) {
                sendEvent.value=result.data
            }
            hideLoading()
        }

    }




}