package com.oss.transport.infrastructure;

import java.util.Optional;

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
            this.environmentUrl = "http://" + environmentUrl;
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
