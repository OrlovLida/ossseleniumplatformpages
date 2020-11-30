package com.oss.services;

import com.comarch.oss.addressinventory.api.dto.AddressDTO;
import com.comarch.oss.addressinventory.api.dto.AddressItemDTO;
import com.comarch.oss.addressinventory.api.dto.GeographicalAddressDTO;
import com.jayway.restassured.http.ContentType;
import com.oss.untils.Constants;
import com.oss.untils.Environment;

import javax.ws.rs.core.Response;
import java.util.List;

/**
 * @author Milena Miętkiewicz
 */

public class AddressClient {

    private static final String GEOGRAPHICAL_ADDRESS_API_PATH = "/geographicaladdress";
    private static final String ADDRESS_ITEM_API_PATH = "/addressitem";
    private static final String ADDRESS_ITEM_IDS_API_PATH = "/addressitem/{ids}";
    private static AddressClient instance;
    private final Environment ENV;

    public AddressClient(Environment environment) {
        ENV = environment;
    }

    public static AddressClient getInstance(Environment pEnvironment) {
        if (instance != null) {
            return instance;
        }
        instance = new AddressClient(pEnvironment);
        return instance;
    }

    public AddressDTO[] createGeographicalAddress(GeographicalAddressDTO address) {
        return ENV.getAddressCoreRequestSpecification()
                .given()
                .contentType(ContentType.JSON)
                .body(address)
                .when()
                .post(GEOGRAPHICAL_ADDRESS_API_PATH)
                .then()
                .statusCode(Response.Status.OK.getStatusCode()).assertThat()
                .extract()
                .as(AddressDTO[].class);
    }

    public String createCountry(List<AddressItemDTO> country) {
        com.jayway.restassured.response.Response response = ENV.getAddressCoreRequestSpecification()
                .given()
                .contentType(ContentType.JSON)
                .body(country)
                .when()
                .post(ADDRESS_ITEM_API_PATH);
        List<String> addressItemsUrlLinks = response.jsonPath().get();
        String countryId = addressItemsUrlLinks.get(0).toString().substring(52, 60);
        return countryId;
    }

    public void createPostalCode(List<AddressItemDTO> postalCode) {
        ENV.getAddressCoreRequestSpecification()
                .given()
                .contentType(ContentType.JSON)
                .body(postalCode)
                .when()
                .post(ADDRESS_ITEM_API_PATH)
                .then()
                .statusCode(Response.Status.OK.getStatusCode()).assertThat();
    }

    public void createCity(List<AddressItemDTO> city) {
        ENV.getAddressCoreRequestSpecification()
                .given()
                .contentType(ContentType.JSON)
                .body(city)
                .when()
                .post(ADDRESS_ITEM_API_PATH)
                .then()
                .statusCode(Response.Status.OK.getStatusCode()).assertThat();
    }

    public List<String> getCountryNames(String existingCountryId) {
        com.jayway.restassured.response.Response response = ENV.getAddressCoreRequestSpecification()
                .given()
                .pathParam(Constants.IDS, (existingCountryId))
                .queryParam(Constants.PERSPECTIVE, Constants.LIVE)
                .when()
                .get(AddressClient.ADDRESS_ITEM_IDS_API_PATH);
        List<String> nameList = response.jsonPath().getList("addressItem.name");
        return nameList;
    }

    public List<String> getPostalCodeNames(String existingPostalCodeId) {
        com.jayway.restassured.response.Response response = ENV.getAddressCoreRequestSpecification()
                .given()
                .pathParam(Constants.IDS, (existingPostalCodeId))
                .queryParam(Constants.PERSPECTIVE, Constants.LIVE)
                .when()
                .get(AddressClient.ADDRESS_ITEM_IDS_API_PATH);
        List<String> nameList = response.jsonPath().getList("addressItem.name");
        return nameList;
    }

    public List<String> getCityNames(String existingCityId) {
        com.jayway.restassured.response.Response response = ENV.getAddressCoreRequestSpecification()
                .given()
                .pathParam(Constants.IDS, (existingCityId))
                .queryParam(Constants.PERSPECTIVE, Constants.LIVE)
                .when()
                .get(AddressClient.ADDRESS_ITEM_IDS_API_PATH);
        List<String> nameList = response.jsonPath().getList("addressItem.name");
        return nameList;
    }

}
