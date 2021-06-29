package com.oss.services.fixedaccessclient;

import com.comarch.oss.inventory.fixedaccess.distributionarea.api.dto.DistributionAreaDTO;
import com.comarch.oss.inventory.fixedaccess.distributionarea.api.dto.DistributionAreaPersistenceResponseDTO;
import com.comarch.oss.inventory.fixedaccess.distributionarea.api.dto.DistributionAreaSyncDTO;
import com.jayway.restassured.http.ContentType;
import com.jayway.restassured.response.Response;
import com.jayway.restassured.response.ValidatableResponse;
import com.oss.untils.Environment;

import static java.net.HttpURLConnection.HTTP_OK;


public class DistributionAreaClient {

    private static final String DISTRIBUTION_AREAS_PATH = "distribution-areas/";
    private static final String SYNC_PATH = "sync";
    private static final String DISTRIBUTION_AREAS_V2_PATH = DISTRIBUTION_AREAS_PATH + "v2/";
    private static DistributionAreaClient instance;
    private final Environment ENV;

    public DistributionAreaClient(Environment environment) {
        ENV = environment;
    }

    public static DistributionAreaClient getInstance(Environment pEnvironment) {
        if (instance != null) {
            return instance;
        }
        instance = new DistributionAreaClient(pEnvironment);
        return instance;
    }

    public DistributionAreaPersistenceResponseDTO synchronizeDistributionArea(DistributionAreaSyncDTO pDistributionAreaSyncDTO) {
        return executeSyncRequest(pDistributionAreaSyncDTO)
                .extract()
                .as(DistributionAreaPersistenceResponseDTO.class);
    }

    public Response removeDistributionArea(Long pDistributionAreaId) {
        return ENV.getFixedAccessCoreSpecification()
                .when()
                .delete(DISTRIBUTION_AREAS_PATH + "{id}", pDistributionAreaId)
                .then()
                .log()
                .status()
                .log()
                .body()
                .statusCode(HTTP_OK)
                .extract()
                .response();
    }

    public DistributionAreaDTO getDistributionAreaV2(Long pDistributionAreaId) {
        return ENV.getFixedAccessCoreSpecification()
                .when()
                .get(DISTRIBUTION_AREAS_V2_PATH + pDistributionAreaId + "?perspective=LIVE")
                .then()
                .log()
                .status()
                .log()
                .body()
                .contentType(ContentType.JSON)
                .statusCode(HTTP_OK)
                .extract()
                .as(DistributionAreaDTO.class);
    }

    private ValidatableResponse executeSyncRequest(DistributionAreaSyncDTO pDistributionAreaSyncDTO) {
        return ENV.getFixedAccessCoreSpecification()
                .given()
                .contentType(ContentType.JSON)
                .body(pDistributionAreaSyncDTO)
                .when()
                .post(DISTRIBUTION_AREAS_PATH + SYNC_PATH)
                .then()
                .log()
                .status()
                .log()
                .body()
                .contentType(ContentType.JSON);
    }

}
