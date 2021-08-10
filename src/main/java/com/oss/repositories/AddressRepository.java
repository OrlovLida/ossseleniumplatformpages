package com.oss.repositories;

import com.comarch.oss.addressinventory.api.dto.AddressDTO;
import com.comarch.oss.addressinventory.api.dto.AddressItemDTO;
import com.comarch.oss.addressinventory.api.dto.GeographicalAddressDTO;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.oss.services.AddressClient;
import com.oss.untils.Environment;

import java.util.Arrays;
import java.util.Optional;


/**
 * @author Milena Miętkiewicz
 */

@JsonIgnoreProperties(ignoreUnknown = true)
public class AddressRepository {

    private Environment env;

    public AddressRepository(Environment env) {
        this.env = env;
    }

    public Long updateOrCreateAddress(String countryName, String postalCodeName, String regionName, String cityName, String districtName) {
        AddressClient client = new AddressClient(env);
        AddressDTO[] addressDTO = client.createGeographicalAddress(
                buildAddress(countryName, postalCodeName, regionName, cityName, districtName));
        Long addressId = Arrays.stream(addressDTO).findAny().get().getId();
        return addressId;
    }

    public Long createOrUpdateGeographicalAddressWithStreetNumber(String countryName, String postalCodeName,
        String regionName, String cityName, String districtName, String streetNumber) {
        AddressClient client = new AddressClient(env);
        AddressDTO[] addressDTO = client.createGeographicalAddress(
                buildGeographicalAddressWithStreetNumber(countryName, postalCodeName, regionName, cityName, districtName, streetNumber));
        return Arrays.stream(addressDTO).findAny().get().getId();
    }

    public String getGeographicalAddressName(Long addressId) {
        AddressClient client = new AddressClient(env);
        GeographicalAddressDTO[] addressResponse = client.getGeographicalAddressById(addressId);
        if (!(Arrays.stream(addressResponse).findAny().get().getName().equals(Optional.empty()))) {
            String addressName = Arrays.stream(addressResponse).findAny().get().getName().toString();
            return addressName.substring(addressName.indexOf("[") + 1, addressName.indexOf("]")).trim();
        }
        return "";
    }

    public void deleteGeographicalAddress(Long geographicalAddressId) {
        AddressClient client = new AddressClient(env);
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

    private GeographicalAddressDTO buildAddress(String countryName, String postalCodeName, String regionName, String cityName, String districtName) {
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
