package com.oss.pages.stockmanagement.assets;

import lombok.Builder;

import java.util.Arrays;
import java.util.Map;
import java.util.Optional;

@Builder
public class Asset {
    private final String name;
    private final String description;
    private final State state;
    private final String serialNumber;
    private final Map<String, String> materialKeys;
    private final String materialKeyValues;
    private final String objectIdentifier;
    private final String objectModelName;
    private final String storageName;

    public Optional<String> getName() {
        return Optional.ofNullable(this.name);
    }

    public Optional<String> getDescription() {
        return Optional.ofNullable(this.description);
    }

    public Optional<State> getState() {
        return Optional.ofNullable(this.state);
    }

    public Optional<String> getSerialNumber() {
        return Optional.ofNullable(this.serialNumber);
    }

    public Optional<Map<String, String>> getMaterialKeys() {
        return Optional.ofNullable(this.materialKeys);
    }

    public Optional<String> getMaterialKeyValues() {
        return Optional.ofNullable(this.materialKeyValues);
    }

    public Optional<String> getObjectIdentifier() {
        return Optional.ofNullable(this.objectIdentifier);
    }

    public Optional<String> getObjectModelName() {
        return Optional.ofNullable(this.objectModelName);
    }

    public Optional<String> getStorageName() {
        return Optional.ofNullable(this.storageName);
    }


    public enum State {
        NEW("New"),
        ON_STOCK("On Stock"),
        RESERVED("Reserved"),
        INSTALLED("Installed"),
        DECOMMISSIONED("Decommissioned"),
        DEFECTIVE("Defective"),
        END_OF_LIFE("End of life");

        private static final String INVALID_STATE_LABEL = "Invalid label. Cannot find any state with label %s.";
        private final String label;

        State(String label) {
            this.label = label;
        }

        public static State getStateByLabel(String label) {
            return Arrays.stream(State.values()).filter(state -> state.getLabel().equals(label)).findFirst().orElseThrow(() ->
                    new IllegalArgumentException(String.format(INVALID_STATE_LABEL, label)));
        }

        public String getLabel() {
            return label;
        }
    }

}
