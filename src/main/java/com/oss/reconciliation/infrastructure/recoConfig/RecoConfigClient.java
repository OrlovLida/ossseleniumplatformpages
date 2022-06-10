package com.oss.reconciliation.infrastructure.recoConfig;

import com.jayway.restassured.http.ContentType;
import com.oss.transport.infrastructure.EnvironmentRequestClient;

/**
 * Created by Artur Grzelak on 2022-06-10
 */
public class RecoConfigClient {

    private static RecoConfigClient instance;
    private final EnvironmentRequestClient requestClient;

    private RecoConfigClient(EnvironmentRequestClient environmentRequestClient) {
        this.requestClient = environmentRequestClient;
    }

    public static RecoConfigClient getInstance(EnvironmentRequestClient requestClient) {
        if (instance != null) {
            return instance;
        }
        instance = new RecoConfigClient(requestClient);
        return instance;
    }

    public String getRootPath() {
        return requestClient.getSoftwareManagementCoreSpecification()
            .get("software-repository/root-path")
            .then().log().status().log()
            .body().contentType(ContentType.TEXT).extract().asString();
    }

    public void deletePath(String path, boolean contentOnly) {
        requestClient.getSoftwareManagementCoreSpecification()
            .given()
            .queryParam("path", path)
            .queryParam("contentOnly", contentOnly)
            .delete("software-repository/delete")
            .then().log()
            .status().log();
    }
}
