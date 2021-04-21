package com.coupang.common.utils

import android.view.Gravity
import com.coupang.common.widget.toast.CustomToast

/**
 * @author Allen
 * @date 2020-05-27.
 * description：
 */

object ToastKt {

//    private var currentShowingToast: Toast? = null
//
//    fun short(text: String) {
//        if (text.isNotEmpty()) {
//            currentShowingToast?.cancel()
//            // 为解决小米部分手机会自动在Toast前加App名称的问题，必须先带空字符串再设置文字
//            currentShowingToast = Toast.makeText(ContextUtils.getSharedContext(), "", Toast.LENGTH_SHORT)
//            currentShowingToast?.setText(text)
//            currentShowingToast?.show()
//        }
//    }


    private var currentShowingToast: CustomToast? = null

    fun short(text: String) {
        if (text.isNotEmpty()) {
            currentShowingToast?.cancel()
            setValue(text, Gravity.BOTTOM)
            currentShowingToast?.show()
        }
    }

    fun shortWithCenter(text: String) {
        if (text.isNotEmpty()) {
            currentShowingToast?.cancel()
            setValue(text, Gravity.CENTER)
            currentShowingToast?.show()
        }
    }

    private fun setValue(text: String, gravity: Int) {
        // 为解决小米部分手机会自动在Toast前加App名称的问题，必须先带空字符串再设置文字
        currentShowingToast = CustomToast.makeToast(ContextUtils.getSharedContext(), "", gravity, true)
        currentShowingToast?.setText(text)
    }

}