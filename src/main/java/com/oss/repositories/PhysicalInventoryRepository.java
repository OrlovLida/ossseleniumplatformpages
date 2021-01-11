package com.oss.repositories;

import com.comarch.oss.physicalinventory.api.dto.AttributeDTO;
import com.comarch.oss.physicalinventory.api.dto.PhysicalDeviceDTO;
import com.comarch.oss.physicalinventory.api.dto.ResourceDTO;
import com.oss.services.PhysicalInventoryClient;
import com.oss.untils.Constants;
import com.oss.untils.Environment;

public class PhysicalInventoryRepository {

    private Environment env;

    public PhysicalInventoryRepository(Environment env) {
        this.env = env;
    }

    public String createDevice(String locationType, Long locationId, Long deviceModelId, String deviceName) {
        PhysicalInventoryClient client = new PhysicalInventoryClient(env);
        ResourceDTO resourceDTO = client.createDevice(buildDevice(locationType, locationId, deviceModelId, deviceName));
        String deviceId = resourceDTO.getUri().toString().substring(59,67);
        return deviceId;
    }

    private PhysicalDeviceDTO buildDevice(String locationType, Long locationId, Long deviceModelId, String deviceName) {
        return PhysicalDeviceDTO.builder()
                .deviceModel(getDeviceModelId(deviceModelId))
                .location(getLocation(locationId, locationType))
                .name(deviceName)
                .build();
    }

    private com.comarch.oss.physicalinventory.api.dto.AttributeDTO getDeviceModelId(Long id) {
        return AttributeDTO.builder()
                .id(id)
                .type(Constants.DEVICE_MODEL_TYPE)
                .build();
    }

    private com.comarch.oss.physicalinventory.api.dto.AttributeDTO getLocation(Long id, String locationType) {
        return AttributeDTO.builder()
                .id(id)
                .type(locationType)
                .build();
    }
}
