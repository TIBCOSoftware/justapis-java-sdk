package com.anypresence.gw.cache;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.net.CacheRequest;
import java.util.List;
import java.util.Map;

/**
 * Cache request
 * 
 */
public class InMemoryCacheRequest extends CacheRequest {
    ByteArrayOutputStream baos;

    public InMemoryCacheRequest(String keyedUri,
            Map<String, List<String>> rspHeaders) {
        baos = new ByteArrayOutputStream();

        ObjectOutputStream o;
        try {
            o = new ObjectOutputStream(baos);
            o.writeObject(rspHeaders);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public byte[] getData() {
        return baos.toByteArray();
    }

    @Override
    public OutputStream getBody() throws IOException {
        return baos;
    }

    @Override
    public void abort() {
        // Close the stream
        try {
            baos.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
