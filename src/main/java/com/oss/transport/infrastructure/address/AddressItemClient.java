package com.oss.transport.infrastructure.address;

import com.comarch.oss.addressinventory.api.dto.AddressItemBrowseDTO;
import com.comarch.oss.addressinventory.api.dto.AddressItemDTO;
import com.comarch.oss.addressinventory.api.dto.AddressItemSearchResultDTO;
import com.comarch.oss.addressinventory.api.dto.AddressItemTypeDTO;
import com.google.common.collect.ImmutableList;
import com.jayway.restassured.http.ContentType;
import com.oss.transport.infrastructure.EnvironmentRequestClient;

import javax.ws.rs.core.Response.Status;
import java.net.URI;
import java.util.Arrays;
import java.util.Collection;
import java.util.Optional;

public class AddressItemClient {

    private final EnvironmentRequestClient requestClient;

    public AddressItemClient(EnvironmentRequestClient requestClient) {
        this.requestClient = requestClient;
    }

    public AddressItemDTO createAddressItem(AddressItemDTO dto) {
        URI[] uris = requestClient.getAddressCoreRequestSpecification()
                .given().contentType(ContentType.JSON).body(ImmutableList.of(dto))
                .when().post(getAddressItemPath())
                .then().log().status().log().body()
                .assertThat().statusCode(Status.OK.getStatusCode()).contentType(ContentType.JSON)
                .extract().as(URI[].class);
        AddressItemBrowseDTO addressItemBrowse = requestClient.prepareRequestSpecificationWithoutUri()
                .when().get(uris[0])
                .then().log().status().log().body()
                .assertThat().contentType(ContentType.JSON)
                .extract().as(AddressItemBrowseDTO[].class)[0];
        AddressItemDTO newAddressItem = addressItemBrowse.getAddressItem().get();
        return AddressItemDTO.builder().from(newAddressItem).directParentId(dto.getDirectParentId()).build();
    }

    public Optional<AddressItemDTO> getAddressItemByNameAndParentId(String name, String type, Optional<Long> parentId) {
        AddressItemSearchResultDTO foundAddresses = requestClient.getAddressCoreRequestSpecification()
                .when().get(getAddressItemSearchPath(name, type, parentId))
                .then().log().status().log().body()
                .assertThat().statusCode(Status.OK.getStatusCode()).contentType(ContentType.JSON)
                .extract().as(AddressItemSearchResultDTO.class);
        Optional<String> addressUri = foundAddresses.getFoundItemsUris().stream().findFirst();
        Optional<AddressItemDTO> addressItem = addressUri.map(this::getAddressItem);
        return addressItem.map(item -> AddressItemDTO.builder().from(item).directParentId(parentId.map(String::valueOf)).build());
    }

    private AddressItemDTO getAddressItem(String uri) {
        return requestClient.prepareRequestSpecificationWithoutUri().given()
                .when().get(uri)
                .then().contentType(ContentType.JSON).extract().as(AddressItemBrowseDTO[].class)[0].getAddressItem().get();
    }

    public Collection<AddressItemTypeDTO> getAddressItemTypes() {
        AddressItemTypeDTO[] results = requestClient.getAddressCoreRequestSpecification()
                .when().get(getAddressItemTypePath())
                .then().log().status().log().body()
                .assertThat().statusCode(Status.OK.getStatusCode()).contentType(ContentType.JSON)
                .extract().as(AddressItemTypeDTO[].class);
        return Arrays.asList(results);
    }

    public void deleteAddressItem(Long id) {
        requestClient.getAddressCoreRequestSpecification()
                .when().delete(getAddressItemDeletePath(id))
                .then().log().status().log().body()
                .assertThat().statusCode(Status.ACCEPTED.getStatusCode());
    }

    private String getAddressItemPath() {
        return "/addressitem";
    }

    private String getAddressItemSearchPath(String name, String type, Optional<Long> parentId) {
        String path = "/addressitem?rsql=type=='" + type + "';name=='" + name + "'";
        if (parentId.isPresent()) {
            path += ";parent==" + parentId.get();
        }
        return path;
    }

    private String getAddressItemTypePath() {
        return "/addressitemtypes";
    }

    private String getAddressItemDeletePath(Long id) {
        return String.format("/addressitem/%s", id);
    }

}
