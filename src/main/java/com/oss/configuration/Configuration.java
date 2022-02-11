package com.oss.configuration;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Paths;
import java.util.Properties;

import org.assertj.core.util.Strings;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Configuration {
    
    private static final Logger log = LoggerFactory.getLogger(Configuration.class);
    
    public static final Configuration CONFIGURATION = new Configuration();
    private static final String DEFAULT_DOWNLOAD_DIR =
            Paths.get(System.getProperty("user.dir") + File.separator + "target" + File.separator + "downloadFiles").toString();
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
    
    public String getLogin() {
        if (System.getProperty("LOGIN") == null) {
            return CONFIGURATION.getValue("user");
        }
        return System.getProperty("LOGIN");
    }
    
    public String getPassword() {
        if (System.getProperty("PASSWORD") == null) {
            return CONFIGURATION.getValue("password");
        }
        return System.getProperty("PASSWORD");
    }
    
    public String getDriver() {
        if (System.getProperty("DRIVER") == null) {
            return CONFIGURATION.getValue("driver");
        }
        return System.getProperty("DRIVER");
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
            return getUrl().split(":")[1].replace("//", "");
        }
        log.info("ENV: {}", env);
        return env;
    }
    
    public String getCheckErrors() {
        if (System.getProperty("CHECK_ERRORS") == null) {
            return CONFIGURATION.getValue("checkErrors");
        }
        return System.getProperty("CHECK_ERRORS");
    }
    
    public String getApplicationPort() {
        return CONFIGURATION.getValue("port");
    }
    
}
