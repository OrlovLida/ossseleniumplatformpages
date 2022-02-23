package com.oss.services.nfv;

import com.jayway.restassured.http.ContentType;
import com.oss.untils.Environment;

import javax.ws.rs.core.Response;

public class OnboardClient {

    private static final String ONBOARD = "onboard";
    public static final String FROM_XML_TEXT = "/from-xml-text";

    private static OnboardClient instance;
    private final Environment ENV;

    private OnboardClient(Environment environment) {
        ENV = environment;
    }

    public static OnboardClient getInstance(Environment environment) {
        if (instance == null) {
            instance = new OnboardClient(environment);
        }
        return instance;
    }

    public String uploadResourceSpecificationAndGetVNFId(String xmlContent) {
        return ENV.getNFVCoreSpecification()
                .given().contentType(ContentType.XML).body(xmlContent)
                .when().post(ONBOARD + FROM_XML_TEXT)
                .then().log().status().log().body().statusCode(Response.Status.OK.getStatusCode())
                .extract()
                .body()
                .jsonPath()
                .getString("resourceSpecificationIdentifications[0].xId");
    }

}
