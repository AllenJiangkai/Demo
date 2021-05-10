package com.de.danaemas.module.profile

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.coupang.common.base.BaseViewModel
import com.coupang.common.network.Result
import kotlinx.coroutines.launch

class ProfileModel : BaseViewModel<ProfileRepository>() {

    val profileItemInfo = MutableLiveData<ProfileItemInfo>()
    val profileRedInfo = MutableLiveData<ArrayList<ProfileRedInfo>>()
    val networkError = MutableLiveData<Boolean>()

    override fun createRepository(): ProfileRepository {
        return ProfileRepository()
    }


    fun requestItemList( ) {
        viewModelScope.launch {
            val result = repository.itemList()
            if (result is Result.Success) {
                profileItemInfo.value=result.data
            }else {
                networkError.value=true
            }
        }
    }
    fun requestRedData( ) {
        viewModelScope.launch {
            val result = repository.requestRedData()
            if (result is Result.Success) {
                profileRedInfo.value=result.data
            }else {
                networkError.value=true
            }
        }
    }

}