package com.oss.services;

import java.util.List;

import javax.ws.rs.core.Response;

import com.comarch.oss.radio.api.dto.BaseStationResponseIdDTO;
import com.comarch.oss.radio.api.dto.CarrierDTO;
import com.comarch.oss.radio.api.dto.CarrierResponseDTO;
import com.comarch.oss.radio.api.dto.Cell5gDTO;
import com.comarch.oss.radio.api.dto.CellResponseIdDTO;
import com.comarch.oss.radio.api.dto.GnodeBCUUPDTO;
import com.comarch.oss.radio.api.dto.GnodeBDTO;
import com.comarch.oss.radio.api.dto.GnodeBDUDTO;
import com.comarch.oss.radio.api.dto.HostRelationDTO;
import com.comarch.oss.radio.api.dto.RanBandType5GDTO;
import com.comarch.oss.radio.api.dto.RanBandTypeResponseDTO;
import com.jayway.restassured.http.ContentType;
import com.oss.untils.Constants;
import com.oss.untils.Environment;

public class Radio5gClient {

    private static final String G_NODE_API_PATH = "/gnodeb";
    private static final String G_NODE_CU_UP_API_PATH = "/gnodeb/cu/up";
    private static final String G_NODE_DU_API_PATH = "/gnodeb/du";
    private static final String CELL_API_PATH = "/gnodeb/%s/cell";
    private static final String HOST_RELATION_G_NODE_API_PATH = "/gnodeb/%s/hostrelation";
    private static final String HOST_RELATION_CELL_API_PATH = "/gnodeb/%1$s/cell/%2$s/hostrelation";
    private static final String BAND_TYPE = "bandtype";
    private static final String CARRIER_BY_NAME_PATH = "carrier/name/";
    private static final String CARRIER = "carrier";

    private static Radio5gClient instance;
    private final Environment env;

    private Radio5gClient(Environment environment) {
        env = environment;
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
        return env.getRadioCore5GSpecification()
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
        return env.getRadioCore5GSpecification()
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
        return env.getRadioCore5GSpecification()
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
        return env.getRadioCore5GSpecification()
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
        env.getRadioCore5GSpecification()
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
        env.getRadioCore5GSpecification()
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

    public List<Integer> getBandTypeByName(String bandTypeName) {
        return env.getRadioCore5GSpecification()
                .given()
                .queryParam(Constants.COMMON_NAME_ATTRIBUTE, bandTypeName)
                .when()
                .get(BAND_TYPE)
                .jsonPath()
                .getList(Constants.ID);
    }

    public Long createBandType(RanBandType5GDTO ranBandType5GDTO) {
        return env.getRadioCore5GSpecification()
                .given()
                .body(ranBandType5GDTO)
                .when()
                .post(BAND_TYPE)
                .then()
                .statusCode(Response.Status.OK.getStatusCode()).assertThat()
                .log()
                .body()
                .extract()
                .as(RanBandTypeResponseDTO.class)
                .getId();
    }

    public boolean isCarriePresent(String carrierName) {
        return env.getRadioCore5GSpecification()
                .given()
                .when()
                .get(CARRIER_BY_NAME_PATH + carrierName)
                .then()
                .log()
                .status()
                .extract().response().getStatusCode() == Response.Status.OK.getStatusCode();
    }

    public void createCarrier(CarrierDTO carrierDTO) {
        env.getRadioCore5GSpecification()
                .given()
                .body(carrierDTO)
                .when()
                .post(CARRIER)
                .then()
                .statusCode(Response.Status.OK.getStatusCode()).assertThat()
                .log()
                .body()
                .extract()
                .as(CarrierResponseDTO.class)
                .getId();
    }
}
