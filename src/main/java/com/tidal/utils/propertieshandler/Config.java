package com.tidal.utils.propertieshandler;


import java.util.function.UnaryOperator;

import static com.tidal.utils.utils.CheckString.isNullOrEmpty;


public class Config {

    private static final String[] defaultArrayValue = new String[]{};
    private static final UnaryOperator<String> CONFIGURATION = PropertiesFinder::getProperty;

    public static final String DRIVER_EXECUTABLE_FOLDER = CONFIGURATION.apply("driver.executable.folder") != null ? CONFIGURATION.apply("driver.executable.folder") : "";
    public static final String RUNNER = CONFIGURATION.apply("runner") != null ? CONFIGURATION.apply("runner") : "TestNgRunner";
    public static final boolean RETRY_FAILED_TESTS = CONFIGURATION.apply("retry.failed.tests") != null && Boolean.parseBoolean(CONFIGURATION.apply("retry.failed.tests"));

    private Config() {
    }

    static {
        if(!isNullOrEmpty(Config.DRIVER_EXECUTABLE_FOLDER)) System.setProperty("wdm.cachePath", Config.DRIVER_EXECUTABLE_FOLDER);
    }

}
