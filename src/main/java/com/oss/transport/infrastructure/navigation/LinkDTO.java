package com.oss.transport.infrastructure.navigation;

import java.util.Objects;

public class LinkDTO {
    private String rel;
    private String href;
    private MethodEnum method;
    
    public String getRel() {
        return rel;
    }
    
    public String getHref() {
        return href;
    }
    
    public MethodEnum getMethod() {
        return method;
    }

    public LinkDTO() {
    }

    public LinkDTO(Builder builder) {
        rel = builder.rel;
        href = builder.href;
        method = builder.method;
    }

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {
        private String rel;
        private String href;
        private MethodEnum method;

        private Builder() {
        }

        public Builder rel(String rel) {
            this.rel = rel;
            return this;
        }

        public final Builder href(String href) {
            this.href = href;
            return this;
        }

        public final Builder method(MethodEnum method) {
            this.method = method;
            return this;
        }

        public LinkDTO build() {
            Objects.requireNonNull(rel);
            Objects.requireNonNull(href);
            Objects.requireNonNull(method);
            return new LinkDTO(this);
        }
    }
    
    public enum MethodEnum {
        GET, POST, PUT, DELETE
    }

}
