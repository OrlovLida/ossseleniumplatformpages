package com.oss.repositories;

import com.comarch.oss.physicalinventory.api.dto.AttributeDTO;
import com.comarch.oss.physicalinventory.api.dto.PhysicalDeviceDTO;
import com.comarch.oss.physicalinventory.api.dto.ResourceDTO;
import com.oss.services.PhysicalInventoryClient;
import com.oss.untils.Environment;

public class PhysicalInventoryRepository {

    private Environment env;

    public PhysicalInventoryRepository(Environment env) {
        this.env = env;
    }

    public String createDevice(String locationType, Long locationId, Long deviceModelId, String deviceName, String deviceModelType) {
        PhysicalInventoryClient client = new PhysicalInventoryClient(env);
        ResourceDTO resourceDTO = client.createDevice(buildDevice(locationType, locationId, deviceModelId, deviceName, deviceModelType));
        String deviceId = resourceDTO.getUri().toString().substring(59, 67);
        return deviceId;
    }

    private PhysicalDeviceDTO buildDevice(String locationType, Long locationId, Long deviceModelId, String deviceName, String deviceModelType) {
        return PhysicalDeviceDTO.builder()
                .deviceModel(getDeviceModelId(deviceModelId, deviceModelType))
                .location(getLocation(locationId, locationType))
                .name(deviceName)
                .build();
    }

    private com.comarch.oss.physicalinventory.api.dto.AttributeDTO getDeviceModelId(Long id, String deviceModelType) {
        return AttributeDTO.builder()
                .id(id)
                .type(deviceModelType)
                .build();
    }

    private com.comarch.oss.physicalinventory.api.dto.AttributeDTO getLocation(Long id, String locationType) {
        return AttributeDTO.builder()
                .id(id)
                .type(locationType)
                .build();
    }
}
