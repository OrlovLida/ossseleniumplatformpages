package com.oss.pages.bpm.forecasts;

import lombok.Builder;
import lombok.Getter;

import java.util.Optional;

/**
 * @author Paweł Rother
 */
@Builder
@Getter
public class Forecast {
    private final Optional<String> name;
    private final Optional<Long> startPlusDays;
    private final Optional<Long> endPlusDaysShortWay;
    private final Optional<Long> endPlusDaysLongWay;
    private final Optional<Boolean> longWorkWeakShortWay;
    private final Optional<Boolean> longWorkWeakLongWay;
}
