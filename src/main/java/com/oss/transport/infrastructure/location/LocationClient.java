package com.oss.transport.infrastructure.location;

import com.comarch.oss.locationinventory.api.dto.PhysicalLocationBrowseDTO;
import com.comarch.oss.locationinventory.api.dto.PhysicalLocationDTO;
import com.comarch.oss.locationinventory.api.dto.ResourceDTO;
import com.comarch.oss.locationinventory.api.dto.SearchDTO;
import com.comarch.oss.locationinventory.api.dto.SearchResultDTO;
import com.comarch.oss.locationinventory.api.dto.SublocationBrowseDTO;
import com.comarch.oss.locationinventory.api.dto.SublocationDTO;
import com.jayway.restassured.http.ContentType;
import com.jayway.restassured.response.ValidatableResponse;
import com.oss.serviceClient.EnvironmentRequestClient;
import com.oss.transport.infrastructure.planning.PlanningContext;

import javax.ws.rs.core.Response.Status;
import java.util.Optional;

public class LocationClient {

    private final EnvironmentRequestClient requestClient;

    public LocationClient(EnvironmentRequestClient requestClient) {
        this.requestClient = requestClient;
    }

    public Long createLocation(PhysicalLocationDTO dto, PlanningContext context) {
        ResourceDTO resource = requestClient.getLocationInventoryCoreRequestSpecification()
                .given().contentType(ContentType.JSON).body(dto)
                .when().post(getLocationPath(context))
                .then().log().status().log().body()
                .assertThat().statusCode(Status.OK.getStatusCode()).contentType(ContentType.JSON)
                .extract().as(ResourceDTO.class);
        PhysicalLocationBrowseDTO location = requestClient.prepareRequestSpecificationWithoutUri()
                .when().get(resource.getUri())
                .then().log().status().log().body()
                .assertThat().contentType(ContentType.JSON)
                .extract().as(PhysicalLocationBrowseDTO[].class)[0];
        return location.getId().get();
    }

    public Long createSublocation(SublocationDTO dto, PlanningContext context) {
        ResourceDTO resource = requestClient.getLocationInventoryCoreRequestSpecification()
                .given().contentType(ContentType.JSON).body(dto)
                .when().post(getSublocationPath(context))
                .then().log().status().log().body()
                .assertThat().statusCode(Status.OK.getStatusCode()).contentType(ContentType.JSON)
                .extract().as(ResourceDTO.class);
        SublocationBrowseDTO sublocation = requestClient.prepareRequestSpecificationWithoutUri()
                .when().get(resource.getUri())
                .then().log().status().log().body()
                .assertThat().contentType(ContentType.JSON)
                .extract().as(SublocationBrowseDTO[].class)[0];
        return sublocation.getId().get();
    }

    public Optional<SearchDTO> getLocationByName(String name, PlanningContext context) {
        SearchResultDTO searchResults = requestClient.getLocationInventoryCoreRequestSpecification()
                .when().get(getLocationSearchPath(name, context))
                .then().log().status().log().body()
                .assertThat().statusCode(Status.OK.getStatusCode()).contentType(ContentType.JSON)
                .extract().as(SearchResultDTO.class);
        return searchResults.getSearchResult().stream().findFirst();
    }

    public Optional<Long> getSublocationByName(String name, PlanningContext context) {
        SearchResultDTO searchResults = requestClient.getLocationInventoryCoreRequestSpecification()
                .when().get(getSublocationSearchPath(name, context))
                .then().log().status().log().body()
                .assertThat().statusCode(Status.OK.getStatusCode()).contentType(ContentType.JSON)
                .extract().as(SearchResultDTO.class);
        return searchResults.getSearchResult().stream().findFirst().map(SearchDTO::getId);
    }

    private String getLocationPath(PlanningContext context) {
        return "/physicallocations?" + context.getQueryParamName() + "=" + context.getQueryParamValue();
    }

    private String getSublocationPath(PlanningContext context) {
        return "/sublocations?" + context.getQueryParamName() + "=" + context.getQueryParamValue();
    }

    private String getLocationSearchPath(String name, PlanningContext context) {
        return String.format("/physicallocations?query=Name==%s&%s=%s", name, context.getQueryParamName(), context.getQueryParamValue());
    }

    private String getSublocationSearchPath(String name, PlanningContext context) {
        return String.format("/sublocations?query=Name==%s&%s=%s", name, context.getQueryParamName(), context.getQueryParamValue());
    }

    public void delete(Long locationId, String type, PlanningContext context) {
        deleteRaw(locationId, type, context).statusCode(Status.NO_CONTENT.getStatusCode());
    }

    public ValidatableResponse deleteRaw(Long locationId, String type, PlanningContext context) {
        return requestClient.getLocationInventoryCoreRequestSpecification()
                .given().contentType(ContentType.JSON)
                .when().delete(getLocationDeletePath(locationId, type, context))
                .then().log().status().log().body();
    }

    private String getLocationDeletePath(Long locationId, String type, PlanningContext context) {
        return String.format("/physicallocations/%s/%s?%s", type, locationId,
                context.getQueryParamName() + "=" + context.getQueryParamValue());
    }

}
