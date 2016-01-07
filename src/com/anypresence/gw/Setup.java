package com.anypresence.gw;

public class Setup {
    private static ILogger logger = new BaseLogger();

    public static ILogger getLogger() {
        return logger;
    }

    public static void setLogger(ILogger newLogger) {
        logger = newLogger;
    }

}
