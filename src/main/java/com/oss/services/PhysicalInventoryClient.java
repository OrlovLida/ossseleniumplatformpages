package com.oss.services;

import com.comarch.oss.physicalinventory.api.dto.PhysicalDeviceDTO;
import com.comarch.oss.physicalinventory.api.dto.ResourceDTO;
import com.jayway.restassured.http.ContentType;
import com.oss.untils.Constants;
import com.oss.untils.Environment;

import javax.ws.rs.core.Response;

public class PhysicalInventoryClient {

    private static final String DEVICES_API_PATH = "/devices";
    private static final String DEVICE_STRUCTURE_API_PATH = "/devices/%s/devicestructurebyjpa";
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

    private PhysicalDeviceDTO getDeviceStructure(Long deviceId){
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

    public String getDevicePortId(Long deviceId, String portName){
        return getDeviceStructure(deviceId).getPorts().stream().filter(e-> (e.getName().equals(portName))).map(e -> e.getId().get()).findAny().get().toString();
    }

    public String getAntennaArrayId(Long antennaId, String arrayName){
        return getDeviceStructure(antennaId).getAntennaArrays().stream().filter(e-> (e.getName().equals(arrayName))).map(e -> e.getId().get()).findAny().get().toString();
    }

}
