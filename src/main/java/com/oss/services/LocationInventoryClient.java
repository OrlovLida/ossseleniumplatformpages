package com.oss.services;

import java.util.List;

import javax.ws.rs.core.Response;

import com.comarch.oss.locationinventory.api.dto.PhysicalLocationDTO;
import com.comarch.oss.locationinventory.api.dto.ResourceDTO;
import com.comarch.oss.locationinventory.api.dto.SublocationDTO;
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
    private static final String SUB_LOCATION_API_PATH = "/sublocations";

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

    public List<Integer> getPhysicalLocationByName(String locationName) {
        com.jayway.restassured.response.Response response = env.getLocationInventoryCoreRequestSpecification()
                .given()
                .queryParam(Constants.PERSPECTIVE, Constants.LIVE)
                .queryParam(Constants.NAME_PARAM, locationName)
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


    public Optional<String> getLocationId(String locationName){
        LocationInventoryClient client = new LocationInventoryClient(env);
        List<Integer> locationIds = client.getPhysicalLocationByName(locationName);
        return locationIds.stream().findFirst().map(Object::toString);
    }

    public void updateSubLocation(Long subLocationId,String subLocationType, String subLocationName, Long preciseLocation, String preciseLocationType,
                                  Long parentLocationId, String parentLocationType){
        LocationInventoryClient client = new LocationInventoryClient(env);
        SublocationDTO subLocation = SublocationDTO.builder()
                .location(getLocation(parentLocationId, parentLocationType))
                .preciseLocation(getLocation(preciseLocation, preciseLocationType))
                .name(subLocationName)
                .type(subLocationType)
                .id(subLocationId)
                .build();
        client.updateSubLocation(subLocation, subLocationId.toString());
    }

    public void deleteSubLocation(String ids){
        LocationInventoryClient client = new LocationInventoryClient(env);
        client.deleteSubLocation(ids);
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

}
