package com.anypresence.gw.serializers;


import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.Map;

public class JSONSerializer {
    private Gson gson;

    private static JSONSerializer jsonSerializer;

    private JSONSerializer() {}

    public static JSONSerializer getInstance() {
        if (jsonSerializer == null) {
            jsonSerializer = new JSONSerializer();
            jsonSerializer.gson = createDefaultGsonSerializerBuilder().create();
        }

        return jsonSerializer;
    }

    public String parseToString(Map<String, Object> map) {
        return gson.toJson(map);
    }

    private static GsonBuilder createDefaultGsonSerializerBuilder() {
        return new GsonBuilder()
                .excludeFieldsWithoutExposeAnnotation()
                .serializeNulls();
    }
}
