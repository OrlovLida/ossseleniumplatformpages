package com.oss.services;

import com.comarch.oss.locationinventory.api.dto.PhysicalLocationDTO;
import com.comarch.oss.locationinventory.api.dto.ResourceDTO;
import com.jayway.restassured.http.ContentType;
import com.oss.untils.Constants;
import com.oss.untils.Environment;

import javax.ws.rs.core.Response;
import java.util.List;

/**
 * @author Milena Miętkiewicz
 */

public class LocationInventoryClient {

    private static final String PHYSICAL_LOCATIONS_API_PATH = "/physicallocations";
    public static final String PHYSICAL_LOCATIONS_IDS_API_PATH = "/physicallocations/{ids}";
    private static LocationInventoryClient instance;
    private final Environment ENV;

    public LocationInventoryClient(Environment environment) {
        ENV = environment;
    }

    public static LocationInventoryClient getInstance(Environment pEnvironment) {
        if (instance != null) {
            return instance;
        }
        instance = new LocationInventoryClient(pEnvironment);
        return instance;
    }

    public ResourceDTO createPhysicalLocation(PhysicalLocationDTO location) {
        return ENV.getLocationInventoryCoreRequestSpecification()
                .given()
                .contentType(ContentType.JSON)
                .body(location)
                .when()
                .post(PHYSICAL_LOCATIONS_API_PATH)
                .then()
                .statusCode(Response.Status.OK.getStatusCode()).assertThat()
                .extract()
                .as(ResourceDTO.class);
    }

    public List<String> getPhysicalLocationName(String existingLocationId) {
        com.jayway.restassured.response.Response response = ENV.getLocationInventoryCoreRequestSpecification()
                .given()
                .pathParam(Constants.IDS, (existingLocationId))
                .queryParam(Constants.PERSPECTIVE, Constants.LIVE)
                .when()
                .get(LocationInventoryClient.PHYSICAL_LOCATIONS_IDS_API_PATH);
        List<String> nameList = response.jsonPath().getList("name");
        return nameList;
    }

}