package com.oss.reconciliation.infrastructure;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.Collection;
import java.util.Map;

import javax.ws.rs.core.UriBuilder;
import javax.ws.rs.core.UriBuilderException;

import com.jayway.restassured.response.ValidatableResponse;
import com.jayway.restassured.specification.RequestSpecification;
import com.oss.transport.infrastructure.Environment;
import com.oss.transport.infrastructure.NavigationRequestContext;
import com.oss.transport.infrastructure.ServicesClient;
import com.oss.transport.infrastructure.navigation.LinkDTO;
import com.oss.transport.infrastructure.navigation.ModuleNavigationClient;
import com.oss.transport.infrastructure.planning.PlanningContext;

public class EnvironmentRequestClient {

    private final ServicesClient servicesClient;
    private final ModuleNavigationClient navigationClient;

    public EnvironmentRequestClient(Environment environment) {
        this.servicesClient = new ServicesClient(environment);
        this.navigationClient = new ModuleNavigationClient(servicesClient);
    }

    public ValidatableResponse performOperation(NavigationRequestContext navigationRequestContext, PlanningContext planningContext) {
        LinkDTO linkDTO = navigationClient
                .getDomainOperation(navigationRequestContext.getModuleName(), navigationRequestContext.getDomainName(),
                        navigationRequestContext.getOperationName());

        String url = getUrl(navigationRequestContext.getPathParams(), navigationRequestContext.getQueryParams(), planningContext, linkDTO);
        RequestSpecification requestSpecification =
                getRequestSpecification(navigationRequestContext.getModuleName(), navigationRequestContext);

        return performByMethod(requestSpecification, url, linkDTO.getMethod());
    }

    public ValidatableResponse performOperation(NavigationRequestContext navigationRequestContext) {
        LinkDTO linkDTO = navigationClient
                .getDomainOperation(navigationRequestContext.getModuleName(), navigationRequestContext.getDomainName(),
                        navigationRequestContext.getOperationName());

        String url = getUrl(navigationRequestContext.getPathParams(), navigationRequestContext.getQueryParams(), linkDTO);
        RequestSpecification requestSpecification =
                getRequestSpecification(navigationRequestContext.getModuleName(), navigationRequestContext);

        return performByMethod(requestSpecification, url, linkDTO.getMethod());
    }

    public RequestSpecification getRequestClientByName(String name) {
        return servicesClient.getRequestSpecificationByName(name);
    }

    public RequestSpecification getResourceCatalogRequestSpecification() {
        return servicesClient.getRequestSpecificationByName("resource-catalog-core");
    }

    public RequestSpecification getIpAddressManagementRequestSpecification() {
        return servicesClient.getRequestSpecificationByName("ipaddress-management");
    }

    public RequestSpecification getTemplateFillerCoreSpecification() {
        return servicesClient.getRequestSpecificationByName("template-filler");
    }

    public RequestSpecification getSoftwareManagementCoreSpecification() {
        return servicesClient.getRequestSpecificationByName("software-management-core");
    }

    public RequestSpecification prepareRequestSpecificationWithoutUri() {
        return servicesClient.prepareRequestSpecificationWithoutUri();
    }

    public ServicesClient getServicesClient() {
        return servicesClient;
    }

    private RequestSpecification getRequestSpecification(String moduleName, NavigationRequestContext navigationRequestContext) {
        RequestSpecification requestSpecification =
                servicesClient.getRequestSpecificationByName(moduleName).given().contentType("application/json");
        if (navigationRequestContext.getDto().isPresent()) {
            return requestSpecification.body(navigationRequestContext.getDto()).when();
        }
        return requestSpecification.when();
    }

    private String getUrl(Map<String, Object> pathParams, Map<String, Object> queryParams, PlanningContext planningContext,
            LinkDTO linkDTO) {
        UriBuilder uriBuilder = UriBuilder.fromPath(linkDTO.getHref());
        planningContext.applyPlanningContext(uriBuilder);
        uriBuilder = fillUriBuilderWithPathParams(pathParams, uriBuilder);
        uriBuilder = fillUriBuilderWithQueryParams(queryParams, uriBuilder);
        String url = uriBuilder.build().toString();
        return decodePath(url);
    }

    private String getUrl(Map<String, Object> pathParams, Map<String, Object> queryParams, LinkDTO linkDTO) {
        UriBuilder uriBuilder = UriBuilder.fromPath(linkDTO.getHref());
        uriBuilder = fillUriBuilderWithPathParams(pathParams, uriBuilder);
        uriBuilder = fillUriBuilderWithQueryParams(queryParams, uriBuilder);
        String url = uriBuilder.build().toString();
        return decodePath(url);
    }

    private String decodePath(String path) {
        try {
            return URLDecoder.decode(path, "UTF-8");
        } catch (UnsupportedEncodingException | IllegalArgumentException | UriBuilderException exception) {
            throw new RuntimeException();
        }
    }

    @SuppressWarnings("rawtypes")
    private UriBuilder fillUriBuilderWithQueryParams(Map<String, Object> queryParams, UriBuilder uriBuilder) {
        for (Map.Entry<String, Object> queryParam : queryParams.entrySet()) {
            Object paramValue = queryParam.getValue();
            if (paramValue instanceof Collection) {
                uriBuilder = uriBuilder.queryParam(queryParam.getKey(), ((Collection) paramValue).toArray());
            } else {
                uriBuilder = uriBuilder.queryParam(queryParam.getKey(), queryParam.getValue());
            }
        }
        return uriBuilder;
    }

    private UriBuilder fillUriBuilderWithPathParams(Map<String, Object> pathParams, UriBuilder uriBuilder) {
        for (Map.Entry<String, Object> pathParam : pathParams.entrySet()) {
            uriBuilder = uriBuilder.resolveTemplate(pathParam.getKey(), pathParam.getValue());
        }
        return uriBuilder;
    }

    private ValidatableResponse performByMethod(RequestSpecification requestSpecification, String url, LinkDTO.MethodEnum methodType) {
        switch (methodType) {
            case DELETE:
                return performDeleteMethod(requestSpecification, url);
            case GET:
                return performGetMethod(requestSpecification, url);
            case POST:
                return performPostMethod(requestSpecification, url);
            case PUT:
                return performPutMethod(requestSpecification, url);
            default:
                throw new IllegalStateException("Unknown Method Type for given path: " + url);
        }
    }

    private ValidatableResponse performPutMethod(RequestSpecification requestSpecification, String url) {
        return requestSpecification.put(url).then().log().status().log().body();
    }

    private ValidatableResponse performPostMethod(RequestSpecification requestSpecification, String url) {
        return requestSpecification.post(url).then().log().status().log().body();
    }

    private ValidatableResponse performGetMethod(RequestSpecification requestSpecification, String url) {
        return requestSpecification.get(url).then().log().status().log().body();
    }

    private ValidatableResponse performDeleteMethod(RequestSpecification requestSpecification, String url) {
        return requestSpecification.delete(url).then().log().status().log().body();
    }
}
