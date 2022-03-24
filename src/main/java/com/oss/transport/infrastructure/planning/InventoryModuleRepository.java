package com.oss.transport.infrastructure.planning;

import com.comarch.oss.planning.api.dto.ObjectIdDTO;
import com.comarch.oss.planning.api.dto.ObjectsDescriptionDTO;
import com.comarch.oss.planning.api.dto.PerformObjectResultDTO;
import com.comarch.oss.planning.api.dto.PlannedObjectDTO;
import com.jayway.restassured.http.ContentType;
import com.oss.transport.infrastructure.EnvironmentRequestClient;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public final class InventoryModuleRepository {

    private static final String INVENTORY_OBJECTS_V2_PATH = "objects/v2";
    private static final String NAME_QUERY_PARAM = "Name";
    private final EnvironmentRequestClient requestClient;

    public InventoryModuleRepository(EnvironmentRequestClient requestClient) {
        this.requestClient = requestClient;
    }

    public ObjectsDescriptionDTO queryObjectsByInventoryModule(Map<String, String> queryParams, String pObjectType) {
        return requestClient.getPlanningCoreRequestSpecification()
                .given().contentType("application/json").queryParams(queryParams)
                .when().get(INVENTORY_OBJECTS_V2_PATH + "/" + pObjectType)
                .then().log().status().log().body()
                .contentType(ContentType.JSON).extract().as(ObjectsDescriptionDTO.class);
    }

    public Long getExistingObjectId(String pObjectName, String objectType, PlanningContext context) {
        Map<String, String> queryParams = new HashMap<>();
        queryParams.put("query", NAME_QUERY_PARAM + "=='" + pObjectName + "'");
        queryParams.put(context.getQueryParamName(), context.getQueryParamValue());

        ObjectsDescriptionDTO queryObjects = queryObjectsByInventoryModule(queryParams, objectType);
        return getObjectOrThrowException(queryObjects, "Cannot find Object with Name: " + pObjectName);
    }

    public Optional<ObjectIdDTO> getObjectIdByName(String pObjectName, String pObjectType) {
        ObjectsDescriptionDTO queryObjects = getQueryObjectsByAttribute(pObjectType, pObjectName);
        return queryObjects.getObjectIds().stream().findFirst();
    }

    private ObjectsDescriptionDTO getQueryObjectsByAttribute(String pObjectType, String attributeValue) {
        return requestClient.getPlanningCoreRequestSpecification()
                .given().contentType("application/json")
                .queryParam("query", NAME_QUERY_PARAM + "==\"" + attributeValue + "\"")
                .when().get(INVENTORY_OBJECTS_V2_PATH + "/" + pObjectType)
                .then().log().status().log().body()
                .contentType(ContentType.JSON).extract().as(ObjectsDescriptionDTO.class);
    }

    public Optional<Long> getObjectIdByName(String pObjectName, String pObjectType, PlanningContext context) {
        Map<String, String> queryParams = new HashMap<>();
        queryParams.put("query", NAME_QUERY_PARAM + "=='" + pObjectName + "'");
        queryParams.put(context.getQueryParamName(), context.getQueryParamValue());

        ObjectsDescriptionDTO queryObjects = queryObjectsByInventoryModule(queryParams, pObjectType);
        return queryObjects.getObjectIds().stream()
                .findFirst()
                .map(ObjectIdDTO::getId);
    }

    public List<PerformObjectResultDTO> performObjectInLive(List<PlannedObjectDTO> objects) {
        PerformObjectResultDTO[] results = requestClient.getPlanningCoreRequestSpecification()
                .given().contentType("application/json").body(objects)
                .when().post("objects")
                .then()
                .log().status().log().body()
                .contentType(ContentType.JSON).extract().as(PerformObjectResultDTO[].class);
        return Arrays.asList(results);
    }

    private Long getObjectOrThrowException(ObjectsDescriptionDTO pQueryObjects, String pExeptionMessage) {
        return pQueryObjects.getObjectIds().stream()
                .findFirst()
                .orElseThrow(() -> new IllegalStateException(pExeptionMessage))
                .getId();
    }
}
