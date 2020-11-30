package com.oss.repositories;

import com.comarch.oss.addressinventory.api.dto.AddressDTO;
import com.comarch.oss.addressinventory.api.dto.AddressItemDTO;
import com.comarch.oss.addressinventory.api.dto.GeographicalAddressDTO;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.oss.services.AddressClient;
import com.oss.untils.Environment;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * @author Milena Miętkiewicz
 */

@JsonIgnoreProperties(ignoreUnknown = true)
public class AddressRepository {

    private Environment env;

    public AddressRepository(Environment env) {
        this.env = env;
    }

    public Long updateOrCreateAddress(String countryName, String countryId, String postalCodeName, String cityName) {
        AddressClient client = new AddressClient(env);
        AddressDTO[] addressDTO = client.createGeographicalAddress(buildAddress(countryName, countryId, postalCodeName, cityName));
        Long addressId = Arrays.stream(addressDTO).findAny().get().getId();
        return addressId;
    }

    public String[] getOrCreateCountry(String existingCountryId, String countryNameForCreate) {
        AddressClient client = new AddressClient(env);
        List<String> countryNames = client.getCountryNames(existingCountryId);
        if (!countryNames.isEmpty()) {
            String countryName = countryNames.get(0);
            return new String[]{countryName, existingCountryId};
        } else {
            List<AddressItemDTO> list = new ArrayList<>();
            list.add(buildCountry(countryNameForCreate));
            String countryId = client.createCountry(list);
            return new String[] {countryNameForCreate, countryId};
        }
    }

    public String getOrCreatePostalCode(String existingPostalCodeId, String countryId, String postalCodeNameForCreate) {
        AddressClient client = new AddressClient(env);
        List<String> postalCodeNames = client.getPostalCodeNames(existingPostalCodeId);
        if (!postalCodeNames.isEmpty()) {
            String postalCodeName = postalCodeNames.get(0);
            return postalCodeName;
        } else {
            List<AddressItemDTO> list = new ArrayList<>();
            list.add(buildPostalCode(postalCodeNameForCreate, countryId));
            client.createPostalCode(list);
            return postalCodeNameForCreate;
        }
    }

    public String getOrCreateCity(String existingCityId, String countryId, String cityNameForCreate) {
        AddressClient client = new AddressClient(env);
        List<String> cityNames = client.getCityNames(existingCityId);
        if (!cityNames.isEmpty()) {
            String cityName = cityNames.get(0);
            return cityName;
        } else {
            List<AddressItemDTO> list = new ArrayList<>();
            list.add(buildCity(cityNameForCreate, countryId));
            client.createCity(list);
            return cityNameForCreate;
        }
    }

    private List<AddressItemDTO> getAddressItemNames(String countryName, String countryId, String postalCodeName, String cityName) {
        List<AddressItemDTO> list = new ArrayList<>();
        list.add(buildCountry(countryName));
        list.add(buildPostalCode(postalCodeName, countryId));
        list.add(buildCity(cityName, countryId));
        return list;
    }

    private GeographicalAddressDTO buildAddress(String countryName, String countryId, String postalCodeName, String cityName) {
        return GeographicalAddressDTO.builder()
                .addressItems(getAddressItemNames(countryName, countryId, postalCodeName, cityName))
                .build();
    }

    private AddressItemDTO buildCountry(String countryName) {
        return AddressItemDTO.builder()
                .object("Country")
                .name(countryName)
                .build();
    }

    private AddressItemDTO buildPostalCode(String postalCodeName, String countryId) {
        return AddressItemDTO.builder()
                .object("PostalCode")
                .name(postalCodeName)
                .directParentId(countryId)
                .build();
    }

    private AddressItemDTO buildCity(String cityName, String countryId) {
        return AddressItemDTO.builder()
                .object("City")
                .name(cityName)
                .directParentId(countryId)
                .build();
    }

}
