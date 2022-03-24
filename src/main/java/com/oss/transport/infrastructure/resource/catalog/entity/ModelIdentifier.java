package com.oss.transport.infrastructure.resource.catalog.entity;

import java.util.Objects;
import java.util.Optional;

public class ModelIdentifier {

    private final String name;
    private final String manufacturer;
    private final String type;
    private final Optional<String> instanceName;

    private ModelIdentifier(Builder builder) {
        name = builder.name;
        manufacturer = builder.manufacturer;
        type = builder.type;
        instanceName = builder.instanceName;
    }

    public static Builder builder() {
        return new Builder();
    }

    public String getName() {
        return name;
    }

    public String getManufacturer() {
        return manufacturer;
    }

    public String getType() {
        return type;
    }

    public Optional<String> getInstanceName() {
        return instanceName;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        ModelIdentifier that = (ModelIdentifier) o;
        return Objects.equals(name, that.name) &&
                Objects.equals(manufacturer, that.manufacturer) &&
                Objects.equals(type, that.type);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, manufacturer, type);
    }

    @Override
    public String toString() {
        return "{" +
                "name='" + name + '\'' +
                ", manufacturer='" + manufacturer + '\'' +
                ", type='" + type + '\'' +
                ", instanceName=" + instanceName +
                '}';
    }

    public static final class Builder {

        private String name;
        private String manufacturer;
        private String type;
        private Optional<String> instanceName = Optional.empty();

        public Builder withName(String val) {
            name = val;
            return this;
        }

        public Builder withManufacturer(String val) {
            manufacturer = val;
            return this;
        }

        public Builder withType(String val) {
            type = val;
            return this;
        }

        public Builder withInstanceName(Optional<String> val) {
            instanceName = val;
            return this;
        }

        public ModelIdentifier build() {
            return new ModelIdentifier(this);
        }
    }
}
