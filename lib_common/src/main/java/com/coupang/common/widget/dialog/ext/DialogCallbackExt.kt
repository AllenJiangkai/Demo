package com.coupang.common.widget.dialog.ext

import com.coupang.common.widget.dialog.*


/**
 * @author Allen
 * @date 2020-05-27.
 * description：
 */


internal fun MutableList<MDDialogCallback>.invokeAll(dialog: TextButtonStyleDialog) {
    for (callback in this) {
        callback.invoke(dialog)
    }
}