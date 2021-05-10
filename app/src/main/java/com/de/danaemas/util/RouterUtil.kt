package com.de.danaemas.util

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.util.Log
import com.coupang.common.network.DTO
import com.coupang.common.user.UserManager.isLogin
import com.coupang.common.utils.ContextUtils
import com.de.danaemas.R
import com.de.danaemas.config.ConstantConfig.TITLE_KEY
import com.de.danaemas.config.ConstantConfig.WEB_URL_KEY
import com.de.danaemas.module.login.LoginActivity
import com.de.danaemas.module.main.MainActivity
import com.de.danaemas.module.product.ProductActivity
import com.de.danaemas.module.set.SettingActivity
import com.de.danaemas.module.web.NewWebActivity
import java.net.URLDecoder
import java.util.*

object RouterUtil {

    val APP_SCHEME: String = ContextUtils.getApplication().getString(R.string.mu_app_scheme)

    fun getWay(context: Context, linkUrl: String) {


    }

    fun goWebActivity(context: Context?, url: String, title: String) {
        var intent = Intent(context,NewWebActivity::class.java)
        intent.putExtra(WEB_URL_KEY, url)
        intent.putExtra(TITLE_KEY, title)
        context?.startActivity(intent)
    }


    fun router(context: Context?, linkUrl: String?, isNeedLogin: Boolean = false) {
        if (isNeedLogin&& !isLogin()) {
            context?.startActivity(Intent(context, LoginActivity::class.java))
            return
        }
        if (linkUrl == null || linkUrl.isEmpty() || context == null) return

        try {
            when {
                linkUrl.startsWith("http") -> {
                    goWebActivity(context, linkUrl, "")
                }
                linkUrl.startsWith(APP_SCHEME) -> {
                    toUrlBean(linkUrl)?.apply {
                        var myPath=paths?.get((paths?.size ?: 0) - 1)
                        if (getLoginStatus(myPath) && !isLogin()) {
                            context.startActivity(Intent(context, LoginActivity::class.java))
                        } else {
                            val intent =
                                Intent(context, getPageActivity(myPath))
                            if (queryMap != null && queryMap?.isNotEmpty()!!) {
                                for (params in queryMap?.keys!!) {
                                    intent.putExtra(params, queryMap?.get(params))
                                }
                            }
                            context.startActivity(intent)
                        }
                    }
                }
                else -> {
                    context.startActivity(Intent(Intent.ACTION_VIEW, Uri.parse(linkUrl)))
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    private fun getPageActivity(path: String?): Class<*> {
        return when (path) {
            "login" -> LoginActivity::class.java
            "productDetail" -> ProductActivity::class.java
            "setting" -> SettingActivity::class.java
            "main" -> MainActivity::class.java
            else -> MainActivity::class.java
        }
    }

    private fun getLoginStatus(path: String?): Boolean {
        return when (path) {
            "login" -> false
            "productDetail" -> true
            "setting" -> true
            "main" -> false
            else -> true
        }
    }

    fun toUrlBean(url: String?): RouterInfo? {
        return try {
            val linkerModel = RouterInfo()
            val uri = Uri.parse(url)
            linkerModel.urlString = url
            linkerModel.scheme = uri.scheme
            linkerModel.host = uri.host
            linkerModel.port = uri.port

            if (uri.path != null) {
                val paths = uri.path!!.split("/".toRegex()).toTypedArray()
                if (paths.isNotEmpty()) {
                    val pathList: MutableList<String> = ArrayList()
                    for (path in paths) {
                        if (!path.isEmpty()) {
                            pathList.add(path)
                        }
                    }
                    linkerModel.paths = pathList
                }
            }
            if (uri.query != null) {
                val querys = uri.encodedQuery!!.split("&".toRegex()).toTypedArray()
                if (querys != null && querys.size > 0) {
                    val queryMap: MutableMap<String, String> = HashMap()
                    val queryList: MutableList<Param> =
                        ArrayList<Param>()
                    for (query in querys) {
                        if (query.indexOf("=") >= 0) {
                            val param: Param = Param()
                            val key = query.substring(0, query.indexOf("="))
                            val value = URLDecoder.decode(query.replace("$key=", ""))
                            param.key = key
                            param.value = value
                            queryList.add(param)
                            queryMap[key] = value
                        }
                    }
                    linkerModel.queryMap = queryMap
                    linkerModel.querys = queryList
                }
            }
            linkerModel
        } catch (e: java.lang.Exception) {
            e.printStackTrace()
            Log.e("TAG", "com.webview.webviewlib.lib.UrlUtil : URLBean解析失败")
            null
        }
    }
}


class RouterInfo : DTO {
    var urlString: String? = null
    var scheme: String? = null
    var host: String? = null
    var port = 0
    var paths: List<String>? = null
    var querys: List<Param>? = null
    var queryMap: Map<String, String>? = null
}

class Param : DTO {
    var key: String? = null
    var value: String? = null

}