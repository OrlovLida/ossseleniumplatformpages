package com.oss.transport.infrastructure.device;

import java.util.Map;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;

import com.comarch.oss.physicalinventory.api.dto.AttributeDTO;
import com.comarch.oss.physicalinventory.api.dto.CardDTO;
import com.comarch.oss.physicalinventory.api.dto.ImmutablePhysicalDeviceDTO;
import com.comarch.oss.physicalinventory.api.dto.PhysicalDeviceDTO;
import com.comarch.oss.physicalinventory.api.dto.PortDTO;
import com.comarch.oss.planning.api.dto.ObjectIdDTO;
import com.oss.serviceClient.EnvironmentRequestClient;
import com.oss.transport.infrastructure.ObjectIdentifier;
import com.oss.transport.infrastructure.RefId;
import com.oss.transport.infrastructure.planning.InventoryModuleRepository;
import com.oss.transport.infrastructure.planning.PlanningContext;
import com.oss.transport.infrastructure.resource.catalog.control.ModelRepository;

public class DeviceFactory {

    public static final String CARD_TYPE = "Card";
    public static final String CARD_MODEL = "CardModel";
    public static final String MISSING_DEVICE_TYPE_EXCEPTION = "Missing device type";
    private static final String PLUGGABLE_MODULE_MODEL = "PluggableModuleModel";
    public static final String MISSING_PORT_DTO_ID_EXCEPTION = "Missing PortDTO ID";
    private final InventoryModuleRepository inventoryClient;
    private final DeviceClient physicalClient;
    private final ModelRepository modelRepository;

    public DeviceFactory(EnvironmentRequestClient env) {
        inventoryClient = new InventoryModuleRepository(env);
        physicalClient = new DeviceClient(env);
        modelRepository = new ModelRepository(env);
    }

    public Long getOrCreatePhysicalDevice(ObjectIdentifier device, ObjectIdentifier model, Optional<RefId> locationId,
                                          Optional<RefId> preciseLocationId, PlanningContext context) {

        Optional<Long> radioEquipId = inventoryClient.getObjectIdByName(device.getName(), device.getType(), context);
        return radioEquipId.orElseGet(() -> createPhysicalDevice(device, model, locationId, preciseLocationId, context));
    }

    public Long createPhysicalDevice(ObjectIdentifier device, ObjectIdentifier model, Optional<RefId> locationId,
                                     Optional<RefId> preciseLocationId, PlanningContext context) {

        Long modelId = getOrCreateDeviceModel(model.getName(), model.getType());
        ImmutablePhysicalDeviceDTO.Builder builder = PhysicalDeviceDTO.builder()
                .type(device.getType())
                .deviceModel(AttributeDTO.builder()
                        .id(modelId)
                        .type(model.getType())
                        .build())
                .name(device.getName());
        locationId.ifPresent(id -> builder.location(AttributeDTO.builder()
                .id(id.getId())
                .type(id.getType())
                .build()));
        preciseLocationId.ifPresent(id -> builder.preciseLocation(AttributeDTO.builder()
                .id(id.getId())
                .type(id.getType())
                .build()));

        return physicalClient.createDeviceAndReturnId(builder.build(), context);
    }

    public Long getOrCreatePhysicalDevice(PhysicalDeviceDTO dto, PlanningContext context) {
        String name = dto.getName().orElseThrow(() -> new IllegalStateException("Cannot get device without Name"));
        Optional<Long> deviceId = inventoryClient.getObjectIdByName(name, dto.getType().orElseThrow(() -> new IllegalStateException(MISSING_DEVICE_TYPE_EXCEPTION)), context);
        return deviceId.orElseGet(() -> createPhysicalDevice(dto, context));
    }

    public Long createPhysicalDevice(PhysicalDeviceDTO dto, PlanningContext context) {
        return physicalClient.createDeviceAndReturnId(dto, context);
    }

    public Long getOrCreateCard(String name, String modelName, Long deviceId, String chassis, String slot, PlanningContext context) {
        Optional<Long> cardId = inventoryClient.getObjectIdByName(name, CARD_TYPE, context);
        return cardId.orElseGet(() -> createCard(name, modelName, deviceId, chassis, slot, context));
    }

    public Long getOrCreateDeviceModel(String modelName, String modelType) {
        Optional<ObjectIdDTO> objectId = inventoryClient.getObjectIdByName(modelName, modelType);
        if (objectId.isPresent()) {
            return objectId.get().getId();
        }
        return modelRepository.createDeviceModel(modelName);
    }

    public Long getOrCreateCardModel(String modelName) {
        Optional<ObjectIdDTO> objectId = inventoryClient.getObjectIdByName(modelName, CARD_MODEL);
        if (objectId.isPresent()) {
            return objectId.get().getId();
        }
        return modelRepository.createCardModel(modelName);
    }

    public Long getOrCreatePluggableModuleModel(String modelName) {
        Optional<ObjectIdDTO> objectId = inventoryClient.getObjectIdByName(modelName, PLUGGABLE_MODULE_MODEL);
        if (objectId.isPresent()) {
            return objectId.get().getId();
        }
        return modelRepository.createPluggableModuleModel(modelName);
    }

    public Map<String, Long> getDevicePortsIds(Long deviceId, PlanningContext context) {
        PhysicalDeviceDTO device = physicalClient.getDeviceStructure(deviceId, context);
        return device.getPorts().stream().filter(this::isPortIdNotEmpty)
                .collect(Collectors.toMap(PortDTO::getName, dto -> dto.getId().orElseThrow(() -> new IllegalStateException(MISSING_PORT_DTO_ID_EXCEPTION))));
    }

    public Map<String, PortDTO> getDevicePortObjects(Long deviceId, PlanningContext context) {
        PhysicalDeviceDTO device = physicalClient.getDeviceStructure(deviceId, context);
        return device.getPorts().stream().filter(this::isPortIdNotEmpty)
                .collect(Collectors.toMap(PortDTO::getName, Function.identity()));
    }

    private Long createCard(String name, String modelName, Long deviceId, String chassis, String slot, PlanningContext context) {
        Long modelId = getOrCreateCardModel(modelName);
        CardDTO dto = CardDTO.builder()
                .deviceId(deviceId)
                .name(name)
                .type(CARD_TYPE)
                .cardModel(AttributeDTO.builder()
                        .id(modelId)
                        .type(CARD_MODEL)
                        .build())
                .chassisName(chassis)
                .slotName(slot)
                .build();
        return physicalClient.createCard(dto, context);
    }

    private boolean isPortIdNotEmpty(PortDTO dto) {
        return dto.getId().isPresent();
    }
}
