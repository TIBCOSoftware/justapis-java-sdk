package com.anypresence.gw;

import java.util.Collections;
import java.util.Map;

public class ResponseFromRequest {
    public final int statusCode;
    public final String data;
    public final Map<String,String> headers;
    
    public ResponseFromRequest(int statusCode, String data, Map<String,String> headers) {
        this.statusCode = statusCode;
        this.data = data;
        this.headers = headers;
    }
    
    public ResponseFromRequest(String data) {
        this(200, data, Collections.<String, String>emptyMap());
    }

    public ResponseFromRequest(int responseCode, String data) {
        this(responseCode, data, Collections.<String, String>emptyMap());
    }
   
}
