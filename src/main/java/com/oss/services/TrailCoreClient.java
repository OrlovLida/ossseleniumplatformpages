package com.oss.services;

import com.comarch.oss.transport.trail.api.dto.TrailListDTO;
import com.oss.untils.Constants;
import com.oss.untils.Environment;

public class TrailCoreClient {

    private static final String TRAILS_SEARCH_PATH = "trails/search?terminationId=";
    private static TrailCoreClient instance;
    private final Environment env;

    public TrailCoreClient(Environment environment) {
        env = environment;
    }

    public static TrailCoreClient getInstance(Environment pEnvironment) {
        if (instance != null) {
            return instance;
        }
        instance = new TrailCoreClient(pEnvironment);
        return instance;
    }

    public TrailListDTO searchTrailsByTerminations(Long terminationPointId) {
        return env.getTrailCoreSpecification()
                .given()
                .queryParam(Constants.PERSPECTIVE, Constants.LIVE)
                .when()
                .get(TRAILS_SEARCH_PATH + terminationPointId)
                .then()
                .log().body()
                .extract()
                .as(TrailListDTO.class);
    }

}
