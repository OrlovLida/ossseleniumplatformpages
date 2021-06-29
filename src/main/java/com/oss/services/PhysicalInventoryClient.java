package com.oss.services;

import com.comarch.oss.physicalinventory.api.dto.CardDTO;
import com.comarch.oss.physicalinventory.api.dto.ChassisDTO;
import com.comarch.oss.physicalinventory.api.dto.PhysicalDeviceDTO;
import com.comarch.oss.physicalinventory.api.dto.PluggableModuleDTO;
import com.comarch.oss.physicalinventory.api.dto.PortDTO;
import com.comarch.oss.physicalinventory.api.dto.ResourceDTO;
import com.jayway.restassured.http.ContentType;
import com.oss.untils.Constants;
import com.oss.untils.Environment;
import org.assertj.core.util.Lists;

import javax.ws.rs.core.Response;
import java.util.Optional;

import static java.net.HttpURLConnection.HTTP_NO_CONTENT;

public class PhysicalInventoryClient {

    private static final String DEVICES_API_PATH = "/devices";
    private static final String DEVICE_STRUCTURE_API_PATH = "/devices/%s/devicestructurebyjpa";
    private static final String DEVICES_PORTS_API_PATH = "/devices/%s/ports";
    private static final String PORTS_PLUGGABLE_API_PATH = "/ports/%s/pluggablemodule?invokeTPService=true&checkCompatibility=false";
    private static final String CHASSIS_STRUCTURE_API_PATH = "/chassis/%s/structure";
    private static final String CARDS_STRUCTURE_API_PATH = "/cards/%s/structure";
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

    public ResourceDTO addPortToDevice(Long deviceId, PortDTO portDTO) {
        String devicesPortsPath = String.format(DEVICES_PORTS_API_PATH, deviceId);
        return ENV.getPhysicalInventoryCoreRequestSpecification()
                .given()
                .contentType(ContentType.JSON)
                .body(Lists.newArrayList(portDTO))
                .when()
                .put(devicesPortsPath)
                .then()
                .statusCode(Response.Status.OK.getStatusCode())
                .extract()
                .as(ResourceDTO.class);
    }

    public ResourceDTO addPluggableModuleToPort(Long portId, PluggableModuleDTO pluggableModuleDTO) {
        String portsPluggableModulePath = String.format(PORTS_PLUGGABLE_API_PATH, portId);
        return ENV.getPhysicalInventoryCoreRequestSpecification()
                .given()
                .contentType(ContentType.JSON)
                .body(pluggableModuleDTO)
                .when()
                .put(portsPluggableModulePath)
                .then()
                .statusCode(Response.Status.OK.getStatusCode())
                .extract()
                .as(ResourceDTO.class);
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

    private ChassisDTO getChassisStructure(Long chassisId) {
        String chassisStructurePath = String.format(CHASSIS_STRUCTURE_API_PATH, chassisId);
        return ENV.getPhysicalInventoryCoreRequestSpecification()
                .given()
                .queryParam(Constants.PERSPECTIVE, Constants.LIVE)
                .when()
                .get(chassisStructurePath)
                .then()
                .log().body()
                .extract()
                .as(ChassisDTO.class);
    }

    private CardDTO getCardStructure(Long cardId) {
        String cardStructurePath = String.format(CARDS_STRUCTURE_API_PATH, cardId);
        return ENV.getPhysicalInventoryCoreRequestSpecification()
                .given()
                .queryParam(Constants.PERSPECTIVE, Constants.LIVE)
                .when()
                .get(cardStructurePath)
                .then()
                .log().body()
                .extract()
                .as(CardDTO.class);
    }

    public com.jayway.restassured.response.Response removeDevice(Long deviceId) {
        return ENV.getPhysicalInventoryCoreRequestSpecification()
                .when()
                .delete(DEVICES_API_PATH + "/{id}", deviceId)
                .then()
                .log()
                .status()
                .log()
                .body()
                .statusCode(HTTP_NO_CONTENT)
                .extract()
                .response();
    }

    public String getDevicePortId(Long deviceId, String portName) {
        return getDeviceStructure(deviceId).getPorts().stream().filter(e -> (e.getName().equals(portName))).map(e -> e.getId().get()).findAny().get().toString();
    }

    public Long getDeviceChassisId(Long deviceId, String chassisName) {
        return getDeviceStructure(deviceId).getChassis().stream().filter(e -> (e.getName().equals(chassisName))).map(e -> e.getId().get()).findAny().get();
    }

    public Long getDeviceCardUnderChassisId(Long chassisId, String cardName) {
        return getChassisStructure(chassisId).getCards().stream().filter(e -> (e.getName().equals(Optional.of(cardName)))).map(e -> e.getId().get()).findAny().get();
    }

    public Long getDevicePortUnderCardId(Long cardId, String portName) {
        return getCardStructure(cardId).getPorts().stream().filter(e -> (e.getName().equals(portName))).map(e -> e.getId().get()).findAny().get();
    }

    public String getAntennaArrayId(Long antennaId, String arrayName) {
        return getDeviceStructure(antennaId).getAntennaArrays().stream().filter(e -> (e.getName().equals(arrayName))).map(e -> e.getId().get()).findAny().get().toString();
    }

}
