package com.de.danaemas.module.login

import android.Manifest
import android.content.Intent
import androidx.core.widget.addTextChangedListener
import androidx.lifecycle.Observer
import com.coupang.common.base.BaseSimpleActivity
import com.coupang.common.extentions.createViewModel
import com.coupang.common.user.UserManager
import com.coupang.common.user.UserManager.phone
import com.coupang.common.utils.ContextUtils
import com.coupang.common.utils.setStatusBarTextColor
import com.de.danaemas.R
import com.de.danaemas.config.AFAction
import com.de.danaemas.config.ConstantConfig.LOGIN_AGREEMENT_URL
import com.de.danaemas.event.ActionEnum
import com.de.danaemas.event.ActionUtil
import com.de.danaemas.module.main.MainActivity
import com.de.danaemas.module.web.HttpConstants.H5_SERVICE_URL
import com.de.danaemas.util.EventUtil
import com.de.danaemas.util.PermissionUtil.requestPermission
import com.de.danaemas.util.RouterUtil
import com.de.danaemas.util.upload.UploadManager
import com.yanzhenjie.permission.Action
import kotlinx.android.synthetic.main.activity_login.*

class LoginActivity : BaseSimpleActivity() {


    private val mustPermissions = arrayOf(Manifest.permission.READ_CONTACTS)
    private val viewModel by lazy { createViewModel(LoginModel::class.java) }
    override fun getLayoutId(): Int {
        return R.layout.activity_login
    }

    override fun initView() {
        EventUtil.event(this, AFAction.LOGIN_PAGE_INIT)
//        title_bar.apply {
//            setTitle(strings(R.string.title_order))
//            onClickBackListener {
//                finish()
//            }
//        }
        initListener()
    }

    private fun initListener() {
        tv_code.setOnClickListener {
            viewModel.sendVerificationCode(ed_phone.text.toString())
        }

        img_back.setOnClickListener {
            finish()
        }

        tv_sms.setOnClickListener {
            viewModel.sendVoiceCode(ed_phone.text.toString())
        }
        bt_login.setOnClickListener {
            viewModel.login(ed_phone.text.toString(), ed_code.text.toString())
        }

        check_box.setOnCheckedChangeListener { p0, p1 ->
            changBtnStyle()
        }

        ed_phone.addTextChangedListener{
            changBtnStyle()
        }
        ed_code.addTextChangedListener{
            changBtnStyle()
        }

        tv_link.setOnClickListener {
            RouterUtil.goWebActivity(this@LoginActivity, H5_SERVICE_URL + LOGIN_AGREEMENT_URL, "")
        }

        ed_phone.setText(UserManager.phone)
    }


    override fun registerObserver() {
        registerLiveDataCommonObserver(viewModel)

        viewModel.verificationCodeInfo.observe(this, Observer {
            ed_code.requestFocus()
        })

        viewModel.btnEnabled.observe(this, Observer {
            tv_code.isClickable = it
        })

        viewModel.verificationBtnStr.observe(this, Observer {
            tv_code.text = it
        })

        viewModel.userInfo.observe(this, Observer {
            UserManager.saveUserInfo(it.item)
            phone=ed_phone.text.toString()
            reqPermission()
        })
    }

    override fun initData() {


    }

    private fun goMainActivity() {
        ActionUtil.actionRecord(ActionEnum.Register, null, 0, 0)
        UploadManager.uploadAllInfo()

        val intent = Intent(
            this@LoginActivity,
            MainActivity::class.java
        )
        startActivity(intent)
        finish()
    }

    private fun changBtnStyle() {
        if ((ed_phone.text.toString().startsWith("8")
                    || ed_phone.text.toString().startsWith("08"))
            && ed_phone.text.toString().length >= 9
            && ed_phone.text.toString().length <= 13) {
            if (ed_code.text.toString().length >= 6 && check_box.isChecked) {
                bt_login.isEnabled = true
                return
            }
        }
        bt_login.isEnabled = false
    }


    private fun reqPermission() {
        requestPermission(this, mustPermissions, Action { goMainActivity() })
    }

    override fun onResume() {
        super.onResume()
        setStatusBarTextColor(window, true)
    }

    override fun onBackPressed() {
        super.onBackPressed()
        EventUtil.event(ContextUtils.getApplication(), AFAction.LOGIN_CLICK_BACK)
    }
}
