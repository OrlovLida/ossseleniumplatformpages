package com.oss.planning.validationresults;

import com.google.common.base.Preconditions;

import java.util.Optional;
import java.util.UUID;


public class ValidationResult {
    private final UUID validationResultId;
    private final String description;
    private final String type;
    private final String owner;
    private final Severity severity;
    private final ValidationStatus validationStatus;
    private final boolean suppressEnabled;
    private final SuppressionDetails suppressionDetails;

    ValidationResult(ValidationResultBuilder builder) {
        this.validationResultId = builder.validationResultId;
        this.description = builder.description;
        this.type = builder.type;
        this.owner = builder.owner;
        this.severity = builder.severity;
        this.suppressEnabled = builder.suppressEnabled;
        this.validationStatus = builder.validationStatus;
        this.suppressionDetails = builder.suppressionDetails;
    }

    public static ValidationResultBuilder builder() {
        return new ValidationResultBuilder();
    }

    public Optional<UUID> getValidationResultId() {
        return Optional.ofNullable(this.validationResultId);
    }

    public String getDescription() {
        return this.description;
    }

    public String getType() {
        return this.type;
    }

    public boolean getSuppressEnabled() {
        return this.suppressEnabled;
    }

    public Optional<String> getOwner() {
        return Optional.ofNullable(this.owner);
    }

    public Severity getSeverity() {
        return this.severity;
    }

    public ValidationStatus getValidationStatus() {
        return this.validationStatus;
    }

    public Optional<SuppressionDetails> getSuppressionDetails() {
        return Optional.ofNullable(this.suppressionDetails);
    }


    public enum Severity {
        CRITICAL,
        HIGH,
        MEDIUM,
        LOW,
        INTERNAL,
    }

    public enum ValidationStatus {
        VALID,
        INVALID,
        IN_PROGRESS
    }


    public static class ValidationResultBuilder {
        private static final String NULL_ARGUMENT_EXCEPTION = "Argument '%s' cannot be empty.";
        private static final String SUPPRESS_ENABLED_ARGUMENT_EXCEPTION = "Argument 'suppressEnabled' has wrong value. " +
                "For 'CRITICAL' severity it must be 'false' and for 'MEDIUM, LOW, INTERNAL' severities it must be true";
        private UUID validationResultId;
        private String description;
        private String type;
        private String owner;
        private Severity severity = Severity.HIGH;
        private ValidationStatus validationStatus = ValidationStatus.INVALID;
        private SuppressionDetails suppressionDetails;
        private Boolean suppressEnabled;

        ValidationResultBuilder() {
        }

        public ValidationResultBuilder validationResultId(UUID validationResultId) {
            this.validationResultId = validationResultId;
            return this;
        }

        public ValidationResultBuilder validationResultId(Optional<UUID> validationResultId) {
            validationResultId.ifPresent(uuid -> this.validationResultId = uuid);
            return this;
        }

        public ValidationResultBuilder description(String description) {
            this.description = description;
            return this;
        }

        public ValidationResultBuilder type(String type) {
            this.type = type;
            return this;
        }

        public ValidationResultBuilder owner(String owner) {
            this.owner = owner;
            return this;
        }

        public ValidationResultBuilder owner(Optional<String> owner) {
            owner.ifPresent(ownerName -> this.owner = ownerName);
            return this;
        }

        public ValidationResultBuilder severity(Severity severity) {
            this.severity = severity;
            return this;
        }

        public ValidationResultBuilder validationStatus(ValidationStatus validationStatus) {
            this.validationStatus = validationStatus;
            return this;
        }

        public ValidationResultBuilder suppressEnabled(boolean suppressEnabled) {
            this.suppressEnabled = suppressEnabled;
            return this;
        }

        public ValidationResultBuilder suppression(Optional<SuppressionDetails> suppression) {
            suppression.ifPresent(details -> this.suppressionDetails = details);
            return this;
        }

        public ValidationResultBuilder suppression(SuppressionDetails suppressionDetails) {
            this.suppressionDetails = suppressionDetails;
            return this;
        }

        public ValidationResult build() {
            Preconditions.checkArgument(description != null, NULL_ARGUMENT_EXCEPTION, "description");
            Preconditions.checkArgument(type != null, NULL_ARGUMENT_EXCEPTION, "type");
            if (suppressEnabled == null) {
                suppressEnabled = !Severity.CRITICAL.equals(severity);
            } else if (Boolean.TRUE.equals(suppressEnabled) && severity.equals(Severity.CRITICAL)) {
                throw new IllegalArgumentException(SUPPRESS_ENABLED_ARGUMENT_EXCEPTION);
            } else if (Boolean.FALSE.equals(suppressEnabled) && !(severity.equals(Severity.CRITICAL) || severity.equals(Severity.HIGH))) {
                throw new IllegalArgumentException(SUPPRESS_ENABLED_ARGUMENT_EXCEPTION);
            }
            return new ValidationResult(this);
        }
    }

    public static class SuppressionDetails {
        private final String suppressionReason;
        private final String suppressedByUserName;

        public SuppressionDetails(String suppressionReason, String suppressedByUserName) {
            this.suppressionReason = suppressionReason;
            this.suppressedByUserName = suppressedByUserName;
        }

        public String getSuppressionReason() {
            return suppressionReason;
        }

        public String getSuppressedByUserName() {
            return suppressedByUserName;
        }
    }
}
