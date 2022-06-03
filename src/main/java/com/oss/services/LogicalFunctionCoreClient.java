package com.oss.services;

import java.util.List;

import javax.ws.rs.core.Response;

import com.comarch.oss.logical.function.api.dto.LogicalFunctionBulkIdentificationsDTO;
import com.comarch.oss.logical.function.api.dto.LogicalFunctionIdentificationsDTO;
import com.comarch.oss.logical.function.api.dto.LogicalFunctionViewDTO;
import com.comarch.oss.logical.function.v2.api.dto.LogicalFunctionBulkDTO;
import com.google.common.collect.Lists;
import com.jayway.restassured.http.ContentType;
import com.oss.untils.Constants;
import com.oss.untils.Environment;

public class LogicalFunctionCoreClient {

    private static final String BULK_V2 = "/v2/bulk";
    private static final String SPECIFICATION = "/specification";
    private static final String NAME = "/name";
    private static LogicalFunctionCoreClient instance;
    private final Environment env;

    private LogicalFunctionCoreClient(Environment environment) {
        env = environment;
    }

    public static LogicalFunctionCoreClient getInstance(Environment environment) {
        if (instance == null) {
            instance = new LogicalFunctionCoreClient(environment);
        }
        return instance;
    }

    public LogicalFunctionBulkIdentificationsDTO createLogicalFunctionBulk(LogicalFunctionBulkDTO dto) {
        return env.getLogicalFunctionCoreSpecification()
                .given()
                .contentType(ContentType.JSON)
                .body(dto)
                .when()
                .queryParam(Constants.PERSPECTIVE, Constants.LIVE)
                .post(BULK_V2)
                .then().log().status().log().body()
                .statusCode(Response.Status.OK.getStatusCode())
                .extract()
                .as(LogicalFunctionBulkIdentificationsDTO.class);
    }

    public void deleteLogicalFunction(long id) {
        env.getLogicalFunctionCoreSpecification()
                .given()
                .contentType(ContentType.JSON)
                .when()
                .queryParam(Constants.PERSPECTIVE, Constants.LIVE)
                .delete("/" + id)
                .then().log().status().log().body()
                .statusCode(Response.Status.NO_CONTENT.getStatusCode());
    }

    public LogicalFunctionIdentificationsDTO getLogicalFunctionBySpecification(String identifier) {
        return env.getLogicalFunctionCoreSpecification()
                .queryParam(Constants.PERSPECTIVE, Constants.LIVE)
                .queryParam(Constants.IDS, identifier)
                .get(SPECIFICATION)
                .then().log().status().log().body()
                .statusCode(Response.Status.OK.getStatusCode())
                .extract()
                .as(LogicalFunctionIdentificationsDTO.class);
    }

    public List<LogicalFunctionViewDTO> getLogicalFunctionByName(String name) {
        return Lists.newArrayList(env.getLogicalFunctionCoreSpecification()
                .queryParam(Constants.PERSPECTIVE, Constants.LIVE)
                .queryParam(Constants.NAME_PARAM.toLowerCase(), name)
                .get(NAME)
                .then().log().status().log().body()
                .statusCode(Response.Status.OK.getStatusCode())
                .extract()
                .as(LogicalFunctionViewDTO[].class));
    }
}
