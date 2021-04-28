package com.mari.lib_utils.system

import android.app.Activity
import android.content.Context
import android.view.inputmethod.InputMethodManager
import android.widget.EditText


/**
 * @author Alan_Xiong
 *
 * @desc: 键盘操作
 * @time 2020/5/27 4:55 PM
 */
class KeymapUtils {

    companion object {
        /**
         * 打开软键盘
         */
        fun openKeyboard(
            mEditText: EditText,
            mContext: Context
        ) {
            mEditText.isFocusable = true
            mEditText.isFocusableInTouchMode = true
            mEditText.requestFocus()
            val imm = mContext
                .getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            imm.showSoftInput(mEditText, InputMethodManager.RESULT_SHOWN)
            imm.toggleSoftInput(
                InputMethodManager.SHOW_FORCED,
                InputMethodManager.HIDE_IMPLICIT_ONLY
            )
        }

        /**
         * 关闭软键盘
         */
        fun closeKeybord(
            mEditText: EditText,
            mContext: Context
        ) {
            val imm = mContext
                .getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            imm.hideSoftInputFromWindow(mEditText.windowToken, 0)
        }

        /**
         * 关闭软键盘
         */
        fun hideInput(activity: Activity) {
            if (activity.currentFocus != null) {
                val inputManager =
                    activity.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                inputManager.hideSoftInputFromWindow(activity.currentFocus!!.windowToken, 0)
            }
        }

        /**
         * 判断当前软键盘是否打开
         */
        fun isSoftInputShow(activity: Activity): Boolean { // 虚拟键盘隐藏 判断view是否为空
            val view = activity.window.peekDecorView()
            if (view != null) { // 隐藏虚拟键盘
                val inputmanger = activity
                    .getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                //       inputmanger.hideSoftInputFromWindow(view.getWindowToken(),0);
                return inputmanger.isActive && activity.window.currentFocus != null
            }
            return false
        }
    }
}