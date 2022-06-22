package com.oss.transport.infrastructure.device;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

import javax.ws.rs.core.Response.Status;

import com.comarch.oss.physicalinventory.api.dto.CardDTO;
import com.comarch.oss.physicalinventory.api.dto.PhysicalDeviceBrowseDTO;
import com.comarch.oss.physicalinventory.api.dto.PhysicalDeviceDTO;
import com.comarch.oss.physicalinventory.api.dto.ResourceDTO;
import com.jayway.restassured.http.ContentType;
import com.jayway.restassured.response.ValidatableResponse;
import com.oss.transport.infrastructure.EnvironmentRequestClient;
import com.oss.transport.infrastructure.planning.PlanningContext;

public class DeviceClient {

    public static final String MISSING_PHYSICAL_RESOURCE_DTO_EXCEPTION = "Missing PhysicalResourceDTO";
    private final EnvironmentRequestClient requestClient;

    public DeviceClient(EnvironmentRequestClient requestClient) {
        this.requestClient = requestClient;
    }

    public Long createDeviceAndReturnId(PhysicalDeviceDTO dto, PlanningContext context) {
        ResourceDTO resourceDTO = create(dto, context);
        PhysicalDeviceBrowseDTO deviceBrowserDto = requestClient.prepareRequestSpecificationWithoutUri()
                .given()
                .when().get(resourceDTO.getUri())
                .then().contentType(ContentType.JSON).extract().as(PhysicalDeviceBrowseDTO[].class)[0];
        return deviceBrowserDto.getId().orElseThrow(() -> new IllegalStateException("Created Device without ID"));
    }

    public PhysicalDeviceDTO getDeviceStructure(Long id, PlanningContext context) {
        return getDeviceRaw(id, context).statusCode(Status.OK.getStatusCode())
                .contentType(ContentType.JSON).extract().as(PhysicalDeviceDTO.class);
    }

    public List<PhysicalDeviceBrowseDTO> getDeviceBrowse(Collection<Long> ids, PlanningContext context) {
        PhysicalDeviceBrowseDTO[] results = getDeviceBrowseRaw(ids, context).statusCode(Status.OK.getStatusCode())
                .contentType(ContentType.JSON).extract().as(PhysicalDeviceBrowseDTO[].class);
        return Arrays.asList(results);
    }

    public Long createCard(CardDTO dto, PlanningContext context) {
        return createRaw(dto, context).statusCode(Status.OK.getStatusCode())
                .contentType(ContentType.JSON).extract().as(ResourceDTO.class)
                .getChangedResources().orElseThrow(() -> new IllegalStateException(MISSING_PHYSICAL_RESOURCE_DTO_EXCEPTION)).getCreated().get(0).getId();
    }

    public void remove(Long deviceId, PlanningContext context) {
        removeRaw(deviceId, context);
    }

    private ResourceDTO create(PhysicalDeviceDTO dto, PlanningContext context) {
        return createRaw(dto, context).statusCode(Status.OK.getStatusCode())
                .contentType(ContentType.JSON).extract().as(ResourceDTO.class);
    }

    private ValidatableResponse createRaw(PhysicalDeviceDTO dto, PlanningContext context) {
        return requestClient.getPhysicalInventoryCoreRequestSpecification()
                .given().contentType(ContentType.JSON).body(dto)
                .when().post(getDevicePath(context))
                .then().log().status().log().body();
    }

    private String getDevicePath(PlanningContext context) {
        return "/devices?" + context.getQueryParamName() + "=" + context.getQueryParamValue();
    }

    private ValidatableResponse getDeviceRaw(Long id, PlanningContext context) {
        return requestClient.getPhysicalInventoryCoreRequestSpecification()
                .given().contentType(ContentType.JSON)
                .when().get(getDeviceStructurePath(id, context))
                .then().log().status().log().body();
    }

    private String getDeviceStructurePath(Long deviceId, PlanningContext context) {
        return String.format("/devices/%s/devicestructurebyjpa?%s=%s", deviceId.toString(), context.getQueryParamName(),
                context.getQueryParamValue());
    }

    private ValidatableResponse getDeviceBrowseRaw(Collection<Long> deviceIds, PlanningContext context) {
        return requestClient.getPhysicalInventoryCoreRequestSpecification()
                .given().contentType(ContentType.JSON)
                .when().get(getDeviceBrowsePath(deviceIds, context))
                .then().log().status().log().body();
    }

    private String getDeviceBrowsePath(Collection<Long> deviceIds, PlanningContext context) {
        String ids = deviceIds.stream().map(String::valueOf).collect(Collectors.joining(","));
        return String.format("/devices/%s?%s=%s", ids, context.getQueryParamName(), context.getQueryParamValue());
    }

    private ValidatableResponse createRaw(CardDTO dto, PlanningContext context) {
        return requestClient.getPhysicalInventoryCoreRequestSpecification()
                .given().contentType(ContentType.JSON).body(dto)
                .when().post(getCardPath(context))
                .then().log().status().log().body();
    }

    private String getCardPath(PlanningContext context) {
        return "/cards/sync?" + context.getQueryParamName() + "=" + context.getQueryParamValue();
    }

    private ValidatableResponse removeRaw(Long deviceId, PlanningContext context) {
        return requestClient.getPhysicalInventoryCoreRequestSpecification()
                .given().contentType(ContentType.JSON)
                .when().delete(getDeviceRemovePath(deviceId, context))
                .then().log().status().log().body();
    }

    private String getDeviceRemovePath(Long deviceId, PlanningContext context) {
        return String.format("/devices/%s?%s", deviceId, context.getQueryParamName() + "=" + context.getQueryParamValue());
    }
}
