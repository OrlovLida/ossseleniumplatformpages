package com.oss.services.fixedaccessclient;

import javax.ws.rs.core.Response;

import com.comarch.oss.inventory.fixedaccess.layer.api.dto.AccessTechnologyLayerDTO;
import com.oss.untils.Constants;
import com.oss.untils.Environment;

public class AccessTechnologyClient {

    private static final String ACCESS_TECH_MANAGEMENT_PATH_V2 = "technologies/v2";
    private static AccessTechnologyClient instance;
    private final Environment env;

    public AccessTechnologyClient(Environment environment) {
        env = environment;
    }

    public static AccessTechnologyClient getInstance(Environment pEnvironment) {
        if (instance != null) {
            return instance;
        }
        instance = new AccessTechnologyClient(pEnvironment);
        return instance;
    }

    public AccessTechnologyLayerDTO[] getAllAccessTechnologies() {
        return env.getFixedAccessCoreSpecification()
                .given()
                .queryParam(Constants.PERSPECTIVE, Constants.LIVE)
                .when()
                .get(AccessTechnologyClient.ACCESS_TECH_MANAGEMENT_PATH_V2)
                .then()
                .log().body()
                .statusCode(Response.Status.OK.getStatusCode()).assertThat()
                .extract()
                .as(AccessTechnologyLayerDTO[].class);
    }

}
