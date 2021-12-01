package com.oss.transport.infrastructure.logical;

import com.comarch.oss.logical.function.api.dto.LogicalFunctionDTO;
import com.comarch.oss.logical.function.api.dto.LogicalFunctionIdentificationDTO;
import com.comarch.oss.logical.function.api.dto.LogicalFunctionTypeDTO;
import com.comarch.oss.logical.function.api.dto.LogicalFunctionTypeIdentificationDTO;
import com.comarch.oss.logical.function.api.dto.LogicalFunctionTypeViewDTO;
import com.comarch.oss.logical.function.api.dto.LogicalFunctionViewDTO;
import com.jayway.restassured.http.ContentType;
import com.jayway.restassured.response.ValidatableResponse;
import com.oss.transport.infrastructure.EnvironmentRequestClient;
import com.oss.transport.infrastructure.planning.PlanningContext;

import javax.ws.rs.core.Response.Status;
import java.util.Arrays;
import java.util.Collection;
import java.util.Optional;

public class LogicalFunctionClient {

    private static final String NAME_PARAM = "name";
    private static final String TYPE_PATH = "type";

    private final EnvironmentRequestClient requestClient;

    public LogicalFunctionClient(EnvironmentRequestClient requestClient) {
        this.requestClient = requestClient;
    }

    public Long create(LogicalFunctionDTO dto, PlanningContext context) {
        LogicalFunctionIdentificationDTO resultDTO = createRaw(dto, context).statusCode(Status.CREATED.getStatusCode())
                .contentType(ContentType.JSON).extract().as(LogicalFunctionIdentificationDTO.class);
        return resultDTO.getId();
    }

    private ValidatableResponse createRaw(LogicalFunctionDTO dto, PlanningContext context) {
        return requestClient.getLogicalFunctionCoreRequestSpecification()
                .given().contentType(ContentType.JSON).body(dto)
                .when().post(getCreatePath(context))
                .then().log().status().log().body();
    }

    private String getCreatePath(PlanningContext context) {
        return "/v2?" + context.getQueryParamName() + "=" + context.getQueryParamValue();
    }

    public LogicalFunctionViewDTO get(Long id, PlanningContext context) {
        return getRaw(id, context).statusCode(Status.OK.getStatusCode())
                .contentType(ContentType.JSON).extract().as(LogicalFunctionViewDTO.class);
    }

    private ValidatableResponse getRaw(Long id, PlanningContext context) {
        return requestClient.getLogicalFunctionCoreRequestSpecification()
                .given().contentType(ContentType.JSON)
                .when().get(getGetPath(id, context))
                .then().log().status().log().body();
    }

    private String getGetPath(Long id, PlanningContext context) {
        return String.format("/%s?%s=%s", id.toString(), context.getQueryParamName(), context.getQueryParamValue());
    }

    public Collection<LogicalFunctionViewDTO> getByName(String name, PlanningContext context) {
        LogicalFunctionViewDTO[] logicalFunctions = getByNameRaw(name, context).statusCode(Status.OK.getStatusCode())
                .contentType(ContentType.JSON).extract().as(LogicalFunctionViewDTO[].class);
        return Arrays.asList(logicalFunctions);
    }

    private ValidatableResponse getByNameRaw(String name, PlanningContext context) {
        return requestClient.getLogicalFunctionCoreRequestSpecification()
                .given().contentType(ContentType.JSON)
                .when().get(getGetByNamePath(name, context))
                .then().log().status().log().body();
    }

    private String getGetByNamePath(String name, PlanningContext context) {
        return String.format("/name?name=%s&%s=%s", name, context.getQueryParamName(), context.getQueryParamValue());
    }

    public Optional<LogicalFunctionTypeViewDTO> getLogicalFunctionType(String name) {
        ValidatableResponse response = requestClient.getLogicalFunctionCoreRequestSpecification()
                .given().contentType(ContentType.JSON)
                .when().pathParam(NAME_PARAM, name).get("type/{name}")
                .then().log().status().log().body();

        int statusCode = response.extract().statusCode();
        if (Status.OK.getStatusCode() == statusCode) {
            return Optional.ofNullable(response.contentType(ContentType.JSON)
                    .extract()
                    .as(LogicalFunctionTypeViewDTO.class));
        }

        return Optional.empty();
    }

    public LogicalFunctionTypeIdentificationDTO createLogicalFunctionType(LogicalFunctionTypeDTO logicalFunctionTypeDTO) {
        return requestClient.getLogicalFunctionCoreRequestSpecification()
                .given().contentType(ContentType.JSON).body(logicalFunctionTypeDTO)
                .when().put(TYPE_PATH)
                .then().log().status().log().body()
                .contentType(ContentType.JSON).statusCode(Status.OK.getStatusCode())
                .extract().as(LogicalFunctionTypeIdentificationDTO.class);
    }
}
