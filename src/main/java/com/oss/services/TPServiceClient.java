package com.oss.services;

import com.comarch.oss.transport.tpt.tp.api.dto.TerminationPointDetailDTO;
import com.oss.untils.Constants;
import com.oss.untils.Environment;

public class TPServiceClient {

    private static final String TERMINATION_POINT_API_PATH = "/device/{deviceId}";

    private static TPServiceClient instance;
    private final Environment env;

    private TPServiceClient(Environment environment) {
        env = environment;
    }

    public static TPServiceClient getInstance(Environment pEnvironment) {
        if (instance != null) {
            return instance;
        }
        instance = new TPServiceClient(pEnvironment);
        return instance;
    }

    private TerminationPointDetailDTO getDeviceStructure(Long deviceId){
        String devicePath = String.format(TERMINATION_POINT_API_PATH, deviceId);
        return env.getTPServiceSpecification()
                .given()
                .queryParam(Constants.PERSPECTIVE, Constants.LIVE)
                .when()
                .get(devicePath)
                .then()
                .extract()
                .as(TerminationPointDetailDTO.class);
    }
}
