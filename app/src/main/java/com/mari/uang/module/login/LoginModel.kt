package com.mari.uang.module.login

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.coupang.common.base.BaseViewModel
import com.coupang.common.network.Result
import com.coupang.common.utils.strings
import com.mari.uang.R
import com.mari.uang.module.login.dto.VerificationCodeInfo
import io.reactivex.Observable
import io.reactivex.Observer
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import kotlinx.coroutines.launch
import java.util.concurrent.TimeUnit

class LoginModel : BaseViewModel<LoginRepository>() {

    val verificationCodeInfo = MutableLiveData<VerificationCodeInfo>()
    val btnEnabled = MutableLiveData<Boolean>()
    val verificationBtnStr = MutableLiveData<String>()
    val userInfo = MutableLiveData<LoginVO>()


    override fun createRepository(): LoginRepository {
        return LoginRepository()
    }


    fun login(phone: String, code: String) {
        showLoading()
        viewModelScope.launch {
            val result = repository.login(phone, code)
            if (result is Result.Success) {
                userInfo.value = result.data
            } else if (result is Result.GeneralError) {
                showToast.value = result.message
            }
            hideLoading()
        }
    }


    fun sendVerificationCode(phoneNumber: String) {
        if (!checkPhone(phoneNumber)) return
        showLoading()
        viewModelScope.launch {
            val result = repository.sendVerificationCode(phoneNumber)
            if (result is Result.Success) {
                verificationCodeInfo.value = result.data
                showToast.value = "Kode verifikasi berhasil dikirim"
                interval()
            } else if (result is Result.GeneralError) {
                showToast.value = result.message
                verificationBtnStr.value = strings(R.string.login_kirim_kode)
            }
            hideLoading()
        }
    }

    fun sendVoiceCode(phoneNumber: String) {
        if (!checkPhone(phoneNumber)) return
        showLoading()
        viewModelScope.launch {
            val result = repository.sendVoiceCode(phoneNumber)
            if (result is Result.Success) {
                verificationCodeInfo.value = result.data
                showToast.value = "Kode verifikasi berhasil dikirim"
            } else if (result is Result.GeneralError) {
                showToast.value = result.message
            }
            hideLoading()
        }
    }

    private fun interval() {
        val codeTimes: Long = 60
        Observable.interval(1, TimeUnit.SECONDS)
            .take(codeTimes)
            .map { aLong -> codeTimes - (aLong + 1) }
            .observeOn(AndroidSchedulers.mainThread())
            .doOnSubscribe {
                btnEnabled.value = false
            }
            .subscribe(object : Observer<Long> {
                override fun onSubscribe(d: Disposable) {}
                override fun onNext(value: Long) {
                    verificationBtnStr.value = "${value}s"
                }

                override fun onError(e: Throwable) {}
                override fun onComplete() {
                    btnEnabled.value = true
                    verificationBtnStr.value = strings(R.string.login_kirim_kode)
                }
            })
    }

    private fun checkPhone(phone: String): Boolean {
        if (!phone.startsWith("8") && !phone.startsWith("08") || phone.length < 9 || phone.length > 13) {
            showToast.value = "Silakan masukkan nomor yang benar"
            return false
        }
        return true
    }

}