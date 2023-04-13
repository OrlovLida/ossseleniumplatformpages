/* @(#) $Id$
 *
 * Copyright (c) 2000-2021 ComArch S.A. All Rights Reserved. Any usage, duplication or redistribution of this
 * software is allowed only according to separate agreement prepared in written between ComArch and authorized party.
 */
package com.oss.services;

import com.comarch.oss.planning.api.dto.LiveResultDTO;
import com.comarch.oss.planning.api.dto.NetworkDTO;
import com.comarch.oss.planning.api.dto.NetworkResultDTO;
import com.comarch.oss.planning.api.dto.ObjectIdDTO;
import com.comarch.oss.planning.api.dto.ObjectsDescriptionDTO;
import com.comarch.oss.planning.api.dto.PlannedObjectDTO;
import com.comarch.oss.planning.api.dto.PlanningPerspectiveDTO;
import com.comarch.oss.planning.api.dto.PlansResultDTO;
import com.comarch.oss.planning.api.dto.ProjectObjectsDTO;
import com.comarch.oss.planning.api.dto.QueryResultDTO;
import com.comarch.oss.planning.api.dto.TypeDescriptionDTO;
import com.comarch.oss.planning.api.dto.v2.ProjectDTO;
import com.comarch.oss.planningmanager.api.dto.internal.ConnectionDTO;
import com.comarch.oss.planningmanager.api.dto.internal.RootPrerequisitesDTO;
import com.jayway.restassured.http.ContentType;
import com.jayway.restassured.specification.RequestSpecification;
import com.oss.framework.utils.DelayUtils;
import com.oss.planning.ObjectIdentifier;
import com.oss.planning.PlanningContext;
import com.oss.untils.Environment;

import javax.ws.rs.core.Response;
import java.text.MessageFormat;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.stream.Collectors;

import static com.oss.untils.Constants.LIVE;
import static com.oss.untils.Constants.PERSPECTIVE;
import static com.oss.untils.Constants.PROJECT_ID;

public class PlanningClient {

    private static final String CANNOT_FIND_OBJECT = "Cannot find object with type \"{0}\" and name \"{1}\"";
    private static final String QUERY_QUERY_PARAM = "query";
    private static final String VALIDATIONS_CHECKING_QUERY_PARAM = "validations_checking";
    private static final String ATTRIBUTES_QUERY_PARAM = "attributes";
    private static final String TAG_QUERY_PARAM = "tag";
    private static final String VERSION_QUERY_PARAM = "version";
    private static final String WITH_REMOVED_QUERY_PARAM = "withRemoved";
    private static final String GIVEN_TYPE_ONLY_QUERY_PARAM = "given_type_only";
    private static final String PRESERVE_PREREQUISITES_QUERY_PARAM = "preserve_prerequisites";
    private static final String DOMAIN_NAME_QUERY_PARAM = "sourceDomainName";
    private static final String RSQL_QUERY_NAME_PARAM = "Name==\"{0}\"";
    private static final String OBJECTS_V2_PATH = "objects/v2";
    private static final String QUERY_OBJECTS_V2_WITH_LIMIT_AND_START_POSITION_PATH = "/objects/v2/%s?objectLimit=%s&startPosition=%s";
    private static final String QUERY_OBJECT_PATH = "objects/%1$s/%2$s";
    private static final String NETWORK_API_PATH = "/network";
    private static final String PROJECT_API_PATH = "/projects";
    private static final String PLANNING_API_PATH = "/planning/projects";
    private static final String PROJECT_OBJECTS_PATH = PLANNING_API_PATH + "/%s/objects";
    private static final String ROOTS_API_PATH = "/roots";
    private static final String ROOTS_CONNECTION_API_PATH = ROOTS_API_PATH + "/connection";
    private static final String ROOTS_PREREQUISITES_PATH = ROOTS_API_PATH + "/prerequisites";
    private static final String TYPES_PATH = "/types";
    private static final String RESPONSE_STATUS_MESSAGE = "Response status is ";
    private static final String INVALID_TYPE_NAME = "Provided type name doesn't exist";

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
        long stopTime = System.currentTimeMillis() + 30000L;
        int noContentStatus = Response.Status.NO_CONTENT.getStatusCode();
        int status = 0;
        while (status != noContentStatus && stopTime > System.currentTimeMillis()) {
            status = env.getPlanningCoreSpecification()
                    .given()
                    .contentType(ContentType.JSON)
                    .body(perspectiveDTO)
                    .when()
                    .put(PLANNING_API_PATH + "/" + projectId + "/perspective")
                    .getStatusCode();
            DelayUtils.sleep();
        }
        if (status != noContentStatus) {
            throw new IllegalStateException(RESPONSE_STATUS_MESSAGE + status);
        }
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
                .statusCode(Response.Status.NO_CONTENT.getStatusCode()).assertThat();
    }

    private ObjectsDescriptionDTO queryObjectsByRsqlQuery(String rsqlQuery, String objectType) {
        return env.getPlanningCoreSpecification()
                .given().contentType(ContentType.JSON).queryParam(QUERY_QUERY_PARAM, rsqlQuery)
                .log().path()
                .when().get(OBJECTS_V2_PATH + "/" + objectType)
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
        RequestSpecification specification = env.getPlanningCoreSpecification()
                .given()
                .queryParam(PERSPECTIVE, planningContext.getPerspective());
        if (planningContext.isPlanContext()) {
            specification.queryParam(PROJECT_ID, planningContext.getProjectId());
        }
        specification
                .contentType(ContentType.JSON)
                .body(connectionDTOs)
                .when()
                .post(ROOTS_CONNECTION_API_PATH)
                .then()
                .statusCode(Response.Status.NO_CONTENT.getStatusCode()).assertThat();
    }

    public List<QueryResultDTO> queryObject(ObjectIdentifier objectIdentifier, PlanningContext planningContext,
                                            String attributes, String tag, String version,
                                            boolean withRemoved, boolean givenTypeOnly) {
        RequestSpecification specification = env.getPlanningCoreSpecification()
                .given()
                .queryParam(PERSPECTIVE, planningContext.getPerspective());
        if (planningContext.isPlanContext()) {
            specification.queryParam(PROJECT_ID, planningContext.getProjectId());
        }
        if (!attributes.isEmpty()) {
            specification.queryParam(ATTRIBUTES_QUERY_PARAM, attributes);
        }
        if (!tag.isEmpty()) {
            specification.queryParam(TAG_QUERY_PARAM, tag);
        }
        if (!version.isEmpty()) {
            specification.queryParam(VERSION_QUERY_PARAM, version);
        }
        specification
                .queryParam(WITH_REMOVED_QUERY_PARAM, withRemoved)
                .queryParam(GIVEN_TYPE_ONLY_QUERY_PARAM, givenTypeOnly);
        return Arrays.stream(specification.when()
                        .get(String.format(QUERY_OBJECT_PATH, objectIdentifier.getType(), objectIdentifier.getId()))
                        .then()
                        .log().body()
                        .extract()
                        .as(QueryResultDTO[].class))
                .collect(Collectors.toList());
    }

    public PlansResultDTO changeObjectsInPlan(Long projectId, ProjectObjectsDTO projectObjectsDTO,
                                              boolean preservePrerequisites, boolean validationsChecking) {
        return env.getPlanningCoreSpecification()
                .given()
                .queryParam(PRESERVE_PREREQUISITES_QUERY_PARAM, preservePrerequisites)
                .queryParam(VALIDATIONS_CHECKING_QUERY_PARAM, validationsChecking)
                .contentType(ContentType.JSON)
                .body(projectObjectsDTO)
                .when()
                .post(String.format(PROJECT_OBJECTS_PATH, projectId))
                .then()
                .statusCode(Response.Status.OK.getStatusCode()).assertThat()
                .extract()
                .as(PlansResultDTO.class);
    }

    public void savePrerequisites(List<RootPrerequisitesDTO> rootPrerequisitesDTOs, PlanningContext planningContext) {
        RequestSpecification specification = env.getPlanningCoreSpecification()
                .given()
                .queryParam(PERSPECTIVE, planningContext.getPerspective());
        if (planningContext.isPlanContext()) {
            specification.queryParam(PROJECT_ID, planningContext.getProjectId());
        }
        specification
                .contentType(ContentType.JSON)
                .body(rootPrerequisitesDTOs)
                .when()
                .post(ROOTS_PREREQUISITES_PATH)
                .then()
                .statusCode(Response.Status.NO_CONTENT.getStatusCode()).assertThat();
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

    public TypeDescriptionDTO getTypeDescription(String typeName) {
        return Arrays.stream(env.getPlanningCoreSpecification()
                .when()
                .get(TYPES_PATH + "/" + typeName)
                .then()
                .statusCode(Response.Status.OK.getStatusCode()).assertThat()
                .log().body()
                .extract()
                .as(TypeDescriptionDTO[].class)).findFirst().orElseThrow(() -> new IllegalArgumentException(INVALID_TYPE_NAME));
    }

    public LiveResultDTO changeObjectsInLive(List<PlannedObjectDTO> plannedObjectDTOs, String domainName, boolean validationsChecking) {
        RequestSpecification specification = env.getPlanningCoreSpecification()
                .given()
                .queryParam(VALIDATIONS_CHECKING_QUERY_PARAM, validationsChecking);
        if (!domainName.isEmpty()) {
            specification.queryParam(DOMAIN_NAME_QUERY_PARAM, domainName);
        }
        return specification
                .contentType(ContentType.JSON)
                .body(plannedObjectDTOs)
                .when()
                .post(OBJECTS_V2_PATH)
                .then()
                .statusCode(Response.Status.OK.getStatusCode()).assertThat()
                .extract()
                .as(LiveResultDTO.class);
    }

    public NetworkResultDTO changeObjectsInNetwork(List<NetworkDTO> networkDTOs, boolean preservePrerequisites) {
        return env.getPlanningCoreSpecification()
                .given()
                .queryParam(PRESERVE_PREREQUISITES_QUERY_PARAM, preservePrerequisites)
                .contentType(ContentType.JSON)
                .body(networkDTOs)
                .when()
                .post(NETWORK_API_PATH + "/v2")
                .then()
                .statusCode(Response.Status.OK.getStatusCode()).assertThat()
                .extract()
                .as(NetworkResultDTO.class);
    }

}
