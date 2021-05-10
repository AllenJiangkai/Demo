package com.de.danaemas.widget

import android.app.Dialog
import android.content.Context
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.coupang.common.extentions.gone
import com.coupang.common.extentions.visible
import com.coupang.common.utils.dip2px
import com.coupang.common.utils.setHtmlText
import com.de.danaemas.MyApplication
import com.de.danaemas.R
import kotlinx.android.synthetic.main.widget_tips_dialog.*

class TipsDialog(context: Context) : Dialog(context, R.style.TipsDialogTheme) {

    private var rootView: View
    private var clickNegativeListener: (() -> Boolean?)? = null
    private var clickPositiveListener: (() -> Boolean?)? = null


    init {
        val inflater = LayoutInflater.from(context)
        rootView = inflater.inflate(R.layout.widget_tips_dialog, null, false)
        setContentView(rootView)

        window?.setGravity(Gravity.CENTER)
        window?.setLayout(
            MyApplication.screenWidth - dip2px(60),
            ViewGroup.LayoutParams.WRAP_CONTENT
        )
        findView()
    }


    inline fun show(func: TipsDialog.() -> Unit): TipsDialog {
        this.func()
        this.show()
        return this
    }

    private fun showClose(isShow: Boolean): TipsDialog {
        if (isShow) {
            ivClose.visible()
        } else {
            ivClose.gone()
        }
        return this
    }

    fun setTitle(title: String?): TipsDialog {
        if (title != null) {
            tvTitle.text = title
            tvTitle.visible()
            view_title.visible()
        } else {
            tvTitle.gone()
            view_title.gone()
            tvTitle.text = null
        }

        return this
    }

    fun setMessage(message: String?): TipsDialog {
        if (message != null) {
            tvContent.visible()
            setHtmlText(tvContent,message)
        } else {
            tvContent.gone()
            tvContent.text = null
        }
        return this
    }

    fun isCancelable(isCancelable: Boolean): TipsDialog {
        setCancelable(isCancelable)
        return this
    }

    private fun findView() {
        btnLeft.setOnClickListener {
            if (clickNegativeListener?.let {
                    it()
                } != false) {
                dismiss()
            }
        }
        btnRight.setOnClickListener {
            if (clickPositiveListener?.let {
                    it()
                } != false) {
                dismiss()
            }
        }

        ivClose.setOnClickListener {
            dismiss()
        }
    }

    fun setPositiveButton(text: String?, callback: (() -> Boolean?)? = null): TipsDialog {
        if (text != null) {
            btnRight.visible()
            btnRight.text = text
            this.clickPositiveListener = callback
        } else {
            btnRight.gone()
            btnRight.text = null
            this.clickPositiveListener = null
        }
        return this
    }

    fun setNegativeButton(text: String?, callback: (() -> Boolean?)? = null): TipsDialog {
        if (text != null) {
            btnLeft.visible()
            btnLeft.text = text
            this.clickNegativeListener = callback
        } else {
            btnLeft.gone()
            btnLeft.text = null
            this.clickNegativeListener = null
        }
        return this
    }



}

