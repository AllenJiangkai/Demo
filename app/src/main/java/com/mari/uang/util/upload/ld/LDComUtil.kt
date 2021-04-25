package com.mari.uang.util.upload.ld

import android.content.Context
import android.content.pm.PackageInfo
import android.content.pm.PackageManager
import android.text.TextUtils
import androidx.core.app.ActivityCompat

/**
 *
 * @ProjectName:    Business
 * @Package:        com.mari.uang.util.upload.ld
 * @ClassName:      LDComUtil
 * @Description:     java类作用描述
 * @Author:         jtao
 * @CreateDate:     2021/4/21 4:21 PM
 * @UpdateUser:     更新者
 * @UpdateDate:     2021/4/21 4:21 PM
 * @UpdateRemark:   更新说明
 * @Version:        1.0
 */
object LDComUtil {
    fun getNonNullText(text: String?): String? {
        return if (TextUtils.isEmpty(text)) "" else text
    }

    fun haveSelfPermission(
        paramContext: Context?,
        paramString: String?
    ): Boolean {
        return ActivityCompat.checkSelfPermission(paramContext!!, paramString!!) == 0
    }

    fun getVersionCode(context: Context): Int {
        var info: PackageInfo? = null
        var versionCode = 0
        try {
            info = context.packageManager.getPackageInfo(context.packageName, 0)
            versionCode = info.versionCode
        } catch (var3: PackageManager.NameNotFoundException) {
            var3.printStackTrace()
        }
        return versionCode
    }

    fun getVersionName(context: Context): String? {
        var info: PackageInfo? = null
        var versionName: String? = ""
        try {
            info = context.packageManager.getPackageInfo(context.packageName, 0)
            versionName = info.versionName
        } catch (var3: PackageManager.NameNotFoundException) {
            var3.printStackTrace()
        }
        return versionName
    }

    fun getPackageName(context: Context): String? {
        var info: PackageInfo? = null
        var packageName: String? = ""
        try {
            info = context.packageManager.getPackageInfo(context.packageName, 0)
            packageName = info.packageName
        } catch (var3: PackageManager.NameNotFoundException) {
            var3.printStackTrace()
        }
        return packageName
    }
}