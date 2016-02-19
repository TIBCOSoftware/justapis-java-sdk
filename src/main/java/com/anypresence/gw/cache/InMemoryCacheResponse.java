package com.anypresence.gw.cache;

import java.net.*;
import java.io.*;
import java.util.*;

public class InMemoryCacheResponse extends CacheResponse {

    private Map<String, List<String>> headers;
    private InMemoryCacheRequest request;
    private Date expires;

    public InMemoryCacheResponse(InMemoryCacheRequest request, URLConnection uc)
            throws IOException {

        this.request = request;
    }

    private void readHeaders(ByteArrayInputStream bis) {
        try {
            ObjectInputStream ois = new ObjectInputStream(bis);
            headers = (Map<String, List<String>>) ois.readObject();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    public InputStream getBody() {
        ByteArrayInputStream in = new ByteArrayInputStream(request.getData());
        readHeaders(in);

        return in;
    }

    public Map<String, List<String>> getHeaders() throws IOException {
        return headers;
    }

    public boolean isExpired() {
        if (expires == null)
            return false;
        else {
            Date now = new Date();
            return expires.before(now);
        }
    }

}