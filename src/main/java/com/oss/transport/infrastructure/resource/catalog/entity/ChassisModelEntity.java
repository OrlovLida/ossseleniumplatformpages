package com.oss.transport.infrastructure.resource.catalog.entity;

import java.util.Collection;

public class ChassisModelEntity {

    private final String name;
    private final ModelEntity modelEntity;
    private final Collection<SlotEntity> slots;

    private ChassisModelEntity(Builder builder) {
        name = builder.name;
        modelEntity = builder.modelEntity;
        slots = builder.slots;
    }

    public static Builder builder() {
        return new Builder();
    }

    public String getName() {
        return name;
    }

    public ModelEntity getModelEntity() {
        return modelEntity;
    }

    public Collection<SlotEntity> getSlots() {
        return slots;
    }

    public static final class Builder {

        private String name;
        private ModelEntity modelEntity;
        private Collection<SlotEntity> slots;

        private Builder() {
        }

        public Builder withName(String val) {
            name = val;
            return this;
        }

        public Builder withModelEntity(ModelEntity val) {
            modelEntity = val;
            return this;
        }

        public Builder withSlots(Collection<SlotEntity> val) {
            slots = val;
            return this;
        }

        public ChassisModelEntity build() {
            return new ChassisModelEntity(this);
        }
    }
}
