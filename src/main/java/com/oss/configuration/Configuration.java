package com.oss.configuration;

import org.assertj.core.util.Strings;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class Configuration {


    public static final Configuration CONFIGURATION = new Configuration();
    private final Properties properties = new Properties();

    public Configuration() {
        InputStream inputStream = Configuration.class.getClassLoader().getResourceAsStream("config.properties");
        try {
            properties.load(inputStream);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public String getValue(String key) {
        return properties.getProperty(key);
    }

    public String getUrl() {
        if (System.getProperty("URL") == null) {
            return CONFIGURATION.getValue("baseUrl");
        }
        return System.getProperty("URL");
    }

    public String getDriver() {
        if (System.getProperty("driver") == null) {
            return CONFIGURATION.getValue("driver");
        }
        return System.getProperty("driver");
    }

    public String getApplicationIp() {
        String env = System.getProperty("env");
        if (Strings.isNullOrEmpty(env)) {
            String ip = getUrl().split(":")[1].replace("//", "");
            return ip;
        }
        System.out.println("ENV: " + env);
        return env;
    }

    public String getApplicationPort() {
        String port = getUrl().split(":")[2];
        return port;
    }

}