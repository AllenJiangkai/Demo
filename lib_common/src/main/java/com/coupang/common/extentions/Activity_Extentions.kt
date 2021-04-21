package com.coupang.common.extentions

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.inputmethod.InputMethodManager
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders

/**
 * @author Allen
 * @date 2020-05-27.
 * description：
 */

fun <T : ViewModel> FragmentActivity.createViewModel(cls: Class<T>, factory: ViewModelProvider.Factory? = null): T {
    return ViewModelProviders.of(this, factory).get(cls)
}

fun <T : ViewModel> Fragment.createViewModel(cls: Class<T>, factory: ViewModelProvider.Factory? = null): T {
    return ViewModelProviders.of(this, factory).get(cls)
}

fun <T : Activity> Activity.gotoActivity(cls: Class<T>, finishSelf: Boolean = false) {
    gotoActivity(Intent().also {
        it.setClass(this, cls)
    }, null, finishSelf)
}

fun Activity.gotoActivity(intent: Intent, options: Bundle? = null, finishSelf: Boolean = false) {
    gotoActivityForResult(intent, options)
    if (finishSelf) this.finish()
}

fun <T : Activity> Activity.gotoActivityForResult(cls: Class<T>, requestCode: Int = -1) {
    this.startActivityForResult(Intent().also {
        it.setClass(this, cls)
    }, requestCode, null)
}

fun Activity.gotoActivityForResult(intent: Intent, options: Bundle? = null, requestCode: Int = -1) {
    this.startActivityForResult(intent, requestCode, options)
}

fun Activity.hideKeyBoard(): Boolean {
    return try {
        val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(window.decorView.windowToken, 0)
    } catch (exception: RuntimeException) {
        false
    }
}