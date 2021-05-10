package com.coupang.common.widget.dialog

import android.app.Dialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import com.coupang.common.base.BaseDialogFragment
import com.de.common.R

/**
 * @author Allen
 * @date 2020-05-27.
 * description：
 */

class LoadingDialogFgm : BaseDialogFragment() {

    var radius: Float? = null
        internal set

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.common_dialog_loading, container, false)
    }

    fun cancelable(cancelable: Boolean): LoadingDialogFgm {
        this.isCancelable = cancelable
        return this
    }

    fun cancelOnTouchOutside(cancelable: Boolean): LoadingDialogFgm {
        dialog?.setCanceledOnTouchOutside(cancelable)
        return this
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val dialog = Dialog(mContext, R.style.common_ProgressDialog)
        dialog.setContentView(R.layout.common_dialog_loading)
        val window = dialog.window
        val res = mContext.resources
        window?.let {
            val lp = WindowManager.LayoutParams().apply {
                copyFrom(window?.attributes)
                width = res.getDimensionPixelSize(R.dimen.common_loading_dialog_width)
                height = res.getDimensionPixelSize(R.dimen.common_loading_dialog_height)
                dimAmount = 0.35f
            }
            it.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
            it.attributes = lp
            it.addFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND)
        }
        return dialog
    }
}