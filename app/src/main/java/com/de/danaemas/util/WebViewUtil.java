package com.de.danaemas.util;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.text.TextUtils;
import android.webkit.DownloadListener;
import android.webkit.WebSettings;
import android.webkit.WebView;

import com.coupang.common.user.UserManager;
import com.coupang.common.utils.ContextUtils;
import com.coupang.common.utils.spf.SpConfig;
import com.de.danaemas.R;
import com.de.danaemas.module.web.HttpConstants;

import java.util.HashMap;
import java.util.Map;

import ai.advance.common.BuildConfig;

/**
 * @ProjectName: My Application
 * @Package: com.kejutan.dk.utils.web
 * @ClassName: WebUtil
 * @Description: java类作用描述
 * @Author: jtao
 * @CreateDate: 2021/1/9 3:58 PM
 * @UpdateUser: 更新者
 * @UpdateDate: 2021/1/9 3:58 PM
 * @UpdateRemark: 更新说明
 * @Version: 1.0
 */
public class WebViewUtil {
    public static void initSetting(WebView mWebView, Context context){
        mWebView.setDownloadListener(new DownloadListener() {
            @Override
            public void onDownloadStart(String url, String userAgent, String contentDisposition, String mimetype, long contentLength) {
                Uri uri = Uri.parse(url);
                Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                context.getApplicationContext().startActivity(intent);
            }
        });

        mWebView.setWebContentsDebuggingEnabled(BuildConfig.DEBUG);

        WebSettings settings = mWebView.getSettings();
        settings.setJavaScriptEnabled(true);
        settings.setAllowFileAccess(true);
        settings.setCacheMode(WebSettings.LOAD_DEFAULT);
        settings.setDomStorageEnabled(true);
        settings.setUseWideViewPort(true);
        settings.setLayoutAlgorithm(WebSettings.LayoutAlgorithm.SINGLE_COLUMN);
        settings.setAllowContentAccess(true); // 是否可访问Content Provider的资源，默认值 true
        settings.setAllowFileAccessFromFileURLs(false); // 是否允许通过file url加载的Javascript读取全部资源(包括文件,http,https)，默认值 false
        settings.setAllowUniversalAccessFromFileURLs(false);
        settings.setLoadWithOverviewMode(true);
        settings.setJavaScriptCanOpenWindowsAutomatically(true);
        settings.setDatabaseEnabled(true);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            settings.setLoadsImagesAutomatically(true);
        } else {
            settings.setLoadsImagesAutomatically(false);
        }
        //webview在安卓5.0之前默认允许其加载混合网络协议内容
        // 在安卓5.0之后，默认不允许加载http与https混合内容，需要设置webview允许其加载混合网络协议内容
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            settings.setMixedContentMode(WebSettings.MIXED_CONTENT_ALWAYS_ALLOW);
        }
    }

    /**
     * 给url添加全局统一请求参数信息
     *
     * @param url
     * @return
     */
    public static String getUrl(String url) {
        if (TextUtils.isEmpty(url)) {
            return "";
        }
        String ret_url = "";

        /**** 第三方的webview请求不带参数，自己的请求需要带参数 ****/
        if (url.startsWith(HttpConstants.H5_SERVICE_URL)) {  //不是第三方
            if (url.contains("clientType=android&appVersion=")) {
                return url;
            } else {
                if (url.contains("?")) {
                    ret_url = url + "&";
                } else {
                    ret_url = url + "?";
                }
                ret_url += "clientType=android&appVersion=" + BuildConfig.VERSION_NAME
                        + "&deviceId=" + MyDevTool.getDeviceId(ContextUtils.getApplication())
                        + "&gps_adid=" + SpConfig.INSTANCE.getGps_adid()
                        + "&mobilePhone=" +UserManager.INSTANCE.getUsername()
                        + "&deviceName=" + MyDevTool.getDeviceName()
                        + "&sessionId=" +UserManager.INSTANCE.getSessionid()
                        + "&userId=" + UserManager.INSTANCE.getUid()
                        + "&merchantNumber=" + ContextUtils.getApplication().getString(R.string.mu_channel_merchant_number)
                        + "&appName=" +ContextUtils.getApplication().getString(R.string.mu_channel_name_short)
                        + "&appMarket=" + ContextUtils.getApplication().getString(R.string.mu_app_mark)
                        + "&channel=" + ContextUtils.getApplication().getString(R.string.mu_channel_name_short)
                        + "&osVersion=" + MyDevTool.getOsVersion()
                        + "&merchant=" + ContextUtils.getApplication().getString(R.string.mu_channel_merchant_number)
                        + "&platform=" + "1"
                        + "&packageId=" +ContextUtils.getApplication().getString(R.string.mu_application_id);
                return ret_url.replace(" ", "");
            }
        } else {
            return url;
        }
    }

    public static Map<String, String> getHeader(){
        Map<String, String> headerMap = new HashMap<>();
        headerMap.put("sessionid",UserManager.INSTANCE.getSessionid());
        headerMap.put("userId", UserManager.INSTANCE.getUid());
        headerMap.put("channel",ContextUtils.getApplication().getString(R.string.mu_channel_name_short));
        return headerMap;
    }
}
