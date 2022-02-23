package com.oss.services.nfv;

import javax.ws.rs.core.Response;

import com.oss.untils.Environment;

public class VNFSpecificationClient {

    private static final String VNF_SPECIFICATION_PATH = "/vnf-specification/";
    public static final String PERSPECTIVE_LIVE_PARAM = "?perspective=LIVE";

    private static VNFSpecificationClient instance;
    private final Environment env;

    private VNFSpecificationClient(Environment environment) {
        env = environment;
    }

    public static VNFSpecificationClient getInstance(Environment environment) {
        if (instance == null) {
            instance = new VNFSpecificationClient(environment);
        }
        return instance;
    }

    public void deleteVnfSpecificationById(String id) {
        env.getNFVCoreSpecification()
                .when().delete(VNF_SPECIFICATION_PATH + id + PERSPECTIVE_LIVE_PARAM)
                .then().log().status().log().body().statusCode(Response.Status.NO_CONTENT.getStatusCode());
    }

}
