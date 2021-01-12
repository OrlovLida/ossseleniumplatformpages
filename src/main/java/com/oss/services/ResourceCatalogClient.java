package com.oss.services;

import com.oss.untils.Constants;
import com.oss.untils.Environment;

import java.util.List;

public class ResourceCatalogClient {

    private static final String MODELS_API_PATH = "/models";

    private static ResourceCatalogClient instance;
    private final Environment ENV;

    public ResourceCatalogClient(Environment environment) {
        ENV = environment;
    }

    public static ResourceCatalogClient getInstance(Environment pEnvironment) {
        if (instance != null) {
            return instance;
        }
        instance = new ResourceCatalogClient(pEnvironment);
        return instance;
    }

    public Long getModelIds(String modelName) {
        com.jayway.restassured.response.Response response = ENV.getResourceCatalogSpecification()
                .given()
                .queryParam(Constants.NAME_PARAM, modelName)
                .when()
                .get(ResourceCatalogClient.MODELS_API_PATH);
        List<Integer> idsList = response.jsonPath().getList("searchResult.id");
        Long id = Long.valueOf(idsList.get(0).toString());
        return id;
    }

}
