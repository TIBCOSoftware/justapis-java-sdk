package com.anypresence.gw;

public interface ILogger {

    public void log(String message);

    public void log(String message, Exception ex);

}
