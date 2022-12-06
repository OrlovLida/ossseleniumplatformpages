package com.oss.pages.reconciliation.audit;

import java.util.Optional;

public class SimpleValueCheckDefinition {

    private final String groupName;
    private final String interfaceName;
    private final String objectType;
    private final String attributeName;
    private final String operator;
    private final String value1;
    private final String value2;
    private final String state;

    public static Builder builder() {
        return new Builder();
    }

    private SimpleValueCheckDefinition(Builder builder) {
        this.groupName = builder.groupName;
        this.interfaceName = builder.interfaceName;
        this.objectType = builder.objectType;
        this.attributeName = builder.attributeName;
        this.operator = builder.operator;
        this.value1 = builder.value1;
        this.value2 = builder.value2;
        this.state = builder.state;
    }

    public Optional<String> getGroupName() {
        return Optional.ofNullable(groupName);
    }

    public Optional<String> getInterfaceName() {
        return Optional.ofNullable(interfaceName);
    }

    public Optional<String> getObjectType() {
        return Optional.ofNullable(objectType);
    }

    public Optional<String> getAttributeName() {
        return Optional.ofNullable(attributeName);
    }

    public String getOperator() {
        return operator;
    }

    public Optional<String> getValue1() {
        return Optional.ofNullable(value1);
    }

    public Optional<String> getValue2() {
        return Optional.ofNullable(value2);
    }

    public Optional<String> getState() {
        return Optional.ofNullable(state);
    }

    public static class Builder {
        private static final String NOT_ALL_MANDATORY_ATTRIBUTES_ARE_SET = "Not all mandatory attributes are set.";
        private String groupName;
        private String interfaceName;
        private String objectType;
        private String attributeName;
        private String operator;
        private String value1;
        private String value2;
        private String state;

        public Builder setGroupName(String value) {
            this.groupName = value;
            return this;
        }

        public Builder setInterfaceName(String value) {
            this.interfaceName = value;
            return this;
        }

        public Builder setObjectType(String value) {
            this.objectType = value;
            return this;
        }

        public Builder setAttributeName(String value) {
            this.attributeName = value;
            return this;
        }

        public Builder setOperator(String value) {
            this.operator = value;
            return this;
        }

        public Builder setValue1(String value) {
            this.value1 = value;
            return this;
        }

        public Builder setValue2(String value) {
            this.value2 = value;
            return this;
        }

        public Builder setState(String value) {
            this.state = value;
            return this;
        }

        public SimpleValueCheckDefinition build() {
            if (this.operator == null || this.operator.isEmpty())
                throw new IllegalArgumentException(NOT_ALL_MANDATORY_ATTRIBUTES_ARE_SET);
            return new SimpleValueCheckDefinition(this);
        }
    }
}
