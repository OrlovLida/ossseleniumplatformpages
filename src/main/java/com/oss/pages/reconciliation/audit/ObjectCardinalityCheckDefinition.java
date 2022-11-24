package com.oss.pages.reconciliation.audit;

import java.util.Optional;

public class ObjectCardinalityCheckDefinition {

    private final String groupName;
    private final String interfaceName;
    private final String parentObjectType;
    private final String childObjectType;
    private final String expectedCardinality;
    private final String state;

    private ObjectCardinalityCheckDefinition(ObjectCardinalityCheckDefinition.Builder builder) {
        this.groupName = builder.groupName;
        this.interfaceName = builder.interfaceName;
        this.parentObjectType = builder.parentObjectType;
        this.childObjectType = builder.childObjectType;
        this.expectedCardinality = builder.expectedCardinality;
        this.state = builder.state;
    }

    public static ObjectCardinalityCheckDefinition.Builder builder() {
        return new ObjectCardinalityCheckDefinition.Builder();
    }

    public Optional<String> getGroupName() {
        return Optional.ofNullable(groupName);
    }

    public Optional<String> getInterfaceName() {
        return Optional.ofNullable(interfaceName);
    }

    public String getParentObjectType() {
        return parentObjectType;
    }

    public Optional<String> getChildObjectType() {
        return Optional.ofNullable(childObjectType);
    }

    public String getExpectedCardinality() {
        return expectedCardinality;
    }

    public Optional<String> getState() {
        return Optional.ofNullable(state);
    }

    public static class Builder {
        private static final String NOT_ALL_MANDATORY_ATTRIBUTES_ARE_SET = "Not all mandatory attributes are set.";
        private String groupName;
        private String interfaceName;
        private String parentObjectType;
        private String childObjectType;
        private String expectedCardinality;
        private String state;

        public ObjectCardinalityCheckDefinition.Builder setGroupName(String value) {
            this.groupName = value;
            return this;
        }

        public ObjectCardinalityCheckDefinition.Builder setInterfaceName(String value) {
            this.interfaceName = value;
            return this;
        }

        public ObjectCardinalityCheckDefinition.Builder setParentObjectType(String value) {
            this.parentObjectType = value;
            return this;
        }

        public ObjectCardinalityCheckDefinition.Builder setChildObjectType(String value) {
            this.childObjectType = value;
            return this;
        }

        public ObjectCardinalityCheckDefinition.Builder setExpectedCardinality(String value) {
            this.expectedCardinality = value;
            return this;
        }

        public ObjectCardinalityCheckDefinition.Builder setState(String value) {
            this.state = value;
            return this;
        }

        public ObjectCardinalityCheckDefinition build() {
            if (this.parentObjectType == null || this.parentObjectType.isEmpty() || this.expectedCardinality == null)
                throw new IllegalArgumentException(NOT_ALL_MANDATORY_ATTRIBUTES_ARE_SET);
            return new ObjectCardinalityCheckDefinition(this);
        }
    }
}