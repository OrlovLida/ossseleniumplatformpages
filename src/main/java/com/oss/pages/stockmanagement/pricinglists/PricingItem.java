package com.oss.pages.stockmanagement.pricinglists;

import lombok.Builder;

import java.util.Map;
import java.util.Optional;

@Builder
public class PricingItem {
    private final String name;
    private final String description;
    private final Double price;
    private final String currency;
    private final Map<String, String> materialKeys;

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

}
