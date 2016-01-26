package com.anypresence.gw;


/**
 * 
 *
 */
public class StringRequestContext extends RequestContext<String> {
    private static final long serialVersionUID = 1L;
    
    /** callback to handle the response */
    private IAPFutureCallback<String> callback;
    
    public StringRequestContext(
            HTTPMethod requestMethod,
            String url) {
        super(requestMethod, url);
    }
    
    public StringRequestContext(
            HTTPMethod requestMethod,
            String url, IAPFutureCallback<String> callback) {
        super(requestMethod, url);
        
        this.setCallback(callback);
    }

    /**
     * 
     */
    @Override
    protected TransformedResponse<String> parseResponse(ResponseFromRequest responseFromRequest, Exception e) {
        if (getCallback() != null) {
            getCallback().finished(responseFromRequest.data, e);
        }
        return new TransformedResponse<String>(responseFromRequest.data);
    }

    public IAPFutureCallback<String> getCallback() {
        return callback;
    }

    public void setCallback(IAPFutureCallback<String> callback2) {
        this.callback = callback2;
    }

}
