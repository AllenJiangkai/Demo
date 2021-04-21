package com.coupang.common.network.abc;

import java.lang.annotation.Annotation;
import java.lang.reflect.Type;

import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Converter;
import retrofit2.Retrofit;


/**
 * A {@linkplain Converter.Factory converter} which uses FastJson for JSON.
 * <p>
 * Because FastJson is so flexible in the types it supports, this converter assumes that it can
 * handle all types. If you are mixing JSON serialization with something else (such as protocol
 * buffers), you must {@linkplain Retrofit.Builder#addConverterFactory(Converter.Factory) add
 * this instance} last to allow the other converters a chance to see their types.
 */
public class ConverterFactory extends Converter.Factory{

  public static ConverterFactory create() {
    return new ConverterFactory();
  }

  /**
   * 需要重写父类中responseBodyConverter，该方法用来转换服务器返回数据
   */
  @Override
  public Converter<ResponseBody, ?> responseBodyConverter(Type type, Annotation[] annotations, Retrofit retrofit) {
    return new RspBodyConverter<>(type);
  }

  /**
   * 需要重写父类中responseBodyConverter，该方法用来转换发送给服务器的数据
   */
  @Override
  public Converter<?, RequestBody> requestBodyConverter(Type type, Annotation[] parameterAnnotations, Annotation[] methodAnnotations, Retrofit retrofit) {
    return new ReqBodyConverter<>();
  }
}
