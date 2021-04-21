package com.mari.uang.module.contact

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.mari.uang.module.contact.dto.ContactInfo
import com.coupang.common.base.BaseViewModel
import com.coupang.common.network.Result
import com.mari.uang.module.contact.dto.NameTypeInfo
import kotlinx.coroutines.launch
import okhttp3.RequestBody

class ContactModel : BaseViewModel<ContactRepository>() {

    val networkError = MutableLiveData<Boolean>()
    val contactInfo = MutableLiveData<ContactInfo>()
    val saveInfo = MutableLiveData<Boolean>()

    override fun createRepository(): ContactRepository {
        return ContactRepository()
    }


    fun getPersonInfo(id: String?) {
        showLoading()
        viewModelScope.launch {
            val result = repository.requestContactInfo(id ?: "")
            if (result is Result.Success) {
                contactInfo.value=result.data
            }else {
                networkError.value=true
            }
            hideLoading()
        }
    }
    fun saveContactInfo(body: RequestBody ) {
        showLoading()
        viewModelScope.launch {


            val result = repository.saveContactInfo(body)
            if (result is Result.Success) {
                saveInfo.value=true
            }else {
                networkError.value=true
            }
            hideLoading()
        }
    }


}