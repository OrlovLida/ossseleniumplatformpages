package com.oss.transport.infrastructure.resource.catalog.control;

import com.comarch.oss.resourcecatalog.api.dto.CompatibilityDTO;
import com.comarch.oss.resourcecatalog.api.dto.DeviceModelStructureDTO;
import com.comarch.oss.resourcecatalog.api.dto.LogicalCrossconnectDTO;
import com.comarch.oss.resourcecatalog.api.dto.ManufacturerJPADTO;
import com.comarch.oss.resourcecatalog.api.dto.ModelDTO;
import com.comarch.oss.resourcecatalog.api.dto.ModelSearchResultDTO;
import com.comarch.oss.resourcecatalog.api.dto.PhysicalCrossconnectDTO;
import com.comarch.oss.resourcecatalog.api.dto.PhysicalDeviceModelEntityDTO;
import com.comarch.oss.resourcecatalog.api.dto.PortLogicalCrossconnectDTO;
import com.comarch.oss.resourcecatalog.api.dto.PortPhysicalCrossconnectDTO;
import com.comarch.oss.resourcecatalog.api.dto.SearchDTO;
import com.comarch.oss.resourcecatalog.api.dto.StructureChassisDTO;
import com.comarch.oss.resourcecatalog.api.dto.StructurePluggableModuleSlotModelDTO;
import com.comarch.oss.resourcecatalog.api.dto.StructurePortDTO;
import com.comarch.oss.resourcecatalog.api.dto.StructureSlotDTO;
import com.comarch.oss.resourcecatalog.api.dto.TerminationPointsDTO;
import com.comarch.oss.resourcecatalog.logical.function.model.api.dto.LogicalFunctionModelDTO;
import com.comarch.oss.resourcecatalog.logical.function.model.api.dto.LogicalFunctionModelResponseDTO;
import com.comarch.oss.resourcecatalog.logical.function.model.api.dto.LogicalFunctionModelViewDTO;
import com.jayway.restassured.http.ContentType;
import com.jayway.restassured.response.ValidatableResponse;
import com.oss.serviceClient.EnvironmentRequestClient;

import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriBuilder;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

public class ResourceCatalogClient {

    private static final String TYPE_PARAM = "type";
    private static final String IDENTIFIER_PARAM = "identifier";
    private static final String MANUFACTURER_NAME_PARAM = "manufacturerName";

    private static ResourceCatalogClient instance;
    private final EnvironmentRequestClient requestClient;

    private ResourceCatalogClient(EnvironmentRequestClient requestClient) {
        this.requestClient = requestClient;
    }

    public static ResourceCatalogClient getInstance(EnvironmentRequestClient requestClient) {
        if (instance != null) {
            return instance;
        }
        instance = new ResourceCatalogClient(requestClient);
        return instance;
    }

    public List<SearchDTO> getModels(String name, String manufacturerName, String type) {
        String query = String.format("Name=='%s';MasterManufacturer.Name=='%s'", name, manufacturerName);
        ModelSearchResultDTO models = requestClient.getResourceCatalogRequestSpecification()
                .given().contentType(ContentType.JSON)
                .when()
                .queryParam("model_type", type)
                .queryParam("query", query)
                .get("/models")
                .then().log().status().log()
                .body().contentType(ContentType.JSON).extract().as(ModelSearchResultDTO.class);
        return models.getSearchResult();
    }

    public SearchDTO createModel(ModelDTO dto) {
        return requestClient.getResourceCatalogRequestSpecification()
                .given().contentType(ContentType.JSON).body(dto)
                .when().post("/models").then().log().status().log()
                .body().contentType(ContentType.JSON).extract().as(SearchDTO.class);
    }

    public SearchDTO addPortToDevice(Long id, StructurePortDTO dto) {
        return requestClient.getResourceCatalogRequestSpecification()
                .given().contentType(ContentType.JSON).body(dto)
                .when().put("/physicaldevicemodel/{id}/port", id).then().log().status().log()
                .body().contentType(ContentType.JSON).extract().as(SearchDTO.class);
    }

    public SearchDTO addPortToCard(Long id, StructurePortDTO dto) {
        return requestClient.getResourceCatalogRequestSpecification()
                .given().contentType(ContentType.JSON).body(dto)
                .when().put("/cardmodel/{id}/port", id).then().log().status().log()
                .body().contentType(ContentType.JSON).extract().as(SearchDTO.class);
    }

    public SearchDTO addChassisToDevice(Long id, StructureChassisDTO dto) {
        return requestClient.getResourceCatalogRequestSpecification()
                .given().contentType(ContentType.JSON).body(dto)
                .when().put("/models/{id}/structure/chassis", id).then().log().status().log()
                .body().contentType(ContentType.JSON).extract().as(SearchDTO.class);
    }

    public SearchDTO addSlotToObject(Long id, StructureSlotDTO dto) {
        return requestClient.getResourceCatalogRequestSpecification()
                .given().contentType(ContentType.JSON).body(dto)
                .when().put("/models/{id}/structure/slot", id).then().log().status().log()
                .body().contentType(ContentType.JSON).extract().as(SearchDTO.class);
    }

    public SearchDTO addTerminationPointToPort(Long id, TerminationPointsDTO dto) {
        return requestClient.getResourceCatalogRequestSpecification()
                .given().contentType(ContentType.JSON).body(dto)
                .when().put("/portmodel/{id}/terminationpoints", id).then().log().status().log()
                .body().contentType(ContentType.JSON).extract().as(SearchDTO.class);
    }

    public SearchDTO addTerminationPointToPluggableModule(Long id, TerminationPointsDTO dto) {
        return requestClient.getResourceCatalogRequestSpecification()
                .given().contentType(ContentType.JSON).body(dto)
                .when().put("/pluggablemodulemodel/{id}/terminationpoints", id).then().log().status().log()
                .body().contentType(ContentType.JSON).extract().as(SearchDTO.class);
    }

    public SearchDTO addPluggableModuleSlotToPort(Long id, StructurePluggableModuleSlotModelDTO dto) {
        return requestClient.getResourceCatalogRequestSpecification()
                .given().contentType(ContentType.JSON).body(dto)
                .when().put("/portmodel/{id}/pluggablemoduleslot", id).then().log().status().log()
                .body().contentType(ContentType.JSON).extract().as(SearchDTO.class);
    }

    public SearchDTO addPhysicalCrossesToDevice(Long id, List<PhysicalCrossconnectDTO> crosses) {
        return requestClient.getResourceCatalogRequestSpecification()
                .given().contentType(ContentType.JSON).body(crosses)
                .when().put("/physicaldevicemodel/{id}/crossconnects/physicalcrossconnect", id).then().log().status().log()
                .body().contentType(ContentType.JSON).extract().as(SearchDTO.class);
    }

    public SearchDTO addDSRCrossesToDevice(Long id, List<LogicalCrossconnectDTO> crosses) {
        return requestClient.getResourceCatalogRequestSpecification()
                .given().contentType(ContentType.JSON).body(crosses)
                .when().put("/physicaldevicemodel/{id}/crossconnects/dsrcrossconnect", id).then().log().status().log()
                .body().contentType(ContentType.JSON).extract().as(SearchDTO.class);
    }

    public SearchDTO addPhysicalCrossesToCard(Long id, List<PhysicalCrossconnectDTO> crosses) {
        return requestClient.getResourceCatalogRequestSpecification()
                .given().contentType(ContentType.JSON).body(crosses)
                .when().put("/cardmodel/{id}/crossconnects/physicalcrossconnect", id).then().log().status().log()
                .body().contentType(ContentType.JSON).extract().as(SearchDTO.class);
    }

    public SearchDTO addDSRCrossesToCard(Long id, List<LogicalCrossconnectDTO> crosses) {
        return requestClient.getResourceCatalogRequestSpecification()
                .given().contentType(ContentType.JSON).body(crosses)
                .when().put("/cardmodel/{id}/crossconnects/dsrcrossconnect", id).then().log().status().log()
                .body().contentType(ContentType.JSON).extract().as(SearchDTO.class);
    }

    public SearchDTO addPhysicalCrossesToPort(Long id, List<PortPhysicalCrossconnectDTO> crosses) {
        return requestClient.getResourceCatalogRequestSpecification()
                .given().contentType(ContentType.JSON).body(crosses)
                .when().put("/portmodel/{id}/crossconnects/physicalcrossconnect", id).then().log().status().log()
                .body().contentType(ContentType.JSON).extract().as(SearchDTO.class);
    }

    public SearchDTO addDSRCrossesToPort(Long id, List<PortLogicalCrossconnectDTO> crosses) {
        return requestClient.getResourceCatalogRequestSpecification()
                .given().contentType(ContentType.JSON).body(crosses)
                .when().put("/portmodel/{id}/crossconnects/dsrcrossconnect", id).then().log().status().log()
                .body().contentType(ContentType.JSON).extract().as(SearchDTO.class);
    }

    public SearchDTO addPhysicalCrossesToPluggableModule(Long id, List<PortPhysicalCrossconnectDTO> crosses) {
        return requestClient.getResourceCatalogRequestSpecification()
                .given().contentType(ContentType.JSON).body(crosses)
                .when().put("/pluggablemodulemodel/{id}/crossconnects/physicalcrossconnect", id).then().log().status().log()
                .body().contentType(ContentType.JSON).extract().as(SearchDTO.class);
    }

    public SearchDTO addDSRCrossesToPluggableModule(Long id, List<PortLogicalCrossconnectDTO> crosses) {
        return requestClient.getResourceCatalogRequestSpecification()
                .given().contentType(ContentType.JSON).body(crosses)
                .when().put("/pluggablemodulemodel/{id}/crossconnects/dsrcrossconnect", id).then().log().status().log()
                .body().contentType(ContentType.JSON).extract().as(SearchDTO.class);
    }

    public SearchDTO createCompatibility(Long id, Collection<CompatibilityDTO> dto) {
        return requestClient.getResourceCatalogRequestSpecification()
                .given().contentType(ContentType.JSON).body(dto)
                .when().post("/pluggablemoduleslotmodel/{id}/compatibilities", id).then().log().status().log()
                .body().contentType(ContentType.JSON).extract().as(SearchDTO.class);
    }

    public DeviceModelStructureDTO getDeviceModelStructure(Long id) {
        return requestClient.getResourceCatalogRequestSpecification()
                .given().contentType(ContentType.JSON)
                .when().get("/physicaldevicemodel/{id}/devicemodelstructurebyjpa", id).then().log().status().log()
                .body().contentType(ContentType.JSON).extract().as(DeviceModelStructureDTO.class);
    }

    public SearchDTO addSupportedLayersToDeviceModel(Long id, PhysicalDeviceModelEntityDTO dto) {
        return requestClient.getResourceCatalogRequestSpecification()
                .given().contentType(ContentType.JSON).body(dto)
                .when().post("/physicaldevicemodel/{id}/specification", id).then().log().status().log()
                .body().contentType(ContentType.JSON).extract().as(SearchDTO.class);
    }

    public SearchDTO removeSupportedLayers(Long id) {
        return requestClient.getResourceCatalogRequestSpecification()
                .given().contentType(ContentType.JSON)
                .when().delete("/physicaldevicemodel/{id}/specification", id).then().log().status().log()
                .body().contentType(ContentType.JSON).extract().as(SearchDTO.class);
    }

    public List<ManufacturerJPADTO> getAllManufacturers() {
        return Arrays.asList(requestClient.getResourceCatalogRequestSpecification()
                .given().contentType(ContentType.JSON)
                .when().get("manufacturers/all").then().log().status().log()
                .body().contentType(ContentType.JSON).extract().as(ManufacturerJPADTO[].class));
    }

    public List<ManufacturerJPADTO> getManufacturer(String name) {
        return Arrays.asList(requestClient.getResourceCatalogRequestSpecification()
                .given().contentType(ContentType.JSON).pathParam(MANUFACTURER_NAME_PARAM, name)
                .when().get("manufacturers/name/{manufacturerName}").then().log().status().log()
                .body().contentType(ContentType.JSON).extract().as(ManufacturerJPADTO[].class));
    }

    public void delete(Collection<Long> ids) {
        if (!ids.isEmpty()) {
            String idsPath = ids.stream().map(Objects::toString).collect(Collectors.joining(","));
            String path = UriBuilder.fromPath("/models/").path(idsPath).build().toString();
            requestClient.getResourceCatalogRequestSpecification()
                    .given().contentType(ContentType.JSON)
                    .when().delete(path)
                    .then().log().status().log();
        }
    }

    public LogicalFunctionModelResponseDTO createLogicalFunctionModel(LogicalFunctionModelDTO logicalFunctionModelDTO, String modelType) {
        return requestClient.getResourceCatalogRequestSpecification()
                .given().contentType(ContentType.JSON).body(logicalFunctionModelDTO)
                .pathParam(TYPE_PARAM, modelType)
                .when().put("/logicalfunction/model/{type}")
                .then().log().status().log().body()
                .contentType(ContentType.JSON).extract().as(LogicalFunctionModelResponseDTO.class);
    }

    public Optional<LogicalFunctionModelViewDTO> getLogicalFunctionModel(String identifier, String modelType) {
        ValidatableResponse response = requestClient.getResourceCatalogRequestSpecification()
                .given().contentType(ContentType.JSON)
                .pathParam(TYPE_PARAM, modelType).pathParam(IDENTIFIER_PARAM, identifier)
                .when().get("/logicalfunction/model/{type}/{identifier}")
                .then().log().status().log().body();

        if (Response.Status.OK.getStatusCode() == response.extract().statusCode()) {
            return Optional.ofNullable(response.contentType(ContentType.JSON)
                    .extract()
                    .as(LogicalFunctionModelViewDTO.class));
        }

        return Optional.empty();
    }

}
