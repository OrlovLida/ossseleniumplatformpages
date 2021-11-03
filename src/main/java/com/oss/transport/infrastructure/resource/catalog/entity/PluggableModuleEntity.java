package com.oss.transport.infrastructure.resource.catalog.entity;

import com.comarch.oss.resourcecatalog.api.dto.PortLogicalCrossconnectDTO;
import com.comarch.oss.resourcecatalog.api.dto.PortPhysicalCrossconnectDTO;
import com.comarch.oss.resourcecatalog.api.dto.TerminationPointsDTO;

import java.util.Collections;
import java.util.List;

public class PluggableModuleEntity {

    private final ModelEntity modelEntity;
    private final TerminationPointsDTO tps;
    private final List<PortPhysicalCrossconnectDTO> physicalCrosses;
    private final List<PortLogicalCrossconnectDTO> dsrCrosses;

    private PluggableModuleEntity(Builder builder) {
        this.modelEntity = builder.modelEntity;
        this.tps = builder.tps;
        this.physicalCrosses = builder.physicalCrosses;
        this.dsrCrosses = builder.dsrCrosses;
    }

    public static Builder builder() {
        return new Builder();
    }

    public ModelEntity getModelEntity() {
        return modelEntity;
    }

    public TerminationPointsDTO getTps() {
        return tps;
    }

    public List<PortPhysicalCrossconnectDTO> getPhysicalCrosses() {
        return physicalCrosses;
    }

    public List<PortLogicalCrossconnectDTO> getDSRCrosses() {
        return dsrCrosses;
    }

    public static class Builder {

        private ModelEntity modelEntity;
        private TerminationPointsDTO tps;
        private List<PortPhysicalCrossconnectDTO> physicalCrosses = Collections.emptyList();
        private List<PortLogicalCrossconnectDTO> dsrCrosses = Collections.emptyList();

        private Builder() {
        }

        public Builder modelEntity(ModelEntity modelEntity) {
            this.modelEntity = modelEntity;
            return this;
        }

        public Builder tps(TerminationPointsDTO tps) {
            this.tps = tps;
            return this;
        }

        public Builder physicalCrosses(List<PortPhysicalCrossconnectDTO> physicalCrosses) {
            this.physicalCrosses = physicalCrosses;
            return this;
        }

        public Builder dsrCrosses(List<PortLogicalCrossconnectDTO> dsrCrosses) {
            this.dsrCrosses = dsrCrosses;
            return this;
        }

        public PluggableModuleEntity build() {
            return new PluggableModuleEntity(this);
        }
    }
}
