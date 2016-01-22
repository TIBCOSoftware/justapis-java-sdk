package com.anypresence.gw;

import java.util.ArrayList;
import java.util.Date;
import java.util.Map;

import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;

public class JSONParser implements IParser {
    public static GsonBuilder defaultGsonSerializer;
    public static GsonBuilder defaultGsonDeserializer;

    static {
        defaultGsonSerializer = new GsonBuilder()
                .excludeFieldsWithoutExposeAnnotation().serializeNulls();

        defaultGsonDeserializer = new GsonBuilder()
                .excludeFieldsWithoutExposeAnnotation().serializeNulls();
    }

    public <T> T parse(String data, Class<T> clazz) {
        JsonElement jsonObject = new com.google.gson.JsonParser().parse(data);

        if (jsonObject.isJsonArray()) {
            jsonObject = jsonObject.getAsJsonArray();
            System.out.println("@@ is a json array");
        } else {
            // Not a json array
            System.out.println("@@ not a json array");
        }

        if (clazz == JsonElement.class)
            return (T) jsonObject;

        return defaultGsonDeserializer.create().fromJson(jsonObject, clazz);
    }

}
