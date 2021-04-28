package com.coupang.common.network.interceptor

import com.coupang.common.network.NetworkHelper
import okhttp3.Interceptor
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.Protocol
import okhttp3.Response
import okhttp3.ResponseBody

/**
 * @author Allen
 * @date 2020-12-28.
 */
class MockInterceptor : Interceptor {

    override fun intercept(chain: Interceptor.Chain): Response {
        val request = chain.request()
        val response = chain.proceed(request)
        return if(NetworkHelper.mockManager?.getMockStatusByUrl(getRequestUrl(request.url.toString())) == true){
            mockResponse(response, request.url.toString())
        }else{
            response
        }

    }


    private fun mockResponse(response: Response,url:String): Response {
        val responseString = NetworkHelper.mockManager?.getMockDataByUrl(getRequestUrl(url))
        return Response.Builder()
                .code(200)
                .request(response.request)
                .protocol(Protocol.HTTP_1_0)
                .message("操作成功")
                .body(ResponseBody.create("application/json".toMediaTypeOrNull(), responseString?:""))
                .addHeader("content-type", "application/json")
                .build()
    }

    private fun getRequestUrl(requestUrl:String):String{
        return requestUrl.replace("", "")
    }


}