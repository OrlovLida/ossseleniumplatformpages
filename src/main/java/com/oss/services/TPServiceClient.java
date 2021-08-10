package com.oss.services;

import com.comarch.oss.transport.tpt.tp.api.dto.TerminationPointDetailDTO;
import com.oss.untils.Constants;
import com.oss.untils.Environment;

import java.util.Arrays;

public class TPServiceClient {

    private static final String TERMINATION_POINT_API_PATH = "/device/%s";

    private static TPServiceClient instance;
    private final Environment env;

    public TPServiceClient(Environment environment) {
        env = environment;
    }

    public static TPServiceClient getInstance(Environment pEnvironment) {
        if (instance != null) {
            return instance;
        }
        instance = new TPServiceClient(pEnvironment);
        return instance;
    }

    public TerminationPointDetailDTO[] getDeviceConnectors(Long deviceId, String portId) {
        String devicePath = String.format(TERMINATION_POINT_API_PATH, deviceId);
        return env.getTPServiceSpecification()
                .given()
                .queryParam(Constants.PERSPECTIVE, Constants.LIVE)
                .queryParam(Constants.PORT_ID, portId)
                .when()
                .get(devicePath)
                .then()
                .log().body()
                .extract()
                .as(TerminationPointDetailDTO[].class);
    }

    public String getConnectorId(Long deviceId, String portId, String connectorName) {
        return Arrays.stream(getDeviceConnectors(deviceId, portId))
               .filter(e -> (e.getName()
               .equals(connectorName)))
               .map(e -> e.getId())
               .findAny()
               .get()
               .toString();
    }

    public Long getAccessInterfaceId(Long deviceId, String portId, String layerName) {
        return Arrays.stream(getDeviceConnectors(deviceId, portId))
               .filter(e -> (e.getLayer().equals(layerName)))
               .map(e -> e.getId())
               .findAny()
               .get();
    }
}
