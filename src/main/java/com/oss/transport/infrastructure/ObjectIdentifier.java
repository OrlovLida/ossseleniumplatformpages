package com.oss.transport.infrastructure;

public class ObjectIdentifier {

    private final String name;
    private final String type;

    public ObjectIdentifier(String name, String type) {
        this.name = name;
        this.type = type;
    }

    public String getName() {
        return name;
    }

    public String getType() {
        return type;
    }
}
