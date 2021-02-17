package com.oss.services;

import javax.ws.rs.core.Response;

import com.comarch.oss.logical.function.api.dto.LogicalFunctionBulkIdentificationsDTO;
import com.comarch.oss.logical.function.v2.api.dto.LogicalFunctionBulkDTO;
import com.jayway.restassured.http.ContentType;
import com.oss.untils.Constants;
import com.oss.untils.Environment;

public class LogicalFunctionClient {

    private static final String BULK_V2 = "/v2/bulk";
    private static LogicalFunctionClient instance;
    private final Environment ENV;

    private LogicalFunctionClient(Environment environment) {
        ENV = environment;
    }

    public static LogicalFunctionClient getInstance(Environment environment) {
        if (instance == null) {
            instance = new LogicalFunctionClient(environment);
        }
        return instance;
    }

    public LogicalFunctionBulkIdentificationsDTO createLogicalFunction(LogicalFunctionBulkDTO dto) {
        return ENV.getLogicalFunctionSpecification()
                   .given()
                   .contentType(ContentType.JSON)
                   .body(dto)
                   .when()
                   .queryParam(Constants.PERSPECTIVE, Constants.LIVE)
                   .post(BULK_V2)
                   .then()
                   .statusCode(Response.Status.OK.getStatusCode())
                   .extract()
                   .as(LogicalFunctionBulkIdentificationsDTO.class);
    }

    public void deleteLogicalFunction(long id) {
        ENV.getLogicalFunctionSpecification()
            .given()
            .contentType(ContentType.JSON)
            .when()
            .queryParam(Constants.PERSPECTIVE, Constants.LIVE)
            .delete("/" + id)
            .then()
            .statusCode(Response.Status.NO_CONTENT.getStatusCode());
    }

}
