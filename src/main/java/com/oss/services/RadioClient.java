package com.oss.services;

import java.util.List;

import javax.ws.rs.core.Response;

import com.comarch.oss.radio.api.dto.MccMncDataDTO;
import com.comarch.oss.radio.api.dto.MccMncIdentificationDTO;
import com.oss.untils.Environment;

public class RadioClient {

    private static final String OPERATOR_NAME = "operatorName";
    private static final String BY_OPERATOR_PATH = "mccmnc/search/operator";
    private static final String ID = "id";
    private static final String MCC_MNC = "mccmnc";

    private static RadioClient instance;
    private final Environment env;

    private RadioClient(Environment environment) {
        env = environment;
    }

    public static RadioClient getInstance(Environment pEnvironment) {
        if (instance != null) {
            return instance;
        }
        instance = new RadioClient(pEnvironment);
        return instance;
    }

    public Long getOrCreateMccMnc(String operator, String mcc, String mnc) {
        List<Integer> operatorIds = instance.getMccMncByOperator(operator);
        if (operatorIds.isEmpty()) {
            MccMncDataDTO mccMncDataDTO = MccMncDataDTO.builder()
                    .operator(operator)
                    .mcc(mcc)
                    .mnc(mnc)
                    .build();
            return createMccMnc(mccMncDataDTO);
        }
        return Long.valueOf(operatorIds.get(0));
    }

    public List<Integer> getMccMncByOperator(String operator) {
        return env.getRadioCoreSpecification()
                .given()
                .queryParam(OPERATOR_NAME, operator)
                .when()
                .get(BY_OPERATOR_PATH)
                .jsonPath()
                .getList(ID);
    }

    public Long createMccMnc(MccMncDataDTO mccMncDataDTO) {
        return env.getRadioCoreSpecification()
                .given()
                .body(mccMncDataDTO)
                .when()
                .put(MCC_MNC)
                .then()
                .statusCode(Response.Status.OK.getStatusCode()).assertThat()
                .log()
                .body()
                .extract()
                .as(MccMncIdentificationDTO.class)
                .getId();
    }
}
