package com.mari.lib_utils.tools

import android.R
import android.app.Activity
import android.content.Context
import android.content.res.Resources
import android.os.Build
import android.util.TypedValue
import android.view.Window
import android.view.WindowManager

class BarUtils private constructor() {
    companion object {
        /**
         * 设置透明状态栏(api大于19方可使用)
         *
         * 可在Activity的onCreat()中调用
         *
         * 需在顶部控件布局中加入以下属性让内容出现在状态栏之下
         *
         * android:clipToPadding="true"
         *
         * android:fitsSystemWindows="true"
         *
         * @param activity 当前活动
         */
        fun setTransparentStatusBar(activity: Activity) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) { //透明状态栏
                activity.window.addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
                //透明导航栏
                activity.window
                    .addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION)
            }
        }

        /**
         * 隐藏状态栏
         *
         * 也就是设置全屏，一定要在setContentView之前调用，否则报错
         *
         * 此方法Activity可以继承AppCompatActivity
         *
         * 启动的时候状态栏会显示一下再隐藏，比如QQ的欢迎界面
         *
         * 在配置文件中Activity加属性android:theme="@android:style/Theme.NoTitleBar.Fullscreen"
         *
         * 如加了以上配置Activity不能继承AppCompatActivity，会报错
         *
         * @param activity 当前活动
         */
        fun hideStatusBar(activity: Activity) {
            activity.requestWindowFeature(Window.FEATURE_NO_TITLE)
            activity.window.setFlags(
                WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN
            )
        }

        /**
         * 获取状态栏高度
         *
         * @param context 上下文
         * @return 状态栏高度
         */
        fun getStatusBarHeight(context: Context): Int {
            var result = 0
            val resourceId =
                context.resources.getIdentifier("status_bar_height", "dimen", "android")
            if (resourceId > 0) {
                result = context.resources.getDimensionPixelSize(resourceId)
            }
            return result
        }

        /**
         * 不传Context获取状态栏高度
         *
         * @return
         */
        val statusBarHeight: Int
            get() {
                val resources = Resources.getSystem()
                val resourceId = resources.getIdentifier("status_bar_height", "dimen", "android")
                return resources.getDimensionPixelSize(resourceId)
            }

        /**
         * 判断状态栏是否存在
         *
         * @param activity activity
         * @return `true`: 存在<br></br>`false`: 不存在
         */
        fun isStatusBarExists(activity: Activity): Boolean {
            val params = activity.window.attributes
            return params.flags and WindowManager.LayoutParams.FLAG_FULLSCREEN != WindowManager.LayoutParams.FLAG_FULLSCREEN
        }

        /**
         * 获取ActionBar高度
         *
         * @param activity activity
         * @return ActionBar高度
         */
        fun getActionBarHeight(activity: Activity): Int {
            val tv = TypedValue()
            return if (activity.theme.resolveAttribute(R.attr.actionBarSize, tv, true)) {
                TypedValue.complexToDimensionPixelSize(
                    tv.data, activity.resources
                        .displayMetrics
                )
            } else 0
        }
    }

    init {
        throw UnsupportedOperationException("u can't instantiate me...")
    }
}