package com.oss.services;

import com.comarch.oss.radio.api.dto.*;
import com.jayway.restassured.http.ContentType;
import com.oss.untils.Constants;
import com.oss.untils.Environment;

import javax.ws.rs.core.Response;
import java.util.Collections;

public class Radio5gClient {


    private static final String G_NODE_API_PATH = "/gnodeb";
    private static final String G_NODE_CU_UP_API_PATH = "/gnodeb/cu/up";
    private static final String G_NODE_DU_API_PATH = "/gnodeb/du";
    private static final String CELL_API_PATH = "/gnodeb/%s/cell";
    private static final String HOST_RELATION_G_NODE_API_PATH = "/gnodeb/%s/hostrelation";
    private static final String HOST_RELATION_CELL_API_PATH = "/gnodeb/%1$s/cell/%2$s/hostrelation";

    private static Radio5gClient instance;
    private final Environment ENV;

    private Radio5gClient(Environment environment) {
        ENV = environment;
    }

    public static Radio5gClient getInstance(Environment pEnvironment) {
        if (instance != null) {
            return instance;
        }
        instance = new Radio5gClient(pEnvironment);
        return instance;
    }

    private static final String PERSPECTIVE = "perspective";

    public BaseStationResponseIdDTO createGNodeB(GnodeBDTO pGNodeB) {
        return ENV.getRadioCore5GSpecification()
                .given()
                .contentType(ContentType.JSON)
                .body(pGNodeB)
                .when()
                .queryParam(PERSPECTIVE, Constants.LIVE)
                .queryParam(Constants.GENERATE_BASE_STATION_ID, true)
                .post(G_NODE_API_PATH)
                .then()
                .statusCode(Response.Status.OK.getStatusCode())
                .extract()
                .as(BaseStationResponseIdDTO.class);
    }

    public BaseStationResponseIdDTO createGNodeBCUUP(GnodeBCUUPDTO pGNodeBCUUP) {
        return ENV.getRadioCore5GSpecification()
                .given()
                .contentType(ContentType.JSON)
                .body(pGNodeBCUUP)
                .when()
                .queryParam(PERSPECTIVE, Constants.LIVE)
                .queryParam(Constants.GENERATE_BASE_STATION_ID, true)
                .post(G_NODE_CU_UP_API_PATH)
                .then()
                .statusCode(Response.Status.OK.getStatusCode())
                .extract()
                .as(BaseStationResponseIdDTO.class);
    }

    public BaseStationResponseIdDTO createGNodeBDU(GnodeBDUDTO pGNodeBDU) {
        return ENV.getRadioCore5GSpecification()
                .given()
                .contentType(ContentType.JSON)
                .body(pGNodeBDU)
                .when()
                .queryParam(PERSPECTIVE, Constants.LIVE)
                .queryParam(Constants.GENERATE_BASE_STATION_ID, true)
                .post(G_NODE_DU_API_PATH)
                .then()
                .statusCode(Response.Status.OK.getStatusCode())
                .extract()
                .as(BaseStationResponseIdDTO.class);
    }

    public CellResponseIdDTO createCell5G(Cell5gDTO pCell5g, Long gNodeBId) {
        String cellPath = String.format(CELL_API_PATH, gNodeBId);
        return ENV.getRadioCore5GSpecification()
                .given()
                .contentType(ContentType.JSON)
                .body(pCell5g)
                .when()
                .queryParam(PERSPECTIVE, Constants.LIVE)
                .post(cellPath)
                .then()
                .statusCode(Response.Status.OK.getStatusCode())
                .extract()
                .as(CellResponseIdDTO.class);
    }

    public void createHRGNodeB(HostRelationDTO hRENodeB, Long gNodeId) {
        String gNodeBHRPath = String.format(HOST_RELATION_G_NODE_API_PATH, gNodeId);
        ENV.getRadioCore5GSpecification()
                .given()
                .contentType(ContentType.JSON)
                .body(hRENodeB)
                .when()
                .queryParam(Constants.PERSPECTIVE, Constants.LIVE)
                .queryParam(Constants.ID, gNodeId)
                .post(gNodeBHRPath)
                .then()
                .statusCode(Response.Status.NO_CONTENT.getStatusCode()).assertThat();
    }

    public void createHRCell(HostRelationDTO hRGNodeB, Long gNodeId, Long cellId) {
        String cellHRPath = String.format(HOST_RELATION_CELL_API_PATH, gNodeId, cellId);
        ENV.getRadioCore5GSpecification()
                .given()
                .contentType(ContentType.JSON)
                .body(hRGNodeB)
                .when()
                .queryParam(Constants.PERSPECTIVE, Constants.LIVE)
                .queryParam(Constants.ID, gNodeId)
                .queryParam(Constants.CELL_ID, cellId)
                .post(cellHRPath)
                .then()
                .statusCode(Response.Status.NO_CONTENT.getStatusCode()).assertThat();
    }
}
