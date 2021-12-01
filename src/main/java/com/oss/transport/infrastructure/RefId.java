package com.oss.transport.infrastructure;

public class RefId {

    private final Long id;
    private final String type;

    public RefId(Long id, String type) {
        this.id = id;
        this.type = type;
    }

    public Long getId() {
        return id;
    }

    public String getType() {
        return type;
    }
}
