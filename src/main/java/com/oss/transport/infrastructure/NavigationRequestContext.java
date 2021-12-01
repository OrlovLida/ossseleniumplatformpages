package com.oss.transport.infrastructure;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;

public class NavigationRequestContext {
    private Map<String, Object> pathParams;
    private Map<String, Object> queryParams;
    private Optional<Object> dto;
    private String moduleName;
    private String domainName;
    private String operationName;

    private NavigationRequestContext(Builder builder) {
        pathParams = builder.pathParams;
        moduleName = builder.moduleName;
        domainName = builder.domainName;
        operationName = builder.operationName;
        queryParams = builder.queryParams;
        dto = builder.dto;
    }

    Map<String, Object> getPathParams() {
        return pathParams;
    }

    Map<String, Object> getQueryParams() {
        return queryParams;
    }

    String getModuleName() {
        return moduleName;
    }

    String getDomainName() {
        return domainName;
    }

    String getOperationName() {
        return operationName;
    }

    public Optional<Object> getDto() {
        return dto;
    }

    public static Builder builder() {
        return new Builder();
    }

    public static final class Builder {
        private Map<String, Object> pathParams = new HashMap<>();
        private Map<String, Object> queryParams = new HashMap<>();
        private String moduleName;
        private String domainName;
        private String operationName;
        private Optional<Object> dto = Optional.empty();

        public Builder() {
        }

        public Builder addAllPathParams(Map<String, Object> val) {
            pathParams = val;
            return this;
        }

        public Builder addPathParam(String paramName, Object paramValue) {
            pathParams.put(paramName, paramValue);
            return this;
        }

        public Builder addAllQueryParams(Map<String, Object> val) {
            queryParams = val;
            return this;
        }

        public Builder addQueryParam(String paramName, Object paramValue) {
            queryParams.put(paramName, paramValue);
            return this;
        }

        public Builder moduleName(String moduleName) {
            this.moduleName = moduleName;
            return this;
        }

        public Builder domainName(String domainName) {
            this.domainName = domainName;
            return this;
        }

        public Builder operationName(String operationName) {
            this.operationName = operationName;
            return this;
        }

        public Builder dto(Object dto) {
            this.dto = Optional.of(dto);
            return this;
        }

        public NavigationRequestContext build() {
            Objects.requireNonNull(moduleName, " Module Name cannot be null");
            Objects.requireNonNull(domainName, " Domain Name cannot be null");
            Objects.requireNonNull(operationName, " Operation Name name cannot be null");
            return new NavigationRequestContext(this);
        }
    }

}
