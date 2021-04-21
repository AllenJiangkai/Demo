package com.mari.lib_utils.tools.json;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import com.google.gson.JsonPrimitive;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;

import java.lang.reflect.Type;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * @author Allen
 * @date 2020-05-27.
 * description：
 */
public class GsonFactoryUtil {
    private static Gson gson;

    private GsonFactoryUtil() {
    }

    public static Gson getInstance() {
        if (gson == null) {
            GsonBuilder gsonBuilder = new GsonBuilder();
            gsonBuilder.setLenient();
            gsonBuilder.registerTypeAdapter(Date.class, new GsonFactoryUtil.DateSerializer()).setDateFormat("yyyy-MM-dd'T'HH:mm:ss");
            gsonBuilder.registerTypeAdapter(Date.class, new GsonFactoryUtil.DateDeserializer()).setDateFormat("yyyy-MM-dd'T'HH:mm:ss");
            gson = gsonBuilder.create();
        }

        return gson;
    }

    public static <T> T jsonToObj(String result, Class<T> clazz) {
        return getInstance().fromJson(result, clazz);
    }

    public static String objectToJsonStr(Object bean) {
        return getInstance().toJson(bean);
    }

    private static class DateSerializer implements JsonSerializer<Date> {
        private DateSerializer() {
        }

        public JsonElement serialize(Date src, Type typeOfSrc, JsonSerializationContext context) {
            return new JsonPrimitive(src.getTime());
        }
    }

    private static class DateDeserializer implements JsonDeserializer<Date> {
        SimpleDateFormat df;

        private DateDeserializer() {
            this.df = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
        }

        public Date deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
            try {
                return this.df.parse(json.getAsString());
            } catch (ParseException var5) {
                var5.printStackTrace();
                return null;
            }
        }
    }
}
