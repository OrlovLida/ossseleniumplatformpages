package com.oss.pages.reconciliation.audit;

import java.util.Optional;

public class ComplexValueCheckDefinition {

    private final String groupName;
    private final String interfaceName;
    private final String objectType;
    private final String ruleDefinitions;
    private final String state;

    private ComplexValueCheckDefinition(ComplexValueCheckDefinition.Builder builder) {
        this.groupName = builder.groupName;
        this.interfaceName = builder.interfaceName;
        this.objectType = builder.objectType;
        this.ruleDefinitions = builder.ruleDefinitions;
        this.state = builder.state;
    }

    public static ComplexValueCheckDefinition.Builder builder() {
        return new ComplexValueCheckDefinition.Builder();
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

    public String getRuleDefinitions() {
        return ruleDefinitions;
    }

    public Optional<String> getState() {
        return Optional.ofNullable(state);
    }

    public static class Builder {
        private static final String NOT_ALL_MANDATORY_ATTRIBUTES_ARE_SET = "Not all mandatory attributes are set.";
        private String groupName;
        private String interfaceName;
        private String objectType;
        private String ruleDefinitions;
        private String state;

        public ComplexValueCheckDefinition.Builder setGroupName(String value) {
            this.groupName = value;
            return this;
        }

        public ComplexValueCheckDefinition.Builder setInterfaceName(String value) {
            this.interfaceName = value;
            return this;
        }

        public ComplexValueCheckDefinition.Builder setObjectType(String value) {
            this.objectType = value;
            return this;
        }

        public ComplexValueCheckDefinition.Builder setRuleDefinitions(String value) {
            this.ruleDefinitions = value;
            return this;
        }

        public ComplexValueCheckDefinition.Builder setState(String value) {
            this.state = value;
            return this;
        }

        public ComplexValueCheckDefinition build() {
            if (this.ruleDefinitions == null || this.ruleDefinitions.isEmpty())
                throw new IllegalArgumentException(NOT_ALL_MANDATORY_ATTRIBUTES_ARE_SET);
            return new ComplexValueCheckDefinition(this);
        }
    }
}