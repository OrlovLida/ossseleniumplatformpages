package com.oss.repositories;

import com.oss.services.TrailCoreClient;
import com.oss.untils.Environment;

public class TrailCoreRepository {

    private final TrailCoreClient trailCoreClient;

    public TrailCoreRepository(Environment env) {
        trailCoreClient = TrailCoreClient.getInstance(env);
    }

    public Long getFirstMediumIdConnectedOnTerminationPoint(Long terminationPointId, String mediumType) {
        return trailCoreClient.searchTrailsByTerminations(terminationPointId)
                .getTrails()
                .stream()
                .filter(e -> e.getType().equals(mediumType))
                .map(e -> e.getId())
                .findFirst()
                .orElseThrow(() -> new RuntimeException("Can't find any medium type " + mediumType + " on TP with id " + terminationPointId))
                .longValue();

    }

}
