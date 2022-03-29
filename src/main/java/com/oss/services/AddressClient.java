package com.oss.services;

import java.util.List;

import javax.ws.rs.core.Response;

import com.comarch.oss.addressinventory.api.dto.AddressDTO;
import com.comarch.oss.addressinventory.api.dto.AddressGlobalSearchResultDTO;
import com.comarch.oss.addressinventory.api.dto.AddressItemDTO;
import com.comarch.oss.addressinventory.api.dto.AddressItemSearchResultDTO;
import com.comarch.oss.addressinventory.api.dto.GeographicalAddressDTO;
import com.google.common.collect.Lists;
import com.jayway.restassured.http.ContentType;
import com.oss.untils.Constants;
import com.oss.untils.Environment;

import static java.net.HttpURLConnection.HTTP_NO_CONTENT;

/**
 * @author Milena Miętkiewicz
 */

public class AddressClient {

    private static final String GEOGRAPHICAL_ADDRESS_API_PATH = "/geographicaladdress";
    private static final String ADDRESS_ITEM_API_PATH = "/addressitem";
    private static final String GEOGRAPHICAL_ADDRESS_SEARCH_PATH = "/search";
    private static AddressClient instance;
    private final Environment env;

    public AddressClient(Environment environment) {
        env = environment;
    }

    public static AddressClient getInstance(Environment pEnvironment) {
        if (instance != null) {
            return instance;
        }
        instance = new AddressClient(pEnvironment);
        return instance;
    }

    public AddressDTO[] createGeographicalAddress(GeographicalAddressDTO address) {
        return env.getAddressCoreRequestSpecification()
                .given()
                .contentType(ContentType.JSON)
                .body(address)
                .when()
                .queryParam(Constants.SHOULD_CREATE_MISSING_ADDRESS_ITEMS, true)
                .post(GEOGRAPHICAL_ADDRESS_API_PATH)
                .then()
                .statusCode(Response.Status.OK.getStatusCode()).assertThat()
                .extract()
                .as(AddressDTO[].class);
    }

    public String createAddressItem(AddressItemDTO addressItem) {
        com.jayway.restassured.response.Response response = env.getAddressCoreRequestSpecification()
                .given()
                .contentType(ContentType.JSON)
                .body(Lists.newArrayList(addressItem))
                .when()
                .post(ADDRESS_ITEM_API_PATH)
                .then()
                .log()
                .body()
                .extract().as(com.jayway.restassured.response.Response.class);
        List<String> addressItemsUrlLinks = response.jsonPath().get();
        String addressItemUrl = addressItemsUrlLinks.get(0);
        return addressItemUrl.substring(addressItemUrl.lastIndexOf("/") + 1);
    }

    public AddressItemSearchResultDTO getAddressItemByName(String name) {
        return env.getAddressCoreRequestSpecification()
                .given()
                .queryParam(Constants.PERSPECTIVE, Constants.LIVE)
                .queryParam(Constants.RSQL, "name=='" + name + "'")
                .when()
                .get(AddressClient.ADDRESS_ITEM_API_PATH)
                .then()
                .log().body()
                .statusCode(Response.Status.OK.getStatusCode()).assertThat()
                .extract()
                .as(AddressItemSearchResultDTO.class);
    }

    public GeographicalAddressDTO[] getGeographicalAddressById(Long addressId) {
        return env.getAddressCoreRequestSpecification()
                .given()
                .queryParam(Constants.PERSPECTIVE, Constants.LIVE)
                .when()
                .get(AddressClient.GEOGRAPHICAL_ADDRESS_API_PATH + "/" + addressId)
                .then()
                .log().body()
                .statusCode(Response.Status.OK.getStatusCode()).assertThat()
                .extract()
                .as(GeographicalAddressDTO[].class);
    }

    public com.jayway.restassured.response.Response removeGeographicalAddress(Long addressId) {
        return env.getAddressCoreRequestSpecification()
                .when()
                .delete(GEOGRAPHICAL_ADDRESS_API_PATH + "/" + addressId)
                .then()
                .log()
                .status()
                .log()
                .body()
                .statusCode(HTTP_NO_CONTENT)
                .extract()
                .response();
    }

    public List<AddressGlobalSearchResultDTO> getGeographicalAddresses() {
        AddressGlobalSearchResultDTO[] geographicalAddresses = env.getAddressCoreRequestSpecification()
                .given()
                .queryParam(Constants.TYPES, "GeographicalAddress")
                .queryParam(Constants.PERSPECTIVE, Constants.LIVE)
                .when()
                .get(GEOGRAPHICAL_ADDRESS_SEARCH_PATH)
                .then()
                .log().body()
                .statusCode(Response.Status.OK.getStatusCode()).assertThat()
                .extract()
                .as(AddressGlobalSearchResultDTO[].class);
        return Lists.newArrayList(geographicalAddresses);
    }

}
