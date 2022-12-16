package com.oss.repositories;

import com.comarch.oss.locationinventory.api.dto.AttributeDTO;
import com.comarch.oss.locationinventory.api.dto.PhysicalLocationDTO;
import com.comarch.oss.locationinventory.api.dto.ResourceDTO;
import com.comarch.oss.locationinventory.api.dto.SearchResultDTO;
import com.comarch.oss.locationinventory.api.dto.SublocationDTO;
import com.oss.services.LocationInventoryClient;
import com.oss.untils.Constants;
import com.oss.untils.Environment;

import java.util.List;
import java.util.Optional;

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
            return locationIds.get(0).toString();
        }
        return createLocation(locationName, locationType, addressId);
    }

    public List<Integer> getLocationsIds() {
        return client.getPhysicalLocation();
    }

    public List<Integer> getFirstSite() {
        return client.getFirstSite();
    }

    public String createLocation(String locationName, String locationType, Long addressId) {
        ResourceDTO resourceDTO = client.createPhysicalLocation(buildLocation(locationType, locationName, addressId, ""));
        String locationId = resourceDTO.getUri().toString();
        return locationId.substring(locationId.lastIndexOf("/") + 1, locationId.indexOf("?"));
    }

    public String createLocation(String locationName, String locationType, Long addressId, Long projectId) {
        ResourceDTO resourceDTO = client.createPhysicalLocation(buildLocation(locationType, locationName, addressId, ""), projectId);
        String locationId = resourceDTO.getUri().toString();
        return locationId.substring(locationId.lastIndexOf("/") + 1, locationId.indexOf("?"));
    }

    public void createLocationInLocation(String locationType, String subLocationSiteNameForCreate, Long addressId, Long parentId,
                                         String parentLocationType) {
        client.createPhysicalLocation(
                buildLocationWithParent(locationType, subLocationSiteNameForCreate, addressId, parentId, parentLocationType));
    }

    public Long createSubLocation(String subLocationType, String subLocationName, Long preciseLocation, String preciseLocationType,
                                  Long parentLocationId, String parentLocationType) {
        SublocationDTO subLocation = buildSubLocation(subLocationType, subLocationName, preciseLocation, preciseLocationType,
                parentLocationId, parentLocationType);
        ResourceDTO resourceDTO = client.createSubLocation(subLocation);
        String subLocationId = resourceDTO.getUri().toString();
        return Long.valueOf(subLocationId.substring(subLocationId.lastIndexOf("/") + 1, subLocationId.indexOf("?")));
    }

    public void updateLocation(String locationName, String locationType, String locationId, Long addressId, String description,
                               long projectId) {
        client.updateLocation(buildLocation(locationType, locationName, addressId, description), locationId, projectId);
    }

    private SublocationDTO buildSubLocation(String subLocationType, String subLocationName, Long preciseLocation,
                                            String preciseLocationType, Long parentLocationId, String parentLocationType) {
        return SublocationDTO.builder()
                .location(getLocation(parentLocationId, parentLocationType))
                .preciseLocation(getLocation(preciseLocation, preciseLocationType))
                .name(subLocationName)
                .type(subLocationType)
                .build();
    }

    public void deleteLocation(Long locationId, String locationType) {
        client.removeLocation(locationId, locationType);
    }

    public void deleteLocation(Long locationId, String locationType, long projectId) {
        client.removeLocation(locationId, locationType, projectId);
    }

    public Optional<String> getLocationId(String locationName) {
        List<Integer> locationIds = client.getPhysicalLocationByName(locationName);
        return locationIds.stream().findFirst().map(Object::toString);
    }

    public void updateSubLocation(Long subLocationId, String subLocationType, String subLocationName, Long preciseLocation,
                                  String preciseLocationType,
                                  Long parentLocationId, String parentLocationType) {
        SublocationDTO subLocation = SublocationDTO.builder()
                .location(getLocation(parentLocationId, parentLocationType))
                .preciseLocation(getLocation(preciseLocation, preciseLocationType))
                .name(subLocationName)
                .type(subLocationType)
                .id(subLocationId)
                .build();
        client.updateSubLocation(subLocation, subLocationId.toString());
    }

    public void deleteSubLocation(String ids) {
        client.deleteSubLocation(ids);
    }

    public Long getSublocationId(String locationId, String floorName) {
        SearchResultDTO sublocationId = client.getSublocationId(locationId, "Name==" + floorName);
        return sublocationId.getSearchResult().get(0).getId();
    }

    private PhysicalLocationDTO buildLocation(String locationType, String locationName, Long addressId, String description) {
        return PhysicalLocationDTO.builder()
                .type(locationType)
                .name(locationName)
                .masterAddress(getAddress(addressId))
                .description(description)
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
