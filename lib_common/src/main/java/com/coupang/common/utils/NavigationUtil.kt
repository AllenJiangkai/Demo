package com.coupang.common.utils

import android.app.Activity
import android.content.Context
import android.content.Context.WINDOW_SERVICE
import android.graphics.Point
import android.os.Build
import android.provider.Settings
import android.util.DisplayMetrics
import android.view.Display
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.LinearLayout
import androidx.appcompat.widget.FitWindowsLinearLayout

/**
 * @author Allen
 * @date 2020-05-27.
 * description：
 */
class NavigationUtil private constructor() {

    companion object {
        fun hasNavigationBar(activity: Activity?): Boolean {
            return activity?.let {
                when {
                    commonPhoneDevice() -> {
                        val display = it.windowManager.defaultDisplay
                        val size = Point()
                        val realSize = Point()
                        display.getSize(size)
                        display.getRealSize(realSize)
                        realSize.y - size.y > 100 //华为判断不准，没有也不一样
                    }
                    isMIUI() -> getHeightOfNavigationBar(it) > 0
                    else -> navigationBarIsOpen(it)
                }
            } ?: false
        }

        fun getBottomNavigationBarHeight(activity: Activity): Int {
            val resources = activity.resources
            val resourceId = resources.getIdentifier("navigation_bar_height", "dimen", "android")
            return resources.getDimensionPixelSize(resourceId)
        }

        fun getStatusBarsHeight(activity: Activity): Int {
            val resources = activity.resources
            val resourceId = resources.getIdentifier("status_bar_height", "dimen", "android")
            return resources.getDimensionPixelSize(resourceId)
        }
        fun getScreenSize(context: Context): IntArray {
            var wm: WindowManager?
            try {
                wm = context.getSystemService(WINDOW_SERVICE) as WindowManager
            } catch (var9: Throwable) {
                wm = null
            }

            if (wm == null) {
                return intArrayOf(0, 0)
            } else {
                var display: Display? = null

                try {
                    display = wm.defaultDisplay
                } catch (var8: Throwable) {
                }

                val dm: DisplayMetrics
                return if (display == null) {
                    try {
                        dm = context.resources.displayMetrics
                        intArrayOf(dm.widthPixels, dm.heightPixels)
                    } catch (var5: Throwable) {
                        intArrayOf(0, 0)
                    }

                } else if (Build.VERSION.SDK_INT < 13) {
                    try {
                        dm = DisplayMetrics()
                        display.getMetrics(dm)
                        intArrayOf(dm.widthPixels, dm.heightPixels)
                    } catch (var6: Throwable) {
                        intArrayOf(0, 0)
                    }

                } else {
                    try {
                        val size = Point()
                        val method = display.javaClass.getMethod("getRealSize", Point::class.java)
                        method.isAccessible = true
                        method.invoke(display, size)
                        intArrayOf(size.x, size.y)
                    } catch (var7: Throwable) {
                        intArrayOf(0, 0)
                    }

                }
            }
        }
        /**
         * @return false 关闭了NavigationBar ,true 开启
         */
        private fun navigationBarIsOpen(activity: Activity): Boolean {
            var navigationBarHeight = 0
            findRootLinearLayout(activity)?.also {
                (it.layoutParams as? ViewGroup.MarginLayoutParams)?.let { params ->
                    navigationBarHeight = params.bottomMargin
                }
            }
            return navigationBarHeight != 0
        }

        private fun commonPhoneDevice(): Boolean {
            return (Build.FINGERPRINT.contains("samsung", true)
                    || Build.FINGERPRINT.contains("HUAWEI", true)
                    || Build.FINGERPRINT.contains("HONOR", true)
                    || Build.FINGERPRINT.contains("vivo", true)
                    || Build.FINGERPRINT.contains("OPPO", true))
        }

        private fun isMIUI(): Boolean {
            return Build.FINGERPRINT.contains("xiaomi", true)
        }

        private fun getHeightOfNavigationBar(context: Context): Int {
         //   如果小米手机开启了全面屏手势隐藏了导航栏则返回 0
            if (Settings.Global.getInt(context.contentResolver, "force_fsg_nav_bar", 0) != 0) {
                return 0
            }
            val realHeight =   getScreenSize(context)[1]
            val d = (context.getSystemService(WINDOW_SERVICE) as WindowManager).defaultDisplay
            val displayMetrics = DisplayMetrics()
            d.getMetrics(displayMetrics)
            val displayHeight = displayMetrics.heightPixels
            return realHeight - displayHeight
        }

        /**
         * 从R.id.content从上遍历，拿到 DecorView 下的唯一子布局LinearLayout
         * 获取对应的bottomMargin 即可得到对应导航栏的高度，0为关闭了或没有导航栏
         */
        private fun findRootLinearLayout(activity: Activity): ViewGroup? {
            var onlyLinearLayout: ViewGroup? = null
            try {
                val window = activity.window
                val decorView = window.decorView as ViewGroup
                (activity.findViewById<View>(android.R.id.content))?.also {
                    var temp = it
                    while (temp.parent != decorView) {
                        val parent = temp.parent as ViewGroup
                        if (parent is LinearLayout) {
                            //如果style设置了不带toolbar,mContentView上层布局会由原来的 ActionBarOverlayLayout->FitWindowsLinearLayout)
                            if (parent is FitWindowsLinearLayout) {
                                temp = parent
                                continue
                            } else {
                                onlyLinearLayout = parent
                                break
                            }
                        } else {
                            temp = parent
                        }
                    }
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
            return onlyLinearLayout
        }
    }


}
