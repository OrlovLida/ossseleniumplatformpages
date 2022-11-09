package com.oss.services;

import java.util.List;

import javax.ws.rs.core.Response;

import com.comarch.oss.radio.api.dto.BaseStationResponseIdDTO;
import com.comarch.oss.radio.api.dto.CarrierDTO;
import com.comarch.oss.radio.api.dto.CarrierResponseDTO;
import com.comarch.oss.radio.api.dto.Cell4gDTO;
import com.comarch.oss.radio.api.dto.CellResponseIdDTO;
import com.comarch.oss.radio.api.dto.EnodeBDTO;
import com.comarch.oss.radio.api.dto.HostRelationDTO;
import com.comarch.oss.radio.api.dto.RanBandType4GDTO;
import com.comarch.oss.radio.api.dto.RanBandTypeResponseDTO;
import com.jayway.restassured.http.ContentType;
import com.oss.untils.Constants;
import com.oss.untils.Environment;

public class Radio4gClient {

    private static final String E_NODE_API_PATH = "/enodeb";
    private static final String CELL_API_PATH = "/enodeb/%s/cell";
    private static final String HOST_RELATION_E_NODE_API_PATH = "/enodeb/%s/hostrelation";
    private static final String HOST_RELATION_CELL_API_PATH = "/enodeb/%1$s/cell/%2$s/hostrelation";
    private static final String BAND_TYPE = "bandtype";
    private static final String CARRIER = "carrier";
    private static final String CARRIER_BY_NAME_PATH = "carrier/name/";

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

    public void createHRENodeB(HostRelationDTO hRENodeB, Long eNodeId) {
        String eNodeBHRPath = String.format(HOST_RELATION_E_NODE_API_PATH, eNodeId);
        env.getRadioCore4GSpecification()
                .given()
                .contentType(ContentType.JSON)
                .body(hRENodeB)
                .when()
                .queryParam(Constants.PERSPECTIVE, Constants.LIVE)
                .queryParam(Constants.ID, eNodeId)
                .post(eNodeBHRPath)
                .then()
                .statusCode(Response.Status.NO_CONTENT.getStatusCode()).assertThat();
    }

    public void createHRCell(HostRelationDTO hRENodeB, Long eNodeId, Long cellId) {
        String cellHRPath = String.format(HOST_RELATION_CELL_API_PATH, eNodeId, cellId);
        env.getRadioCore4GSpecification()
                .given()
                .contentType(ContentType.JSON)
                .body(hRENodeB)
                .when()
                .queryParam(Constants.PERSPECTIVE, Constants.LIVE)
                .queryParam(Constants.ID, eNodeId)
                .queryParam(Constants.CELL_ID, cellId)
                .post(cellHRPath)
                .then()
                .statusCode(Response.Status.NO_CONTENT.getStatusCode()).assertThat();
    }

    public Long getOrCreateBandType(String bandTypeName, int dLfrequencyStart, int dLfrequencyEnd, int uLfrequencyStart, int uLfrequencyEnd) {
        List<Integer> bandTypeIds = instance.getBandTypeByName(bandTypeName);
        if (bandTypeIds.isEmpty()) {
            RanBandType4GDTO ranBandType4GDTO = RanBandType4GDTO.builder()
                    .name(bandTypeName)
                    .dLfrequencyStart(dLfrequencyStart)
                    .dLfrequencyEnd(dLfrequencyEnd)
                    .uLfrequencyStart(uLfrequencyStart)
                    .uLfrequencyEnd(uLfrequencyEnd)
                    .build();
            return createBandType(ranBandType4GDTO);
        }
        return Long.valueOf(bandTypeIds.get(0));
    }

    public List<Integer> getBandTypeByName(String bandTypeName) {
        return env.getRadioCore4GSpecification()
                .given()
                .queryParam(Constants.COMMON_NAME_ATTRIBUTE, bandTypeName)
                .when()
                .get(BAND_TYPE)
                .jsonPath()
                .getList(Constants.ID);
    }

    public Long createBandType(RanBandType4GDTO ranBandType4GDTO) {
        return env.getRadioCore4GSpecification()
                .given()
                .body(ranBandType4GDTO)
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

    public void getOrCreateCarrier(String carrierName, int downlinkChannel, int uplinkChannel, int dlCentreFrequency, int ulCentreFrequency, Long bandTypeId) {
        if (!instance.isCarriePresent(carrierName)) {
            CarrierDTO carrierDTO = CarrierDTO.builder()
                    .name(carrierName)
                    .downlinkChannel(downlinkChannel)
                    .uplinkChannel(uplinkChannel)
                    .dlCentreFrequency(dlCentreFrequency)
                    .ulCentreFrequency(ulCentreFrequency)
                    .bandTypeId(bandTypeId)
                    .build();
            createCarrier(carrierDTO);
        }
    }

    public boolean isCarriePresent(String carrierName) {
        return env.getRadioCore4GSpecification()
                .given()
                .when()
                .get(CARRIER_BY_NAME_PATH + carrierName)
                .then()
                .log()
                .status()
                .extract().response().getStatusCode() == Response.Status.OK.getStatusCode();
    }

    public void createCarrier(CarrierDTO carrierDTO) {
        env.getRadioCore4GSpecification()
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
