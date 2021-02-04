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
import java.util.Collections;
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

    public Long updateOrCreateAddressV2(String countryName, String countryId, String cityName, String regionName, String regionId) {
        AddressClient client = new AddressClient(env);
        AddressDTO[] addressDTO = client.createGeographicalAddress(buildAddressV2(countryName, countryId, cityName, regionName, regionId));
        Long addressId = Arrays.stream(addressDTO).findAny().get().getId();
        return addressId;
    }

    public String getOrCreateCountry(String countryName) {
        AddressClient client = new AddressClient(env);
        AddressItemSearchResultDTO addressItemSearchResultDTO = client.getCountriesUrlList(countryName);
        List<String> countryUrls = addressItemSearchResultDTO.getFoundItemsUris();
        if (!countryUrls.isEmpty()) {
            String countryId = countryUrls.get(0).substring(52, 60);
            return countryId;
        } else {
            List<AddressItemDTO> list = new ArrayList<>();
            list.add(buildCountry(countryName));
            String countryId = client.createCountry(list);
            return countryId;
        }
    }

    public String getOrCreateCountryV2(String countryName) {
        AddressClient client = new AddressClient(env);
        AddressItemSearchResultDTO addressItemSearchResultDTO = client.getCountriesUrlList(countryName);
        List<String> countryUrls = addressItemSearchResultDTO.getFoundItemsUris();
        if (!countryUrls.isEmpty()) {
            String countryId = countryUrls.get(0).substring(51, 59);
            return countryId;
        } else {
            List<AddressItemDTO> list = new ArrayList<>();
            list.add(buildCountry(countryName));
            String countryId = client.createCountryV2(list);
            return countryId;
        }
    }

    public String getOrCreateRegion(String countryId, String regionName) {
        AddressClient client = new AddressClient(env);
        AddressItemSearchResultDTO addressItemSearchResultDTO = client.getRegionsUrlList(regionName);
        List<String> regionUrls = addressItemSearchResultDTO.getFoundItemsUris();
        if (!regionUrls.isEmpty()) {
            String regionId = regionUrls.get(0).substring(51, 59);
            return regionId;
        } else {
            List<AddressItemDTO> list = new ArrayList<>();
            list.add(buildRegion(regionName, countryId));
            String regionId = client.createRegion(list);
            return regionId;
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

    public void getOrCreateCityV2(String regionId, String cityName) {
        AddressClient client = new AddressClient(env);
        AddressItemSearchResultDTO addressItemSearchResultDTO = client.getCountriesUrlList(cityName);
        List<String> cityUrls = addressItemSearchResultDTO.getFoundItemsUris();
        if (!cityUrls.isEmpty()) {
        } else {
            List<AddressItemDTO> list = new ArrayList<>();
            list.add(buildCity(cityName, regionId));
            client.createCity(list);
        }
    }

    private GeographicalAddressDTO buildAddress(String countryName, String countryId, String postalCodeName, String cityName) {
        return GeographicalAddressDTO.builder()
                .addressItems(getAddressItemNames(countryName, countryId, postalCodeName, cityName))
                .build();
    }

    private GeographicalAddressDTO buildAddressV2(String countryName, String countryId, String cityName, String regionName, String regionId) {
        return GeographicalAddressDTO.builder()
                .addressItems(getAddressItemNamesV2(countryName, countryId, cityName, regionName, regionId))
                .build();
    }

    private List<AddressItemDTO> getAddressItemNames(String countryName, String countryId, String postalCodeName, String cityName) {
        List<AddressItemDTO> list = new ArrayList<>();
        list.add(buildCountry(countryName));
        list.add(buildPostalCode(postalCodeName, countryId));
        list.add(buildCity(cityName, countryId));
        return list;
    }

    private List<AddressItemDTO> getAddressItemNamesV2(String countryName, String countryId, String cityName, String regionName, String regionId) {
        List<AddressItemDTO> list = new ArrayList<>();
        list.add(buildCountry(countryName));
        list.add(buildRegion(regionName, countryId));
        list.add(buildCityV2(cityName, regionId));
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

    private AddressItemDTO buildCityV2(String cityName, String regionId) {
        return AddressItemDTO.builder()
                .object("City")
                .name(cityName)
                .directParentId(regionId)
                .build();
    }

    private AddressItemDTO buildRegion(String regionName, String countryId) {
        return AddressItemDTO.builder()
                .object("Region")
                .name(regionName)
                .directParentId(countryId)
                .build();
    }

}
