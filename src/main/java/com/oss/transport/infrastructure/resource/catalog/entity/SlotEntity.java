package com.oss.transport.infrastructure.resource.catalog.entity;

public class SlotEntity {

    private final ModelEntity modelEntity;
    private final String portInstanceName;

    public SlotEntity(ModelEntity modelEntity, String portInstanceName) {
        this.modelEntity = modelEntity;
        this.portInstanceName = portInstanceName;
    }

    public ModelEntity getModelEntity() {
        return modelEntity;
    }

    public String getPortInstanceName() {
        return portInstanceName;
    }
}
