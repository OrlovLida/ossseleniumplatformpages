package com.oss.services;

import com.comarch.oss.addressinventory.api.dto.AddressDTO;
import com.comarch.oss.addressinventory.api.dto.AddressItemDTO;
import com.comarch.oss.addressinventory.api.dto.AddressItemSearchResultDTO;
import com.comarch.oss.addressinventory.api.dto.GeographicalAddressDTO;
import com.jayway.restassured.http.ContentType;
import com.oss.untils.Constants;
import com.oss.untils.Environment;
import org.assertj.core.util.Lists;

import javax.ws.rs.core.Response;
import java.util.List;

import static java.net.HttpURLConnection.HTTP_NO_CONTENT;

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
                .queryParam(Constants.SHOULD_CREATE_MISSING_ADDRESS_ITEMS, true)
                .post(GEOGRAPHICAL_ADDRESS_API_PATH)
                .then()
                .statusCode(Response.Status.OK.getStatusCode()).assertThat()
                .extract()
                .as(AddressDTO[].class);
    }

    public String createAddressItem(AddressItemDTO addressItem) {
        com.jayway.restassured.response.Response response = ENV.getAddressCoreRequestSpecification()
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
        return ENV.getAddressCoreRequestSpecification()
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
        return ENV.getAddressCoreRequestSpecification()
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
        return ENV.getAddressCoreRequestSpecification()
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

}
