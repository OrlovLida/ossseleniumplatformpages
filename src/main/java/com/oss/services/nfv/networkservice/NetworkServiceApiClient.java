package com.oss.services.nfv.networkservice;

import com.jayway.restassured.http.ContentType;
import com.oss.untils.Environment;

import javax.ws.rs.core.Response;

public class NetworkServiceApiClient {

    private static final String NETWORK_SERVICE = "/network-service/";
    public static final String PERSPECTIVE_LIVE_PARAM = "?perspective=LIVE";

    private static NetworkServiceApiClient instance;
    private final Environment env;

    private NetworkServiceApiClient(Environment environment) {
        env = environment;
    }

    public static NetworkServiceApiClient getInstance(Environment environment) {
        if (instance == null) {
            instance = new NetworkServiceApiClient(environment);
        }
        return instance;
    }

    public void createNetworkService(String json) {
        env.getNetworkServiceCoreSpecification()
                .given()
                .contentType(ContentType.JSON)
                .body(json)
                .when().post(NETWORK_SERVICE + PERSPECTIVE_LIVE_PARAM)
                .then().log().status().log().body().statusCode(Response.Status.CREATED.getStatusCode());
    }

    public void deleteNetworkServiceById(Long id) {
        env.getNetworkServiceCoreSpecification()
                .when().delete(NETWORK_SERVICE + id.toString() + PERSPECTIVE_LIVE_PARAM)
                .then().log().status().log().body();
    }

}
