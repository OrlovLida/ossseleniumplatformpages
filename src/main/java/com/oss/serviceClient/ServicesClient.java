package com.oss.serviceClient;

import com.jayway.restassured.RestAssured;
import com.jayway.restassured.authentication.OAuthSignature;
import com.jayway.restassured.config.RestAssuredConfig;
import com.jayway.restassured.http.ContentType;
import com.jayway.restassured.path.json.JsonPath;
import com.jayway.restassured.response.Response;
import com.jayway.restassured.specification.RequestSpecification;
import com.oss.configuration.Configuration;

import java.sql.SQLOutput;
import java.util.HashMap;
import java.util.Map;

import static com.oss.configuration.Configuration.CONFIGURATION;

public class ServicesClient {
    private static Map<String, Service> services = new HashMap<>();
    private final Environment environment;
    private final TokenClient tokenClient;
    public static final String BASIC_URL = CONFIGURATION.getUrl();
    
    ServicesClient(Environment environment) {
        this.environment = environment;
        this.tokenClient = TokenClient.getInstance(environment);
    }
    
    private RequestSpecification findApplicationBasePath(String applicationName) {
        if (!services.containsKey(applicationName)) {
            Service service = getService(applicationName);
            services.put(applicationName, service);
        }
        System.out.println("Jestem przed ostatnia czescia");
        
        Service service = services.get(applicationName);
        System.out.println("!!!!!!!!!!!serviceHOSTNaKoncu: " + service.host);
        System.out.println("!!!!!!!!!!!serviceHOSTNaKoncu: " + service.url);
        if ("https".equals(environment.getEnvironmentUrl().substring(0, 5))){
            return RestAssured.given()
                    .baseUri("https://" + service.host)
                    .port(25081)
                    .basePath(service.url);
        }
        return RestAssured.given()
                .baseUri("http://" + service.host)
                .port(service.port)
                .basePath(service.url);
    }
    
    private Service getService(String applicationName) {
        Response response = getServiceByName(applicationName);
        if (response.getStatusCode() != 200) {
            throw new RuntimeException(
                    "Service discovery lookup failed. Missing: " + applicationName + ". Status: " + response.getStatusCode());
        }
        System.out.println("!!!!!!!!!!WYCHODZE Z GETSERVICE");

        JsonPath jsonPath = response.jsonPath();
        System.out.println("???????response: " + response);
        System.out.println("??????jsonPath: " + jsonPath.prettyPrint());
        System.out.println("??????jsonPath string host: " + jsonPath.getString("host"));
        String host = jsonPath.getString("host");
        String url = jsonPath.getString("url");
        int port = jsonPath.getInt("port");

        return new Service(host, url, port);
    }

    private Response getServiceByName(String applicationName) {
        System.out.println("??????????????getServiceByName, environment.getEnvironmentUrl: " + environment.getEnvironmentUrl());
        return RestAssured.given()
                .baseUri(environment.getEnvironmentUrl())
                .port(environment.getEnvironmentPort()).authentication()
                .oauth2(tokenClient.getKeycloackToken(), OAuthSignature.HEADER)
                .get("rest/discovery/v2/service/" + applicationName);
    }

    public RequestSpecification getRequestSpecificationByName(String name) {
        RequestSpecification findApplicationBasePath = findApplicationBasePath(name);
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
        private int port;
        
        public Service(String host, String url, int port) {
            this.host = host;
            this.url = url;
            this.port = port;
        }
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
}
