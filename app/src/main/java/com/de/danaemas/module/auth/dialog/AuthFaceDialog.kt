package com.de.danaemas.module.auth.dialog

import android.app.Dialog
import android.content.Context
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.de.danaemas.R
import kotlinx.android.synthetic.main.dialog_face_auth.*

class AuthFaceDialog(context: Context) : Dialog(context, R.style.TipsDialogTheme) {
    private var rootView: View
    private lateinit var clickSubmitListener: () -> Unit

    init {
        val inflater = LayoutInflater.from(context)
        rootView = inflater.inflate(R.layout.dialog_face_auth, null, false)
        setContentView(rootView)

        window?.setGravity(Gravity.CENTER)
        window?.setLayout(
            ViewGroup.LayoutParams.WRAP_CONTENT,
            ViewGroup.LayoutParams.WRAP_CONTENT
        )
        setCancelable(false)
        bt_submit.setOnClickListener {
            if (::clickSubmitListener.isInitialized) {
                clickSubmitListener()
            }
            dismiss()
        }
        img_close.setOnClickListener {
            dismiss()
        }
    }

    inline fun show(func: AuthFaceDialog.() -> Unit): AuthFaceDialog {
        this.func()
        this.show()
        return this
    }

    fun onClickSubmitListener(callback: () -> Unit): AuthFaceDialog  {
        this.clickSubmitListener = callback
        return this
    }


}