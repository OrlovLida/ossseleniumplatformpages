package com.oss.services;

import java.util.Arrays;
import java.util.List;

import com.comarch.oss.transport.tpt.tp.api.dto.TerminationPointDetailDTO;
import com.comarch.oss.transport.tpt.tp.api.dto.TerminationPointResultDTO;
import com.oss.framework.utils.DelayUtils;
import com.oss.untils.Constants;
import com.oss.untils.Environment;

public class TPServiceClient {

    private static final String TERMINATION_POINT_API_PATH = "/device/%s";
    private static final String TERMINATION_POINT_API_PATH_V2 = "/device/%s/v2";

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
        DelayUtils.sleep(15000);
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
                .map(TerminationPointDetailDTO::getId)
                .findAny()
                .orElseThrow(() -> new RuntimeException("Cant find termination point with name " + connectorName + " on port id " + portId))
                .toString();

    }

    public Long getAccessInterfaceId(Long deviceId, String portId, String layerName) {
        return Arrays.stream(getDeviceConnectors(deviceId, portId))
                .filter(e -> (e.getLayer().equals(layerName)))
                .map(TerminationPointDetailDTO::getId)
                .findAny()
                .orElseThrow(() -> new RuntimeException("Cant find AccessInterface with layer name " + layerName + " on port id " + portId));
    }

    public List<TerminationPointDetailDTO> getTerminationPointDetailList(Long deviceId) {
        String devicePath = String.format(TERMINATION_POINT_API_PATH_V2, deviceId);
        return env.getTPServiceSpecification()
                .given()
                .queryParam(Constants.PERSPECTIVE, Constants.LIVE)
                .when()
                .get(devicePath)
                .then()
                .log().body()
                .extract()
                .as(TerminationPointResultDTO.class)
                .getTerminationPoints();
    }
}
