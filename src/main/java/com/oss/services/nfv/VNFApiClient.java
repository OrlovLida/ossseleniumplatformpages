package com.oss.services.nfv;

import com.oss.untils.Environment;

import javax.ws.rs.core.Response;

public class VNFApiClient {

    private static final String VNF_PATH = "/vnf/";
    public static final String PERSPECTIVE_LIVE_PARAM = "?perspective=LIVE";

    private static VNFApiClient instance;
    private final Environment ENV;

    private VNFApiClient(Environment environment) {
        ENV = environment;
    }

    public static VNFApiClient getInstance(Environment environment) {
        if (instance == null) {
            instance = new VNFApiClient(environment);
        }
        return instance;
    }

    public void deleteVnfById(String id) {
        ENV.getNFVCoreSpecification()
                .when().delete(VNF_PATH + id + PERSPECTIVE_LIVE_PARAM)
                .then().log().status().log().body().statusCode(Response.Status.NO_CONTENT.getStatusCode());
    }

}
