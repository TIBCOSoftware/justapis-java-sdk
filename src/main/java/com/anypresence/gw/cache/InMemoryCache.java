package com.anypresence.gw.cache;

import java.net.*;
import java.io.*;
import java.net.ResponseCache;
import java.util.*;
import java.util.concurrent.*;

public class InMemoryCache extends ResponseCache {

    private static Map<URI, InMemoryCacheResponse> responses = new ConcurrentHashMap<URI, InMemoryCacheResponse>();

    private int maxEntries = 100;

    public InMemoryCache() {
        this(100);
    }

    public InMemoryCache(int maxEntries) {
        this.maxEntries = maxEntries;
    }

    public CacheRequest put(URI uri, URLConnection connection)
            throws IOException {
        if (responses.size() >= maxEntries)
            return null;

        String cacheControl = connection.getHeaderField("Cache-Control");

        if (cacheControl != null && cacheControl.indexOf("no-cache") >= 0) {
            return null;
        }

        InMemoryCacheRequest request = new InMemoryCacheRequest(uri.toString(),
                connection.getHeaderFields());

        InMemoryCacheResponse response = new InMemoryCacheResponse(request,
                connection);

        responses.put(uri, response);

        return request;
    }

    public CacheResponse get(URI uri, String requestMethod,
            Map<String, List<String>> requestHeaders) throws IOException {

        InMemoryCacheResponse response = responses.get(uri);
        
        if (response != null) {
            if (response.isExpired()) { // check expiration date
                System.out.println("expired?");
                responses.remove(response);
                response = null;
            }
        }

        return response;
    }

}