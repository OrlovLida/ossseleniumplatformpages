package com.oss.nfv.common;

public enum ResourceSpecification {

    LOGICAL_FUNCTION("LogicalFunction");

    private final String inventoryType;


    ResourceSpecification(String inventoryType) {
        this.inventoryType = inventoryType;
    }

    public String getInventoryType() {
        return inventoryType;
    }
}
