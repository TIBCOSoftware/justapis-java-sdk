package com.anypresence.gw;

import java.util.concurrent.PriorityBlockingQueue;

import com.anypresence.gw.exceptions.RequestException;

/**
 * This class represents a request queue that is used for asynchronous requests.
 *
 */
public class RequestQueue {
    private PriorityBlockingQueue<RequestContext<?>> queue = new PriorityBlockingQueue<RequestContext<?>>();
    
    private Thread thread;
    private volatile boolean shouldQuit = false;
    public volatile boolean isRunning = false;
    
    public void start() {
        stop();
        resume();       
    }
    
    public void resume() {
        // Pull requests from queue and run them
        isRunning = true;
        
        // TODO: Create multiple threads for this?
        Runnable runnable = new Runnable() {

            public void run() {
                while (!shouldQuit) {
                    RequestContext<?> requestContext;
                    try {
                        requestContext = queue.take();
                    } catch (InterruptedException e) {
                        continue;
                    }
                    
                    if (requestContext != null) {                        
                        APGateway gateway = requestContext.getGateway();
                        if (gateway == null) {
                            continue;
                        }
                        Exception exceptionOccurred = null;
                        try {
                            gateway.execute();
                        } catch (RequestException e) {
                            exceptionOccurred = e;
                        }
                        
                        // Get the response
                        ResponseFromRequest responseFromRequest = gateway.readResponse();
                        
                        if (responseFromRequest == null) {
                            continue;
                        } else {                            
                            // Now parse the raw response
                            TransformedResponse<?> transformedResponse = requestContext.parseResponse(responseFromRequest, exceptionOccurred);
                        }
                    }
                }              
                
            }

        };

        thread = new Thread(runnable);
        thread.start();
        
        
    }
    
    public void pause() {
        stop();
    }
    
    public void stop() {
        if (thread != null) {
            shouldQuit = true;
        }
        isRunning = false;
    }
    
    /**
     * Adds a request context to the queue.
     * 
     * @param request
     * @return
     */
    public synchronized <T> RequestContext<T> add(RequestContext<T> request) {
        queue.add(request);
                
        return request;
    }
}
