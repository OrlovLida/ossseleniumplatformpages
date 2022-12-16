package com.oss.repositories;

import com.comarch.oss.physicalinventory.api.dto.AttributeDTO;
import com.comarch.oss.physicalinventory.api.dto.CardDTO;
import com.comarch.oss.physicalinventory.api.dto.ChassisDTO;
import com.comarch.oss.physicalinventory.api.dto.PhysicalDeviceDTO;
import com.comarch.oss.physicalinventory.api.dto.PluggableModuleDTO;
import com.comarch.oss.physicalinventory.api.dto.PortDTO;
import com.comarch.oss.physicalinventory.api.dto.ResourceDTO;
import com.comarch.oss.physicalinventory.api.dto.SearchDTO;
import com.comarch.oss.resourcehierarchy.api.dto.ResourceHierarchyDTO;
import com.oss.services.PhysicalInventoryClient;
import com.oss.transport.infrastructure.planning.PlanningContext;
import com.oss.untils.Environment;

import java.util.Collection;
import java.util.Collections;
import java.util.List;

public class PhysicalInventoryRepository {

    private static final String CHASSIS = "Chassis";
    private PhysicalInventoryClient client;

    public PhysicalInventoryRepository(Environment env) {
        client = new PhysicalInventoryClient(env);
    }

    public Long createDevice(String locationType, Long locationId, Long deviceModelId, String deviceName, String deviceModelType) {
        ResourceDTO resourceDTO = client.createDevice(buildDevice(locationType, locationId, deviceModelId, deviceName, deviceModelType));
        String deviceId = resourceDTO.getUri().toString();
        return Long.valueOf(deviceId.substring(deviceId.lastIndexOf("/") + 1, deviceId.indexOf("?")));
    }

    public Long createDevice(String locationType, Long locationId, Long deviceModelId, String deviceName, String deviceModelType,
                             long projectId) {
        ResourceDTO resourceDTO =
                client.createDevice(buildDevice(locationType, locationId, deviceModelId, deviceName, deviceModelType), projectId);
        String deviceId = resourceDTO.getUri().toString();
        return Long.valueOf(deviceId.substring(deviceId.lastIndexOf("/") + 1, deviceId.indexOf("?")));
    }

    public void updateDeviceSerialNumber(long deviceId, String deviceName, String locationType, Long locationId, String serialNumber, Long deviceModelId,
                                         String deviceModelType, long projectId) {
        PhysicalDeviceDTO build = PhysicalDeviceDTO.builder()
                .serialNumber(serialNumber)
                .name(deviceName)
                .location(getLocation(locationId, locationType))
                .deviceModel(getDeviceModelId(deviceModelId, deviceModelType))
                .build();
        client.updateDevice(build, deviceId, projectId);
    }

    public Long createDeviceWithCard(String locationType, Long locationId, Long deviceModelId, String deviceName, String deviceModelType,
                                     String slotName, Long cardModelId, String cardModelType) {
        ResourceDTO resourceDTO = client.createDevice(buildDeviceWithCard(locationType, locationId, deviceModelId, deviceName,
                deviceModelType, slotName, cardModelId, cardModelType));
        String deviceId = resourceDTO.getUri().toString();
        return Long.valueOf(deviceId.substring(deviceId.lastIndexOf("/") + 1, deviceId.indexOf("?")));
    }

    public void createCard(Long deviceId, String slotName, Long cardModelId, String cardModelType) {
        CardDTO cardDTO = CardDTO.builder()
                .deviceId(deviceId)
                .slotName(slotName)
                .chassisName(CHASSIS)
                .cardModel(getCardModelId(cardModelId, cardModelType))
                .build();
        client.addCardToDevice(cardDTO);
    }

    public void createCard(Long deviceId, String slotName, Long cardModelId, String cardModelType, long projectId) {
        CardDTO cardDTO = CardDTO.builder()
                .deviceId(deviceId)
                .slotName(slotName)
                .chassisName(CHASSIS)
                .cardModel(getCardModelId(cardModelId, cardModelType))
                .build();
        client.addCardToDevice(cardDTO, projectId);
    }

    public void deleteDevice(Long deviceId) {
        client.deleteDevice(String.valueOf(deviceId));
    }

    public void addPortToDevice(Long deviceId, Long portModelId, String cardModelType, String portName) {
        client.addPortToDevice(deviceId, buildAddPortToDevice(portModelId, cardModelType, portName));
    }

    public void addPluggableModuleToDevice(Long portId, Long pluggableModelId, String pluggableModelType) {
        client.addPluggableModuleToPort(portId, buildAddPluggableModuleToPort(pluggableModelId, portId, pluggableModelType));
    }

    private PluggableModuleDTO buildAddPluggableModuleToPort(Long pluggableModelId, Long portId, String pluggableModuleType) {
        return PluggableModuleDTO.builder()
                .portId(portId)
                .model(getPluggableModuleModelId(pluggableModelId, pluggableModuleType))
                .build();
    }

    private PortDTO buildAddPortToDevice(Long portModelId, String portModelType, String portName) {
        return PortDTO.builder()
                .portModel(getPortModelId(portModelId, portModelType))
                .name(portName)
                .build();
    }

    public void deleteDevice(String deviceId) {
        client.deleteDevice(deviceId);
    }

    public void deleteDeviceV3(Collection<String> deviceIds,
                               PlanningContext context) {
        client.deleteDeviceV3(deviceIds, context);
    }

    public void deleteDevice(String deviceId, long projectId) {
        client.deleteDevice(deviceId, projectId);
    }

    public Long getDeviceId(String locationId, String deviceName) {
        return getDeviceSearchDTO(locationId, deviceName).get(0).getId();
    }

    public boolean isDevicePresent(String locationId, String deviceName) {
        return !getDeviceSearchDTO(locationId, deviceName).isEmpty();
    }

    private List<SearchDTO> getDeviceSearchDTO(String locationId, String deviceName) {
        return client.getDeviceId(locationId, "Name==" + deviceName).getSearchResult();
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

    private AttributeDTO getDeviceModelId(Long id, String deviceModelType) {
        return AttributeDTO.builder()
                .id(id)
                .type(deviceModelType)
                .build();
    }

    private AttributeDTO getLocation(Long id, String locationType) {
        return AttributeDTO.builder()
                .id(id)
                .type(locationType)
                .build();
    }

    private ChassisDTO getChassis(String slotName, Long id, String cardModelType) {
        return ChassisDTO.builder()
                .name(CHASSIS)
                .cards(Collections.singleton(getCard(slotName, id, cardModelType)))
                .build();
    }

    private CardDTO getCard(String slotName, Long id, String cardModelType) {
        return CardDTO.builder()
                .cardModel(getCardModelId(id, cardModelType))
                .slotName(slotName)
                .build();
    }

    private AttributeDTO getCardModelId(Long id, String cardModelType) {
        return AttributeDTO.builder()
                .id(id)
                .type(cardModelType)
                .build();
    }

    private AttributeDTO getPortModelId(Long portModelId, String portModelType) {
        return AttributeDTO.builder()
                .id(portModelId)
                .type(portModelType)
                .build();
    }

    private AttributeDTO getPluggableModuleModelId(Long pluggalbeModuleModelId, String pluggableModuleType) {
        return AttributeDTO.builder()
                .id(pluggalbeModuleModelId)
                .type(pluggableModuleType)
                .build();
    }

    public ResourceHierarchyDTO getResourceHierarchy(String resourceType, String resourceId, String mode) {
        return client.getResourceHierarchy(resourceType, resourceId, mode);
    }

    public PhysicalDeviceDTO getDevice(String deviceId) {
        return client.getDevice(deviceId);
    }
}
