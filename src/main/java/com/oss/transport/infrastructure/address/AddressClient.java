package com.oss.transport.infrastructure.address;

import com.comarch.oss.addressinventory.api.dto.AddressDTO;
import com.comarch.oss.addressinventory.api.dto.AddressSearchResultDTO;
import com.comarch.oss.addressinventory.api.dto.GeographicalAddressDTO;
import com.jayway.restassured.http.ContentType;
import com.oss.transport.infrastructure.EnvironmentRequestClient;

import javax.ws.rs.core.Response.Status;
import java.util.Optional;

public class AddressClient {

    private final EnvironmentRequestClient requestClient;

    public AddressClient(EnvironmentRequestClient requestClient) {
        this.requestClient = requestClient;
    }

    public Long createGeographicalAddress(GeographicalAddressDTO dto) {
        AddressDTO[] createdAddresses = requestClient.getAddressCoreRequestSpecification()
                .given().contentType(ContentType.JSON).body(dto)
                .when().post(getAddressPath())
                .then().log().status().log().body()
                .assertThat().statusCode(Status.OK.getStatusCode()).contentType(ContentType.JSON)
                .extract().as(AddressDTO[].class);
        return createdAddresses[0].getId();
    }

    public Optional<Long> searchGeographicalAddress(GeographicalAddressDTO dto) {
        AddressSearchResultDTO searchResult = requestClient.getAddressCoreRequestSpecification()
                .given().contentType(ContentType.JSON).body(dto)
                .when().post(getAddressSearchPath())
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

    private String getAddressPath() {
        return "/geographicaladdress";
    }

    private String getAddressSearchPath() {
        return "/geographicaladdress/search?exact=true";
    }

    private String getAddressDeletePath(Long id) {
        return String.format("/geographicaladdress/%s", id);
    }

}
