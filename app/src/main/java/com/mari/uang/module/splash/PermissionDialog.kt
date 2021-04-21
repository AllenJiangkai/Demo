package com.mari.uang.module.splash

import android.app.Dialog
import android.content.Context
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import com.mari.uang.MyApplication
import com.mari.uang.R
import kotlinx.android.synthetic.main.dialog_permission_details.*

class PermissionDialog(context: Context) : Dialog(context, R.style.TipsDialogTheme) {
    private var rootView: View
    private lateinit var clickSubmitListener: () -> Unit

    init {
        val inflater = LayoutInflater.from(context)
        rootView = inflater.inflate(R.layout.dialog_permission_details, null, false)
        setContentView(rootView)

        window?.setGravity(Gravity.BOTTOM)
        window?.setLayout(
            MyApplication.screenWidth,
            (MyApplication.screenHeight * 0.9f).toInt()
        )
        setCancelable(false)
        tvConfirm.setOnClickListener {
            if (::clickSubmitListener.isInitialized) {
                clickSubmitListener()
            }
            dismiss()
        }
    }

    inline fun show(func: PermissionDialog.() -> Unit): PermissionDialog {
        this.func()
        this.show()
        return this
    }

    fun onClickSubmitListener(callback: () -> Unit): PermissionDialog  {
        this.clickSubmitListener = callback
        return this
    }


}