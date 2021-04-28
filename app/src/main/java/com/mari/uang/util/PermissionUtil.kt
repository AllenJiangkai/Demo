package com.mari.uang.util

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Color
import android.net.Uri
import android.provider.Settings
import androidx.core.content.ContextCompat
import com.bigkoo.pickerview.OptionsPickerView
import com.bigkoo.pickerview.OptionsPickerView.OnOptionsSelectListener
import com.coupang.common.utils.strings
import com.mari.uang.R
import com.mari.uang.widget.TipsDialog
import com.yanzhenjie.permission.Action
import com.yanzhenjie.permission.AndPermission
import com.yanzhenjie.permission.runtime.PermissionDef


object PermissionUtil {

    fun requestPermission(
        context: Activity?,
        @PermissionDef permission: Array<String>?,
        action: Action<List<String>>, needFinish: Boolean = false, canBackDismiss: Boolean = true
    ){
        AndPermission.with(context)
            .runtime()
            .permission(permission)
            .onGranted(action)
            .onDenied { permissions: List<String?>? ->
                if (AndPermission.hasAlwaysDeniedPermission(context, permissions)) {
                    showPermissionTipsDialog(
                        context,
                        canBackDismiss
                    ) {
                        if (needFinish) {
                            context?.finish()
                        }
                    }
                } else {
                    secondShowPermissionTipsDialog(
                        context,
                        canBackDismiss,
                        {
                            if (needFinish) {
                                context?.finish()
                            }
                        },
                        {
                            requestPermission(
                                context = context,
                                permission = permission,
                                action = action,
                                needFinish = needFinish,
                                canBackDismiss = canBackDismiss
                            )
                        })
                }
            }
            .start()
    }

    /**
     * 检查是否没有权限
     * @param context
     * @param permission
     * @return false 没有权限
     */
    fun checkPermission(
        context: Context,
        vararg permission: String
    ): Boolean {
        val pm = context.packageManager
        val perNames: Array<String> = permission as Array<String>
        for (perName in perNames) {
            if (PackageManager.PERMISSION_GRANTED != pm.checkPermission(
                    perName,
                    context.packageName
                )
            ) {
                return false
            }
        }
        return true
    }

    private fun showPermissionTipsDialog(
        context: Activity?, canBackDismiss: Boolean = true,
        callback: (() -> Unit)
    ) {
        context?.apply {
            TipsDialog(context).setTitle(strings(R.string.dialog_per_prompt))
                .setMessage(
                    String.format(
                        strings(
                            R.string.dialog_per_message,
                            strings(R.string.app_name)
                        )
                    )
                )
                .isCancelable(canBackDismiss)
                .setNegativeButton(strings(R.string.dialog_per_left)){
                    callback()
                    true
                }
                .setPositiveButton(strings(R.string.dialog_per_right)){
                    val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
                    intent.data = Uri.parse("package:" + context.packageName)
                    context.startActivityForResult(intent, 100)
                    false
                }.show()
        }
    }


    fun secondShowPermissionTipsDialog(
        context: Activity?,
        canBackDismiss: Boolean,
        leftCallback: (() -> Unit),
        rightCallback: (() -> Unit)
    ){
        context?.apply {
            TipsDialog(context).setTitle(strings(R.string.dialog_per_prompt))
                .setMessage(
                    String.format(
                        strings(
                            R.string.dialog_per_message,
                            strings(R.string.app_name)
                        )
                    )
                )
                .isCancelable(canBackDismiss)
                .setNegativeButton(strings(R.string.dialog_per_left)){
                    leftCallback()
                    true
                }
                .setPositiveButton(strings(R.string.dialog_per_right_again)){
                    rightCallback()
                    true
                }.show()
        }
    }

    fun firstShowPermissionTipsDialog(
        context: Activity?,
        message: String,
        rightCallback: (() -> Unit)
    ){
        context?.apply {
            TipsDialog(context).setTitle(strings(R.string.dialog_per_prompt))
                .setMessage(message)
                .setNegativeButton(strings(R.string.dialog_cancel)){
                    true
                }
                .setPositiveButton(strings(R.string.dialog_confirm)){
                    rightCallback()
                    true
                }.show()
        }
    }


    fun showOptionDialog(context: Context,
        onOptionsSelectListener: OnOptionsSelectListener?
    ): OptionsPickerView<*> {
        return OptionsPickerView.Builder(context, onOptionsSelectListener)
            .setSubCalSize(16)
            .setSubmitColor(ContextCompat.getColor(context, R.color.main_color))
            .setCancelColor(ContextCompat.getColor(context, R.color.color_999999))
            .setTextColorCenter(ContextCompat.getColor(context, R.color.color_666666))
            .setTextColorOut(ContextCompat.getColor(context, R.color.color_999999))
            .setDividerColor(ContextCompat.getColor(context, R.color.color_f4f5f6))
            .setTitleBgColor(ContextCompat.getColor(context, R.color.white))
            .setContentTextSize(20)
            .setDividerColor(Color.BLACK)
            .setCancelText(context.getString(R.string.dialog_per_left))
            .setSubmitText(context.getString(R.string.dialog_confirm))
            .setOutSideCancelable(true)
            .build()
    }



}