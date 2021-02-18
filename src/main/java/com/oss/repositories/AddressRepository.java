package com.oss.repositories;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import com.comarch.oss.addressinventory.api.dto.AddressDTO;
import com.comarch.oss.addressinventory.api.dto.AddressItemDTO;
import com.comarch.oss.addressinventory.api.dto.AddressItemSearchResultDTO;
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
    
    public Long updateOrCreateAddress(String countryName, String countryId, String cityName, String cityId, String regionName,
            String regionId, String districtName) {
        AddressClient client = new AddressClient(env);
        AddressDTO[] addressDTO = client.createGeographicalAddress(
                buildAddress(countryName, countryId, cityName, cityId, regionName, regionId, districtName));
        Long addressId = Arrays.stream(addressDTO).findAny().get().getId();
        return addressId;
    }
    
    public String getOrCreateCountry(String countryName) {
        AddressClient client = new AddressClient(env);
        AddressItemSearchResultDTO addressItemSearchResultDTO = client.getCountriesUrlList(countryName);
        List<String> countryUrls = addressItemSearchResultDTO.getFoundItemsUris();
        if (!countryUrls.isEmpty()) {
            String countryUrl = countryUrls.get(0);
            return countryUrl.substring(countryUrl.lastIndexOf("/") + 1, countryUrl.length());
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
            String countryUrl = countryUrls.get(0);
            return countryUrl.substring(countryUrl.lastIndexOf("/") + 1, countryUrl.length());
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
            String regionUrl = regionUrls.get(0);
            return regionUrl.substring(regionUrl.lastIndexOf("/") + 1, regionUrl.length());
        } else {
            List<AddressItemDTO> list = new ArrayList<>();
            list.add(buildRegion(regionName, countryId));
            String regionId = client.createRegion(list);
            return regionId;
        }
    }
    
    public String getOrCreateDistrict(String countryId, String districtName) {
        AddressClient client = new AddressClient(env);
        AddressItemSearchResultDTO addressItemSearchResultDTO = client.getAddressItemByName(districtName);
        List<String> regionUrls = addressItemSearchResultDTO.getFoundItemsUris();
        if (!regionUrls.isEmpty()) {
            String districtUrl = regionUrls.get(0);
            return districtUrl.substring(districtUrl.lastIndexOf("/") + 1, districtUrl.length());
        }
        AddressItemDTO districtDTO = buildDistrict(districtName, countryId);
        return client.createAddressItem(districtDTO);
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
    
    public String getOrCreateCityV2(String regionId, String cityName) {
        AddressClient client = new AddressClient(env);
        AddressItemSearchResultDTO addressItemSearchResultDTO = client.getCountriesUrlList(cityName);
        List<String> cityUrls = addressItemSearchResultDTO.getFoundItemsUris();
        if (!cityUrls.isEmpty()) {
            String cityUrl = cityUrls.get(0);
            return cityUrl.substring(cityUrl.lastIndexOf("/") + 1, cityUrl.length());
        }
        AddressItemDTO cityDTO = buildCity(cityName, regionId);
        return client.createAddressItem(cityDTO);
    }
    
    private GeographicalAddressDTO buildAddress(String countryName, String countryId, String postalCodeName, String cityName) {
        return GeographicalAddressDTO.builder()
                .addressItems(getAddressItemNames(countryName, countryId, postalCodeName, cityName))
                .build();
    }
    
    private GeographicalAddressDTO buildAddressV2(String countryName, String countryId, String cityName, String regionName,
            String regionId) {
        return GeographicalAddressDTO.builder()
                .addressItems(getAddressItemNamesV2(countryName, countryId, cityName, regionName, regionId))
                .build();
    }
    
    private GeographicalAddressDTO buildAddress(String countryName, String countryId, String cityName, String cityId, String regionName,
            String regionId, String districtName) {
        return GeographicalAddressDTO.builder()
                .addressItems(getAddressItemNamesV2(countryName, countryId, cityName, regionName, regionId))
                .addAddressItems(buildDistrict(districtName, cityId))
                .build();
    }
    
    private List<AddressItemDTO> getAddressItemNames(String countryName, String countryId, String postalCodeName, String cityName) {
        List<AddressItemDTO> list = new ArrayList<>();
        list.add(buildCountry(countryName));
        list.add(buildPostalCode(postalCodeName, countryId));
        list.add(buildCity(cityName, countryId));
        return list;
    }
    
    private List<AddressItemDTO> getAddressItemNamesV2(String countryName, String countryId, String cityName, String regionName,
            String regionId) {
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
    
    private AddressItemDTO buildDistrict(String name, String parentId) {
        return AddressItemDTO.builder()
                .object("District")
                .name(name)
                .directParentId(parentId)
                .build();
    }
    
}
