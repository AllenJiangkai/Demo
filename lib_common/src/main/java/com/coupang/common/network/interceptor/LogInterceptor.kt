package com.coupang.common.network.interceptor

import android.util.Log
import okhttp3.*
import okio.Buffer
import java.io.IOException

/**
 * @author Allen
 * @date 2020-10-09.
 */
class LogInterceptor : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {

        if(chain.request().body is MultipartBody){
            Log.e("Http","[HTTP Request]${chain.request().url}  ")
        }else{
            Log.e("Http","[HTTP Request]${chain.request().url} ${bodyToString(chain.request().body)} ")
        }
        val response = chain.proceed(chain.request())
        Log.e("Http","[HTTP Response]${response.request.url} ${response.peekBody(1024 * 1024).string()}")
        return response
    }


    private fun bodyToString(request: RequestBody?): String {
        return try {
            val buffer = Buffer()
            request?.let {
                it.writeTo(buffer)
                buffer.readUtf8()
            } ?: ""
        } catch (e: IOException) {
            ""
        }
    }
}
