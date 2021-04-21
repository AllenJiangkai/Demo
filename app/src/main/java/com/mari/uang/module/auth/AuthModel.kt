package com.mari.uang.module.auth

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.mari.uang.module.auth.dto.AuthCardInfo
import com.mari.uang.module.auth.dto.AuthSubmitInfo
import com.mari.uang.module.auth.dto.CanClickInfo
import com.mari.uang.module.auth.dto.FaceAuthInfo
import com.coupang.common.base.BaseViewModel
import com.coupang.common.network.Result
import kotlinx.coroutines.launch

class AuthModel : BaseViewModel<AuthRepository>() {

    val networkError = MutableLiveData<Boolean>()
    val faceAuthInfo = MutableLiveData<FaceAuthInfo>()
    val authCardInfo = MutableLiveData<AuthCardInfo>()
    val canClickInfo = MutableLiveData<CanClickInfo>()
    val submitInfo = MutableLiveData<AuthSubmitInfo>()
    val submitCardInfo = MutableLiveData<AuthCardInfo>()

    override fun createRepository(): AuthRepository {
        return AuthRepository()
    }


    fun getPersonInfo(id: String) {
        showLoading()
        viewModelScope.launch {
            val result = repository.getPersonInfo(id)
            if (result is Result.Success) {
                faceAuthInfo.value=result.data
            }else {
                networkError.value=true
            }
            hideLoading()
        }
    }
    fun uploadImage(path: String, type: Int) {
        showLoading()
        viewModelScope.launch {
            val result = repository.uploadImage(path, type)
            if (result is Result.Success) {
                result.data.type=type
                result.data.imagePath=path
                authCardInfo.value=result.data
            }
            hideLoading()
        }
    }
    fun sendLog() {
        showLoading()
        viewModelScope.launch {
            val result = repository.advanceLog()
            if (result is Result.Success) {
                canClickInfo.value=result.data
            }else if(result is Result.GeneralError){
                showToast.value=result.message
            }
            hideLoading()
        }
    }

    fun sendSubmit() {
        showLoading()
        viewModelScope.launch {
            val result = repository.submitAuth()
            if (result is Result.Success) {
                submitInfo.value=result.data
            }
            hideLoading()
        }
    }

    fun submitCardInfo(info: AuthCardInfo,orderNo:String?,product_id :String?){
        showLoading()
        viewModelScope.launch {
            val result = repository.saveBaseInfo(info,orderNo?:"",product_id?:"")
            if (result is Result.Success) {
                submitCardInfo.value=info
            }
            hideLoading()
        }

    }


}