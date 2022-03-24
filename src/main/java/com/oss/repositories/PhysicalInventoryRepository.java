package com.oss.repositories;

import java.util.Collections;

import com.comarch.oss.physicalinventory.api.dto.AttributeDTO;
import com.comarch.oss.physicalinventory.api.dto.CardDTO;
import com.comarch.oss.physicalinventory.api.dto.ChassisDTO;
import com.comarch.oss.physicalinventory.api.dto.PhysicalDeviceDTO;
import com.comarch.oss.physicalinventory.api.dto.ResourceDTO;
import com.comarch.oss.physicalinventory.api.dto.SearchResultDTO;
import com.oss.services.PhysicalInventoryClient;
import com.oss.untils.Environment;

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
    
    public Long createDevice(String locationType, Long locationId, Long deviceModelId, String deviceName, String deviceModelType,
            long projectId) {
        PhysicalInventoryClient client = new PhysicalInventoryClient(env);
        ResourceDTO resourceDTO =
                client.createDevice(buildDevice(locationType, locationId, deviceModelId, deviceName, deviceModelType), projectId);
        String deviceId = resourceDTO.getUri().toString();
        return Long.valueOf(deviceId.substring(deviceId.lastIndexOf("/") + 1, deviceId.indexOf("?")));
    }
    
    public void updateDeviceSerialNumber(long deviceId, String locationType, Long locationId, String serialNumber, Long deviceModelId,
            String deviceModelType, long projectId) {
        PhysicalInventoryClient client = new PhysicalInventoryClient(env);
        PhysicalDeviceDTO build = PhysicalDeviceDTO.builder()
                .serialNumber(serialNumber)
                .location(getLocation(locationId, locationType))
                .deviceModel(getDeviceModelId(deviceModelId, deviceModelType))
                .build();
        client.updateDevice(build, deviceId, projectId);
    }
    
    public Long createDeviceWithCard(String locationType, Long locationId, Long deviceModelId, String deviceName, String deviceModelType,
            String slotName, Long cardModelId, String cardModelType) {
        PhysicalInventoryClient client = new PhysicalInventoryClient(env);
        ResourceDTO resourceDTO = client.createDevice(buildDeviceWithCard(locationType, locationId, deviceModelId, deviceName,
                deviceModelType, slotName, cardModelId, cardModelType));
        String deviceId = resourceDTO.getUri().toString();
        return Long.valueOf(deviceId.substring(deviceId.lastIndexOf("/") + 1, deviceId.indexOf("?")));
    }
    
    public void deleteDevice(String deviceId) {
        PhysicalInventoryClient client = new PhysicalInventoryClient(env);
        client.deleteDevice(deviceId);
    }
    
    public Long getDeviceId(String locationId, String deviceName) {
        PhysicalInventoryClient client = new PhysicalInventoryClient(env);
        SearchResultDTO deviceId = client.getDeviceId(locationId, "Name==" + deviceName);
        return deviceId.getSearchResult().get(0).getId();
    }
    
    private PhysicalDeviceDTO buildDevice(String locationType, Long locationId, Long deviceModelId, String deviceName,
            String deviceModelType) {
        return PhysicalDeviceDTO.builder()
                .deviceModel(getDeviceModelId(deviceModelId, deviceModelType))
                .location(getLocation(locationId, locationType))
                .name(deviceName)
                .build();
    }
    
    private PhysicalDeviceDTO buildDeviceWithCard(String locationType, Long locationId, Long deviceModelId, String deviceName,
            String deviceModelType, String slotName, Long id, String cardModelType) {
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
