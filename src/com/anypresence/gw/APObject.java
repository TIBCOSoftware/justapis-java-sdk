package com.anypresence.gw;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

/**
 * This class represents data that is used with APGateway. When data is
 * serialized, it can be deserialized into this object.
 * 
 */
public class APObject implements Serializable {
	private static final long serialVersionUID = 1L;

	private Map<String, Object> data = new HashMap<String, Object>();

	public APObject() {
	}

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
