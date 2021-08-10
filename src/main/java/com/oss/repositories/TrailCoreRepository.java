package com.oss.repositories;

import com.oss.services.TrailCoreClient;
import com.oss.untils.Environment;

public class TrailCoreRepository {

    private final TrailCoreClient trailCoreClient;
    private final Environment env;

    public TrailCoreRepository(Environment env) {
        this.env = env;
        trailCoreClient = TrailCoreClient.getInstance(env);
    }

    public Long getFirstMediumIdConnectedOnTerminationPoint(Long terminationPointId, String mediumType) {
        Long mediumId = trailCoreClient.searchTrailsByTerminations(terminationPointId)
                .getTrails()
                .stream()
                .filter(e -> e.getType().equals(mediumType))
                .map(e -> e.getId())
                .findFirst().
                get();
        return mediumId;
    }

}
