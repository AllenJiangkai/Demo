package com.coupang.common.utils

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.animation.ValueAnimator
import android.annotation.SuppressLint
import android.app.Activity
import android.app.ActivityManager
import android.content.Context
import android.graphics.Bitmap
import android.os.Build
import android.provider.Settings
import android.provider.Settings.SettingNotFoundException
import android.text.Html
import android.text.TextUtils
import android.view.View
import android.view.Window
import android.widget.TextView
import androidx.annotation.ColorInt
import androidx.annotation.ColorRes
import androidx.annotation.StringRes
import com.coupang.common.extentions.visible
import java.io.ByteArrayOutputStream
import java.text.DecimalFormat
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*

/**
 * @author Allen
 * @date 2020-05-27.
 * description：
 */
fun dip2px(dpValue: Int): Int {
    val scale = ContextUtils.getSharedContext().resources.displayMetrics.density
    return (dpValue * scale + 0.5f).toInt()
}

fun dip2pxOfFloat(dpValue: Float): Float {
    val scale = ContextUtils.getSharedContext().resources.displayMetrics.density
    return dpValue * scale + 0.5f
}

fun sp2px(spValue: Float): Int {
    val fontScale = ContextUtils.getSharedContext().resources.displayMetrics.scaledDensity
    return (spValue * fontScale + 0.5f).toInt()
}

fun strings(@StringRes res: Int): String {
    return ContextUtils.getSharedContext().getString(res)
}

fun strings(@StringRes res: Int, vararg formatArgs: Any): String {
    return ContextUtils.getSharedContext().getString(res, *formatArgs)
}

@ColorInt
fun colors(@ColorRes color: Int): Int {
    return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
        ContextUtils.getSharedContext().getColor(color)
    } else {
        ContextUtils.getSharedContext().resources.getColor(color)
    }
}

fun setStatusBarColor(activity: Activity, color: Int) {
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
        val decorView = activity.window.decorView
        val option = View.SYSTEM_UI_FLAG_LAYOUT_STABLE
        decorView.systemUiVisibility = option
        activity.window.statusBarColor = color
    }
}

fun shortToast(toast: String) {
    ToastKt.short(toast)
}

@SuppressLint("SimpleDateFormat")
fun getDateByTimeStamp(pattern: String, stamp: Long): String {
    val d = Date(stamp)
    val sf = SimpleDateFormat(pattern)
    return sf.format(d)
}

fun isIDCardLegal(idCard: String): Boolean {
    if (idCard.length == 15 || idCard.length == 18) {
        if (idCard.length == 18) {
            var legal = false
            var sum = 0
            val checkBit = charArrayOf('1', '0', 'X', '9', '8', '7', '6', '5', '4', '3', '2')
            val add = intArrayOf(7, 9, 10, 5, 8, 4, 2, 1, 6, 3, 7, 9, 10, 5, 8, 4, 2)
            val stringArr = idCard.toCharArray()
            for (i in 0..16) {
                sum += add[i] * (stringArr[i] - '0')
            }
            if (stringArr[17] == checkBit[sum % 11]) {
                legal = true
            }
            return legal
        } else {
            return true
        }
    }
    return false
}

fun getMonthDesc(time: Long): String {
    val calendar = Calendar.getInstance()
    val timeDesc = StringBuilder()
    calendar.time = Date(time)
    when (calendar.get(Calendar.MONTH) + 1) {
       1 -> {
          timeDesc.append("Jan.")
       }
       2 -> {
          timeDesc.append("Feb.")
       }
       3 -> {
          timeDesc.append("Mar.")
       }
       4 -> {
          timeDesc.append("Apr.")
       }
       5 -> {
          timeDesc.append("May.")
       }
       6 -> {
          timeDesc.append("Jun.")
       }
       7 -> {
          timeDesc.append("Jul.")
       }
       8 -> {
          timeDesc.append("Aug.")
       }
       9 -> {
          timeDesc.append("Sept.")
       }
       10 -> {
          timeDesc.append("Oct.")
       }
       11 -> {
          timeDesc.append("Nov.")
       }
       12 -> {
          timeDesc.append("Dec.")
       }
    }
    return timeDesc.append(calendar.get(Calendar.DAY_OF_MONTH)).toString()
}

fun timeToStamp(timers: String, format: String): Long {
    var timestamp: Long = 0
    try {
        val sf = SimpleDateFormat(format, Locale.getDefault())
        val d = sf.parse(timers)// 日期转换为时间戳
        timestamp = d.time
    } catch (e: ParseException) {
        e.printStackTrace()
    }
    return timestamp
}

fun String.maskCodeOfCertificateNumber(): String {
    val length = this.length
    var result = this
    if (length >= 3) {
        val replaceCharCount = length - 2
        val builder = StringBuilder()
        for (i in 0..replaceCharCount) {
            builder.append("*")
        }
        result = this.replaceRange(1, length - 1, builder.toString())
    }
    return result
}

fun String.maskCodeOfPhoneNumber(): String {
    val length = this.length
    var result = this
    if (length == 11) {
        result = this.replaceRange(3, 7, "****")
    }
    return result
}

fun getCurYearFirstMonth(): Long {
    return try {
        val format = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
        format.parse(Calendar.getInstance().get(Calendar.YEAR).toString() + "-01-01").time
    } catch (e: ParseException) {
        e.printStackTrace()
        System.currentTimeMillis()
    }
}

fun getCurYear(): Int {
    return Calendar.getInstance().get(Calendar.YEAR)
}

fun getCurYear(time: Long): Int {
    val calendar = Calendar.getInstance()
    calendar.time = Date(time)
    return calendar.get(Calendar.YEAR)
}

fun getCurMonth(): Int {
    return Calendar.getInstance().get(Calendar.MONTH) + 1
}

fun getCurMonth(time: Long): Int {
    val calendar = Calendar.getInstance()
    calendar.time = Date(time)
    return calendar.get(Calendar.MONTH) + 1
}

fun getCurDay(): Int {
    return Calendar.getInstance().get(Calendar.DAY_OF_MONTH)
}

fun getCurDay(time: Long): Int {
    val calendar = Calendar.getInstance()
    calendar.time = Date(time)
    return calendar.get(Calendar.DAY_OF_MONTH)
}

fun getNextYearMonth(curTime: Long): Long {
    return curTime + 60L * 60 * 24 * 366 * 1000
}

fun getNextMonth(curTime: Long): Long {
    return curTime + 60L * 60 * 24 * 31 * 1000
}

fun getStatusBarHeight(context: Context): Int {
    val resourceId = context.resources.getIdentifier("status_bar_height", "dimen", "android")
    return context.resources.getDimensionPixelSize(resourceId)
}

fun adaptNotchStatusBar(context: Context, statusBar: View?) {
    if (statusBar != null) {
        val layoutParams = statusBar.layoutParams
        layoutParams.height = getStatusBarHeight(context)
        statusBar.layoutParams = layoutParams
    }
}

fun subZeroAndDot(s: String): String {
    var result = s
    if (result.indexOf(".") > 0) {
        result = result.replace("0+?$".toRegex(), "")
        result = result.replace("[.]$".toRegex(), "")
    }
    return result
}

fun getAppPackageName(context: Context): String {
    val activityManager = context.getSystemService(Context.ACTIVITY_SERVICE) as ActivityManager
    val taskInfo = activityManager.getRunningTasks(1)
    val componentInfo = taskInfo[0].topActivity
    return componentInfo?.packageName ?: ""
}

fun bitmap2byte(bitmap: Bitmap): ByteArray {
    val bao = ByteArrayOutputStream()
    bitmap.compress(Bitmap.CompressFormat.PNG, 100, bao)
    return bao.toByteArray()
}

fun setStatusBarTextColor(window: Window, lightStatusBar: Boolean) {
    val decor = window.decorView
    var ui = decor.systemUiVisibility
    ui = ui or View.SYSTEM_UI_FLAG_LAYOUT_STABLE
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
        ui = if (lightStatusBar) {
            ui or View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
        } else {
            ui and View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR.inv()
        }
    }
    decor.systemUiVisibility = ui
}

fun isLocationEnabled(context: Context): Boolean {
    var locationMode = 0
    val locationProviders: String
    return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
        locationMode = try {
            Settings.Secure.getInt(context.contentResolver, Settings.Secure.LOCATION_MODE)
        } catch (e: SettingNotFoundException) {
            e.printStackTrace()
            return false
        }
        locationMode != Settings.Secure.LOCATION_MODE_OFF
    } else {
        locationProviders = Settings.Secure.getString(
           context.contentResolver,
           Settings.Secure.LOCATION_PROVIDERS_ALLOWED
        )
        !TextUtils.isEmpty(locationProviders)
    }
}


fun openFoldAnim(v: View, start: Int, height: Int) {
    v.visibility = View.VISIBLE
    val animator = createFoldAnimator(v, start, height)
    animator.start()
}

fun createFoldAnimator(v: View, start: Int, end: Int): ValueAnimator {
    val animator = ValueAnimator.ofInt(start, end)
    animator.addUpdateListener { arg0 ->
        val value = arg0.animatedValue as Int
        val layoutParams = v.layoutParams
        layoutParams.height = value
        v.layoutParams = layoutParams
    }
    return animator
}


fun setHtmlText(textView: TextView, text: String?) {
   if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
      textView.text = Html.fromHtml(text, Html.FROM_HTML_MODE_LEGACY)
   } else {
      textView.text = Html.fromHtml(text)
   }
}
fun closeFoldAnim(hideView: View, showView: View?, start: Int, end: Int = 0) {
    val animator = createFoldAnimator(hideView, start, end)
    animator.addListener(object : AnimatorListenerAdapter() {
       override fun onAnimationEnd(animation: Animator) {
          hideView.visibility = View.GONE
          showView?.visible()
       }
    })
    animator.start()
}

fun getRunningActivityName(context: Context): String {
    val manager = context.getSystemService(Context.ACTIVITY_SERVICE) as ActivityManager
    return manager.getRunningTasks(1)[0].topActivity?.className ?: ""
}

fun formatLikeCount(count: Int): String {
    val df = DecimalFormat("0.0")
    return when {
        count < 1000 -> {
            count.toString()
        }
        count < 10000 -> {
            count.toFloat().let {
                return if (it % 1000 == 0.0f) {
                    (count / 1000).toString() + "k"
                } else {
                    df.format(it / 1000) + "k"
                }
            }
        }
        count < 10000000 -> {
            count.toFloat().let {
                return if (it % 10000 == 0.0f) {
                    (count / 10000).toString() + "w"
                } else {
                    df.format(it / 10000) + "w"
                }
            }
        }
        else -> {
            "1000w+"
        }
    }


}







