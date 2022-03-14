/* @(#) $Id$
 *
 * Copyright (c) 2000-2021 ComArch S.A. All Rights Reserved. Any usage, duplication or redistribution of this
 * software is allowed only according to separate agreement prepared in written between ComArch and authorized party.
 */
package com.oss.services;

import java.text.MessageFormat;
import java.util.Collections;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Optional;

import javax.ws.rs.core.Response;

import com.comarch.oss.planning.api.dto.ObjectIdDTO;
import com.comarch.oss.planning.api.dto.ObjectsDescriptionDTO;
import com.comarch.oss.planning.api.dto.PlanningPerspectiveDTO;
import com.comarch.oss.planning.api.dto.v2.ProjectDTO;
import com.jayway.restassured.http.ContentType;
import com.oss.untils.Environment;

public class PlanningClient {
    
    private static final String CANNOT_FIND_OBJECT = "Cannot find object with type \"{0}\" and name \"{1}\"";
    
    private static final String QUERY_QUERY_PARAM = "query";
    private static final String RSQL_QUERY_NAME_PARAM = "Name==\"{0}\"";
    
    private static final String QUERY_OBJECTS_V2_PATH = "objects/v2/{0}";
    private static final String PROJECT_API_PATH = "/projects";
    
    private static PlanningClient instance;
    
    private final Environment ENV;
    
    public static PlanningClient getInstance(Environment environment) {
        if (instance == null) {
            instance = new PlanningClient(environment);
        }
        return instance;
    }
    
    public PlanningClient(Environment env) {
        ENV = env;
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
        ENV.getPlanningCoreSpecification()
                .given()
                .contentType(ContentType.JSON)
                .body(perspectiveDTO)
                .when()
                .put(PROJECT_API_PATH + "/" + projectId + "/perspective")
                .then()
                .statusCode(Response.Status.NO_CONTENT.getStatusCode()).assertThat();
    }

    public Long createProject(ProjectDTO projectDTO){
        String location = ENV.getPlanningCoreSpecification()
                .given()
                .contentType(ContentType.JSON)
                .body(projectDTO)
                .when()
                .post(PROJECT_API_PATH)
                .then()
                .statusCode(Response.Status.CREATED.getStatusCode()).assertThat()
                .extract()
                .headers().get("Location").getValue();
       return get(ProjectDTO.class, location).getProjectId().orElseThrow(()->new NoSuchElementException("Cannot Create Project"));
    }
    
    private ObjectsDescriptionDTO queryObjectsByRsqlQuery(String rsqlQuery, String objectType) {
        return ENV.getPlanningCoreSpecification()
                .given().contentType(ContentType.JSON).queryParam(QUERY_QUERY_PARAM, rsqlQuery)
                .log().path()
                .when().get(MessageFormat.format(QUERY_OBJECTS_V2_PATH, objectType))
                .then().log().status().log().body()
                .extract().as(ObjectsDescriptionDTO.class);
        
    }

    public <T> T get(Class<T> asClass, String absoluteUri, String... pathParams) {
        return get(asClass, absoluteUri, 200, Collections.emptyMap(), pathParams);
    }

    public <T> T get(Class<T> asClass, String absoluteUri, int statusCode, Map<String, String> parametersMap, String... pathParams) {
        return ENV.getPlanningCoreSpecification()
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
