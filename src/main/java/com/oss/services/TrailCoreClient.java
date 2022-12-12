package com.oss.services;

import com.comarch.oss.transport.trail.api.dto.TrailCreationDTO;
import com.comarch.oss.transport.trail.api.dto.TrailIdentificationDTO;
import com.comarch.oss.transport.trail.api.dto.TrailListDTO;
import com.jayway.restassured.http.ContentType;
import com.jayway.restassured.specification.RequestSpecification;
import com.oss.transport.infrastructure.planning.PlanningContext;
import com.oss.untils.Constants;
import com.oss.untils.Environment;

public class TrailCoreClient {

    private static final String TRAILS_SEARCH_PATH = "trails/search?terminationId=";
    private static final String TRAILS_CREATE_WITH_TYPE_PATH = "trails/{type}";
    private static final String TYPE_PARAM = "type";
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

    public TrailIdentificationDTO createTrail(TrailCreationDTO trailCreationDTO,
                                              String type,
                                              PlanningContext planningContext) {
        RequestSpecification requestSpecification = env.getTrailCoreSpecification()
                .given()
                .body(trailCreationDTO).contentType(ContentType.JSON)
                .pathParam(TYPE_PARAM, type);
        if (planningContext.isProjectContext()) {
            requestSpecification.queryParam(PlanningContext.PROJECT_PARAM, planningContext.getProjectId());
        } else {
            requestSpecification.queryParam(PlanningContext.PERSPECTIVE_PARAM, planningContext.getPerspective());
        }
        return requestSpecification.post(TRAILS_CREATE_WITH_TYPE_PATH)
                .then()
                .log().body()
                .statusCode(201).assertThat()
                .extract().as(TrailIdentificationDTO.class);
    }

}
