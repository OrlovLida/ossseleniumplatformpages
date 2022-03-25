package com.oss.repositories;

import java.util.Arrays;
import java.util.NoSuchElementException;

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
    
    private Environment env;
    
    public AddressRepository(Environment env) {
        this.env = env;
    }
    
    public Long updateOrCreateAddress(String countryName, String postalCodeName, String regionName, String cityName, String districtName) {
        AddressClient client = new AddressClient(env);
        AddressDTO[] addressDTO = client.createGeographicalAddress(
                buildAddress(countryName, postalCodeName, regionName, cityName, districtName));
        return Arrays.stream(addressDTO).findAny().orElseThrow(() -> new NoSuchElementException("Cannot create or update Address")).getId();
    }
    
    public Long getFirstGeographicalAddressId() {
        AddressClient client = new AddressClient(env);
        return client.getGeographicalAddresses().get(0).getId();
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
