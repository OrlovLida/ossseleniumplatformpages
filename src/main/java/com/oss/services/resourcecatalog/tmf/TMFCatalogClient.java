package com.oss.services.resourcecatalog.tmf;

import com.comarch.oss.resourcecatalog.tmf.api.dto.ResourceSpecificationDTO;
import com.jayway.restassured.http.ContentType;
import com.jayway.restassured.response.Response;
import com.oss.untils.Environment;

import java.util.Optional;

public class TMFCatalogClient {

    private static final String RESOURCE_SPECIFICATION_PATH = "resourceSpecification/";
    public static final String CASCADE_PARAM = "cascade";

    private static TMFCatalogClient instance;
    private final Environment ENV;

    private TMFCatalogClient(Environment environment) {
        ENV = environment;
    }

    public static TMFCatalogClient getInstance(Environment environment) {
        if (instance == null) {
            instance = new TMFCatalogClient(environment);
        }
        return instance;
    }

    public Optional<ResourceSpecificationDTO> getResourceSpecification(String specificationIdentifier) {
        Response response = ENV.getTMFResourceCatalog()
                .when().get(RESOURCE_SPECIFICATION_PATH + specificationIdentifier)
                .then().log().status().log().body()
                .extract().response();
        if (response.getStatusCode() == javax.ws.rs.core.Response.Status.NOT_FOUND.getStatusCode()) {
            return Optional.empty();
        }
        return Optional.of(response.as(ResourceSpecificationDTO.class));
    }

    public void deleteResourceSpecification(String specificationIdentifier, boolean cascade) {
        ENV.getTMFResourceCatalog()
                .when()
                .queryParam(CASCADE_PARAM, cascade)
                .delete(RESOURCE_SPECIFICATION_PATH + specificationIdentifier)
                .then().log().status().log().body();
    }

}
