package com.oss.serviceClient;

import java.util.HashMap;
import java.util.Map;

import com.jayway.restassured.RestAssured;
import com.jayway.restassured.authentication.OAuthSignature;
import com.jayway.restassured.http.ContentType;
import com.jayway.restassured.path.json.JsonPath;
import com.jayway.restassured.response.Response;
import com.jayway.restassured.specification.RequestSpecification;

import static com.oss.configuration.Configuration.CONFIGURATION;

public class ServicesClient {
    public static final String BASIC_URL = CONFIGURATION.getUrl();
    private static Map<String, Service> services = new HashMap<>();
    private final Environment environment;
    private final TokenClient tokenClient;

    ServicesClient(Environment environment) {
        this.environment = environment;
        this.tokenClient = TokenClient.getInstance(environment);
    }

    public RequestSpecification getRequestSpecificationByName(String name) {
        RequestSpecification findApplicationBasePath = findApplicationBasePath(name);
        return prepareRequestSpecification(findApplicationBasePath);
    }

    public RequestSpecification getRequestSpecificationByNameUsingBaseURL(String name) {
        RequestSpecification findApplicationBasePath = findApplicationBasePathUsingBaseURL(name);
        return prepareRequestSpecification(findApplicationBasePath);
    }

    public RequestSpecification getRequestSpecificationByApplicationBasePath(String path) {
        RequestSpecification findApplicationBasePath = RestAssured.given()
                .baseUri(environment.getEnvironmentUrl())
                .port(environment.getEnvironmentPort())
                .basePath(path);
        return prepareRequestSpecification(findApplicationBasePath);
    }

    public RequestSpecification prepareRequestSpecificationWithoutUri() {
        RequestSpecification given = RestAssured.given();
        return prepareRequestSpecification(given);
    }

    public Boolean testService(String applicationName) {
        Response response = getServiceByName(applicationName);
        response.then().log().body().log().status();
        return response.getStatusCode() == 200;
    }

    public String getServiceAddress(String applicationName) {
        Service service = getService(applicationName);
        return String.format("%s://%s:%d%s", "http", service.host, service.port, service.url);
    }

    private RequestSpecification findApplicationBasePath(String applicationName) {
        if (!services.containsKey(applicationName)) {
            Service service = getService(applicationName);
            services.put(applicationName, service);
        }
        Service service = services.get(applicationName);
        return RestAssured.given()
                .baseUri(service.protocol + "://" + service.host)
                .port(service.port)
                .basePath(service.url);
    }

    private RequestSpecification findApplicationBasePathUsingBaseURL(String applicationName) {
        if (!services.containsKey(applicationName)) {
            Service service = getService(applicationName);
            services.put(applicationName, service);
        }
        Service service = services.get(applicationName);
        return RestAssured.given()
                .baseUri(environment.getEnvironmentUrl())
                .port(environment.getEnvironmentPort())
                .basePath(service.url);
    }

    private Service getService(String applicationName) {
        Response response = getServiceByName(applicationName);
        if (response.getStatusCode() != 200) {
            throw new RuntimeException("Service discovery lookup failed. Missing: " + applicationName + ". Status: " + response.getStatusCode());
        }

        JsonPath jsonPath = response.jsonPath();

        String host = jsonPath.getString("host");
        String url = jsonPath.getString("url");
        String protocol = jsonPath.getString("protocol");
        int port = jsonPath.getInt("port");

        return new Service(host, url, protocol, port);
    }

    private Response getServiceByName(String applicationName) {
        return RestAssured.given()
                .baseUri(environment.getEnvironmentUrl())
                .port(environment.getEnvironmentPort())
                .authentication()
                .oauth2(tokenClient.getKeycloackToken(), OAuthSignature.HEADER)
                .get("rest/discovery/v2/service/" + applicationName);
    }

    private RequestSpecification getRequestSpecification(String baseUri, Integer port, String basePath) {
        RequestSpecification requestSpec = RestAssured.given()
                .baseUri(baseUri)
                .port(port)
                .basePath(basePath);
        return prepareRequestSpecification(requestSpec);
    }

    private RequestSpecification prepareRequestSpecification(RequestSpecification requestSpecification) {
        return requestSpecification
                .authentication()
                .oauth2(tokenClient.getKeycloackToken(), OAuthSignature.HEADER)
                .contentType(ContentType.JSON)
                .log()
                .method()
                .log()
                .path()
                .log()
                .body();
    }

    private static final class Service {
        private String host;
        private String url;
        private String protocol;
        private int port;

        public Service(String host, String url, String protocol, int port) {
            this.host = host;
            this.url = url;
            this.protocol = protocol;
            this.port = port;
        }
    }
}
