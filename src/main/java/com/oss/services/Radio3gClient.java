package com.oss.services;

import com.comarch.oss.radio.api.dto.*;
import com.jayway.restassured.http.ContentType;
import com.oss.untils.Constants;
import com.oss.untils.Environment;

import javax.ws.rs.core.Response;

public class Radio3gClient {

    private static final String NODE_API_PATH = "/nodeb";
    private static final String RNC_API_PATH = "/rnc";
    private static final String CELL_API_PATH = "/nodeb/%s/cell";
    private static final String HOST_RELATION_NODE_API_PATH = "/nodeb/%s/hostrelation";
    private static final String HOST_RELATION_CELL_API_PATH = "/nodeb/%1$s/cell/%2$s/hostrelation";

    private static Radio3gClient instance;
    private final Environment env;

    private Radio3gClient(Environment environment) {
        env = environment;
    }

    public static Radio3gClient getInstance(Environment pEnvironment) {
        if (instance != null) {
            return instance;
        }
        instance = new Radio3gClient(pEnvironment);
        return instance;
    }

    public BaseStationResponseIdDTO createNodeB(NodeBDTO nodeB) {
        return env.getRadioCore3GSpecification()
                .given()
                .contentType(ContentType.JSON)
                .body(nodeB)
                .when()
                .queryParam(Constants.PERSPECTIVE, Constants.LIVE)
                .queryParam(Constants.GENERATE_BASE_STATION_ID, true)
                .post(NODE_API_PATH)
                .then()
                .statusCode(Response.Status.OK.getStatusCode())
                .extract()
                .as(BaseStationResponseIdDTO.class);
    }

    public ControllerResponseIdDTO createRNC(RNCDTO rnc) {
        return env.getRadioCore3GSpecification()
                .given()
                .contentType(ContentType.JSON)
                .body(rnc)
                .when()
                .queryParam(Constants.PERSPECTIVE, Constants.LIVE)
                .queryParam(Constants.GENERATE_CONTROLLER_ID, true)
                .post(RNC_API_PATH)
                .then()
                .statusCode(Response.Status.OK.getStatusCode())
                .extract()
                .as(ControllerResponseIdDTO.class);
    }

    public CellResponseIdDTO createCell3G(Cell3gDTO cell3g, Long nodeId) {
        String cellPath = String.format(CELL_API_PATH, nodeId);
        return env.getRadioCore3GSpecification()
                .given()
                .contentType(ContentType.JSON)
                .body(cell3g)
                .when()
                .queryParam(Constants.PERSPECTIVE, Constants.LIVE)
                .post(cellPath)
                .then()
                .statusCode(Response.Status.OK.getStatusCode())
                .extract()
                .as(CellResponseIdDTO.class);
    }

    public void createHRNodeB(HostRelationDTO hRENodeB, Long nodeId) {
        String nodeBHRPath = String.format(HOST_RELATION_NODE_API_PATH, nodeId);
        env.getRadioCore3GSpecification()
                .given()
                .contentType(ContentType.JSON)
                .body(hRENodeB)
                .when()
                .queryParam(Constants.PERSPECTIVE, Constants.LIVE)
                .queryParam(Constants.ID, nodeId)
                .post(nodeBHRPath)
                .then()
                .statusCode(Response.Status.NO_CONTENT.getStatusCode()).assertThat();
    }

    public void createHRCell(HostRelationDTO hRNodeB, Long nodeId, Long cellId) {
        String cellHRPath = String.format(HOST_RELATION_CELL_API_PATH, nodeId, cellId);
        env.getRadioCore3GSpecification()
                .given()
                .contentType(ContentType.JSON)
                .body(hRNodeB)
                .when()
                .queryParam(Constants.PERSPECTIVE, Constants.LIVE)
                .queryParam(Constants.ID, nodeId)
                .queryParam(Constants.CELL_ID, cellId)
                .post(cellHRPath)
                .then()
                .statusCode(Response.Status.NO_CONTENT.getStatusCode()).assertThat();
    }
}
