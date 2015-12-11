package com.anypresence.gw;

import java.util.Date;
import java.util.Map;

import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import java.lang.reflect.Type;

public class JSONParser implements IParser {
	public static GsonBuilder defaultGsonSerializer;
	public static GsonBuilder defaultGsonDeserializer;
	
	static {
		defaultGsonSerializer = new GsonBuilder()
        .excludeFieldsWithoutExposeAnnotation()
        .serializeNulls();
		
		defaultGsonDeserializer = new GsonBuilder()
        .excludeFieldsWithoutExposeAnnotation()
        .serializeNulls();
	}
	
	public Map<String,String> parseMap(String data) {
		Type type = new TypeToken<Map<String, String>>(){}.getType();
		
		return defaultGsonDeserializer.create().fromJson(data, type);
	}


}
