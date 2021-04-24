package com.mari.uang.module.splash

import android.Manifest
import android.content.Intent
import android.net.Uri
import android.provider.Settings
import com.mari.uang.R
import com.mari.uang.module.main.MainActivity
import com.coupang.common.utils.spf.SpConfig
import com.mari.uang.util.PermissionUtil.requestPermission
import com.mari.uang.widget.TipsDialog
import com.coupang.common.base.BaseSimpleActivity
import com.coupang.common.utils.isLocationEnabled
import com.coupang.common.utils.setStatusBarTextColor
import com.coupang.common.utils.strings
import com.mari.uang.util.upload.UploadManager
import com.yanzhenjie.permission.Action
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.functions.Consumer
import java.util.concurrent.TimeUnit

class SplashActivity : BaseSimpleActivity() {

    private val permissions = arrayOf(
        Manifest.permission.READ_PHONE_STATE,
        Manifest.permission.WRITE_EXTERNAL_STORAGE,
        Manifest.permission.READ_EXTERNAL_STORAGE,
        Manifest.permission.ACCESS_COARSE_LOCATION,
        Manifest.permission.ACCESS_FINE_LOCATION
    )
    private var disposable: Disposable? = null

    override fun getLayoutId(): Int {
        return R.layout.activity_slash
    }

    override fun onResume() {
        super.onResume()
        setStatusBarTextColor(window, true)
    }

    override fun initView() {
        if (!SpConfig.isShowPerDialog) {
            PermissionDialog(this).onClickSubmitListener {
                SpConfig.isShowPerDialog=true
                checkPermission()
            }.show ()
        }else{
            checkPermission()
        }
    }

    override fun registerObserver() {

    }

    override fun initData() {


    }


    private fun checkPermission() {
        requestPermission(this, permissions, Action {
            if (isLocationEnabled(this)) {
                UploadManager.uploadDevicesDetail()
                delayedToMain()
            } else {
                showPermissionTipsDialog()
            }

        }, true, canBackDismiss = false)
    }


    private fun showPermissionTipsDialog(){
        TipsDialog(this).isCancelable(false).setTitle(strings(R.string.dialog_per_prompt))
            .setMessage(strings(R.string.dialog_per_message_splash)).setNegativeButton(strings(R.string.dialog_per_splash_left)){
                finish()
                true
            }
            .setPositiveButton(strings(R.string.dialog_per_splash_right)){
                val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
                intent.data = Uri.parse("package:$packageName")
                startActivityForResult(intent, 100)
                false
            }.show ()

    }

    private fun delayedToMain() {
        disposable?.dispose()
        disposable = Observable.timer(3, TimeUnit.SECONDS)
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(Consumer {
                disposable!!.dispose()
                goMainActivity()
            })
    }

    private fun goMainActivity() {
        val intent = Intent(
            this@SplashActivity,
            MainActivity::class.java
        )
        startActivity(intent)
        finish()
    }

}