package com.coupang.common.widget.toast

import android.content.Context
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.widget.TextView
import android.widget.Toast
import com.mari.common.R
import com.coupang.common.utils.dip2px

/**
 * Created by tomwang on 2020-03-20
 */
class CustomToast private constructor(val context: Context) {

    private val toast by lazy { Toast(context) }
    private var customTextView: TextView? = null

    fun setView(view: View) {
        toast.view = view
    }

    /**
     * 为了解决 RuntimeException("This Toast was not created with Toast.makeText()")
     * */
    fun setText(text: String) {
        customTextView?.text = text
    }

    fun setDuration(isShortDuration: Boolean) {
        toast.duration = if (isShortDuration) Toast.LENGTH_SHORT else Toast.LENGTH_LONG
    }

    fun setGravity(gravity: Int) {
        if (gravity == Gravity.BOTTOM) {
            toast.setGravity(gravity, 0, dip2px(25))
        } else {
            toast.setGravity(gravity, 0, 0)
        }
    }

    fun cancel() {
        toast.cancel()
    }

    fun show() {
        toast.show()
    }

    companion object {
        fun makeToast(context: Context, text: String, gravity: Int, isShortDuration: Boolean): CustomToast {
            val result = CustomToast(context)
            val view = LayoutInflater.from(context).inflate(R.layout.common_custom_toast, null)
            result.customTextView = view.findViewById(R.id.tvCustomTextView)
            result.customTextView?.text = text
            result.setView(view)
            result.setGravity(gravity)
            result.setDuration(isShortDuration)
            return result
        }
    }
}