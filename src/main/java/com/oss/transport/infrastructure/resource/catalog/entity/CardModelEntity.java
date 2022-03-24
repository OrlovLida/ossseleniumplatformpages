package com.oss.transport.infrastructure.resource.catalog.entity;

import com.comarch.oss.resourcecatalog.api.dto.LogicalCrossconnectDTO;
import com.comarch.oss.resourcecatalog.api.dto.PhysicalCrossconnectDTO;

import java.util.Collection;
import java.util.Collections;
import java.util.List;

public class CardModelEntity {

    private final ModelEntity modelEntity;
    private final Collection<PortModelEntity> portModels;
    private final Collection<SlotEntity> slotModels;
    private final List<PhysicalCrossconnectDTO> physicalCrosses;
    private final List<LogicalCrossconnectDTO> dsrCrosses;

    private CardModelEntity(Builder builder) {
        this.modelEntity = builder.modelEntity;
        this.portModels = builder.portModels;
        this.slotModels = builder.slotModels;
        this.physicalCrosses = builder.physicalCrosses;
        this.dsrCrosses = builder.dsrCrosses;
    }

    public static Builder builder() {
        return new Builder();
    }

    public ModelEntity getModelEntity() {
        return modelEntity;
    }

    public Collection<PortModelEntity> getPortModels() {
        return portModels;
    }

    public Collection<SlotEntity> getSlotModels() {
        return slotModels;
    }

    public List<PhysicalCrossconnectDTO> getPhysicalCrosses() {
        return physicalCrosses;
    }

    public List<LogicalCrossconnectDTO> getDSRCrosses() {
        return dsrCrosses;
    }

    public static class Builder {

        private ModelEntity modelEntity;
        private Collection<PortModelEntity> portModels = Collections.emptyList();
        private Collection<SlotEntity> slotModels = Collections.emptyList();
        private List<PhysicalCrossconnectDTO> physicalCrosses = Collections.emptyList();
        private List<LogicalCrossconnectDTO> dsrCrosses = Collections.emptyList();

        private Builder() {
        }

        public Builder modelEntity(ModelEntity modelEntity) {
            this.modelEntity = modelEntity;
            return this;
        }

        public Builder portModels(Collection<PortModelEntity> portModels) {
            this.portModels = portModels;
            return this;
        }

        public Builder slotModels(Collection<SlotEntity> slotModels) {
            this.slotModels = slotModels;
            return this;
        }

        public Builder physicalCrosses(List<PhysicalCrossconnectDTO> physicalCrosses) {
            this.physicalCrosses = physicalCrosses;
            return this;
        }

        public Builder dsrCrosses(List<LogicalCrossconnectDTO> dsrCrosses) {
            this.dsrCrosses = dsrCrosses;
            return this;
        }

        public CardModelEntity build() {
            return new CardModelEntity(this);
        }
    }
}
