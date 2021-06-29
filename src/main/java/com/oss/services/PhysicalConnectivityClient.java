package com.oss.services;

import com.comarch.oss.physicalconnectivitycable.api.dto.CableSyncDTO;
import com.comarch.oss.physicalconnectivitycable.api.dto.CableSyncResultDTO;
import com.comarch.oss.physicalconnectivitycable.api.dto.MultipleSegmentDTO;
import com.comarch.oss.physicalconnectivitycable.api.dto.MultipleSegmentResultDTO;
import com.jayway.restassured.http.ContentType;
import com.oss.untils.Constants;
import com.oss.untils.Environment;

import javax.ws.rs.core.Response;
import java.util.Collections;

import static java.net.HttpURLConnection.HTTP_NO_CONTENT;


public class PhysicalConnectivityClient {

    private static final String CABLE_SYNC_API_PATH = "/cable/sync";
    private static final String CABLE_SEGMENTS_API_PATH = "/multiplesegments";
    private static final String CABLE_API_PATH = "/cable";
    private static PhysicalConnectivityClient instance;
    private final Environment env;

    public PhysicalConnectivityClient(Environment environment) {
        env = environment;
    }

    public static PhysicalConnectivityClient getInstance(Environment pEnvironment) {
        if (instance != null) {
            return instance;
        }
        instance = new PhysicalConnectivityClient(pEnvironment);
        return instance;
    }

    public CableSyncResultDTO createCable(CableSyncDTO cable) {
        return env.getPhysicalConnectivityCoreSpecification()
                .given()
                .contentType(ContentType.JSON)
                .queryParam(Constants.PERSPECTIVE, Constants.LIVE)
                .body(Collections.singleton(cable))
                .when()
                .post(CABLE_SYNC_API_PATH)
                .then()
                .statusCode(Response.Status.OK.getStatusCode())
                .extract()
                .as(CableSyncResultDTO.class);
    }

    public MultipleSegmentResultDTO createMultipleSegmentCable(MultipleSegmentDTO cable) {
        return env.getPhysicalConnectivityCoreSpecification()
                .given()
                .contentType(ContentType.JSON)
                .queryParam(Constants.PERSPECTIVE, Constants.LIVE)
                .body(cable)
                .when()
                .post(CABLE_SEGMENTS_API_PATH)
                .then()
                .statusCode(Response.Status.OK.getStatusCode())
                .extract()
                .as(MultipleSegmentResultDTO.class);
    }

    public com.jayway.restassured.response.Response removeCable(Long cableId) {
        return env.getPhysicalConnectivityCoreSpecification()
                .when()
                .delete(CABLE_API_PATH + "?id={id}", cableId)
                .then()
                .log()
                .status()
                .log()
                .body()
                .statusCode(HTTP_NO_CONTENT)
                .extract()
                .response();
    }

}
