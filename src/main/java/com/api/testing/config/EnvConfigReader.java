package com.api.testing.config;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

public class EnvConfigReader {
    private static Properties properties = new Properties();

    static {
        try {
            FileInputStream fis = new FileInputStream("config/env.properties");
            properties.load(fis);
        } catch (IOException e) {
            throw new RuntimeException("Failed to load env.properties", e);
        }
    }

    public static String get(String key) {
        return properties.getProperty(key);
    }

   
}
