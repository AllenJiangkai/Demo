package com.coupang.common.network.interceptor;


import android.util.Log;

import com.coupang.common.user.UserManager;

import org.jetbrains.annotations.NotNull;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

/**
 * @ProjectName: My Application
 * @Package: com.shuxing.network.interceptor
 * @ClassName: HeaderInterceptor
 * @Description: java类作用描述
 * @Author: jtao
 * @CreateDate: 2021/1/4 7:07 PM
 * @UpdateUser: 更新者
 * @UpdateDate: 2021/1/4 7:07 PM
 * @UpdateRemark: 更新说明
 * @Version: 1.0
 */
public class HeaderInterceptor implements Interceptor {
    @NotNull
    @Override
    public Response intercept(@NotNull Chain chain) throws IOException {
        Request request = chain.request();

        Request.Builder builder = request.newBuilder()
                .addHeader("Content-Type", "application/json")
                .addHeader("languages", "zh-CN")
                .addHeader("accept", "*/*")
                .addHeader("channel", "");

        if (UserManager.INSTANCE.isLogin()) {
            builder.addHeader("sessionid", UserManager.INSTANCE.getSessionid())
                    .addHeader("userId", UserManager.INSTANCE.getUid());
        }

        return chain.proceed(builder.build());
    }
}
