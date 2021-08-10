package com.oss.services;

import com.comarch.oss.locationinventory.api.dto.PhysicalLocationDTO;
import com.comarch.oss.locationinventory.api.dto.ResourceDTO;
import com.jayway.restassured.http.ContentType;
import com.oss.untils.Constants;
import com.oss.untils.Environment;

import javax.ws.rs.core.Response;
import java.util.List;

import static java.net.HttpURLConnection.HTTP_NO_CONTENT;

/**
 * @author Milena Miętkiewicz
 */

public class LocationInventoryClient {

    private static final String PHYSICAL_LOCATIONS_API_PATH = "/physicallocations";

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

    public List<Integer> getPhysicalLocationByName(String locationName) {
        com.jayway.restassured.response.Response response = ENV.getLocationInventoryCoreRequestSpecification()
                .given()
                .queryParam(Constants.PERSPECTIVE, Constants.LIVE)
                .queryParam(Constants.NAME_PARAM, locationName)
                .when()
                .get(LocationInventoryClient.PHYSICAL_LOCATIONS_API_PATH);
        return response.jsonPath().getList("searchResult.id");
    }

    public com.jayway.restassured.response.Response removeLocation(Long locationId, String locationType) {
        return ENV.getLocationInventoryCoreRequestSpecification()
                .when()
                .delete(PHYSICAL_LOCATIONS_API_PATH + "/" + locationType + "/" + locationId)
                .then()
                .log()
                .status()
                .log()
                .body()
                .statusCode(HTTP_NO_CONTENT)
                .extract()
                .response();
    }

}