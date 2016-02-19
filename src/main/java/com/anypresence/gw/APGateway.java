package com.anypresence.gw;

import java.net.URI;
import java.net.URISyntaxException;
import java.net.Authenticator.RequestorType;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.Callable;

import org.apache.commons.lang3.NotImplementedException;

import com.anypresence.gw.exceptions.RequestException;
import com.anypresence.gw.http.DefaultRestClient;
import com.anypresence.gw.http.IRestClient;
import com.google.common.util.concurrent.ListenableFuture;

/**
 * 
 *
 */
public class APGateway {
    private ILogger logger = new BaseLogger();

    /** URL to connect to */
    private String url;

    /** HTTP method to use */
    private HTTPMethod method;

    /** Rest client to use */
    private IRestClient restClient;

    private IParser jsonParser = new JSONParser();
    
    /** The request queue */
    private static RequestQueue requestQueue;
    
    /**
     * Payload body for POST requests
     */
    private Map<String,String> postParam;
    
    private boolean useCertPinning = false;
    
    private boolean useCaching = false;
    
    public static void stopRequestQueue() {
        requestQueue.stop();
    }
    
    public static void startRequestQueue() {
        requestQueue.start();
    }

    private APGateway() {
    }

    public String getUrl() {
        return url;
    }

    protected void setUrl(String url) {
        this.url = url;
    }

    public HTTPMethod getMethod() {
        return method;
    }

    public void setMethod(HTTPMethod method) {
        this.method = method;
    }

    /**
     * Executes the request
     * @throws RequestException 
     */
    public void execute() throws RequestException {
        execute(url);
    }

    public void execute(String url) throws RequestException {
        execute(url, null);
    }

    public void execute(HTTPMethod method) throws RequestException {
        execute(this.url, method, null);
    }

    public <T> void execute(final String url, APCallback<T> callback) throws RequestException {
        execute(url, null, callback);
    }

    /**
     * @param <T>
     * @see APGateway#execute()
     * @param url
     *            relative url to connect to
     * @throws RequestException 
     */
    public <T> void execute(final String url, final HTTPMethod method,
            APCallback<T> callback) throws RequestException {
        final HTTPMethod resolvedMethod = (method == null) ? this.method
                : method;

        if (callback == null) {
            connect(url, resolvedMethod);
        } else {
            // Handle asynchronous case
            RequestContext<?> requestContext = callback.createRequestContext(resolvedMethod, url, this);
            getRequestQueue().add(requestContext);
            
            if (!getRequestQueue().isRunning) {
                getRequestQueue().start();
            }
        }
    }

    /**
     * Connect to endpoint using the registered rest client
     * 
     * @param url
     * @param method
     * @throws RequestException
     */
    private void connect(String url, HTTPMethod method) throws RequestException {
        if (getRestClient() instanceof DefaultRestClient) {
            ((DefaultRestClient)getRestClient()).useCertPinning(useCertPinning);
        }

        StringRequestContext request = new StringRequestContext(method, Utilities.updateUrl(this.url, url));
        request.setGateway(this);
        
        switch (method) {
            case PUT:
            case DELETE:
            case POST:
                request.setPostParam(postParam);
                break;
            default:
                // Nothing to do
        }
        getRestClient().executeRequest(request);
    }
    
    public APGateway useCaching(boolean shouldUseCaching) {
        this.useCaching = shouldUseCaching;
        return this;
    }
    
    public boolean getUseCaching() {
        return useCaching;
    }

    /**
     * @see APGateway#post(String)
     */
    public void post() {
        try {
            execute(this.url, HTTPMethod.POST, null);
        } catch (RequestException e) {           
            e.printStackTrace();
        }
    }

    /**
     * Sends post request
     * 
     * @param url
     *            relative url to connect to
     */
    public void post(String url) {
        try {
            execute(url, HTTPMethod.POST, null);
        } catch (RequestException e) {
            e.printStackTrace();
        }
    }

    /**
     * @see APGateway#get(String)
     */
    public void get() {
        try {
            execute(HTTPMethod.GET);
        } catch (RequestException e) {
            e.printStackTrace();
        }
    }

    /**
     * Sends a get request
     * 
     * @param url
     *            relative url to connect to
     */
    public void get(String url) {
        try {
            execute(url, HTTPMethod.GET, null);
        } catch (RequestException e) {
            e.printStackTrace();
        }
    }
    
    public <T> void get(APCallback<T> callback) {
        get(url, callback);
    }

    public <T> void get(String url, APCallback<T> callback) {
        try {
            execute(url, HTTPMethod.GET, callback);
        } catch (RequestException e) {
            e.printStackTrace();
        }
    }

    public <T extends APObject> void readResponseObject(T obj) {
        ResponseFromRequest response = getRestClient().readResponse();

        HashMap<String, String> data = getJsonParser().parse(response.data,
                HashMap.class);

        if (data != null) {
            for (Entry<String, String> entry : data.entrySet()) {
                obj.set(entry.getKey(), entry.getValue().toString());
            }
        }

    }
    
    public ResponseFromRequest readResponse() {
        return getRestClient().readResponse();
    }


    public IRestClient getRestClient() {
        if (restClient == null) {
            restClient = new DefaultRestClient();
        }

        return restClient;
    }

    public void setRestClient(IRestClient restClient) {
        this.restClient = restClient;
    }

    public Map<String,String> getPostParam() {
        return postParam;
    }

    public void setPostParam(Map<String,String> postParam) {
        this.postParam = postParam;
    }

    /**
     * Sets the relative url
     * 
     * @param url
     */
    public void setRelativeUrl(String url) {
        String updatedUrl = this.url + "/" + url;
        URI uri;
        try {
            uri = new URI(updatedUrl);
            uri = uri.normalize();

            this.url = uri.toString();
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }
    }

    public IParser getJsonParser() {
        return jsonParser;
    }

    public void setJsonParser(IParser jsonParser) {
        this.jsonParser = jsonParser;
    }

    public ILogger getLogger() {
        return logger;
    }

    public void setLogger(ILogger logger) {
        this.logger = logger;
    }

    public boolean isUseCertPinning() {
        return useCertPinning;
    }

    public void setUseCertPinning(boolean useCertPinning) {
        this.useCertPinning = useCertPinning;
    }

    public RequestQueue getRequestQueue() {
        if (requestQueue == null) {
            requestQueue = new RequestQueue();
        }
        return requestQueue;
    }

    public void setRequestQueue(RequestQueue requestQueue) {
        this.requestQueue = requestQueue;
    }

    public static CertPinningManager getCertPinningManager() { 
        return CertPinningManager.getInstance();
    }

    /**
     * Builder for APGateway
     * 
     */
    public static class Builder {
        String url;
        HTTPMethod method;
        boolean useCertPinning = false;

        public Builder() {
        }

        public Builder url(String url) {
            this.url = url;
            return this;
        }

        public Builder method(HTTPMethod method) {
            this.method = method;
            return this;
        }
        
        public Builder useCertPinning(boolean useCertPinning) {
            this.useCertPinning = useCertPinning;
            return this;
        }

        public APGateway build() {
            APGateway gw = new APGateway();
            gw.setUrl(url);
            gw.setMethod(method);
            gw.setUseCertPinning(useCertPinning);

            return gw;
        }
    }

}
