package com.coupang.common.base

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import io.reactivex.disposables.CompositeDisposable

/**
 * @author Allen
 * @date 2020-05-27.
 * description：
 */
abstract class BaseSimpleViewModel : ViewModel() {
   val autoCancelCollection by lazy { CompositeDisposable() }
   val showToast = MutableLiveData<String>()
   val showHttpLoading = MutableLiveData<Boolean>()

   override fun onCleared() {
      super.onCleared()
      autoCancelCollection.dispose()
   }

   fun showLoading() {
      showHttpLoading.postValue(true)
   }

   fun hideLoading() {
      showHttpLoading.postValue(false)
   }
}

abstract class BaseViewModel<T : BaseRepository<Any?>> : BaseSimpleViewModel() {

   val repository: T by lazy { createRepository() }

   abstract fun createRepository(): T
}