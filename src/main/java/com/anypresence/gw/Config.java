package com.anypresence.gw;

import java.net.ResponseCache;


public class Config {
    private static ILogger logger = new BaseLogger();
    private static ResponseCache cacheManager;

    public static ILogger getLogger() {
        return logger;
    }

    public static void setLogger(ILogger newLogger) {
        logger = newLogger;
    }
    
    public static void setCacheManager(ResponseCache cacheManager) {
        Config.cacheManager = cacheManager;
        
        ResponseCache.setDefault(Config.getCacheManager());
    }
    
    public static ResponseCache getCacheManager() {
        return cacheManager;
    }

}
