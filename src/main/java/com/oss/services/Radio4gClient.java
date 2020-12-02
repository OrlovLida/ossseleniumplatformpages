package com.oss.services;

import com.comarch.oss.radio.api.dto.*;
import com.jayway.restassured.http.ContentType;
import com.oss.untils.Constants;
import com.oss.untils.Environment;

import javax.ws.rs.core.Response;
import java.util.Collections;

public class Radio4gClient {

    private static final String E_NODE_API_PATH = "/enodeb";
    private static final String CELL_API_PATH = "/enodeb/%s/cell";

    private static Radio4gClient instance;
    private final Environment env;

    private Radio4gClient(Environment environment) {
        env = environment;
    }

    public static Radio4gClient getInstance(Environment pEnvironment) {
        if (instance != null) {
            return instance;
        }
        instance = new Radio4gClient(pEnvironment);
        return instance;
    }

    public BaseStationResponseIdDTO createENodeB(EnodeBDTO eNodeB) {
        return env.getRadioCore4GSpecification()
                .given()
                .contentType(ContentType.JSON)
                .body(eNodeB)
                .when()
                .queryParam(Constants.PERSPECTIVE, Constants.LIVE)
                .queryParam(Constants.GENERATE_BASE_STATION_ID, true)
                .post(E_NODE_API_PATH)
                .then()
                .statusCode(Response.Status.OK.getStatusCode()).assertThat()
                .extract()
                .as(BaseStationResponseIdDTO.class);
    }

    public CellResponseIdDTO createCell4G(Cell4gDTO cell4g, Long eNodeId) {
        String cellPath = String.format(CELL_API_PATH, eNodeId);
        return env.getRadioCore4GSpecification()
                .given()
                .contentType(ContentType.JSON)
                .body(cell4g)
                .when()
                .queryParam(Constants.PERSPECTIVE, Constants.LIVE)
                .post(cellPath)
                .then()
                .statusCode(Response.Status.OK.getStatusCode())
                .extract()
                .as(CellResponseIdDTO.class);
    }

}
