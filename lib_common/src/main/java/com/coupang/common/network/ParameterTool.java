package com.coupang.common.network;

import com.google.gson.Gson;

import java.io.File;
import java.util.Map;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;

public class ParameterTool {
    public static RequestBody toRequestBody(Map<String, Object> map) {
        String json = new Gson().toJson(map);
        return RequestBody.create(MediaType.parse("application/json; charset=utf-8"), json);
    }

    public static RequestBody toRequestBody(String json){
        return RequestBody.create(MediaType.parse("application/json; charset=utf-8"), json);
    }

    public static MultipartBody.Part fileCreate(File file){
        RequestBody requestImgFile = RequestBody.create(MediaType.parse("application/x-www-form-urlencoded"), file);
        MultipartBody.Part requestImgPart =
                MultipartBody.Part.createFormData("attach", file.getName(), requestImgFile);
        return requestImgPart;
    }

}
