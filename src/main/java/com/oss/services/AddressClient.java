package com.oss.services;

import com.comarch.oss.addressinventory.api.dto.AddressDTO;
import com.comarch.oss.addressinventory.api.dto.AddressItemDTO;
import com.comarch.oss.addressinventory.api.dto.AddressItemSearchResultDTO;
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

    public AddressItemSearchResultDTO getCountriesUrlList(String countryName) {
        return ENV.getAddressCoreRequestSpecification()
                .given()
                .queryParam(Constants.PERSPECTIVE, Constants.LIVE)
                .queryParam(Constants.RSQL, "name=='" + countryName + "'")
                .when()
                .get(AddressClient.ADDRESS_ITEM_API_PATH)
                .then()
                .statusCode(Response.Status.OK.getStatusCode()).assertThat()
                .extract()
                .as(AddressItemSearchResultDTO.class);
    }

    public void getPostalCodeUrlList(String postalCodeName) {
        ENV.getAddressCoreRequestSpecification()
                .given()
                .queryParam(Constants.PERSPECTIVE, Constants.LIVE)
                .queryParam(Constants.RSQL, "name=='" + postalCodeName + "'")
                .when()
                .get(AddressClient.ADDRESS_ITEM_API_PATH)
                .then()
                .statusCode(Response.Status.OK.getStatusCode()).assertThat();
    }

    public void getCityUrlList(String cityName) {
        ENV.getAddressCoreRequestSpecification()
                .given()
                .queryParam(Constants.PERSPECTIVE, Constants.LIVE)
                .queryParam(Constants.RSQL, "name=='" + cityName + "'")
                .when()
                .get(AddressClient.ADDRESS_ITEM_API_PATH)
                .then()
                .statusCode(Response.Status.OK.getStatusCode()).assertThat();
    }

}
