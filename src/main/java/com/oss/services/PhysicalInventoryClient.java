package com.oss.services;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

import javax.ws.rs.core.Response;

import org.assertj.core.util.Lists;

import com.comarch.oss.physicalinventory.api.dto.CardDTO;
import com.comarch.oss.physicalinventory.api.dto.ChassisDTO;
import com.comarch.oss.physicalinventory.api.dto.DeviceSlotDTO;
import com.comarch.oss.physicalinventory.api.dto.PhysicalDeviceDTO;
import com.comarch.oss.physicalinventory.api.dto.PluggableModuleDTO;
import com.comarch.oss.physicalinventory.api.dto.PortBrowseDTO;
import com.comarch.oss.physicalinventory.api.dto.PortDTO;
import com.comarch.oss.physicalinventory.api.dto.ResourceDTO;
import com.comarch.oss.physicalinventory.api.dto.SearchResultDTO;
import com.comarch.oss.resourcehierarchy.api.dto.ResourceHierarchyDTO;
import com.jayway.restassured.http.ContentType;
import com.jayway.restassured.specification.RequestSpecification;
import com.oss.transport.infrastructure.planning.PlanningContext;
import com.oss.untils.Constants;
import com.oss.untils.Environment;

public class PhysicalInventoryClient {

    private static final String DEVICES_API_PATH = "/devices";
    private static final String DEVICE_STRUCTURE_API_PATH = "/devices/%s/devicestructurebyjpa";
    private static final String DEVICE_DELETE_API_PATH = "/devices/v2/%s";
    private static final String DEVICE_DELETE_V3_API_PATH = "/devices/v3";
    private static final String PROJECT_ID = "project_id";
    private static final String CHECK_COMPATIBILITY = "checkCompatibility";
    private static final String FALSE = "false";
    private static final String LOCATION = "location";
    private static final String QUERY = "query";
    private static final String DEVICES_PORTS_API_PATH = "/devices/%s/ports";
    private static final String PORTS_PLUGGABLE_API_PATH = "/ports/%s/pluggablemodule?invokeTPService=true&checkCompatibility=false";
    private static final String CHASSIS_STRUCTURE_API_PATH = "/chassis/%s/structure";
    private static final String CARDS_STRUCTURE_API_PATH = "/cards/%s/structure";
    private static final String CARDS_CREATE_API_PATH = "/cards/sync?checkCompatibility=true";
    private static final String RESOURCE_HIERARCHY_API_PATH = "/physical-resource/hierarchy/%s/%s?hierarchyMode=%s";
    private static final String DEVICE_GET_API_PATH = "/devices/%s";
    private static final String DEVICE_SLOTS_API_PATH = "/slots/deviceSlots/{id}";
    private static final String PORTS_V2_API_PATH = "/ports/v2/{id}";
    private static final String ID_PARAM = "id";

    private static PhysicalInventoryClient instance;
    private final Environment env;

    public PhysicalInventoryClient(Environment environment) {
        env = environment;
    }

    public static PhysicalInventoryClient getInstance(Environment pEnvironment) {
        if (instance != null) {
            return instance;
        }
        instance = new PhysicalInventoryClient(pEnvironment);
        return instance;
    }

    public ResourceDTO createDevice(PhysicalDeviceDTO device) {
        return env.getPhysicalInventoryCoreRequestSpecification()
                .given()
                .queryParam(Constants.PERSPECTIVE, Constants.LIVE)
                .contentType(ContentType.JSON)
                .body(device)
                .when()
                .post(DEVICES_API_PATH)
                .then()
                .statusCode(Response.Status.OK.getStatusCode())
                .extract()
                .as(ResourceDTO.class);
    }

    public void addPortToDevice(Long deviceId, PortDTO portDTO) {
        String devicesPortsPath = String.format(DEVICES_PORTS_API_PATH, deviceId);
        env.getPhysicalInventoryCoreRequestSpecification()
                .given()
                .contentType(ContentType.JSON)
                .body(Lists.newArrayList(portDTO))
                .when()
                .put(devicesPortsPath)
                .then()
                .statusCode(Response.Status.OK.getStatusCode());

    }

    public void addPluggableModuleToPort(Long portId, PluggableModuleDTO pluggableModuleDTO) {
        String portsPluggableModulePath = String.format(PORTS_PLUGGABLE_API_PATH, portId);
        env.getPhysicalInventoryCoreRequestSpecification()
                .given()
                .contentType(ContentType.JSON)
                .body(pluggableModuleDTO)
                .when()
                .put(portsPluggableModulePath)
                .then()
                .statusCode(Response.Status.OK.getStatusCode());
    }

    public void addCardToDevice(CardDTO cardDTO) {
        env.getPhysicalInventoryCoreRequestSpecification()
                .given()
                .contentType(ContentType.JSON)
                .body(cardDTO)
                .post(CARDS_CREATE_API_PATH)
                .then()
                .statusCode(Response.Status.OK.getStatusCode());
    }

    public void addCardToDevice(CardDTO cardDTO, long projectId) {
        env.getPhysicalInventoryCoreRequestSpecification()
                .given()
                .queryParam(Constants.PERSPECTIVE, Constants.PLAN)
                .queryParam(PROJECT_ID, projectId)
                .contentType(ContentType.JSON)
                .body(cardDTO)
                .post(CARDS_CREATE_API_PATH)
                .then()
                .statusCode(Response.Status.OK.getStatusCode());
    }

    public String getDevicePortId(Long deviceId, String portName) {
        return getDeviceStructure(deviceId)
                .getPorts()
                .stream()
                .filter(e -> (e.getName().equals(portName)))
                .map(e -> e.getId().get())
                .findAny()
                .orElseThrow(() -> new RuntimeException("Can't find port with name: " + portName + " on device with id: " + deviceId))
                .toString();
    }

    public Long getDeviceChassisId(Long deviceId, String chassisName) {
        return getDeviceStructure(deviceId)
                .getChassis()
                .stream()
                .filter(e -> (e.getName().equals(chassisName)))
                .map(e -> e.getId().get())
                .findAny()
                .orElseThrow(
                        () -> new RuntimeException("Can't find chassis with name: " + chassisName + " on device with id: " + deviceId));
    }

    public Long getDeviceChassisId(Long deviceId, String chassisName, long projectId) {
        return getDeviceStructure(deviceId, projectId)
                .getChassis()
                .stream()
                .filter(e -> (e.getName().equals(chassisName)))
                .map(e -> e.getId().get())
                .findAny()
                .orElseThrow(
                        () -> new RuntimeException("Can't find chassis with name: " + chassisName + " on device with id: " + deviceId));
    }

    public Long getDeviceCardUnderChassisId(Long chassisId, String cardName) {
        return getChassisStructure(chassisId)
                .getCards()
                .stream()
                .filter(e -> (e.getName().equals(Optional.of(cardName))))
                .map(e -> e.getId().get())
                .findAny()
                .orElseThrow(() -> new RuntimeException("Can't find card with name: " + cardName + " on chassis with id: " + chassisId));
    }

    public Long getDeviceCardUnderChassisId(Long chassisId, String cardName, long projectId) {
        return getChassisStructure(chassisId, projectId)
                .getCards()
                .stream()
                .filter(e -> (e.getName().equals(Optional.of(cardName))))
                .map(e -> e.getId().get())
                .findAny()
                .orElseThrow(() -> new RuntimeException("Can't find card with name: " + cardName + " on chassis with id: " + chassisId));
    }

    public Long getDevicePortUnderCardId(Long cardId, String portName) {
        return getCardStructure(cardId)
                .getPorts()
                .stream()
                .filter(e -> (e.getName().equals(portName)))
                .map(e -> e.getId().get())
                .findAny()
                .orElseThrow(() -> new RuntimeException("Can't find port with name: " + portName + " on card with id: " + cardId));
    }

    public Long getDevicePortUnderCardId(Long cardId, String portName, long projectId) {
        return getCardStructure(cardId, projectId)
                .getPorts()
                .stream()
                .filter(e -> (e.getName().equals(portName)))
                .map(e -> e.getId().get())
                .findAny()
                .orElseThrow(() -> new RuntimeException("Can't find port with name: " + portName + " on card with id: " + cardId));
    }

    public String getAntennaArrayId(Long antennaId, String arrayName) {
        return getDeviceStructure(antennaId)
                .getAntennaArrays()
                .stream().filter(e -> (e.getName().equals(arrayName)))
                .map(e -> e.getId().get())
                .findAny()
                .orElseThrow(() -> new RuntimeException("Can't find array with name: " + arrayName + " on antenna with id: " + antennaId))
                .toString();
    }

    public ResourceDTO createDevice(PhysicalDeviceDTO device, long projectId) {
        return env.getPhysicalInventoryCoreRequestSpecification()
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
        env.getPhysicalInventoryCoreRequestSpecification()
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

    public SearchResultDTO getDeviceId(String locationId, String queryVale) {
        return env.getPhysicalInventoryCoreRequestSpecification()
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
                .as(SearchResultDTO.class);
    }

    public void deleteDevice(String deviceId) {
        String devicePath = String.format(DEVICE_DELETE_API_PATH, deviceId);
        env.getPhysicalInventoryCoreRequestSpecification()
                .given()
                .queryParam(Constants.PERSPECTIVE, Constants.LIVE)
                .when()
                .delete(devicePath)
                .then()
                .statusCode(200).assertThat();

    }

    public void deleteDeviceV3(Collection<String> deviceIds,
                               PlanningContext context) {
        RequestSpecification requestSpecification = env.getPhysicalInventoryCoreRequestSpecification()
                .given()
                .queryParam("id", deviceIds)
                .queryParam("markUsedObjectsAsObsolete", false);
        if (context.isProjectContext()) {
            requestSpecification.queryParam(PlanningContext.PROJECT_PARAM, context.getProjectId());
        } else {
            requestSpecification.queryParam(PlanningContext.PERSPECTIVE_PARAM, context.getPerspective());
        }
        requestSpecification
                .when()
                .delete(DEVICE_DELETE_V3_API_PATH)
                .then()
                .log().body()
                .statusCode(200).assertThat();
    }

    public void deleteDevice(String deviceId, long projectId) {
        String devicePath = String.format(DEVICE_DELETE_API_PATH, deviceId);
        env.getPhysicalInventoryCoreRequestSpecification()
                .given()
                .queryParam(Constants.PERSPECTIVE, Constants.PLAN)
                .queryParam(PROJECT_ID, projectId)
                .when()
                .delete(devicePath)
                .then()
                .statusCode(200).assertThat();
    }

    private PhysicalDeviceDTO getDeviceStructure(Long deviceId) {
        String devicePath = String.format(DEVICE_STRUCTURE_API_PATH, deviceId);
        return env.getPhysicalInventoryCoreRequestSpecification()
                .given()
                .queryParam(Constants.PERSPECTIVE, Constants.LIVE)
                .when()
                .get(devicePath)
                .then()
                .log().body()
                .extract()
                .as(PhysicalDeviceDTO.class);
    }

    private PhysicalDeviceDTO getDeviceStructure(Long deviceId, long projectId) {
        String devicePath = String.format(DEVICE_STRUCTURE_API_PATH, deviceId);
        return env.getPhysicalInventoryCoreRequestSpecification()
                .given()
                .queryParam(Constants.PERSPECTIVE, Constants.PLAN)
                .queryParam(PROJECT_ID, projectId)
                .when()
                .get(devicePath)
                .then()
                .log().body()
                .extract()
                .as(PhysicalDeviceDTO.class);
    }

    public PhysicalDeviceDTO getDevice(String deviceId) {
        String devicePath = String.format(DEVICE_GET_API_PATH, deviceId);
        return env.getPhysicalInventoryCoreRequestSpecification()
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
        return env.getPhysicalInventoryCoreRequestSpecification()
                .given()
                .queryParam(Constants.PERSPECTIVE, Constants.LIVE)
                .when()
                .get(chassisStructurePath)
                .then()
                .log().body()
                .extract()
                .as(ChassisDTO.class);
    }

    private ChassisDTO getChassisStructure(Long chassisId, long projectId) {
        String chassisStructurePath = String.format(CHASSIS_STRUCTURE_API_PATH, chassisId);
        return env.getPhysicalInventoryCoreRequestSpecification()
                .given()
                .queryParam(Constants.PERSPECTIVE, Constants.PLAN)
                .queryParam(PROJECT_ID, projectId)
                .when()
                .get(chassisStructurePath)
                .then()
                .log().body()
                .extract()
                .as(ChassisDTO.class);
    }

    private CardDTO getCardStructure(Long cardId) {
        String cardStructurePath = String.format(CARDS_STRUCTURE_API_PATH, cardId);
        return env.getPhysicalInventoryCoreRequestSpecification()
                .given()
                .queryParam(Constants.PERSPECTIVE, Constants.LIVE)
                .when()
                .get(cardStructurePath)
                .then()
                .log().body()
                .extract()
                .as(CardDTO.class);
    }

    private CardDTO getCardStructure(Long cardId, long projectId) {
        String cardStructurePath = String.format(CARDS_STRUCTURE_API_PATH, cardId);
        return env.getPhysicalInventoryCoreRequestSpecification()
                .given()
                .queryParam(Constants.PERSPECTIVE, Constants.PLAN)
                .queryParam(PROJECT_ID, projectId)
                .when()
                .get(cardStructurePath)
                .then()
                .log().body()
                .extract()
                .as(CardDTO.class);
    }

    public ResourceHierarchyDTO getResourceHierarchy(String resourceType, String resourceId, String mode) {
        String resourceHierarchyPath = String.format(RESOURCE_HIERARCHY_API_PATH, resourceType, resourceId, mode);
        return env.getPhysicalInventoryCoreRequestSpecification()
                .given()
                .queryParam(Constants.PERSPECTIVE, Constants.LIVE)
                .when()
                .get(resourceHierarchyPath)
                .then()
                .log().body()
                .extract()
                .as(ResourceHierarchyDTO.class);
    }

    public List<DeviceSlotDTO> getDeviceSlots(String deviceId,
                                              PlanningContext context) {
        RequestSpecification requestSpecification = env.getPhysicalInventoryCoreRequestSpecification()
                .given()
                .pathParam(ID_PARAM, deviceId);
        if (context.isProjectContext()) {
            requestSpecification.queryParam(PlanningContext.PROJECT_PARAM, context.getProjectId());
        } else {
            requestSpecification.queryParam(PlanningContext.PERSPECTIVE_PARAM, context.getPerspective());
        }
        DeviceSlotDTO[] slots = requestSpecification
                .when()
                .get(DEVICE_SLOTS_API_PATH)
                .then()
                .log().body()
                .assertThat().statusCode(Response.Status.OK.getStatusCode())
                .extract().as(DeviceSlotDTO[].class);
        return Arrays.asList(slots);
    }

    public List<PortBrowseDTO> getPorts(Collection<String> portsIds,
                                 PlanningContext context) {
        String portsPath = String.join(",", portsIds);
        RequestSpecification requestSpecification = env.getPhysicalInventoryCoreRequestSpecification()
                .given()
                .pathParam(ID_PARAM, portsPath);
        if (context.isProjectContext()) {
            requestSpecification.queryParam(PlanningContext.PROJECT_PARAM, context.getProjectId());
        } else {
            requestSpecification.queryParam(PlanningContext.PERSPECTIVE_PARAM, context.getPerspective());
        }
        PortBrowseDTO[] ports = requestSpecification
                .when()
                .get(PORTS_V2_API_PATH)
                .then()
                .log().body()
                .assertThat().statusCode(Response.Status.OK.getStatusCode())
                .extract().as(PortBrowseDTO[].class);
        return Arrays.asList(ports);
    }

}
