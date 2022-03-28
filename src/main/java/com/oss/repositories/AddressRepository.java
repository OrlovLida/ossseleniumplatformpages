package com.oss.repositories;

import java.util.Arrays;
import java.util.Optional;

import org.apache.commons.lang3.StringUtils;

import com.comarch.oss.addressinventory.api.dto.AddressDTO;
import com.comarch.oss.addressinventory.api.dto.AddressItemDTO;
import com.comarch.oss.addressinventory.api.dto.GeographicalAddressDTO;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.oss.services.AddressClient;
import com.oss.untils.Environment;

/**
 * @author Milena Miętkiewicz
 */

@JsonIgnoreProperties(ignoreUnknown = true)
public class AddressRepository {

    private AddressClient client;

    public AddressRepository(Environment env) {
        client = new AddressClient(env);
    }

    public Long updateOrCreateAddress(String countryName, String postalCodeName, String regionName, String cityName, String districtName) {
        AddressDTO[] addressDTO = client.createGeographicalAddress(
                buildAddress(countryName, postalCodeName, regionName, cityName, districtName));
        return Arrays.stream(addressDTO)
                .findAny()
                .orElseThrow(() -> new RuntimeException("Can't find id of created address or address wasn't created properly"))
                .getId();
    }

    public Long getFirstGeographicalAddressId() {
        return client.getGeographicalAddresses().get(0).getId();
    }

    public Long createOrUpdateGeographicalAddressWithStreetNumber(String countryName, String postalCodeName,
                                                                  String regionName, String cityName, String districtName, String streetNumber) {
        AddressDTO[] addressDTO = client.createGeographicalAddress(
                buildGeographicalAddressWithStreetNumber(countryName, postalCodeName, regionName, cityName, districtName, streetNumber));
        return Arrays.stream(addressDTO)
                .findAny()
                .orElseThrow(() -> new RuntimeException("Can't find id of address or address wasn't created properly"))
                .getId();
    }

    public String getGeographicalAddressName(Long addressId) {
        GeographicalAddressDTO[] addressResponse = client.getGeographicalAddressById(addressId);
        if (!(Arrays.stream(addressResponse).findAny().orElseThrow(() -> new RuntimeException("Can't find address for id: " + addressId)).getName().equals(Optional.empty()))) {
            String addressName = Arrays.stream(addressResponse)
                    .findAny()
                    .orElseThrow(() -> new RuntimeException("Can't find address name for address id: " + addressId))
                    .getName()
                    .toString();
            return addressName.substring(addressName.indexOf("[") + 1, addressName.indexOf("]")).trim();
        }
        return StringUtils.EMPTY;
    }

    public void deleteGeographicalAddress(Long geographicalAddressId) {
        client.removeGeographicalAddress(geographicalAddressId);
    }

    private GeographicalAddressDTO buildGeographicalAddressWithStreetNumber(String countryName,
                                                                            String postalCodeName, String regionName, String cityName, String districtName, String streetNumber) {
        return GeographicalAddressDTO.builder()
                .addAddressItems(buildCountry(countryName))
                .addAddressItems(buildPostalCode(postalCodeName))
                .addAddressItems(buildRegion(regionName))
                .addAddressItems(buildCity(cityName))
                .addAddressItems(buildDistrict(districtName))
                .streetNumber(streetNumber)
                .build();
    }

    private GeographicalAddressDTO buildAddress(String countryName, String postalCodeName, String regionName, String cityName,
                                                String districtName) {
        return GeographicalAddressDTO.builder()
                .addAddressItems(buildCountry(countryName))
                .addAddressItems(buildPostalCode(postalCodeName))
                .addAddressItems(buildRegion(regionName))
                .addAddressItems(buildCity(cityName))
                .addAddressItems(buildDistrict(districtName))
                .build();
    }

    private AddressItemDTO buildCountry(String countryName) {
        return AddressItemDTO.builder()
                .object("Country")
                .name(countryName)
                .build();
    }

    private AddressItemDTO buildPostalCode(String postalCodeName) {
        return AddressItemDTO.builder()
                .object("PostalCode")
                .name(postalCodeName)
                .build();
    }

    private AddressItemDTO buildRegion(String regionName) {
        return AddressItemDTO.builder()
                .object("Region")
                .name(regionName)
                .build();
    }

    private AddressItemDTO buildCity(String cityName) {
        return AddressItemDTO.builder()
                .object("City")
                .name(cityName)
                .build();
    }

    private AddressItemDTO buildDistrict(String districtName) {
        return AddressItemDTO.builder()
                .object("District")
                .name(districtName)
                .build();
    }

}
