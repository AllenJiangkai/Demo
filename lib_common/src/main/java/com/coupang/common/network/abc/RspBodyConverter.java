package com.coupang.common.network.abc;

import com.alibaba.fastjson.JSON;
import com.coupang.common.network.EmptyVO;

import java.io.IOException;
import java.lang.reflect.Type;

import okhttp3.ResponseBody;
import okio.BufferedSource;
import okio.Okio;
import retrofit2.Converter;

final class RspBodyConverter<T> implements Converter<ResponseBody, T> {

    private final Type type;
    private String TAG = getClass().getSimpleName();

    public RspBodyConverter(Type type) {
        this.type = type;
    }

    /*
     * 转换方法
     */
    @Override
    public T convert(ResponseBody value) throws IOException {
      try {
        BufferedSource bufferedSource = Okio.buffer(value.source());
        String tempStr = bufferedSource.readUtf8();
        bufferedSource.close();
        return JSON.parseObject(tempStr, type);
      }catch (Exception e){
        return JSON.parseObject("{\"code\":\"00\",\"message\":\"success\",\"data\":{}}", type);
      }
    }
}
