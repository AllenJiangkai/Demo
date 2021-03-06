package com.de.danaemas.module.set

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.coupang.common.base.BaseViewModel
import com.coupang.common.network.Result
import kotlinx.coroutines.launch

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
                showToast.value=result.msg?:""
                networkError.value=true
            }
            hideLoading()
        }
    }

}