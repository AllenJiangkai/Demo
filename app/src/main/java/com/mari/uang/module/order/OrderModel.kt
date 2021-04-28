package com.mari.uang.module.order

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.coupang.common.base.BaseViewModel
import com.coupang.common.network.Result
import kotlinx.coroutines.launch

class OrderModel : BaseViewModel<OrderRepository>() {

    val orderList = MutableLiveData<OrderList>()
    val networkError = MutableLiveData<Boolean>()

    override fun createRepository(): OrderRepository {
        return OrderRepository()
    }


    fun orderList(statusId:Int,isShowLoading:Boolean=false) {
        if(isShowLoading){
            showLoading()
        }
        viewModelScope.launch {
            val result = repository.orderList(statusId)
            if (result is Result.Success) {
                orderList.value=result.data
            }else {
                networkError.value=true
            }
            hideLoading()
        }
    }

}