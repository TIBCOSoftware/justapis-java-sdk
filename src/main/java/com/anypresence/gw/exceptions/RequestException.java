package com.anypresence.gw.exceptions;

public class RequestException extends Exception {

    private static final long serialVersionUID = 1L;

    private final int statusCode;

    public RequestException(String message) {
        super(message);
        this.statusCode = -1;
    }

    public RequestException(String message, int statusCode) {
        super(message);
        this.statusCode = statusCode;
    }

    public RequestException(String message, int statusCode, Throwable cause) {
        super(message);
        super.initCause(cause);
        this.statusCode = statusCode;
    }

    public RequestException(String message, Throwable cause) {
        super(message);
        super.initCause(cause);
        this.statusCode = -1;
    }

    public RequestException(Throwable cause) {
        super.initCause(cause);
        this.statusCode = -1;
    }

    public int getStatusCode() {
        return statusCode;
    }
}
