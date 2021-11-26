package com.oss.services;

import java.util.List;

import javax.ws.rs.core.Response;

import com.comarch.oss.locationinventory.api.dto.PhysicalLocationDTO;
import com.comarch.oss.locationinventory.api.dto.ResourceDTO;
import com.comarch.oss.locationinventory.api.dto.SublocationDTO;
import com.jayway.restassured.http.ContentType;
import com.oss.untils.Constants;
import com.oss.untils.Environment;

/**
 * @author Milena Miętkiewicz
 */

public class LocationInventoryClient {
    
    private static final String PHYSICAL_LOCATIONS_API_PATH = "/physicallocations";
    private static final String SUB_LOCATION_API_PATH = "/sublocations";
    
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
        List<Integer> idsList = response.jsonPath().getList("searchResult.id");
        return idsList;
    }
    
    public ResourceDTO createSubLocation(SublocationDTO subLocation) {
        return ENV.getLocationInventoryCoreRequestSpecification()
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

    public void updateSubLocation(SublocationDTO subLocation, String id){
        ENV.getLocationInventoryCoreRequestSpecification()
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
        ENV.getLocationInventoryCoreRequestSpecification()
                .given()
                .queryParam(Constants.PERSPECTIVE, Constants.LIVE)
                .queryParam("ids", ids)
                .when()
                .delete(SUB_LOCATION_API_PATH + "/" + ids)
                .then()
                .statusCode(Response.Status.NO_CONTENT.getStatusCode()).assertThat();
    }
    
}
