package com.oss.services;

import java.util.List;

import com.comarch.oss.locationinventory.api.dto.SearchDTO;
import com.comarch.oss.resourcecatalog.api.dto.ModelDTO;
import com.comarch.oss.resourcecatalog.api.dto.StructurePortDTO;
import com.comarch.oss.resourcecatalog.api.dto.TerminationPointsDTO;
import com.jayway.restassured.http.ContentType;
import com.oss.untils.Constants;
import com.oss.untils.Environment;

public class ResourceCatalogClient {

    private static final String MODELS_API_PATH = "/models";
    private static final String ADD_TERMINATION_POINTS_TO_PORT_MODEL_PATH = "/portmodel/{id}/terminationpoints";
    private static final String ADD_PORT_TO_DEVICE_MODEL_PATH = "/physicaldevicemodel/{id}/port";

    private static ResourceCatalogClient instance;
    private final Environment env;

    public ResourceCatalogClient(Environment environment) {
        env = environment;
    }

    public static ResourceCatalogClient getInstance(Environment pEnvironment) {
        if (instance != null) {
            return instance;
        }
        instance = new ResourceCatalogClient(pEnvironment);
        return instance;
    }

    public Long getModelIds(String modelName) {
        com.jayway.restassured.response.Response response = env.getResourceCatalogSpecification()
                .given()
                .queryParam(Constants.NAME_PARAM, modelName)
                .when()
                .get(ResourceCatalogClient.MODELS_API_PATH);
        List<Integer> idsList = response.jsonPath().getList("searchResult.id");
        return Long.valueOf(idsList.get(0).toString());
    }

    public Long createModel(ModelDTO modelDTO) {
        return env.getResourceCatalogSpecification()
                .given().contentType(ContentType.JSON).body(modelDTO)
                .when().post(MODELS_API_PATH)
                .then().log().status().log().body().contentType(ContentType.JSON)
                .extract().as(SearchDTO.class).getId();
    }

    public SearchDTO addTerminationPointsToPortModel(Long portModelId, TerminationPointsDTO dto) {
        return env.getResourceCatalogSpecification()
                .given().contentType(ContentType.JSON).body(dto)
                .when().put(ADD_TERMINATION_POINTS_TO_PORT_MODEL_PATH, portModelId)
                .then().log().status().log().body().contentType(ContentType.JSON)
                .extract().as(SearchDTO.class);
    }

    public SearchDTO addPortToDeviceModel(Long deviceModelId, StructurePortDTO dto) {
        return env.getResourceCatalogSpecification()
                .given().contentType(ContentType.JSON).body(dto)
                .when().put(ADD_PORT_TO_DEVICE_MODEL_PATH, deviceModelId)
                .then().log().status().log().body().contentType(ContentType.JSON)
                .extract().as(SearchDTO.class);
    }

}
