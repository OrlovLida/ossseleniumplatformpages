package com.oss.repositories.entities;

import java.util.Objects;
import java.util.Optional;

public class Sublocation {

    private final String subLocationType;
    private final String subLocationName;
    private final Long preciseLocation;
    private final String preciseLocationType;
    private final Long parentLocationId;
    private final String parentLocationType;
    private final Long subLocationModelId;
    private final String subLocationModelType;

    public Sublocation(SublocationBuilder sublocationBuilder) {
        this.subLocationType = Objects.requireNonNull(sublocationBuilder.subLocationType);
        this.subLocationName = Objects.requireNonNull(sublocationBuilder.subLocationName);
        this.parentLocationId = Objects.requireNonNull(sublocationBuilder.parentLocationId);
        this.parentLocationType = Objects.requireNonNull(sublocationBuilder.parentLocationType);
        this.preciseLocation = sublocationBuilder.preciseLocation;
        this.preciseLocationType = sublocationBuilder.preciseLocationType;
        this.subLocationModelId = sublocationBuilder.subLocationModelId;
        this.subLocationModelType = sublocationBuilder.subLocationModelType;
    }

    public String getSubLocationType() {
        return subLocationType;
    }

    public String getSubLocationName() {
        return subLocationName;
    }

    public Long getPreciseLocation() {
        return preciseLocation;
    }

    public String getPreciseLocationType() {
        return preciseLocationType;
    }

    public Long getParentLocationId() {
        return parentLocationId;
    }

    public String getParentLocationType() {
        return parentLocationType;
    }

    public Optional<Long> getSubLocationModelId() {
        return Optional.ofNullable(subLocationModelId);
    }

    public Optional<String> getSubLocationModelType() {
        return Optional.ofNullable(subLocationModelType);
    }

    public static SublocationBuilder builder() {
        return new SublocationBuilder();
    }

    public static class SublocationBuilder {

        private String subLocationType;
        private String subLocationName;
        private Long preciseLocation;
        private String preciseLocationType;
        private Long parentLocationId;
        private String parentLocationType;
        private Long subLocationModelId;
        private String subLocationModelType;

        public SublocationBuilder withSubLocationType(String subLocationType) {
            this.subLocationType = subLocationType;
            return this;
        }

        public SublocationBuilder withSubLocationName(String subLocationName) {
            this.subLocationName = subLocationName;
            return this;
        }

        public SublocationBuilder withPreciseLocation(Long preciseLocation) {
            this.preciseLocation = preciseLocation;
            return this;
        }

        public SublocationBuilder withPreciseLocationType(String preciseLocationType) {
            this.preciseLocationType = preciseLocationType;
            return this;
        }

        public SublocationBuilder withParentLocationId(Long parentLocationId) {
            this.parentLocationId = parentLocationId;
            return this;
        }

        public SublocationBuilder withParentLocationType(String parentLocationType) {
            this.parentLocationType = parentLocationType;
            return this;
        }

        public SublocationBuilder withSublocationModelId(Long subLocationModelId) {
            this.subLocationModelId = subLocationModelId;
            return this;
        }

        public SublocationBuilder withSublocationModelType(String subLocationModelType) {
            this.subLocationModelType = subLocationModelType;
            return this;
        }

        public Sublocation build() {
            return new Sublocation(this);
        }

    }

}
