package com.oss.services;

import javax.ws.rs.core.Response;

import com.comarch.oss.physicalinventory.api.dto.PhysicalDeviceDTO;
import com.comarch.oss.physicalinventory.api.dto.ResourceDTO;
import com.comarch.oss.physicalinventory.api.dto.SearchDTO;
import com.jayway.restassured.http.ContentType;
import com.oss.untils.Constants;
import com.oss.untils.Environment;

public class PhysicalInventoryClient {
    
    private static final String DEVICES_API_PATH = "/devices";
    private static final String DEVICE_STRUCTURE_API_PATH = "/devices/%s/devicestructurebyjpa";
    private static final String DEVICE_DELETE_API_PATH = "/devices/v2/%s";
    private static final String PROJECT_ID = "project_id";
    private static final String ID = "id";
    private static final String CHECK_COMPATIBILITY = "checkCompatibility";
    private static final String FALSE = "false";
    private static final String LOCATION = "location";
    private static final String QUERY = "query";
    private static PhysicalInventoryClient instance;
    private final Environment ENV;
    
    public PhysicalInventoryClient(Environment environment) {
        ENV = environment;
    }
    
    public static PhysicalInventoryClient getInstance(Environment pEnvironment) {
        if (instance != null) {
            return instance;
        }
        instance = new PhysicalInventoryClient(pEnvironment);
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
    
    public ResourceDTO createDevice(PhysicalDeviceDTO device, long projectId) {
        return ENV.getPhysicalInventoryCoreRequestSpecification()
                .given()
                .queryParam(Constants.PERSPECTIVE, Constants.PLAN)
                .queryParam(PROJECT_ID, projectId)
                .contentType(ContentType.JSON)
                .body(device)
                .when()
                .post(DEVICES_API_PATH)
                .then()
                .statusCode(Response.Status.OK.getStatusCode())
                .extract()
                .as(ResourceDTO.class);
    }
    
    public void updateDevice(PhysicalDeviceDTO deviceDTO, long deviceId, long projectId) {
        ENV.getPhysicalInventoryCoreRequestSpecification()
                .given()
                .queryParam(Constants.PERSPECTIVE, Constants.PLAN)
                .queryParam(PROJECT_ID, projectId)
                .queryParam(CHECK_COMPATIBILITY, FALSE)
                .contentType(ContentType.JSON)
                .body(deviceDTO)
                .when()
                .put(DEVICES_API_PATH + "/" + deviceId)
                .then()
                .statusCode(Response.Status.OK.getStatusCode()).assertThat();
    }
    
    public SearchDTO getDeviceId(String locationId, String queryVale) {
       return ENV.getPhysicalInventoryCoreRequestSpecification()
                .given()
                .queryParam(Constants.PERSPECTIVE, Constants.LIVE)
                .queryParam(LOCATION, locationId)
                .queryParam(QUERY, queryVale)
                .contentType(ContentType.JSON)
                .when()
                .get(DEVICES_API_PATH)
                .then()
                .statusCode(Response.Status.OK.getStatusCode()).assertThat()
                .extract()
                .as(SearchDTO.class);
    }
    
    private PhysicalDeviceDTO getDeviceStructure(Long deviceId) {
        String devicePath = String.format(DEVICE_STRUCTURE_API_PATH, deviceId);
        return ENV.getPhysicalInventoryCoreRequestSpecification()
                .given()
                .queryParam(Constants.PERSPECTIVE, Constants.LIVE)
                .when()
                .get(devicePath)
                .then()
                .log().body()
                .extract()
                .as(PhysicalDeviceDTO.class);
    }
    
    public String getDevicePortId(Long deviceId, String portName) {
        return getDeviceStructure(deviceId).getPorts().stream().filter(e -> (e.getName().equals(portName))).map(e -> e.getId().get())
                .findAny().get().toString();
    }
    
    public String getAntennaArrayId(Long antennaId, String arrayName) {
        return getDeviceStructure(antennaId).getAntennaArrays().stream().filter(e -> (e.getName().equals(arrayName)))
                .map(e -> e.getId().get()).findAny().get().toString();
    }
    
    public void deleteDevice(String deviceId) {
        String devicePath = String.format(DEVICE_DELETE_API_PATH, deviceId);
        ENV.getPhysicalInventoryCoreRequestSpecification()
                .given()
                .queryParam(Constants.PERSPECTIVE, Constants.LIVE)
                .when()
                .delete(devicePath)
                .then()
                .statusCode(200).assertThat();
        
    }
    
}
