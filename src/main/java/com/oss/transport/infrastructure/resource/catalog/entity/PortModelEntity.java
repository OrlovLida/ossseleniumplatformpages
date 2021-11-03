package com.oss.transport.infrastructure.resource.catalog.entity;

import com.comarch.oss.resourcecatalog.api.dto.PortLogicalCrossconnectDTO;
import com.comarch.oss.resourcecatalog.api.dto.PortPhysicalCrossconnectDTO;
import com.comarch.oss.resourcecatalog.api.dto.TerminationPointsDTO;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

public class PortModelEntity {

    private final ModelEntity modelEntity;
    private final String portInstanceName;
    private final Optional<ModelIdentifier> pluggableModuleSlot;
    private final TerminationPointsDTO tps;
    private final List<PortPhysicalCrossconnectDTO> physicalCrosses;
    private final List<PortLogicalCrossconnectDTO> dsrCrosses;

    public PortModelEntity(Builder builder) {
        modelEntity = builder.modelEntity;
        portInstanceName = builder.portInstanceName;
        pluggableModuleSlot = builder.pluggableModuleSlot;
        tps = builder.tp;
        physicalCrosses = builder.physicalCrosses;
        dsrCrosses = builder.dsrCrosses;
    }

    public static Builder builder() {
        return new Builder();
    }

    public boolean isSlot() {
        return pluggableModuleSlot.isPresent();
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

    public ModelEntity getModelEntity() {
        return modelEntity;
    }

    public String getPortInstanceName() {
        return portInstanceName;
    }

    public Optional<ModelIdentifier> getPluggableModuleSlot() {
        return pluggableModuleSlot;
    }

    public static class Builder {

        private ModelEntity modelEntity;
        private String portInstanceName;
        private Optional<ModelIdentifier> pluggableModuleSlot = Optional.empty();
        private TerminationPointsDTO tp;
        private List<PortPhysicalCrossconnectDTO> physicalCrosses = Collections.emptyList();
        private List<PortLogicalCrossconnectDTO> dsrCrosses = Collections.emptyList();

        public Builder modelEntity(ModelEntity value) {
            modelEntity = value;
            return this;
        }

        public Builder portInstanceName(String value) {
            portInstanceName = value;
            return this;
        }

        public Builder pluggableModuleSlot(Optional<ModelIdentifier> value) {
            pluggableModuleSlot = value;
            return this;
        }

        public Builder portTp(TerminationPointsDTO value) {
            tp = value;
            return this;
        }

        public Builder physicalCrosses(List<PortPhysicalCrossconnectDTO> crosses) {
            physicalCrosses = crosses;
            return this;
        }

        public Builder dsrCrosses(List<PortLogicalCrossconnectDTO> crosses) {
            dsrCrosses = crosses;
            return this;
        }

        public PortModelEntity build() {
            return new PortModelEntity(this);
        }
    }
}
