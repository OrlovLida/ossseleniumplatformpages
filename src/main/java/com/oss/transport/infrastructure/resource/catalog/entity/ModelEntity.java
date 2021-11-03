package com.oss.transport.infrastructure.resource.catalog.entity;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public class ModelEntity {

    private String type;
    private String name;
    private String manufacturer;
    private Map<String, Optional<String>> simpleAttributes;
    private Map<String, AttributeModel> referencedAttributes;

    private ModelEntity(Builder builder) {
        type = builder.type;
        name = builder.name;
        manufacturer = builder.manufacturer;
        simpleAttributes = builder.simpleAttributes;
        referencedAttributes = builder.referencedAttributes;
    }

    public static Builder builder() {
        return new Builder();
    }

    public String getType() {
        return type;
    }

    public String getName() {
        return name;
    }

    public String getManufacturer() {
        return manufacturer;
    }

    public Map<String, Optional<String>> getSimpleAttributes() {
        return simpleAttributes;
    }

    public Map<String, AttributeModel> getReferencedAttributes() {
        return referencedAttributes;
    }

    public ModelIdentifier getModelIdentifier() {
        return ModelIdentifier.builder()
                .withName(name)
                .withManufacturer(manufacturer)
                .withType(type)
                .build();
    }

    public static final class Builder {

        private String type;
        private String name;
        private String manufacturer;
        private Map<String, Optional<String>> simpleAttributes = new HashMap<>();
        private Map<String, AttributeModel> referencedAttributes = new HashMap<>();

        private Builder() {
        }

        public Builder withType(String val) {
            type = val;
            return this;
        }

        public Builder withName(String val) {
            name = val;
            return this;
        }

        public Builder withManufacturer(String val) {
            manufacturer = val;
            return this;
        }

        public Builder addSimpleAttributes(Map<String, Optional<String>> simpleAttribute) {
            this.simpleAttributes = simpleAttribute;
            return this;
        }

        public Builder addReferencedAttributes(Map<String, AttributeModel> referencedAttributes) {
            this.referencedAttributes = referencedAttributes;
            return this;
        }

        public ModelEntity build() {
            return new ModelEntity(this);
        }
    }


    public static class AttributeModel {

        private String type;
        private String name;
        private String value;

        public AttributeModel(String type, String name, String value) {
            this.type = type;
            this.name = name;
            this.value = value;
        }

        public String getType() {
            return type;
        }

        public String getName() {
            return name;
        }

        public String getValue() {
            return value;
        }

        public void setValue(String value) {
            this.value = value;
        }
    }
}
