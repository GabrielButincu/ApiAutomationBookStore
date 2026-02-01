package com.bookstore.api.config;

import org.aeonbits.owner.ConfigFactory;

public class ConfigurationManager {
    
    private static ApiConfig config;
    
    private ConfigurationManager() {
    }
    
    public static ApiConfig getConfig() {
        if (config == null) {
            synchronized (ConfigurationManager.class) {
                if (config == null) {
                    config = ConfigFactory.create(ApiConfig.class);
                }
            }
        }
        return config;
    }
    
    public static void reload() {
        config = null;
        getConfig();
    }
}
