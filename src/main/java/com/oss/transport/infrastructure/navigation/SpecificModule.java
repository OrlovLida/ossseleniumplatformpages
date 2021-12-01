package com.oss.transport.infrastructure.navigation;

public enum SpecificModule {
    TP_SERVICE("tp-service", "/service/navigation");

    private String name;
    private String uri;

    SpecificModule(String name, String uri) {
        this.name = name;
        this.uri = uri;
    }

    SpecificModule(String name) {
        this.name = name;
        this.uri = "";
    }

    public String getName() {
        return name;
    }

    public String getUri() {
        return uri;
    }
}
