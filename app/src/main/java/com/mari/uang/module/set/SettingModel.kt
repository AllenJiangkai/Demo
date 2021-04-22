package com.mari.uang.module.set

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.coupang.common.base.BaseViewModel
import kotlinx.coroutines.launch
import com.coupang.common.network.Result

class SettingModel : BaseViewModel<SettingRepository>() {

    val loginOut = MutableLiveData<Boolean>()
    val networkError = MutableLiveData<Boolean>()

    override fun createRepository(): SettingRepository {
        return SettingRepository()
    }


    fun loginOut( ) {
        showLoading()
        viewModelScope.launch {
            val result = repository.logOut()
            if (result is Result.Success) {
                loginOut.value=true
            }else if(result is Result.GeneralError){
                showToast.value=result.message ?:""
                networkError.value=true
            }
            hideLoading()
        }
    }

}