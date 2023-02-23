package com.oss.pages.stockmanagement.operationalcostslists;

import com.google.common.base.Preconditions;

import java.util.Optional;

public class OperationalCostsItem {
    private final String name;
    private final String description;
    private final Double price;
    private final String currency;

    OperationalCostsItem(OperationalCostsItemBuilder builder) {
        this.name = builder.name;
        this.description = builder.description;
        this.price = builder.price;
        this.currency = builder.currency;
    }

    public static OperationalCostsItemBuilder builder() {

        return new OperationalCostsItemBuilder();
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

    public static class OperationalCostsItemBuilder {
        private static final String PRICE_NEGATIVE_EXCEPTION = "Provided price must be greater than 0.";

        private String name;
        private String description;
        private Double price;
        private String currency;

        OperationalCostsItemBuilder() {
        }

        public OperationalCostsItemBuilder name(String name) {
            this.name = name;
            return this;
        }

        public OperationalCostsItemBuilder description(String description) {
            this.description = description;
            return this;
        }

        public OperationalCostsItemBuilder price(Double price) {
            Preconditions.checkArgument(price > 0, PRICE_NEGATIVE_EXCEPTION, price);
            this.price = price;
            return this;
        }

        public OperationalCostsItemBuilder currency(String currency) {
            this.currency = currency;
            return this;
        }

        public OperationalCostsItem build() {
            return new OperationalCostsItem(this);
        }
    }
}
