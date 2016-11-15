package com.beanstalkdata.android;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

public final class TestConfig {

    public static final String APP_KEY;
    public static final String GOOGLE_MAPS_KEY;

    static {
        Properties apiProperties = getApiProperties(BuildConfig.LIBRARY_MODULE_DIR);
        APP_KEY = apiProperties.getProperty("appKey");
        GOOGLE_MAPS_KEY = apiProperties.getProperty("googleMapsKey");
    }

    private TestConfig() {

    }

    private static Properties getApiProperties(String libraryModuleDir) {
        Properties getProperties = new Properties();
        try {
            getProperties.load(new FileInputStream(new File(libraryModuleDir, "api.properties")));
        } catch (IOException e) {
            e.printStackTrace();
        }
        return getProperties;
    }

}
