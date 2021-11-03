package com.oss.transport.infrastructure.resource.catalog.entity;

import com.comarch.oss.resourcecatalog.api.dto.LogicalCrossconnectDTO;
import com.comarch.oss.resourcecatalog.api.dto.PhysicalCrossconnectDTO;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class DeviceModelEntity {

    private final ModelEntity modelEntity;
    private final List<String> supportedLayers;
    private final List<ChassisModelEntity> chassisModels;
    private final List<PortModelEntity> portModels;
    private final List<PhysicalCrossconnectDTO> physicalCrosses;
    private final List<LogicalCrossconnectDTO> dsrCrosses;

    private DeviceModelEntity(Builder builder) {
        modelEntity = builder.modelEntity;
        supportedLayers = builder.supportedLayers;
        chassisModels = builder.chassisModels;
        portModels = builder.portModels;
        physicalCrosses = builder.physicalCrosses;
        dsrCrosses = builder.dsrCrosses;
    }

    public static Builder builder() {
        return new Builder();
    }

    public ModelEntity getModelEntity() {
        return modelEntity;
    }

    public List<String> getSupportedLayers() {
        return supportedLayers;
    }

    public List<ChassisModelEntity> getChassisModels() {
        return chassisModels;
    }

    public List<PortModelEntity> getPortModels() {
        return portModels;
    }

    public List<PhysicalCrossconnectDTO> getPhysicalCrosses() {
        return physicalCrosses;
    }

    public List<LogicalCrossconnectDTO> getDSRCrosses() {
        return dsrCrosses;
    }

    public static final class Builder {

        private ModelEntity modelEntity;
        private List<String> supportedLayers = new ArrayList<>();
        private List<ChassisModelEntity> chassisModels = new ArrayList<>();
        private List<PortModelEntity> portModels = new ArrayList<>();
        private List<PhysicalCrossconnectDTO> physicalCrosses = Collections.emptyList();
        private List<LogicalCrossconnectDTO> dsrCrosses = Collections.emptyList();

        private Builder() {
        }

        public Builder withModelEntity(ModelEntity val) {
            modelEntity = val;
            return this;
        }

        public Builder withSupportedLayers(List<String> val) {
            supportedLayers = val;
            return this;
        }

        public Builder withChassisModelEntity(ChassisModelEntity val) {
            chassisModels.add(val);
            return this;
        }

        public Builder withPortModelEntity(PortModelEntity val) {
            portModels.add(val);
            return this;
        }

        public Builder withPhysicalCrosses(List<PhysicalCrossconnectDTO> crosses) {
            physicalCrosses = crosses;
            return this;
        }

        public Builder withDSRCrosses(List<LogicalCrossconnectDTO> crosses) {
            dsrCrosses = crosses;
            return this;
        }

        public DeviceModelEntity build() {
            return new DeviceModelEntity(this);
        }
    }
}
