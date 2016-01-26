package com.anypresence.gw.http;

import com.anypresence.gw.RequestContext;
import com.anypresence.gw.ResponseFromRequest;
import com.anypresence.gw.exceptions.RequestException;

public interface IRestClient {

    /**
     * Reads the response
     * 
     * @return
     */
    public ResponseFromRequest readResponse();

    public void executeRequest(RequestContext<?> request) throws RequestException;
}
