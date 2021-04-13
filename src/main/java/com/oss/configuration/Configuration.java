package com.oss.configuration;

import org.assertj.core.util.Strings;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Paths;
import java.util.Properties;

public class Configuration {


    public static final Configuration CONFIGURATION = new Configuration();
    private static final String DEFAULT_DOWNLOAD_DIR = Paths.get(System.getProperty("user.dir") + File.separator + "target" + File.separator + "downloadFiles").toString();
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

    public String getDownloadDir() {
        if (CONFIGURATION.getValue("downloadDir") == null) {
            return DEFAULT_DOWNLOAD_DIR;
        }
        return CONFIGURATION.getValue("downloadDir");
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
        return CONFIGURATION.getValue("port");
    }

}