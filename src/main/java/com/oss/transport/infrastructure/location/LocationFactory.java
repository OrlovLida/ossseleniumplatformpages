package com.oss.transport.infrastructure.location;

import com.comarch.oss.addressinventory.api.dto.AddressItemDTO;
import com.comarch.oss.addressinventory.api.dto.AddressItemTypeDTO;
import com.comarch.oss.addressinventory.api.dto.GeographicalAddressDTO;
import com.comarch.oss.addressinventory.api.dto.ImmutableAddressItemDTO;
import com.comarch.oss.locationinventory.api.dto.AttributeDTO;
import com.comarch.oss.locationinventory.api.dto.PhysicalLocationDTO;
import com.comarch.oss.locationinventory.api.dto.SearchDTO;
import com.comarch.oss.locationinventory.api.dto.SublocationDTO;
import com.google.common.collect.Lists;
import com.oss.transport.infrastructure.address.AddressClient;
import com.oss.transport.infrastructure.address.AddressItemClient;
import com.oss.transport.infrastructure.EnvironmentRequestClient;
import com.oss.transport.infrastructure.ObjectIdentifier;
import com.oss.transport.infrastructure.RefId;
import com.oss.transport.infrastructure.planning.PlanningContext;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

public class LocationFactory {

    public static final String GEOGRAPHICAL_ADDRESS_TYPE = "Address";

    private final LocationClient locationClient;
    private final AddressClient addressClient;
    private final AddressItemClient addressItemClient;

    public LocationFactory(EnvironmentRequestClient env) {
        locationClient = new LocationClient(env);
        addressClient = new AddressClient(env);
        addressItemClient = new AddressItemClient(env);
    }

    public Long getOrCreateLocation(ObjectIdentifier location, String addressName, PlanningContext context) {
        Optional<Long> locationId = locationClient.getLocationByName(location.getName(), context).map(SearchDTO::getId);
        return locationId.orElseGet(() -> createLocation(location, addressName, context));
    }

    public Optional<Long> getLocationByName(String name, PlanningContext context) {
        return locationClient.getLocationByName(name, context).map(SearchDTO::getId);
    }

    public Long getOrCreatePreciseLocation(ObjectIdentifier preciseLocation, RefId parentLocationId, PlanningContext context) {
        Optional<Long> sublocationId = locationClient.getSublocationByName(preciseLocation.getName(), context);
        return sublocationId.orElseGet(() -> createPreciseLocation(preciseLocation, parentLocationId, context));
    }

    public Optional<Long> getPreciseLocationByName(String name, PlanningContext context) {
        return locationClient.getSublocationByName(name, context);
    }

    public Long createLocation(ObjectIdentifier location, String addressName, PlanningContext context) {
        Long addressId = getOrCreateAddress(addressName);
        PhysicalLocationDTO dto = PhysicalLocationDTO.builder()
                .masterAddress(AttributeDTO.builder().id(addressId).type(GEOGRAPHICAL_ADDRESS_TYPE).build())
                .name(location.getName())
                .type(location.getType())
                .build();
        return locationClient.createLocation(dto, context);
    }

    public Long createPreciseLocation(ObjectIdentifier preciseLocation, RefId parentLocation, PlanningContext context) {
        SublocationDTO dto = SublocationDTO.builder()
                .location(AttributeDTO.builder().id(parentLocation.getId()).type(parentLocation.getType()).build())
                .name(preciseLocation.getName())
                .type(preciseLocation.getType())
                .build();
        return locationClient.createSublocation(dto, context);
    }

    public boolean deleteLocation(String name, PlanningContext context) {
        Optional<SearchDTO> locationByName = locationClient.getLocationByName(name, context);
        if (locationByName.isPresent()) {
            SearchDTO location = locationByName.get();
            locationClient.delete(location.getId(), location.getType(), context);
        }
        return true;
    }

    public boolean deleteAddress(String name) {
        GeographicalAddressDTO dto = getGeographicalAddressDTO(name);
        Optional<Long> addressId = addressClient.searchGeographicalAddress(dto);
        addressId.ifPresent(addressClient::deleteGeographicalAddress);
        dto.getAddressItems().forEach(addressItem -> {
            addressItemClient.deleteAddressItem(addressItem.getId().get());
        });
        return true;
    }

    private Long getOrCreateAddress(String addressName) {
        GeographicalAddressDTO dto = createGeographicalAddressDTO(addressName);
        Optional<Long> addressId = addressClient.searchGeographicalAddress(dto);
        return addressId.orElseGet(() -> createAddress(dto));
    }

    private GeographicalAddressDTO createGeographicalAddressDTO(String addressName) {
        Collection<List<AddressItemTypeDTO>> addressPaths = getPathsToMandatoryAddressItems();
        Map<String, AddressItemDTO> processedAddressItems = getOrCreateAddressItems(addressName, addressPaths);
        return GeographicalAddressDTO.builder()
                .addAllAddressItems(processedAddressItems.values())
                .build();
    }

    private GeographicalAddressDTO getGeographicalAddressDTO(String addressName) {
        Collection<List<AddressItemTypeDTO>> addressPaths = getPathsToMandatoryAddressItems();
        Map<String, AddressItemDTO> processedAddressItems = getAddressItems(addressName, addressPaths);
        return GeographicalAddressDTO.builder()
                .addAllAddressItems(processedAddressItems.values())
                .build();
    }

    private Long createAddress(GeographicalAddressDTO dto) {
        return addressClient.createGeographicalAddress(dto);
    }

    private Map<String, AddressItemDTO> getOrCreateAddressItems(String addressName, Collection<List<AddressItemTypeDTO>> addressPaths) {
        Map<String, AddressItemDTO> processedAddressItems = new HashMap<>();
        addressPaths.forEach(addressPath -> {
            processAddressPath(addressPath, addressName, processedAddressItems);
        });
        return processedAddressItems;
    }

    private Map<String, AddressItemDTO> getAddressItems(String addressName, Collection<List<AddressItemTypeDTO>> addressPaths) {
        Map<String, AddressItemDTO> processedAddressItems = new HashMap<>();
        addressPaths.forEach(addressPath -> processGetAddressPath(addressPath, addressName, processedAddressItems));
        return processedAddressItems;
    }

    private void processAddressPath(List<AddressItemTypeDTO> addressPath, String addressName,
            Map<String, AddressItemDTO> processedAddressItems) {
        addressPath.forEach(addressItem -> {
            Optional<Long> parentId = addressItem.getParentAddressItemName()
                    .map(processedAddressItems::get)
                    .flatMap(AddressItemDTO::getId);
            AddressItemDTO newAddressItem =
                    getOrCreateAddressItem(addressName + addressItem.getName(), addressItem.getName(), parentId);
            processedAddressItems.put(addressItem.getName(), newAddressItem);
        });
    }

    private AddressItemDTO getOrCreateAddressItem(String name, String type, Optional<Long> parentId) {
        Optional<AddressItemDTO> addressItem = addressItemClient.getAddressItemByNameAndParentId(name, type, parentId);
        return addressItem.orElseGet(() -> createAddressItem(name, type, parentId));
    }

    private void processGetAddressPath(List<AddressItemTypeDTO> addressPath, String addressName,
            Map<String, AddressItemDTO> processedAddressItems) {

        for (AddressItemTypeDTO addressItemType : addressPath) {
            Optional<Long> parentId = addressItemType.getParentAddressItemName()
                    .map(processedAddressItems::get)
                    .flatMap(AddressItemDTO::getId);
            Optional<AddressItemDTO> existingAddressItem =
                    getAddressItem(addressName + addressItemType.getName(), addressItemType.getName(), parentId);
            existingAddressItem.ifPresent(addressItemDTO -> processedAddressItems.put(addressItemDTO.getName(), addressItemDTO));
        }
    }

    private Optional<AddressItemDTO> getAddressItem(String name, String type, Optional<Long> parentId) {
        return addressItemClient.getAddressItemByNameAndParentId(name, type, parentId);
    }

    private AddressItemDTO createAddressItem(String name, String type, Optional<Long> parentId) {
        ImmutableAddressItemDTO.Builder builder = AddressItemDTO.builder()
                .name(name)
                .object(type);
        parentId.map(String::valueOf).ifPresent(builder::directParentId);
        return addressItemClient.createAddressItem(builder.build());
    }

    private Collection<List<AddressItemTypeDTO>> getPathsToMandatoryAddressItems() {
        Collection<AddressItemTypeDTO> addressItemTypes = addressItemClient.getAddressItemTypes();
        return addressItemTypes.stream()
                .filter(type -> type.getMandatory())
                .map(type -> findPathToRoot(type, addressItemTypes))
                .map(Lists::reverse)
                .collect(Collectors.toList());
    }

    private List<AddressItemTypeDTO> findPathToRoot(AddressItemTypeDTO type, Collection<AddressItemTypeDTO> addressItemTypes) {
        List<AddressItemTypeDTO> results = new ArrayList<>();
        processParentType(type, addressItemTypes, results);
        return results;
    }

    private void processParentType(AddressItemTypeDTO type, Collection<AddressItemTypeDTO> addressItemTypes,
            List<AddressItemTypeDTO> results) {
        results.add(type);
        type.getParentAddressItemName().ifPresent(parentTypeName -> {
            Optional<AddressItemTypeDTO> parentType = addressItemTypes.stream()
                    .filter(itemType -> Objects.equals(itemType.getName(), parentTypeName))
                    .findFirst();
            parentType.ifPresent(parent -> processParentType(parent, addressItemTypes, results));
        });
    }
}
