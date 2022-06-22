package com.oss.transport.infrastructure.address;

import java.util.Optional;

import javax.ws.rs.core.Response.Status;

import com.comarch.oss.addressinventory.api.dto.AddressDTO;
import com.comarch.oss.addressinventory.api.dto.AddressSearchResultDTO;
import com.comarch.oss.addressinventory.api.dto.GeographicalAddressDTO;
import com.jayway.restassured.http.ContentType;
import com.oss.transport.infrastructure.EnvironmentRequestClient;

public class AddressClient {

    private static final String ADDRESS_PATH = "/geographicaladdress";
    private static final String ADDRESS_SEARCH_PATH = "/geographicaladdress/search?exact=true";
    private final EnvironmentRequestClient requestClient;

    public AddressClient(EnvironmentRequestClient requestClient) {
        this.requestClient = requestClient;
    }

    public Long createGeographicalAddress(GeographicalAddressDTO dto) {
        AddressDTO[] createdAddresses = requestClient.getAddressCoreRequestSpecification()
                .given().contentType(ContentType.JSON).body(dto)
                .when().post(ADDRESS_PATH)
                .then().log().status().log().body()
                .assertThat().statusCode(Status.OK.getStatusCode()).contentType(ContentType.JSON)
                .extract().as(AddressDTO[].class);
        return createdAddresses[0].getId();
    }

    public Optional<Long> searchGeographicalAddress(GeographicalAddressDTO dto) {
        AddressSearchResultDTO searchResult = requestClient.getAddressCoreRequestSpecification()
                .given().contentType(ContentType.JSON).body(dto)
                .when().post(ADDRESS_SEARCH_PATH)
                .then().log().status().log().body()
                .assertThat().statusCode(Status.OK.getStatusCode()).contentType(ContentType.JSON)
                .extract().as(AddressSearchResultDTO.class);
        return searchResult.getGeographicalAddresses().stream().findFirst().flatMap(GeographicalAddressDTO::getId);
    }

    public void deleteGeographicalAddress(Long id) {
        requestClient.getAddressCoreRequestSpecification()
                .when().delete(getAddressDeletePath(id))
                .then().log().status().log().body()
                .assertThat().statusCode(Status.NO_CONTENT.getStatusCode());
    }

    private String getAddressDeletePath(Long id) {
        return String.format("/geographicaladdress/%s", id);
    }

}
