package com.oss.repositories;

import com.comarch.oss.addressinventory.api.dto.AddressDTO;
import com.comarch.oss.addressinventory.api.dto.AddressItemDTO;
import com.comarch.oss.addressinventory.api.dto.AddressItemSearchResultDTO;
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

    public String getOrCreateCountry(String countryName) {
        AddressClient client = new AddressClient(env);
        AddressItemSearchResultDTO addressItemSearchResultDTO = client.getCountriesUrlList(countryName);
        List<String> countryUrls = addressItemSearchResultDTO.getFoundItemsUris();
        if (!countryUrls.isEmpty()) {
            String countryId = countryUrls.get(0).toString().substring(52, 60);
            return countryId;
        } else {
            List<AddressItemDTO> list = new ArrayList<>();
            list.add(buildCountry(countryName));
            String countryId = client.createCountry(list);
            return countryId;
        }
    }

    public void getOrCreatePostalCode(String countryId, String postalCodeName) {
        AddressClient client = new AddressClient(env);
        AddressItemSearchResultDTO addressItemSearchResultDTO = client.getCountriesUrlList(postalCodeName);
        List<String> postalCodeUrls = addressItemSearchResultDTO.getFoundItemsUris();
        if (!postalCodeUrls.isEmpty()) {
        } else {
            List<AddressItemDTO> list = new ArrayList<>();
            list.add(buildPostalCode(postalCodeName, countryId));
            client.createPostalCode(list);
        }
    }

    public void getOrCreateCity(String countryId, String cityName) {
        AddressClient client = new AddressClient(env);
        AddressItemSearchResultDTO addressItemSearchResultDTO = client.getCountriesUrlList(cityName);
        List<String> cityUrls = addressItemSearchResultDTO.getFoundItemsUris();
        if (!cityUrls.isEmpty()) {
        } else {
            List<AddressItemDTO> list = new ArrayList<>();
            list.add(buildCity(cityName, countryId));
            client.createCity(list);
        }
    }

    private GeographicalAddressDTO buildAddress(String countryName, String countryId, String postalCodeName, String cityName) {
        return GeographicalAddressDTO.builder()
                .addressItems(getAddressItemNames(countryName, countryId, postalCodeName, cityName))
                .build();
    }

    private List<AddressItemDTO> getAddressItemNames(String countryName, String countryId, String postalCodeName, String cityName) {
        List<AddressItemDTO> list = new ArrayList<>();
        list.add(buildCountry(countryName));
        list.add(buildPostalCode(postalCodeName, countryId));
        list.add(buildCity(cityName, countryId));
        return list;
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
