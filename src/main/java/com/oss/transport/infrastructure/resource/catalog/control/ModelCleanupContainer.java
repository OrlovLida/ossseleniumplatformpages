package com.oss.transport.infrastructure.resource.catalog.control;

import java.util.ArrayList;
import java.util.Collection;

public class ModelCleanupContainer {

    private final Collection<String> deviceModels;
    private final Collection<String> cardModels;
    private final Collection<String> pluggableModuleSlotModels;
    private final Collection<String> pluggableModuleModels;

    private ModelCleanupContainer(Builder builder) {
        deviceModels = builder.deviceModels;
        cardModels = builder.cardModels;
        pluggableModuleSlotModels = builder.pluggableModuleSlotModels;
        pluggableModuleModels = builder.pluggableModuleModels;
    }

    public static Builder builder() {
        return new Builder();
    }

    public Collection<String> getDeviceModels() {
        return deviceModels;
    }

    public Collection<String> getCardModels() {
        return cardModels;
    }

    public Collection<String> getPluggableModuleSlotModels() {
        return pluggableModuleSlotModels;
    }

    public Collection<String> getPluggableModuleModels() {
        return pluggableModuleModels;
    }

    public static final class Builder {

        private Collection<String> deviceModels = new ArrayList<>();
        private Collection<String> cardModels = new ArrayList<>();
        private Collection<String> pluggableModuleSlotModels = new ArrayList<>();
        private Collection<String> pluggableModuleModels = new ArrayList<>();

        private Builder() {
        }

        public Builder addDeviceModel(String deviceModel) {
            this.deviceModels.add(deviceModel);
            return this;
        }

        public Builder addCardModel(String cardModel) {
            this.cardModels.add(cardModel);
            return this;
        }

        public Builder addPluggableModuleSlotModel(String pluggableModuleSlotModel) {
            this.pluggableModuleSlotModels.add(pluggableModuleSlotModel);
            return this;
        }

        public Builder addPluggableModuleModel(String pluggableModuleModel) {
            this.pluggableModuleModels.add(pluggableModuleModel);
            return this;
        }

        public ModelCleanupContainer build() {
            return new ModelCleanupContainer(this);
        }
    }
}
