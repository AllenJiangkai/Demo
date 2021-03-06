package com.coupang.common.network.interceptor;


import com.coupang.common.impl.Tools;
import com.coupang.common.user.UserManager;
import com.coupang.common.utils.ContextUtils;
import com.coupang.common.utils.spf.SpConfig;
import com.de.common.BuildConfig;
import com.de.common.R;

import java.io.IOException;

import okhttp3.HttpUrl;
import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;


public class ParamsInterceptor implements Interceptor {

    public ParamsInterceptor() {
    }

    @Override
    public Response intercept(Chain chain) throws IOException {

//        MUUserInfoModel userInfoModel = MUMMKVTool.getInstance().getUserInfo();

        Request request = chain.request();
        HttpUrl httpUrl = request.url();
        HttpUrl.Builder urlBuilder = httpUrl.newBuilder()
                .addQueryParameter("clientType", "android")
                //app版本
                .addQueryParameter("appVersion", BuildConfig.VERSION_NAME)
                //设备ID
                .addQueryParameter("deviceId", Tools.INSTANCE.getTools().getDeviceId(ContextUtils.getApplication()))
                //google 唯一设备识别码
                .addQueryParameter("gps_adid", SpConfig.INSTANCE.getGps_adid())
                //设备名称
                .addQueryParameter("deviceName", Tools.INSTANCE.getTools().getDeviceName(ContextUtils.getApplication()))
                //系统版本
                .addQueryParameter("osVersion",  Tools.INSTANCE.getTools().getOsVersion(ContextUtils.getApplication()))
                //渠道
                .addQueryParameter("channel", ContextUtils.getSharedContext().getString(R.string.mu_channel_name_short))
                //app名称
                .addQueryParameter("appName", ContextUtils.getSharedContext().getString(R.string.mu_channel_name_short))
                //包名
                .addQueryParameter("packageId", ContextUtils.getSharedContext().getString(R.string.mu_application_id))
                //市场
                .addQueryParameter("appMarket", ContextUtils.getSharedContext().getString(R.string.mu_app_mark))
                .addQueryParameter("merchantNumber", ContextUtils.getSharedContext().getString(R.string.mu_channel_merchant_number))
                .addQueryParameter("merchant", ContextUtils.getSharedContext().getString(R.string.mu_channel_merchant_number))
                //平台
                .addQueryParameter("platform", "1");
        if (UserManager.INSTANCE.isLogin() ){
            urlBuilder.addQueryParameter("userId", UserManager.INSTANCE.getUid());
            //手机号
            urlBuilder.addQueryParameter("mobilePhone", UserManager.INSTANCE.getUsername());
        }

        Request.Builder builder = request.newBuilder().url(urlBuilder.build());
        Response response = chain.proceed(builder.build());
        return response;

    }

}