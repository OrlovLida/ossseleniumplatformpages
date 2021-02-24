package com.oss.repositories;

import com.comarch.oss.locationinventory.api.dto.AttributeDTO;
import com.comarch.oss.locationinventory.api.dto.PhysicalLocationDTO;
import com.comarch.oss.locationinventory.api.dto.ResourceDTO;
import com.oss.services.LocationInventoryClient;
import com.oss.untils.Constants;
import com.oss.untils.Environment;

import java.util.List;

/**
 * @author Milena Miętkiewicz
 */

public class LocationInventoryRepository {

    private Environment env;

    public LocationInventoryRepository(Environment env) {
        this.env = env;
    }

    public String getOrCreateLocation(String locationName, String locationType, Long addressId) {
        LocationInventoryClient client = new LocationInventoryClient(env);
        List<Integer> locationIds = client.getPhysicalLocationByName(locationName);
        if (!locationIds.isEmpty()) {
            String locationId = locationIds.get(0).toString();
            return locationId;
        }
        return createLocation(locationName, locationType, addressId);
    }

    public String createLocation(String locationName, String locationType, Long addressId) {
        LocationInventoryClient client = new LocationInventoryClient(env);
        ResourceDTO resourceDTO = client.createPhysicalLocation(buildLocation(locationType, locationName, addressId));
        String locationId = resourceDTO.getUri().toString();
        return locationId.substring(locationId.lastIndexOf("/") + 1, locationId.indexOf("?"));
    }

    public void createSubLocation(String locationType, String subLocationSiteNameForCreate, Long addressId, Long parentId, String parentLocationType) {
        LocationInventoryClient client = new LocationInventoryClient(env);
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