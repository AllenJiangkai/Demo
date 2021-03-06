package com.de.danaemas.module.set

import android.content.Intent
import androidx.lifecycle.Observer
import com.coupang.common.base.BaseSimpleActivity
import com.coupang.common.extentions.createViewModel
import com.coupang.common.user.UserManager.cleanUserInfo
import com.coupang.common.utils.setStatusBarTextColor
import com.coupang.common.utils.strings
import com.de.danaemas.BuildConfig
import com.de.danaemas.R
import com.de.danaemas.module.main.MainActivity
import com.de.danaemas.widget.TipsDialog
import kotlinx.android.synthetic.main.activity_setting.*

class SettingActivity : BaseSimpleActivity() {

    private val viewModel by lazy { createViewModel(SettingModel::class.java) }
    override fun getLayoutId(): Int {
        return R.layout.activity_setting
    }

    override fun initView() {
        title_bar.apply {
            setTitle(context.getString(R.string.set_title))
            onClickBackListener {
                finish()
            }
        }
        tv_version.text= "V${BuildConfig.VERSION_NAME}"

        bt_submit.setOnClickListener {

            TipsDialog(this).isCancelable(true).setTitle(strings(R.string.set_login_out)+" " +strings(R.string.app_name) )
                .setMessage(getString(R.string.setting_dialog_message))
                .setNegativeButton(strings(R.string.dialog_cancel)){
                    finish()
                    true
                }
                .setPositiveButton(strings(R.string.dialog_confirm)){
                    viewModel.loginOut()
                    true
                }.show ()
        }
    }

    override fun onResume() {
        super.onResume()
        setStatusBarTextColor(window, true)
    }

    override fun registerObserver() {
        registerLiveDataCommonObserver(viewModel)
        viewModel.loginOut.observe(this, Observer {
            cleanUserInfo()
            finish()
            startActivity(Intent(this,MainActivity::class.java))
        })
    }

    override fun initData() {


    }

}