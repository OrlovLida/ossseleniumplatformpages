package com.oss.services.nfv.networkslice;

import com.jayway.restassured.http.ContentType;
import com.oss.untils.Environment;

import javax.ws.rs.core.Response;

public class NetworkSliceApiClient {
    public static final String NETWORK_SLICE_PATH = "network-slice";
    public static final String PERSPECTIVE_LIVE_PARAM = "?perspective=LIVE";

    private static NetworkSliceApiClient instance;
    private final Environment env;

    private NetworkSliceApiClient(Environment environment) {
        env = environment;
    }

    public static NetworkSliceApiClient getInstance(Environment environment) {
        if (instance == null) {
            instance = new NetworkSliceApiClient(environment);
        }
        return instance;
    }

    public void createNetworkSlice(String json) {
        env.getNetworkSliceCoreSpecification()
                .given()
                .contentType(ContentType.JSON)
                .body(json)
                .when().post(NETWORK_SLICE_PATH + PERSPECTIVE_LIVE_PARAM)
                .then().log().status().log().body().statusCode(Response.Status.CREATED.getStatusCode());
    }

    public void deleteNetworkSlice(Long id) {
        env.getNetworkSliceCoreSpecification()
                .when().delete(NETWORK_SLICE_PATH + "/" + id.toString() + PERSPECTIVE_LIVE_PARAM)
                .then().log().status().log().body().statusCode(Response.Status.NO_CONTENT.getStatusCode());
    }
}
