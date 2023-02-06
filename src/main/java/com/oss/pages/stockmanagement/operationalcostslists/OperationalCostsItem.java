package com.oss.pages.stockmanagement.operationalcostslists;

import lombok.Builder;

import java.util.Optional;

@Builder
public class OperationalCostsItem {
    private final String name;
    private final String description;
    private final Double price;
    private final String currency;

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
}
