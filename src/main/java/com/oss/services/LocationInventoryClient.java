package com.oss.services;

import java.util.List;

import javax.ws.rs.core.Response;

import com.comarch.oss.locationinventory.api.dto.PhysicalLocationDTO;
import com.comarch.oss.locationinventory.api.dto.ResourceDTO;
import com.comarch.oss.locationinventory.api.dto.SearchResultDTO;
import com.comarch.oss.locationinventory.api.dto.SublocationDTO;
import com.jayway.restassured.http.ContentType;
import com.oss.untils.Constants;
import com.oss.untils.Environment;

import static java.net.HttpURLConnection.HTTP_NO_CONTENT;

/**
 * @author Milena Miętkiewicz
 */

public class LocationInventoryClient {

    private static final String PHYSICAL_LOCATIONS_API_PATH = "/physicallocations";
    private static final String SUB_LOCATION_API_PATH = "/sublocations";
    private static final String PROJECT_ID = "project_id";

    private static LocationInventoryClient instance;
    private final Environment env;

    public LocationInventoryClient(Environment environment) {
        env = environment;
    }

    public static LocationInventoryClient getInstance(Environment pEnvironment) {
        if (instance != null) {
            return instance;
        }
        instance = new LocationInventoryClient(pEnvironment);
        return instance;
    }

    public ResourceDTO createPhysicalLocation(PhysicalLocationDTO location) {
        return env.getLocationInventoryCoreRequestSpecification()
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

    public ResourceDTO createPhysicalLocation(PhysicalLocationDTO location, long projectId) {
        return env.getLocationInventoryCoreRequestSpecification()
                .given()
                .queryParam(Constants.PERSPECTIVE, Constants.PLAN)
                .queryParam(PROJECT_ID, projectId)
                .contentType(ContentType.JSON)
                .body(location)
                .when()
                .post(PHYSICAL_LOCATIONS_API_PATH)
                .then()
                .statusCode(Response.Status.OK.getStatusCode()).assertThat()
                .extract()
                .as(ResourceDTO.class);
    }

    public ResourceDTO updateLocation(PhysicalLocationDTO location, String locationId, long projectId) {
        return env.getLocationInventoryCoreRequestSpecification()
                .given()
                .queryParam(Constants.PERSPECTIVE, Constants.PLAN)
                .queryParam(PROJECT_ID, projectId)
                .contentType(ContentType.JSON)
                .body(location)
                .when()
                .put(PHYSICAL_LOCATIONS_API_PATH + "/" + locationId)
                .then()
                .statusCode(Response.Status.OK.getStatusCode()).assertThat()
                .extract()
                .as(ResourceDTO.class);
    }

    public List<Integer> getPhysicalLocationByName(String locationName) {
        com.jayway.restassured.response.Response response = env.getLocationInventoryCoreRequestSpecification()
                .given()
                .queryParam(Constants.PERSPECTIVE, Constants.LIVE)
                .queryParam(Constants.NAME_PARAM, locationName)
                .when()
                .get(LocationInventoryClient.PHYSICAL_LOCATIONS_API_PATH);
        return response.jsonPath().getList("searchResult.id");
    }

    public List<Integer> getPhysicalLocation() {
        com.jayway.restassured.response.Response response = env.getLocationInventoryCoreRequestSpecification()
                .given()
                .queryParam(Constants.PERSPECTIVE, Constants.LIVE)
                .when()
                .get(LocationInventoryClient.PHYSICAL_LOCATIONS_API_PATH);
        return response.jsonPath().getList("searchResult.id");
    }

    public ResourceDTO createSubLocation(SublocationDTO subLocation) {
        return env.getLocationInventoryCoreRequestSpecification()
                .given()
                .queryParam(Constants.PERSPECTIVE, Constants.LIVE)
                .contentType(ContentType.JSON)
                .body(subLocation)
                .when()
                .post(SUB_LOCATION_API_PATH)
                .then()
                .statusCode(Response.Status.OK.getStatusCode()).assertThat()
                .extract()
                .as(ResourceDTO.class);
    }

    public ResourceDTO createSubLocation(SublocationDTO subLocation, long projectId) {
        return env.getLocationInventoryCoreRequestSpecification()
                .given()
                .queryParam(Constants.PERSPECTIVE, Constants.PLAN)
                .queryParam(PROJECT_ID, projectId)
                .contentType(ContentType.JSON)
                .body(subLocation)
                .when()
                .post(SUB_LOCATION_API_PATH)
                .then()
                .statusCode(Response.Status.OK.getStatusCode()).assertThat()
                .extract()
                .as(ResourceDTO.class);
    }

    public void updateSubLocation(SublocationDTO subLocation, String id) {
        env.getLocationInventoryCoreRequestSpecification()
                .given()
                .queryParam(Constants.PERSPECTIVE, Constants.LIVE)
                .contentType(ContentType.JSON)
                .body(subLocation)
                .when()
                .put(SUB_LOCATION_API_PATH + "/" + id)
                .then()
                .statusCode(Response.Status.OK.getStatusCode()).assertThat();
    }

    public void deleteSubLocation(String ids) {
        env.getLocationInventoryCoreRequestSpecification()
                .given()
                .queryParam(Constants.PERSPECTIVE, Constants.LIVE)
                .queryParam("ids", ids)
                .when()
                .delete(SUB_LOCATION_API_PATH + "/" + ids)
                .then()
                .statusCode(Response.Status.NO_CONTENT.getStatusCode()).assertThat();
    }

    public com.jayway.restassured.response.Response removeLocation(Long locationId, String locationType) {
        return env.getLocationInventoryCoreRequestSpecification()
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

    /**
     * @deprecated duplicated method (will be removed 3.0.x release), use public void removeLocation(Long locationId, String locationType)
     */
    @Deprecated
    public void deleteLocation(String locationId, String locationType) {
        env.getLocationInventoryCoreRequestSpecification()
                .given()
                .queryParam(Constants.PERSPECTIVE, Constants.LIVE)
                .when()
                .delete(PHYSICAL_LOCATIONS_API_PATH + "/" + locationType + "/" + locationId)
                .then()
                .statusCode(Response.Status.NO_CONTENT.getStatusCode()).assertThat();
    }

    public SearchResultDTO getSublocationId(String locationId, String query) {
        return env.getLocationInventoryCoreRequestSpecification()
                .given()
                .queryParam(Constants.PERSPECTIVE, Constants.LIVE)
                .queryParam("location", locationId)
                .queryParam("query", query)
                .contentType(ContentType.JSON)
                .when()
                .get(SUB_LOCATION_API_PATH)
                .then()
                .statusCode(Response.Status.OK.getStatusCode()).assertThat()
                .extract()
                .as(SearchResultDTO.class);
    }
}
