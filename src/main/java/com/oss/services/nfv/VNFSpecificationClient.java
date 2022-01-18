package com.oss.services.nfv;

import com.jayway.restassured.http.ContentType;
import com.oss.untils.Environment;

import javax.ws.rs.core.Response;

public class VNFSpecificationClient {

    private static final String VNF_SPECIFICATION_PATH = "/vnf-specification/";
    public static final String PERSPECTIVE_LIVE_PARAM = "?perspective=LIVE";

    private static VNFSpecificationClient instance;
    private final Environment ENV;

    private VNFSpecificationClient(Environment environment) {
        ENV = environment;
    }

    public static VNFSpecificationClient getInstance(Environment environment) {
        if (instance == null) {
            instance = new VNFSpecificationClient(environment);
        }
        return instance;
    }

    public void deleteVnfSpecificationById(String id) {
        ENV.getNFVCoreSpecification()
                .when().delete(VNF_SPECIFICATION_PATH + id + PERSPECTIVE_LIVE_PARAM)
                .then().log().status().log().body().statusCode(Response.Status.NO_CONTENT.getStatusCode());
    }

}
