package com.oss.services;

import com.comarch.oss.physicalinventory.api.dto.PhysicalDeviceDTO;
import com.comarch.oss.physicalinventory.api.dto.ResourceDTO;
import com.jayway.restassured.http.ContentType;
import com.oss.untils.Environment;

import javax.ws.rs.core.Response;

public class PhysicalInventoryClient {

    private static final String DEVICES_API_PATH = "/devices";
    private static LocationInventoryClient instance;
    private final Environment ENV;

    public PhysicalInventoryClient(Environment environment) {
        ENV = environment;
    }

    public static LocationInventoryClient getInstance(Environment pEnvironment) {
        if (instance != null) {
            return instance;
        }
        instance = new LocationInventoryClient(pEnvironment);
        return instance;
    }

    public ResourceDTO createDevice(PhysicalDeviceDTO device) {
        return ENV.getPhysicalInventoryCoreRequestSpecification()
                .given()
                .contentType(ContentType.JSON)
                .body(device)
                .when()
                .post(DEVICES_API_PATH)
                .then()
                .statusCode(Response.Status.OK.getStatusCode())
                .extract()
                .as(ResourceDTO.class);
    }
}
