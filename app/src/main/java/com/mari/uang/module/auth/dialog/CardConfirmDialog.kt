package com.mari.uang.module.auth.dialog

import android.app.Dialog
import android.content.Context
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.mari.uang.R
import com.mari.uang.module.auth.dto.AuthCardInfo
import com.coupang.common.utils.dip2px
import com.coupang.common.utils.shortToast
import com.coupang.common.utils.strings
import kotlinx.android.synthetic.main.dialog_confirm.*

class CardConfirmDialog(context: Context) : Dialog(context, R.style.TipsDialogTheme) {

    private var rootView: View
    private lateinit var clickSubmitListener: (bean: AuthCardInfo) -> Unit

    var bean: AuthCardInfo? = null

    init {
        val inflater = LayoutInflater.from(context)
        rootView = inflater.inflate(R.layout.dialog_confirm, null, false)
        setContentView(rootView)

        window?.setGravity(Gravity.CENTER)
        window?.setLayout(
            dip2px(335),
            ViewGroup.LayoutParams.WRAP_CONTENT
        )
        setCancelable(false)
        btnConfirm.setOnClickListener {
            if (etName.text.toString().trim().isEmpty()) {
                shortToast(strings(R.string.dialog_ktp_name))
                return@setOnClickListener
            }
            if (etNum.text.toString().trim().isEmpty()) {
                shortToast(strings(R.string.dialog_ktp_nomor))
                return@setOnClickListener
            }

            bean?.apply {
                name = etName.text.toString().trim()
                idCardNumber = etNum.text.toString().trim()
                day = etDay.text.toString().trim()
                month = etMonth.text.toString().trim()
                year = etYear.text.toString().trim()

                if (::clickSubmitListener.isInitialized) {
                    clickSubmitListener(this)
                }
            }

        }
    }

    inline fun show(func: CardConfirmDialog.() -> Unit): CardConfirmDialog {
        this.func()
        this.show()
        return this
    }

    fun onClickSubmitListener(callback: (bean: AuthCardInfo) -> Unit): CardConfirmDialog {
        this.clickSubmitListener = callback
        return this
    }

    fun setData(bean: AuthCardInfo): CardConfirmDialog {
        this.bean = bean
        etName.setText(bean.name)
        etNum.setText(bean.idCardNumber)
        etDay.setText(bean.day)
        etMonth.setText(bean.month)
        etYear.setText(bean.year)
        return this
    }

}