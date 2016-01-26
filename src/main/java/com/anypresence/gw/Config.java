package com.anypresence.gw;

import com.anypresence.gw.cache.ICacheManager;

public class Config {
    private static ILogger logger = new BaseLogger();
    private static ICacheManager cacheManager;

    public static ILogger getLogger() {
        return logger;
    }

    public static void setLogger(ILogger newLogger) {
        logger = newLogger;
    }
    
    public static void setCacheManager(ICacheManager cacheManager) {
        cacheManager = cacheManager;
    }
    
    public static ICacheManager getCacheManager() {
        return cacheManager;
    }

}
