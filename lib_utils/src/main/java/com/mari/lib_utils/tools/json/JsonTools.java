package com.mari.lib_utils.tools.json;

import android.text.TextUtils;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.Reader;
import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/**
 * @author Allen
 * @date 2020-05-27.
 * description：
 */
public class JsonTools {
    private static final Gson sGson = new Gson();
    private static final Gson sPrettyGson;
    private static final Gson eWXAGson;

    public JsonTools() {
    }

    public static String toPrettyJsonString(Object object) {
        return sPrettyGson.toJson(object);
    }

    public static String toPrettyJsonString(Object object, Class<?> clazz) {
        return sPrettyGson.toJson(object, clazz);
    }

    public static <T> T fromJson(@Nullable String json, Class<T> clazz) {
        return sGson.fromJson(json, clazz);
    }

    public static <T> T fromJson(@Nullable String json, Type typeOfT) {
        return sGson.fromJson(json, typeOfT);
    }

    public static <T> T fromJson(@NonNull Reader reader, Class<T> classOfT) {
        return sGson.fromJson(reader, classOfT);
    }

    public static <T> T fromJson(@NonNull Reader reader, Type typeOfT) {
        return sGson.fromJson(reader, typeOfT);
    }

//    public static <T> T fromJson(@NonNull JsonReader reader, Type typeOfT) {
//        return sGson.fromJson(reader, typeOfT);
//    }

    public static <T> T fromJson(JsonElement element, Type typeOfT) {
        return sGson.fromJson(element, typeOfT);
    }

    public static <T> T fromJson(JsonElement json, Class<T> classOfT) {
        return sGson.fromJson(json, classOfT);
    }

    @Nullable
    public static Map<String, String> fromJsonToMap(String mapJson) {
        if (!TextUtils.isEmpty(mapJson)) {
            JSONObject jsonObject = null;

            try {
                jsonObject = new JSONObject(mapJson);
            } catch (JSONException var7) {
                var7.printStackTrace();
                return null;
            }

            Iterator<String> iterator = jsonObject.keys();
            if (iterator != null) {
                HashMap map = new HashMap();

                while(iterator.hasNext()) {
                    String key = (String)iterator.next();
                    String value = null;

                    try {
                        value = jsonObject.getString(key);
                    } catch (JSONException var8) {
                        var8.printStackTrace();
                        continue;
                    }

                    map.put(key, value);
                }

                return map;
            }
        }

        return null;
    }

    public static String toJsoneWEA(@Nullable Object src) {
        return eWXAGson.toJson(src);
    }

    public static String toJson(@Nullable Object src) {
        return sGson.toJson(src);
    }

    public static String toJson(Object src, @NonNull Type typeOfSrc) {
        return sGson.toJson(src, typeOfSrc);
    }

    public static void toJson(@Nullable Object src, Appendable writer) {
        sGson.toJson(src, writer);
    }

    public static void toJson(Object src, Type typeOfSrc, Appendable writer) {
        sGson.toJson(src, typeOfSrc, writer);
    }

//    public static void toJson(Object src, Type typeOfSrc, JsonWriter writer) {
//        sGson.toJson(src, typeOfSrc, writer);
//    }

    @Nullable
    public static String toJson(Map<String, String> map) {
        if (map != null) {
            JSONObject jsonObject = new JSONObject(map);
            String mapJson = jsonObject.toString();
            return mapJson;
        } else {
            return null;
        }
    }

    public static JsonObject toJsonObject(@NonNull Object src) {
        return toJsonElement(src).getAsJsonObject();
    }

    public static JsonArray toJsonArray(@NonNull Object src) {
        return toJsonElement(src).getAsJsonArray();
    }

    public static JsonElement toJsonElement(@NonNull Object src) {
        return sGson.toJsonTree(src);
    }

    static {
        GsonBuilder builder = new GsonBuilder();
        sPrettyGson = builder.setPrettyPrinting().create();
        eWXAGson = builder.excludeFieldsWithoutExposeAnnotation().create();
    }
}
