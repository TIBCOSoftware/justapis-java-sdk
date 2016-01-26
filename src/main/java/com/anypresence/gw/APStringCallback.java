package com.anypresence.gw;


public abstract class APStringCallback extends APCallback<String> {

    @Override
    public RequestContext<String> createRequestContext(HTTPMethod method, String url, APGateway gateway) {
        StringRequestContext requestContext = new StringRequestContext(method, url);
        requestContext.setGateway(gateway);
        requestContext.setCallback(this);
        
        return requestContext;
    }

    public abstract void finished(String object, Throwable ex);
    
}
