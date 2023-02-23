package com.oss.pages.stockmanagement.pricinglists;

import com.google.common.base.Preconditions;

import java.util.Map;
import java.util.Optional;

public class PricingItem {
    private final String name;
    private final String description;
    private final Double price;
    private final String currency;
    private final Map<String, String> materialKeys;

    PricingItem(PricingItemBuilder builder) {
        this.name = builder.name;
        this.description = builder.description;
        this.price = builder.price;
        this.currency = builder.currency;
        this.materialKeys = builder.materialKeys;
    }

    public static PricingItemBuilder builder() {
        return new PricingItemBuilder();
    }

    public Optional<String> getName() {
        return Optional.ofNullable(name);
    }

    public Optional<String> getDescription() {
        return Optional.ofNullable(description);
    }

    public Optional<Double> getPrice() {
        return Optional.ofNullable(price);
    }

    public Optional<String> getCurrency() {
        return Optional.ofNullable(currency);
    }

    public Optional<Map<String, String>> getMaterialKeys() {
        return Optional.ofNullable(materialKeys);
    }

    public static class PricingItemBuilder {
        private static final String PRICE_NEGATIVE_EXCEPTION = "Provided price must be greater than 0.";
        private String name;
        private String description;
        private Double price;
        private String currency;
        private Map<String, String> materialKeys;

        PricingItemBuilder() {
        }

        public PricingItemBuilder name(String name) {
            this.name = name;
            return this;
        }

        public PricingItemBuilder description(String description) {
            this.description = description;
            return this;
        }

        public PricingItemBuilder price(Double price) {
            Preconditions.checkArgument(price > 0, PRICE_NEGATIVE_EXCEPTION, price);
            this.price = price;
            return this;
        }

        public PricingItemBuilder currency(String currency) {
            this.currency = currency;
            return this;
        }

        public PricingItemBuilder materialKeys(Map<String, String> materialKeys) {
            this.materialKeys = materialKeys;
            return this;
        }

        public PricingItem build() {
            return new PricingItem(this);
        }
    }
}
