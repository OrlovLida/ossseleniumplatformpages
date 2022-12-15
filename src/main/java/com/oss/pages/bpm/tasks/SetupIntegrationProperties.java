package com.oss.pages.bpm.tasks;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;


public class SetupIntegrationProperties {
    private static final String INVALID_PROPERTIES_SETTING = "Invalid SetupIntegrationProperties settings \n";
    private static final String MISSING_IP_NAME = INVALID_PROPERTIES_SETTING + "Missing Integration Process Name";
    private static final String MISSING_IP_FDD = INVALID_PROPERTIES_SETTING +
            "Missing Integration Process Finished Due Date";
    private static final String MISSING_OBJECTS_TO_MOVE = INVALID_PROPERTIES_SETTING +
            "Missing \"objectIdentifiers\" parameter";


    private final String integrationProcessName;
    private final LocalDate finishedDueDate;
    private final String description;
    private final List<String> objectIdentifiers;

    SetupIntegrationProperties(SetupIntegrationPropertiesBuilder builder) {
        this.integrationProcessName = builder.integrationProcessName;
        this.finishedDueDate = builder.finishedDueDate;
        this.description = builder.description;
        this.objectIdentifiers = builder.objectIdentifiers;
    }

    public static SetupIntegrationPropertiesBuilder builder() {
        return new SetupIntegrationPropertiesBuilder();
    }

    public String getIntegrationProcessName() {
        return this.integrationProcessName;
    }

    public LocalDate getFinishedDueDate() {
        return this.finishedDueDate;
    }

    public Optional<String> getDescription() {
        return Optional.ofNullable(this.description);
    }

    public List<String> getObjectIdentifiers() {
        return this.objectIdentifiers;
    }

    public static class SetupIntegrationPropertiesBuilder {
        private String integrationProcessName;
        private LocalDate finishedDueDate;
        private String description;
        private List<String> objectIdentifiers;

        SetupIntegrationPropertiesBuilder() {
        }

        public SetupIntegrationPropertiesBuilder integrationProcessName(String integrationProcessName) {
            this.integrationProcessName = integrationProcessName;
            return this;
        }

        public SetupIntegrationPropertiesBuilder finishedDueDate(LocalDate finishedDueDate) {
            this.finishedDueDate = finishedDueDate;
            return this;
        }

        public SetupIntegrationPropertiesBuilder description(String description) {
            this.description = description;
            return this;
        }

        public SetupIntegrationPropertiesBuilder objectIdentifiers(List<String> objectIdentifiers) {
            this.objectIdentifiers = objectIdentifiers;
            return this;
        }

        public SetupIntegrationProperties build() {
            if (integrationProcessName == null) {
                throw new IllegalArgumentException(MISSING_IP_NAME);
            }
            if (finishedDueDate == null) {
                throw new IllegalArgumentException(MISSING_IP_FDD);
            }
            if (objectIdentifiers == null) {
                throw new IllegalArgumentException(MISSING_OBJECTS_TO_MOVE);
            }
            return new SetupIntegrationProperties(this);
        }

        public String toString() {
            return "SetupIntegrationProperties.SetupIntegrationPropertiesBuilder(integrationProcessName=" + this.integrationProcessName + ", finishedDueDate=" + this.finishedDueDate + ", description=" + this.description + ", objectIdentifiers=" + this.objectIdentifiers + ")";
        }
    }
}
