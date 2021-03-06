package com.coupang.common.network

import com.coupang.common.network.abc.ConverterFactory
import com.coupang.common.network.interceptor.HeaderInterceptor
import com.coupang.common.network.interceptor.LogInterceptor
import com.coupang.common.network.interceptor.ParamsInterceptor
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import java.util.concurrent.TimeUnit

/**
 * @author zli
 * @since 2020/9/23
 */
object NetworkApiFactory {

    private lateinit var retrofit: Retrofit

    init {
        initHttpClient()
    }

    fun initHttpClient() {
        val httpClient = OkHttpClient.Builder()
        httpClient.addInterceptor(HeaderInterceptor())
        httpClient.addInterceptor(ParamsInterceptor())
        httpClient.addInterceptor(LogInterceptor())
        httpClient.connectTimeout(3000, TimeUnit.SECONDS)
        httpClient.writeTimeout(3000, TimeUnit.SECONDS)
        httpClient.readTimeout(3000, TimeUnit.SECONDS)

//
//       if (BuildConfig.DEBUG) {
//           httpClient.addInterceptor(LogInterceptor())
//           httpClient.addInterceptor(MockInterceptor())
//       }
        retrofit = Retrofit.Builder()
            .baseUrl("http://47.103.73.105:9001")
//           .addConverterFactory(GsonConverterFactory.create())
            .addConverterFactory(ConverterFactory.create())
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .client(httpClient.build())
            .build()
    }

    @Synchronized
    fun <T> create(serviceClass: Class<T>): T {
        return retrofit.create(serviceClass)
    }

}