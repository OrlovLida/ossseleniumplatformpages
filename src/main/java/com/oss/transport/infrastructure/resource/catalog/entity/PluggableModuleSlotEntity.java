package com.oss.transport.infrastructure.resource.catalog.entity;

import java.util.Collection;

public class PluggableModuleSlotEntity {

    private final ModelEntity modelEntity;
    private final Collection<ModelIdentifier> compatiblePluggableModels;

    public PluggableModuleSlotEntity(ModelEntity modelEntity, Collection<ModelIdentifier> compatiblePluggableModels) {
        this.modelEntity = modelEntity;
        this.compatiblePluggableModels = compatiblePluggableModels;
    }

    public ModelEntity getModelEntity() {
        return modelEntity;
    }

    public Collection<ModelIdentifier> getCompatiblePluggableModels() {
        return compatiblePluggableModels;
    }
}
