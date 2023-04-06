package com.oss.services;

import javax.ws.rs.core.Response;

import com.comarch.oss.transport.ipaddress.management.api.dto.IPNetworkDTO;
import com.comarch.oss.transport.ipaddress.management.api.dto.IPSubnetCreateDTO;
import com.comarch.oss.transport.ipaddress.management.api.dto.IPSubnetSearchResultDTO;
import com.oss.untils.Environment;

import static com.oss.untils.Constants.COMMON_IDENTIFIER_ATTRIBUTE;

public class IPAMClient {

    private static final String IPNETWORK_PATH = "ipnetwork";
    private static final String SUBNET_PATH = "subnet";
    private static final String SEARCH_PATH = "/search";
    private static IPAMClient instance;
    private final Environment env;

    private IPAMClient(Environment environment) {
        env = environment;
    }

    public static IPAMClient getInstance(Environment pEnvironment) {
        if (instance != null) {
            return instance;
        }
        instance = new IPAMClient(pEnvironment);
        return instance;
    }

    public boolean isIPNetworkPresent(String name) {
        return env.getIPAddressManagementServiceSpecification()
                .given()
                .when()
                .get(IPNETWORK_PATH + SEARCH_PATH + "/" + name)
                .then()
                .log()
                .status()
                .extract().response().getStatusCode() == Response.Status.OK.getStatusCode();
    }

    public boolean isIPv4SubnetPresent(String identifier) {
        return !env.getIPAddressManagementServiceSpecification()
                .given()
                .queryParam(COMMON_IDENTIFIER_ATTRIBUTE, identifier)
                .when()
                .get(SUBNET_PATH + SEARCH_PATH)
                .then()
                .log()
                .status()
                .extract().as(IPSubnetSearchResultDTO.class).getIpSubnetIds().isEmpty();
    }

    public void createIPNetwork(IPNetworkDTO ipNetworkDTO) {
        env.getIPAddressManagementServiceSpecification()
                .given()
                .body(ipNetworkDTO)
                .when()
                .post(IPNETWORK_PATH)
                .then()
                .statusCode(Response.Status.OK.getStatusCode()).assertThat();
    }

    public void createIPv4Subnet(IPSubnetCreateDTO ipSubnetCreateDTO) {
        env.getIPAddressManagementServiceSpecification()
                .given()
                .body(ipSubnetCreateDTO)
                .when()
                .post(SUBNET_PATH)
                .then()
                .statusCode(Response.Status.CREATED.getStatusCode()).assertThat();
    }
}
