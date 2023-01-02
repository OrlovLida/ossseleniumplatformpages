/* @(#) $Id$
 *
 * Copyright (c) 2000-2021 ComArch S.A. All Rights Reserved. Any usage, duplication or redistribution of this
 * software is allowed only according to separate agreement prepared in written between ComArch and authorized party.
 */
package com.oss.services;

import java.text.MessageFormat;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Optional;

import javax.ws.rs.core.Response;

import com.comarch.oss.planning.api.dto.ObjectIdDTO;
import com.comarch.oss.planning.api.dto.ObjectsDescriptionDTO;
import com.comarch.oss.planning.api.dto.PlanningPerspectiveDTO;
import com.comarch.oss.planning.api.dto.v2.ProjectDTO;
import com.comarch.oss.planningmanager.api.dto.internal.ConnectionDTO;
import com.jayway.restassured.http.ContentType;
import com.oss.framework.utils.DelayUtils;
import com.oss.planning.PlanningContext;
import com.oss.untils.Environment;

import dev.failsafe.internal.util.Assert;

import static com.oss.untils.Constants.LIVE;
import static com.oss.untils.Constants.PERSPECTIVE;
import static com.oss.untils.Constants.PLAN;
import static com.oss.untils.Constants.PROJECT_ID;
import static java.net.HttpURLConnection.HTTP_NO_CONTENT;

public class PlanningClient {

    private static final String CANNOT_FIND_OBJECT = "Cannot find object with type \"{0}\" and name \"{1}\"";
    private static final String QUERY_QUERY_PARAM = "query";
    private static final String RSQL_QUERY_NAME_PARAM = "Name==\"{0}\"";
    private static final String QUERY_OBJECTS_V2_PATH = "objects/v2/{0}";
    private static final String QUERY_OBJECTS_V2_WITH_LIMIT_AND_START_POSITION_PATH = "/objects/v2/%s?objectLimit=%s&startPosition=%s";
    private static final String PROJECT_API_PATH = "/projects";
    private static final String PLANNING_API_PATH = "/planning/projects";
    private static final String ROOTS_API_PATH = "/roots";
    private static final String ROOTS_CONNECTION_API_PATH = ROOTS_API_PATH + "/connection";
    private static final String RESPONSE_STATUS_MESSAGE = "Response status is ";

    private static PlanningClient instance;

    private final Environment env;

    public PlanningClient(Environment env) {
        this.env = env;
    }

    public static PlanningClient getInstance(Environment environment) {
        if (instance == null) {
            instance = new PlanningClient(environment);
        }
        return instance;
    }

    public Long findExistingObjectIdByNameAndType(String objectName, String objectType) {
        Optional<Long> foundObject = findObjectIdByNameAndType(objectName, objectType);
        return foundObject.orElseThrow(() -> new IllegalStateException(MessageFormat.format(CANNOT_FIND_OBJECT, objectType, objectName)));
    }

    public Optional<Long> findObjectIdByNameAndType(String objectName, String objectType) {
        String rsqlQuery = MessageFormat.format(RSQL_QUERY_NAME_PARAM, objectName);
        ObjectsDescriptionDTO foundObjects = queryObjectsByRsqlQuery(rsqlQuery, objectType);
        return foundObjects.getObjectIds().stream()
                .findAny()
                .map(ObjectIdDTO::getId);
    }

    public void moveProject(Long projectId, PlanningPerspectiveDTO perspectiveDTO) {
        long currentTime = System.currentTimeMillis();
        long stopTime = currentTime + 30000L;
        int status = 0;
        while (status != Response.Status.NO_CONTENT.getStatusCode() && stopTime > System.currentTimeMillis()) {
            status = env.getPlanningCoreSpecification().given().contentType(ContentType.JSON).body(perspectiveDTO).when().put(PLANNING_API_PATH + "/" + projectId + "/perspective").getStatusCode();
            DelayUtils.sleep();
        }
        Assert.isTrue(status == Response.Status.NO_CONTENT.getStatusCode(), RESPONSE_STATUS_MESSAGE + status);
    }

    public Long createProject(ProjectDTO projectDTO) {
        String location = env.getPlanningCoreSpecification()
                .given()
                .contentType(ContentType.JSON)
                .body(projectDTO)
                .when()
                .post(PROJECT_API_PATH)
                .then()
                .statusCode(Response.Status.CREATED.getStatusCode()).assertThat()
                .extract()
                .headers().get("Location").getValue();
        return get(ProjectDTO.class, location).getProjectId().orElseThrow(() -> new NoSuchElementException("Cannot Create Project"));
    }

    public void cancelProject(Long projectId) {
        env.getPlanningCoreSpecification()
                .when()
                .delete(PLANNING_API_PATH + "/" + projectId)
                .then()
                .statusCode(HTTP_NO_CONTENT).assertThat();
    }

    private ObjectsDescriptionDTO queryObjectsByRsqlQuery(String rsqlQuery, String objectType) {
        return env.getPlanningCoreSpecification()
                .given().contentType(ContentType.JSON).queryParam(QUERY_QUERY_PARAM, rsqlQuery)
                .log().path()
                .when().get(MessageFormat.format(QUERY_OBJECTS_V2_PATH, objectType))
                .then().log().status().log().body()
                .extract().as(ObjectsDescriptionDTO.class);

    }

    public ObjectsDescriptionDTO getObjectByType(String objectType, String objectLimit, String startPosition) {
        String firstObjectByTypePath = String.format(QUERY_OBJECTS_V2_WITH_LIMIT_AND_START_POSITION_PATH, objectType, objectLimit, startPosition);
        return env.getPlanningCoreSpecification()
                .given()
                .queryParam(PERSPECTIVE, LIVE)
                .when()
                .get(firstObjectByTypePath)
                .then()
                .log().body()
                .extract()
                .as(ObjectsDescriptionDTO.class);
    }

    public void connectRoots(List<ConnectionDTO> connectionDTOs, PlanningContext planningContext) {
        if (planningContext.isPlanContext()) {
            env.getPlanningCoreSpecification()
                    .given()
                    .queryParam(PERSPECTIVE, PLAN)
                    .queryParam(PROJECT_ID, planningContext.getProjectId())
                    .contentType(ContentType.JSON)
                    .body(connectionDTOs)
                    .when()
                    .post(ROOTS_CONNECTION_API_PATH)
                    .then()
                    .statusCode(Response.Status.NO_CONTENT.getStatusCode()).assertThat();
        } else {
            env.getPlanningCoreSpecification()
                    .given()
                    .queryParam(PERSPECTIVE, planningContext.getPerspective())
                    .contentType(ContentType.JSON)
                    .body(connectionDTOs)
                    .when()
                    .post(ROOTS_CONNECTION_API_PATH)
                    .then()
                    .statusCode(Response.Status.NO_CONTENT.getStatusCode()).assertThat();
        }

    }

    public <T> T get(Class<T> asClass, String absoluteUri, String... pathParams) {
        return get(asClass, absoluteUri, 200, Collections.emptyMap(), pathParams);
    }

    public <T> T get(Class<T> asClass, String absoluteUri, int statusCode, Map<String, String> parametersMap, String... pathParams) {
        return env.getPlanningCoreSpecification()
                .when()
                .queryParameters(parametersMap)
                .get(absoluteUri, pathParams)
                .then()
                .log()
                .status()
                .log()
                .body()
                .statusCode(statusCode)
                .contentType(ContentType.JSON)
                .extract()
                .as(asClass);
    }

}
