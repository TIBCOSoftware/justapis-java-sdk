package com.anypresence.gw.cache;

public interface ICacheManager {

    void putIntoCache(String requestMethod, String url, String result);
    
    String getFromCache(String requestMethod, String url);

}
