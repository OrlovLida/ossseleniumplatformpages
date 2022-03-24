package com.oss.repositories;

import java.util.List;

import com.comarch.oss.locationinventory.api.dto.AttributeDTO;
import com.comarch.oss.locationinventory.api.dto.PhysicalLocationDTO;
import com.comarch.oss.locationinventory.api.dto.ResourceDTO;
import com.oss.services.LocationInventoryClient;
import com.oss.untils.Constants;
import com.oss.untils.Environment;

/**
 * @author Milena Miętkiewicz
 */

public class LocationInventoryRepository {

    private LocationInventoryClient client;

    public LocationInventoryRepository(Environment env) {
        client = new LocationInventoryClient(env);
    }

    public String getOrCreateLocation(String locationName, String locationType, Long addressId) {
        List<Integer> locationIds = client.getPhysicalLocationByName(locationName);
        if (!locationIds.isEmpty()) {
            String locationId = locationIds.get(0).toString();
            return locationId;
        }
        return createLocation(locationName, locationType, addressId);
    }

    public String createLocation(String locationName, String locationType, Long addressId) {
        ResourceDTO resourceDTO = client.createPhysicalLocation(buildLocation(locationType, locationName, addressId));
        String locationId = resourceDTO.getUri().toString();
        return locationId.substring(locationId.lastIndexOf("/") + 1, locationId.indexOf("?"));
    }

    public void deleteLocation(Long locationId, String locationType) {
        client.removeLocation(locationId, locationType);
    }

    public void createSubLocation(String locationType, String subLocationSiteNameForCreate, Long addressId, Long parentId, String parentLocationType) {
        client.createPhysicalLocation(buildLocationWithParent(locationType, subLocationSiteNameForCreate, addressId, parentId, parentLocationType));
    }

    private PhysicalLocationDTO buildLocation(String locationType, String locationName, Long addressId) {
        return PhysicalLocationDTO.builder()
                .type(locationType)
                .name(locationName)
                .masterAddress(getAddress(addressId))
                .build();
    }

    private AttributeDTO getAddress(Long id) {
        return AttributeDTO.builder()
                .id(id)
                .type(Constants.ADDRESS_TYPE)
                .build();
    }

    private PhysicalLocationDTO buildLocationWithParent(String locationType, String locationName, Long addressId, Long parentId, String parentLocationType) {
        return PhysicalLocationDTO.builder()
                .type(locationType)
                .name(locationName)
                .masterAddress(getAddress(addressId))
                .parentLocation(getParentLocationDetails(parentId, parentLocationType))
                .build();
    }

    private AttributeDTO getParentLocationDetails(Long id, String locationType) {
        return AttributeDTO.builder()
                .id(id)
                .type(locationType)
                .build();
    }

}