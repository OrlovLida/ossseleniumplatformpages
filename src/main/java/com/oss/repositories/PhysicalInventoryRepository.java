package com.oss.repositories;

import com.comarch.oss.physicalinventory.api.dto.*;
import com.oss.services.PhysicalInventoryClient;
import com.oss.untils.Environment;

import java.util.Collections;

public class PhysicalInventoryRepository {

    private Environment env;

    public PhysicalInventoryRepository(Environment env) {
        this.env = env;
    }

    public Long createDevice(String locationType, Long locationId, Long deviceModelId, String deviceName, String deviceModelType) {
        PhysicalInventoryClient client = new PhysicalInventoryClient(env);
        ResourceDTO resourceDTO = client.createDevice(buildDevice(locationType, locationId, deviceModelId, deviceName, deviceModelType));
        String deviceId = resourceDTO.getUri().toString();
        return Long.valueOf(deviceId.substring(deviceId.lastIndexOf("/") + 1, deviceId.indexOf("?")));
    }

    public Long createDeviceWithCard(String locationType, Long locationId, Long deviceModelId, String deviceName, String deviceModelType, String slotName, Long cardModelId, String cardModelType) {
        PhysicalInventoryClient client = new PhysicalInventoryClient(env);
        ResourceDTO resourceDTO = client.createDevice(buildDeviceWithCard(locationType, locationId, deviceModelId, deviceName, deviceModelType, slotName, cardModelId, cardModelType));
        String deviceId = resourceDTO.getUri().toString();
        return Long.valueOf(deviceId.substring(deviceId.lastIndexOf("/") + 1, deviceId.indexOf("?")));
    }

    private PhysicalDeviceDTO buildDevice(String locationType, Long locationId, Long deviceModelId, String deviceName, String deviceModelType) {
        return PhysicalDeviceDTO.builder()
                .deviceModel(getDeviceModelId(deviceModelId, deviceModelType))
                .location(getLocation(locationId, locationType))
                .name(deviceName)
                .build();
    }

    private PhysicalDeviceDTO buildDeviceWithCard(String locationType, Long locationId, Long deviceModelId, String deviceName, String deviceModelType, String slotName, Long id, String cardModelType) {
        return PhysicalDeviceDTO.builder()
                .deviceModel(getDeviceModelId(deviceModelId, deviceModelType))
                .location(getLocation(locationId, locationType))
                .name(deviceName)
                .chassis(Collections.singleton(getChassis(slotName, id, cardModelType)))
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

    private ChassisDTO getChassis(String slotName, Long id, String cardModelType) {
        return ChassisDTO.builder()
                .name("Chassis")
                .cards(Collections.singleton(getCard(slotName, id, cardModelType)))
                .build();
    }

    private CardDTO getCard(String slotName, Long id, String cardModelType) {
        return CardDTO.builder()
                .cardModel(getCardModelId(id, cardModelType))
                .slotName(slotName)
                .build();
    }

    private com.comarch.oss.physicalinventory.api.dto.AttributeDTO getCardModelId(Long id, String cardModelType) {
        return AttributeDTO.builder()
                .id(id)
                .type(cardModelType)
                .build();
    }
}
