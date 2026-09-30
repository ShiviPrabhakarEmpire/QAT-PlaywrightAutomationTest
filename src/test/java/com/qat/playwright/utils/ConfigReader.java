package com.qat.playwright.utils;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

public class ConfigReader {
    private static Properties properties;

    static {
        try {
            properties = new Properties();
            // Load from .env if present
            try (FileInputStream fileInputStream = new FileInputStream(".env")) {
                properties.load(fileInputStream);
            }
        } catch (IOException e) {
            System.out.println("No .env file found or failed to load. Falling back to system properties.");
        }
    }

    public static String getProperty(String key) {
        // System properties override .env properties
        String sysProp = System.getProperty(key);
        if (sysProp != null && !sysProp.isEmpty()) {
            return sysProp;
        }
        return properties.getProperty(key);
    }
    
    public static String getProperty(String key, String defaultValue) {
        String val = getProperty(key);
        return (val != null && !val.isEmpty()) ? val : defaultValue;
    }
}
