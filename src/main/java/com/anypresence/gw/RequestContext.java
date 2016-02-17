package com.anypresence.gw;

import java.io.Serializable;
import java.util.Map;
import java.util.concurrent.PriorityBlockingQueue;

/**
 * Base class for a request.
 * 
 * Instances of this class will also be used for queueing requests. Instances of this class
 * should be created from some type of APCallback.
 * 
 *  @param <T> The type of the parsed response the request wants.
 */
public abstract class RequestContext<T> implements Comparable<RequestContext<T>>, Serializable {
    private static final long serialVersionUID = 1L;
    
    private String url;
    private Map<String,String> headers;
    private boolean shouldPinCert;
    private HTTPMethod method;
    private Map<String,String> postParam;
    
    private APGateway gateway;
    
    public RequestContext(HTTPMethod requestMethod, String url) {
        setMethod(requestMethod);
        setUrl(url);
    }
    
    public RequestContext(APGateway gateway) {
        this.setGateway(gateway);
    }
    
    public String getUrl() {
        return url;
    }
    
    public void setUrl(String url) {
        this.url = url;
    }

    public Map<String,String> getHeaders() {
        return headers;
    }

    public void setHeaders(Map<String,String> headers) {
        this.headers = headers;
    }

    public boolean isShouldPinCert() {
        return shouldPinCert;
    }

    public void setShouldPinCert(boolean shouldPinCert) {
        this.shouldPinCert = shouldPinCert;      
    }

    public HTTPMethod getMethod() {
        return method;
    }

    public void setMethod(HTTPMethod method) {
        this.method = method;
    }

    public Map<String,String> getPostParam() {
        return postParam;
    }

    public void setPostParam(Map<String,String> postParam) {
        this.postParam = postParam;
    }

    public APGateway getGateway() {
        return gateway;
    }

    public void setGateway(APGateway gateway) {
        this.gateway = gateway;
    }

    /**
     * Parses the response and transforms it.
     * 
     * @param responseFromRequest
     * @param e
     * @return
     */
    abstract protected TransformedResponse<T> parseResponse(ResponseFromRequest responseFromRequest, Exception e);
    
    public int compareTo(RequestContext<T> other) {
        return 0;
    }
    
    
    
}
