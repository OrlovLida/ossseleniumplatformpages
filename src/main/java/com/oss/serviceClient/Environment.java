package com.oss.serviceClient;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.Optional;

import static com.oss.configuration.Configuration.CONFIGURATION;

public class Environment {
    
    private static final int DEFAULT_ENVIRONMENT_PORT = 25080;

    private final Optional<String> name;
    private final String environmentUrl;
    private final int environmentPort;
    private final User user;
    
    private Environment(Builder builder) {
        this.name = builder.name;
        this.environmentUrl = builder.environmentUrl;
        this.environmentPort = builder.environmentPort;
        this.user = builder.user;
    }
    
    public Optional<String> getName() {
        return name;
    }
    
    public String getEnvironmentUrl() {
        return environmentUrl;
    }
    
    public int getEnvironmentPort() {
        return environmentPort;
    }
    
    public User getUser() {
        return user;
    }
    
    public static Builder builder() {
        return new Builder();
    }
    
    public static Builder builder(Environment environment) {
        return new Builder(environment);
    }

    public static Environment createEnvironmentFromConfiguration() {
        try {
            URL url = new URL(CONFIGURATION.getUrl());
            System.out.println("!!!!!!!!!!URL: " + url);
            String host = url.getProtocol() + "://" + url.getHost();
            System.out.println("!!!!!!!!!!PROTOCOL: " + url.getProtocol());
            System.out.println("!!!!!!!!!HOST: " + host);
            int port = url.getPort();
            String userName = CONFIGURATION.getLogin();
            String pass = CONFIGURATION.getPassword();
            User user = new User(userName, pass);
            return Environment.builder()
                .withEnvironmentUrl(host)
                .withEnvironmentPort(port)
                .withUser(user)
                .build();
        } catch (MalformedURLException exception) {
            throw new IllegalStateException(exception);
        }
    }
    
    public static final class Builder {
        private Optional<String> name = Optional.empty();
        private String environmentUrl;
        private int environmentPort = DEFAULT_ENVIRONMENT_PORT;
        private User user;
        
        private Builder() {
        }
        
        private Builder(Environment environment) {
            this.name = environment.name;
            this.environmentUrl = environment.environmentUrl;
            this.environmentPort = environment.environmentPort;
            this.user = environment.user;
        }
        
        public Builder withName(String name) {
            this.name = Optional.ofNullable(name);
            return this;
        }
        
        public Builder withName(Optional<String> name) {
            this.name = name;
            return this;
        }
        
        public Builder withEnvironmentUrl(String environmentUrl) {

            this.environmentUrl = environmentUrl;
//            this.environmentUrl = CONFIGURATION.getUrl();

            return this;
        }
        
        public Builder withEnvironmentPort(int environmentPort) {
            this.environmentPort = environmentPort;
            return this;
        }
        
        public Builder withUser(User user) {
            this.user = user;
            return this;
        }
        
        public Environment build() {
            return new Environment(this);
        }
    }
    
}
