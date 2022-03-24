package com.oss.transport.infrastructure.servicecheck;

import com.google.common.collect.ImmutableMap;
import com.jayway.restassured.response.Response;
import com.oss.transport.infrastructure.EnvironmentRequestClient;

import java.util.Map;

public class ServicesChecker {
    private final EnvironmentRequestClient requestClient;

    private final Map<String, UsabilityCheck> usabilityCheckers;

    public ServicesChecker(EnvironmentRequestClient requestClient) {
        this.requestClient = requestClient;

        usabilityCheckers = ImmutableMap.of(); /*ImmutableMap.<String, UsabilityCheck>builder()
                .put("resource-catalog-core", new ResourceCatalogUsabilityChecker(requestClient))
                .put("planning-core", new PlanningCoreUsabilityChecker(requestClient))
                .put("kafka", new KafkaUsabilityChecker(requestClient))
                .put("trail-core", new TrailCoreUsabilityChecker(requestClient))
                .build();*/
    }

    public boolean testRegistration(String applicationName) {
        return requestClient.getServicesClient().testService(applicationName);
    }

    public boolean testHealth(String applicationName) {
        Response response = requestClient.getServicesClient().getRequestSpecificationByName(applicationName)
                .when().get("/health");
        response.then().log().body().log().status();
        javax.ws.rs.core.Response.Status.Family responseFamily = javax.ws.rs.core.Response.Status.Family.familyOf(response.getStatusCode());
        return javax.ws.rs.core.Response.Status.Family.SUCCESSFUL.equals(responseFamily);
    }

    public boolean testHealthByApplicationBasePath(String applicationPath) {
        Response response = requestClient.getServicesClient().getRequestSpecificationByApplicationBasePath(applicationPath)
                .when().get("/health");
        response.then().log().body().log().status();
        javax.ws.rs.core.Response.Status.Family responseFamily = javax.ws.rs.core.Response.Status.Family.familyOf(response.getStatusCode());
        return javax.ws.rs.core.Response.Status.Family.SUCCESSFUL.equals(responseFamily);
    }

    public boolean testUsability(String applicationName) {
        if (usabilityCheckers.containsKey(applicationName)) {
            return usabilityCheckers.get(applicationName).test();
        }
        throw new IllegalStateException(String.format("Usability test for application %s is not supported", applicationName));
    }
}
