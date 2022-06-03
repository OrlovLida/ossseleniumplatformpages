package com.oss.services;

import com.jayway.restassured.http.ContentType;
import com.oss.untils.Environment;

import javax.ws.rs.core.Response;

public class LogicalInventoryViewClient {

    public static final String CUSTOM_WIZARD = "/customWizard";
    private static LogicalInventoryViewClient instance;
    private final Environment env;

    private LogicalInventoryViewClient(Environment environment) {
        env = environment;
    }

    public static LogicalInventoryViewClient getInstance(Environment environment) {
        if (instance == null) {
            instance = new LogicalInventoryViewClient(environment);
        }
        return instance;
    }

    public void saveCustomWizard(String json) {
        env.getLogicalInventoryViewSpecification()
                .given()
                .contentType(ContentType.JSON)
                .body(json)
                .when().put(CUSTOM_WIZARD)
                .then().log().status().log().body().statusCode(Response.Status.NO_CONTENT.getStatusCode());
    }

    public void deleteCustomWizard(String instanceType) {
        env.getLogicalInventoryViewSpecification()
                .given()
                .contentType(ContentType.JSON)
                .when().delete(CUSTOM_WIZARD + "/" + instanceType)
                .then().log().status().log().body().statusCode(Response.Status.NO_CONTENT.getStatusCode());
    }

}
