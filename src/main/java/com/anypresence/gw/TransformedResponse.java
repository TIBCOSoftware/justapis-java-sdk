package com.anypresence.gw;

/**
 * 
 *
 * @param <T>
 */
public class TransformedResponse<T> {
    public final T result;
    
    public TransformedResponse(T result) {
        this.result = result;
    }
}
