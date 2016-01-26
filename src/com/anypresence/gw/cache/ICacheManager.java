package com.anypresence.gw.cache;

public interface ICacheManager {
    
    public String getFromCache(String method, String urlRequest);
    
    public void putIntoCache(String method, String urlRequest, String value);
}
