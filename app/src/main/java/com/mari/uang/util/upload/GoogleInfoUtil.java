package com.mari.uang.util.upload;

import android.content.Context;

import java.lang.reflect.Method;

/**
 * @ProjectName: Business
 * @Package: com.mari.uang.util.upload
 * @ClassName: GoogleInfoUtil
 * @Description: java类作用描述
 * @Author: jtao
 * @CreateDate: 2021/5/1 5:05 PM
 * @UpdateUser: 更新者
 * @UpdateDate: 2021/5/1 5:05 PM
 * @UpdateRemark: 更新说明
 * @Version: 1.0
 */
public class GoogleInfoUtil {

    public static String getPlayAdId(Context context) {
        try {
            Object AdvertisingInfoObject = getAdvertisingInfoObject(context);
            String playAdid = (String) invokeInstanceMethod(AdvertisingInfoObject, "getId", null);
            return playAdid;
        } catch (Throwable t) {
            return null;
        }
    }

    private static Object getAdvertisingInfoObject(Context context) throws Exception {
        return invokeStaticMethod("com.google.android.gms.ads.identifier.AdvertisingIdClient", "getAdvertisingIdInfo", new Class[]{Context.class}, context);
    }

    public static Class forName(String className) {
        try {
            Class classObject = Class.forName(className);
            return classObject;
        } catch (Throwable t) {
            return null;
        }
    }

    public static Object invokeStaticMethod(String className, String methodName, Class[] cArgs, Object... args)
            throws Exception {
        Class classObject = Class.forName(className);
        return invokeMethod(classObject, methodName, null, cArgs, args);
    }

    public static Object invokeInstanceMethod(Object instance, String methodName, Class[] cArgs, Object... args)
            throws Exception {
        Class classObject = instance.getClass();
        return invokeMethod(classObject, methodName, instance, cArgs, args);
    }

    public static Object invokeMethod(Class classObject, String methodName, Object instance, Class[] cArgs, Object... args)
            throws Exception {
        @SuppressWarnings("unchecked")
        Method methodObject = classObject.getMethod(methodName, cArgs);
        if (methodObject == null) {
            return null;
        }

        Object resultObject = methodObject.invoke(instance, args);
        return resultObject;
    }

}
