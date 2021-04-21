package com.coupang.common.widget.dialog.ext

import android.app.Dialog
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.OnLifecycleEvent

/**
 * @author Allen
 * @date 2020-05-27.
 * description：
 */

fun DialogFragment.lifecycleOwner(owner: LifecycleOwner? = null): DialogFragment {
    val observer =
        DialogLifecycleObserver(::dismiss)
    val lifecycleOwner = owner ?: (context as? LifecycleOwner
            ?: throw IllegalStateException("$context is not a LifecycleOwner."))
    lifecycleOwner.lifecycle.addObserver(observer)
    return this
}

internal class DialogLifecycleObserver(private val function: () -> Unit) : LifecycleObserver {
    @OnLifecycleEvent(Lifecycle.Event.ON_DESTROY)
    fun onDestroy() = function()
}