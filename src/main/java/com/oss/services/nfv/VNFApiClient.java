package com.oss.services.nfv;

import com.jayway.restassured.http.ContentType;
import com.oss.untils.Environment;

import javax.ws.rs.core.Response;

public class VNFApiClient {

    private static final String VNF_PATH = "/vnf/";
    public static final String PERSPECTIVE_LIVE_PARAM = "?perspective=LIVE";

    private static VNFApiClient instance;
    private final Environment env;

    private VNFApiClient(Environment environment) {
        env = environment;
    }

    public static VNFApiClient getInstance(Environment environment) {
        if (instance == null) {
            instance = new VNFApiClient(environment);
        }
        return instance;
    }

    public void createVnf(String json) {
        env.getNFVCoreSpecification()
                .given()
                .contentType(ContentType.JSON)
                .body(json)
                .when().post(VNF_PATH + PERSPECTIVE_LIVE_PARAM)
                .then().log().status().log().body().statusCode(Response.Status.CREATED.getStatusCode());
    }

    public void deleteVnfById(String id) {
        env.getNFVCoreSpecification()
                .when().delete(VNF_PATH + id + PERSPECTIVE_LIVE_PARAM)
                .then().log().status().log().body().statusCode(Response.Status.NO_CONTENT.getStatusCode());
    }

}
