package com.oss.softwaremanagement.infrastructure.repository;

import com.jayway.restassured.http.ContentType;
import com.oss.transport.infrastructure.EnvironmentRequestClient;

/**
 * Created by Bartłomiej Jędrzejczyk on 2022-04-19
 */
public class SoftwareRepositoryClient {

    private static SoftwareRepositoryClient instance;
    private final EnvironmentRequestClient requestClient;

    private SoftwareRepositoryClient(EnvironmentRequestClient environmentRequestClient) {
        this.requestClient = environmentRequestClient;
    }

    public static SoftwareRepositoryClient getInstance(EnvironmentRequestClient requestClient) {
        if (instance != null) {
            return instance;
        }
        instance = new SoftwareRepositoryClient(requestClient);
        return instance;
    }

    public String getRootPath() {
        return requestClient.getSoftwareManagementCoreSpecification()
            .get("software-repository/root-path")
            .then().log().status().log()
            .body().contentType(ContentType.TEXT).extract().asString();
    }
}
