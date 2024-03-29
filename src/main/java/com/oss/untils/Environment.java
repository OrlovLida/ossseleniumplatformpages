package com.oss.untils;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.base.Preconditions;
import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import com.jayway.restassured.RestAssured;
import com.jayway.restassured.authentication.OAuthSignature;
import com.jayway.restassured.http.ContentType;
import com.jayway.restassured.path.json.JsonPath;
import com.jayway.restassured.response.Response;
import com.jayway.restassured.specification.RequestSpecification;
import com.oss.configuration.Configuration;

public class Environment {

    public static final Logger LOGGER = LoggerFactory.getLogger("TOOLTIP_" + Environment.class);

    private static final String RADIO_CONNECTION = "radio-connection";
    private static final String RADIO_CORE = "radio-core";
    private static final String RADIO_CORE_3G = "radio-core-3g";
    private static final String RADIO_CORE_4G = "radio-core-4g";
    private static final String RADIO_CORE_5G = "radio-core-5g";
    private static final String LOCATION_INVENTORY_CORE = "location-inventory-core";
    private static final String PHYSICAL_INVENTORY_CORE = "physical-inventory-core";
    private static final String ADDRESS_CORE = "address-core";
    private static final String ANTENNA_ARRAY_CORE = "antenna-array-core";
    private static final String RESOURCE_CATALOG_CORE = "resource-catalog-core";
    private static final String PLANNING_CORE = "planning-core";
    private static final String VALIDATION_RESULTS = "validation-results-core";
    private static final String LOGICAL_FUNCTION_CORE = "logical-function-core";
    private static final String LOGICAL_INVENTORY_VIEW = "logical-inventory-view";
    private static final String IPADDRESS_MANAGEMENT = "ipaddress-management";
    private static final String TP_SERVICE = "tp-service";
    private static final String ETHERNET_CORE = "ethernet-core";
    private static final String CONNECTIVITY_CORE = "physical-connectivity-core";
    private static final String NFV_CORE = "nfv-core";
    private static final String NETWORK_SERVICE_CORE = "network-service-core";
    private static final String NETWORK_SLICE_CORE = "network-slice-core";
    private static final String TMF_CATALOG_CORE = "tmf-catalog-core";

    private static final String KEYCLOAK_PASS_PROP = "keycloak.pass";
    private static final String KEYCLOAK_USERNAME_PROP = "keycloak.username";
    private static final String FIXED_ACCESS_CORE = "fixed-access";
    private static final String TRAIL_CORE = "trail-core";
    private static final Integer CURRENT_TOKEN_IDENTIFIER = 1;
    private static Environment env;
    private final String keycloakUserName;
    private final String keycloakUserPassword;
    private final String basePath;

    private final Map<String, LocalService> serviceCache = new HashMap<>();
    private final LoadingCache<Integer, String> keycloakTokenCache = CacheBuilder.newBuilder()
            .maximumSize(1)
            .refreshAfterWrite(15, TimeUnit.MINUTES)
            .build(
                    new CacheLoader<Integer, String>() {
                        @Override
                        public String load(Integer tokenNumber) { // no checked exception
                            return createNewToken();
                        }
                    });

    private Environment() {
        Configuration configuration = new Configuration();
        this.basePath = configuration.getUrl();
        this.keycloakUserName = System.getProperty(KEYCLOAK_USERNAME_PROP, configuration.getLogin());
        this.keycloakUserPassword = System.getProperty(KEYCLOAK_PASS_PROP, configuration.getPassword());

        Preconditions.checkNotNull(keycloakUserName, "Provide keycloak.username java parameter");
        Preconditions.checkNotNull(keycloakUserPassword, "Provide keycloak.pass java parameter");
    }

    private Environment(String baseUrl, String userName, String password) {
        this.basePath = baseUrl;
        this.keycloakUserName = userName;
        this.keycloakUserPassword = password;
    }

    public static synchronized Environment getInstance() {
        if (env == null) {
            env = new Environment();
        }
        return env;
    }

    public static synchronized Environment getInstance(String baseUrl, String userName, String password) {
        return new Environment(baseUrl, userName, password);
    }

    public String getKeycloackToken() {
        try {
            return keycloakTokenCache.get(CURRENT_TOKEN_IDENTIFIER);
        } catch (ExecutionException e) {
            return createNewToken();
        }
    }

    public LocalService getServiceFromSDCached(String applicationName) {
        if (serviceCache.containsKey(applicationName)) {
            return serviceCache.get(applicationName);
        }
        return getServiceFromSD(applicationName);
    }

    public RequestSpecification getLocationInventoryCoreRequestSpecification() {
        return getRequestSpecificationByName(LOCATION_INVENTORY_CORE);
    }

    public RequestSpecification getPhysicalInventoryCoreRequestSpecification() {
        return getRequestSpecificationByName(PHYSICAL_INVENTORY_CORE);
    }

    public RequestSpecification getEthernetCoreRequestSpecification() {
        return getRequestSpecificationByName(ETHERNET_CORE);
    }

    public RequestSpecification getAddressCoreRequestSpecification() {
        return getRequestSpecificationByName(ADDRESS_CORE);
    }

    public RequestSpecification getAntennaSpecification() {
        return getRequestSpecificationByName(ANTENNA_ARRAY_CORE);
    }

    public RequestSpecification getRadioConnectionSpecification() {
        return getRequestSpecificationByName(RADIO_CONNECTION);
    }

    public RequestSpecification getRadioCoreSpecification() {
        return getRequestSpecificationByName(RADIO_CORE);
    }

    public RequestSpecification getRadioCore3GSpecification() {
        return getRequestSpecificationByName(RADIO_CORE_3G);
    }

    public RequestSpecification getRadioCore4GSpecification() {
        return getRequestSpecificationByName(RADIO_CORE_4G);
    }

    public RequestSpecification getRadioCore5GSpecification() {
        return getRequestSpecificationByName(RADIO_CORE_5G);
    }

    public RequestSpecification getResourceCatalogSpecification() {
        return getRequestSpecificationByName(RESOURCE_CATALOG_CORE);
    }

    public RequestSpecification getPlanningCoreSpecification() {
        return getRequestSpecificationByName(PLANNING_CORE);
    }

    public RequestSpecification getValidationResultsCoreSpecification() {
        return getRequestSpecificationByName(VALIDATION_RESULTS);
    }

    public RequestSpecification getLogicalFunctionCoreSpecification() {
        return getRequestSpecificationByName(LOGICAL_FUNCTION_CORE);
    }

    public RequestSpecification getLogicalInventoryViewSpecification() {
        return getRequestSpecificationByName(LOGICAL_INVENTORY_VIEW);
    }

    public RequestSpecification getTPServiceSpecification() {
        return getRequestSpecificationByName(TP_SERVICE);
    }

    public RequestSpecification getIPAddressManagementServiceSpecification() {
        return getRequestSpecificationByName(IPADDRESS_MANAGEMENT);
    }

    public RequestSpecification getPhysicalConnectivityCoreSpecification() {
        return getRequestSpecificationByName(CONNECTIVITY_CORE);
    }

    public RequestSpecification getFixedAccessCoreSpecification() {
        return getRequestSpecificationByName(FIXED_ACCESS_CORE);
    }

    public RequestSpecification getTrailCoreSpecification() {
        return getRequestSpecificationByName(TRAIL_CORE);
    }

    public RequestSpecification getNFVCoreSpecification() {
        return getRequestSpecificationByName(NFV_CORE);
    }

    public RequestSpecification getNetworkServiceCoreSpecification() {
        return getRequestSpecificationByName(NETWORK_SERVICE_CORE);
    }

    public RequestSpecification getNetworkSliceCoreSpecification() {
        return getRequestSpecificationByName(NETWORK_SLICE_CORE);
    }

    public RequestSpecification getTMFResourceCatalog() {
        return getRequestSpecificationByName(TMF_CATALOG_CORE);
    }

    public RequestSpecification getRequestSpecificationByName(String pName) {
        RequestSpecification findApplicationBasePath = findApplicationBasePath(pName);
        return prepareRequestSpecification(findApplicationBasePath);
    }

    public RequestSpecification prepareRequestSpecificationWithoutUri() {
        RequestSpecification given = RestAssured.given();
        return prepareRequestSpecification(given);
    }

    public void closeSession() {
        if (Objects.isNull(getKeycloackToken())) {
            LOGGER.warn("There is no session to close!");
            return;
        }
        findApplicationBasePath("keycloak-auth-service")
                .basePath("/auth/realms/OSS/protocol/openid-connect/logout")
                .contentType(ContentType.URLENC)
                .content("refresh_token=" + getKeycloackToken() + "&client_id=JBossCore")
                .post();
        LOGGER.info("Closed session: {}", getKeycloackToken());
    }

    private String createNewToken() {
        Response response = RestAssured.given().relaxedHTTPSValidation()
                .baseUri(basePath)
                .contentType(ContentType.URLENC)
                .content("username=" + keycloakUserName + "&password=" + keycloakUserPassword
                        + "&client_id=JBossCore&grant_type=password")
                .post("auth/realms/OSS/protocol/openid-connect/token");

        return response.body()
                .jsonPath()
                .getString("access_token");
    }

    protected RequestSpecification findApplicationBasePath(String applicationName) {
        LocalService service = getServiceFromSDCached(applicationName);

        return RestAssured.given()
                .baseUri("http://" + service.getHost())
                .port(service.getPort())
                .basePath(service.getUrl());
    }

    private LocalService getServiceFromSD(String applicationName) {
        LocalService service = new LocalService();
        Response response = RestAssured.given()
                .baseUri(basePath)
                .contentType(ContentType.URLENC)
                .authentication().oauth2(getKeycloackToken(), OAuthSignature.HEADER)
                .contentType(ContentType.JSON)
                .get("rest/discovery/v2/service/" + applicationName);

        if (response.getStatusCode() != 200) {
            throw new RuntimeException(
                    "Service discovery lookup failed. Missing: " + applicationName + ". Status: " + response.getStatusCode());
        }

        JsonPath jsonPath = response.jsonPath();
        service.setHost(jsonPath.getString("host"));
        service.setUrl(jsonPath.getString("url"));
        service.setPort(jsonPath.getInt("port"));

        serviceCache.put(applicationName, service);
        return service;
    }

    private RequestSpecification prepareRequestSpecification(RequestSpecification pRequestSpecification) {
        return pRequestSpecification
                .authentication()
                .oauth2(getKeycloackToken(), OAuthSignature.HEADER)
                .contentType(ContentType.JSON)
                .log()
                .method()
                .log()
                .path()
                .log()
                .body();
    }

    public static class LocalService {
        private String host;
        private String url;
        private int port;

        public int getPort() {
            return port;
        }

        public void setPort(int port) {
            this.port = port;
        }

        public String getUrl() {
            return url;
        }

        public void setUrl(String url) {
            this.url = url;
        }

        public String getHost() {
            return host;
        }

        public void setHost(String host) {
            this.host = host;
        }
    }
}
