package com.oss.repositories;

import java.util.List;
import java.util.Optional;

import com.comarch.oss.locationinventory.api.dto.AttributeDTO;
import com.comarch.oss.locationinventory.api.dto.PhysicalLocationDTO;
import com.comarch.oss.locationinventory.api.dto.ResourceDTO;
import com.comarch.oss.locationinventory.api.dto.SublocationDTO;
import com.oss.services.LocationInventoryClient;
import com.oss.untils.Constants;
import com.oss.untils.Environment;

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
    
    public void createLocationInLocation(String locationType, String subLocationSiteNameForCreate, Long addressId, Long parentId,
            String parentLocationType) {
        LocationInventoryClient client = new LocationInventoryClient(env);
        client.createPhysicalLocation(
                buildLocationWithParent(locationType, subLocationSiteNameForCreate, addressId, parentId, parentLocationType));
    }
    
    public Long createSubLocation(String subLocationType, String subLocationName, Long preciseLocation, String preciseLocationType,
            Long parentLocationId, String parentLocationType) {
        LocationInventoryClient client = new LocationInventoryClient(env);
        SublocationDTO subLocation = SublocationDTO.builder()
                .location(getLocation(parentLocationId, parentLocationType))
                .preciseLocation(getLocation(preciseLocation, preciseLocationType))
                .name(subLocationName)
                .type(subLocationType)
                .build();
        ResourceDTO resourceDTO = client.createSubLocation(subLocation);
        String subLocationId = resourceDTO.getUri().toString();
        return Long.valueOf(subLocationId.substring(subLocationId.lastIndexOf("/") + 1, subLocationId.indexOf("?")));
    }

    public Optional<String> getLocationId(String locationName){
        LocationInventoryClient client = new LocationInventoryClient(env);
        List<Integer> locationIds = client.getPhysicalLocationByName(locationName);
        return locationIds.stream().findFirst().map(Object::toString);
    }

    public void updateSubLocation(Long subLocationId,String subLocationType, String subLocationName, Long preciseLocation, String preciseLocationType,
                                  Long parentLocationId, String parentLocationType){
        LocationInventoryClient client = new LocationInventoryClient(env);
        SublocationDTO subLocation = SublocationDTO.builder()
                .location(getLocation(parentLocationId, parentLocationType))
                .preciseLocation(getLocation(preciseLocation, preciseLocationType))
                .name(subLocationName)
                .type(subLocationType)
                .id(subLocationId)
                .build();
        client.updateSubLocation(subLocation, subLocationId.toString());
    }

    public void deleteSubLocation(String ids){
        LocationInventoryClient client = new LocationInventoryClient(env);
        client.deleteSubLocation(ids);
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
    
    private AttributeDTO getLocation(Long id, String type) {
        return AttributeDTO.builder()
                .id(id)
                .type(type)
                .build();
    }
    
    private PhysicalLocationDTO buildLocationWithParent(String locationType, String locationName, Long addressId, Long parentId,
            String parentLocationType) {
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
