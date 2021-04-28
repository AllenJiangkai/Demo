package com.coupang.common.utils

import android.app.Activity
import android.content.Context
import android.view.inputmethod.InputMethodManager

object KeymapUtils {

    fun hideInput(context: Activity) {
        val activity = context
        if (context.currentFocus != null) {
            (activity.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager).hideSoftInputFromWindow(
                activity.currentFocus!!.windowToken, InputMethodManager.HIDE_NOT_ALWAYS
            )
        }
    }


}