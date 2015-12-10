package com.anypresence.gw;

import java.util.HashMap;
import java.util.Map;

/**
 * 
 *
 */
public class APObject {
	
	private Map<String, Object> data = new HashMap<String, Object>();
	
	public APObject() {}
	
	public void set(String key, Object value) {
		data.put(key, value);
	}
	
	public void setMap(Map<String, Object> data) {
		this.data.putAll(data);
	}
	
	public Object get(String key) {
		return data.get(key);
	}
}
