package com.oss.transport.infrastructure.resource.catalog.control;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

import com.comarch.oss.planning.api.dto.PerformObjectIdDTO;
import com.comarch.oss.planning.api.dto.PerformObjectResultDTO;
import com.comarch.oss.planning.api.dto.PlannedObjectDTO;
import com.comarch.oss.resourcecatalog.api.dto.ManufacturerJPADTO;
import com.oss.transport.infrastructure.planning.InventoryModuleRepository;
import com.oss.transport.infrastructure.planning.PlanningContext;
import com.oss.transport.infrastructure.resource.catalog.control.json.CardModelDTO;
import com.oss.transport.infrastructure.resource.catalog.control.json.ChassisDTO;
import com.oss.transport.infrastructure.resource.catalog.control.json.DeviceModelDTO;
import com.oss.transport.infrastructure.resource.catalog.control.json.Model;
import com.oss.transport.infrastructure.resource.catalog.control.json.ModelIdentifierDTO;
import com.oss.transport.infrastructure.resource.catalog.control.json.PluggableModuleModelDTO;
import com.oss.transport.infrastructure.resource.catalog.control.json.PluggableModuleSlotModelDTO;
import com.oss.transport.infrastructure.resource.catalog.control.json.PortDTO;
import com.oss.transport.infrastructure.resource.catalog.control.json.ReferenceAttributeDTO;
import com.oss.transport.infrastructure.resource.catalog.control.json.SlotDTO;
import com.oss.transport.infrastructure.resource.catalog.entity.CardModelEntity;
import com.oss.transport.infrastructure.resource.catalog.entity.ChassisModelEntity;
import com.oss.transport.infrastructure.resource.catalog.entity.DeviceModelEntity;
import com.oss.transport.infrastructure.resource.catalog.entity.ModelEntity;
import com.oss.transport.infrastructure.resource.catalog.entity.ModelIdentifier;
import com.oss.transport.infrastructure.resource.catalog.entity.PluggableModuleEntity;
import com.oss.transport.infrastructure.resource.catalog.entity.PluggableModuleSlotEntity;
import com.oss.transport.infrastructure.resource.catalog.entity.PortModelEntity;
import com.oss.transport.infrastructure.resource.catalog.entity.SlotEntity;

class ModelEntityBuilder {

    public static final String MISSING_MANUFACTURER_ID_EXCEPTION = "Missing manufacturer ID";
    static final String PORT_HOLDER_MODEL = "PortHolderModel";
    static final String PLUGGABLE_MODULE_MODEL = "PluggableModuleModel";
    static final String MODEL_NAME = "Name";
    static final String MANUFACTURER = "MasterManufacturer";
    static final String MANUFACTURER_TYPE = "Manufacturer";
    static final String EQUIPMENT_TYPE = "MasterEquipmentType";
    private final Map<String, Long> typeIds = new HashMap<>();
    private final ResourceCatalogClient resourceCatalogClient;
    private final InventoryModuleRepository planningClient;

    ModelEntityBuilder(ResourceCatalogClient resourceCatalogClient, InventoryModuleRepository planningClient) {
        this.resourceCatalogClient = resourceCatalogClient;
        this.planningClient = planningClient;
    }

    DeviceModelEntity buildDeviceModelEntity(DeviceModelDTO model) {
        DeviceModelEntity.Builder builder = DeviceModelEntity.builder();
        for (ChassisDTO chassis : model.chassis) {
            builder.withChassisModelEntity(buildChassisModelEntity(chassis));
        }
        for (PortDTO port : model.ports) {
            builder.withPortModelEntity(buildPortModelEntity(port));
        }
        return builder
                .withModelEntity(buildModelEntity(model))
                .withSupportedLayers(model.supportedLayers)
                .withPhysicalCrosses(model.physicalCrossconnects)
                .withDSRCrosses(model.dsrCrossconnects)
                .build();
    }

    CardModelEntity buildCardModelEntity(CardModelDTO model) {
        Collection<PortModelEntity> portList = model.ports.stream()
                .map(this::buildPortModelEntity)
                .collect(Collectors.toList());
        Collection<SlotEntity> slotList = buildSlotsEntities(model.slots);
        ModelEntity modelEntity = buildModelEntity(model);

        return CardModelEntity.builder()
                .modelEntity(modelEntity)
                .portModels(portList)
                .slotModels(slotList)
                .physicalCrosses(model.physicalCrossconnects)
                .dsrCrosses(model.dsrCrossconnects)
                .build();
    }

    PluggableModuleSlotEntity buildPluggableModuleSlotModelEntity(PluggableModuleSlotModelDTO model) {
        Collection<ModelIdentifier> compatiblePluggableModels = model.compatiblePluggableModules.stream()
                .map(this::buildCompatiblePluggableModuleModelIdentifier)
                .collect(Collectors.toList());
        return new PluggableModuleSlotEntity(buildModelEntity(model), compatiblePluggableModels);
    }

    PluggableModuleEntity buildPluggableModuleModelEntity(PluggableModuleModelDTO model) {
        ModelEntity modelEntity = buildModelEntity(model);
        return PluggableModuleEntity.builder()
                .modelEntity(modelEntity)
                .tps(model.terminationPoints)
                .physicalCrosses(model.physicalCrossconnects)
                .dsrCrosses(model.dsrCrossconnects)
                .build();
    }

    private ChassisModelEntity buildChassisModelEntity(ChassisDTO chassis) {
        return ChassisModelEntity.builder()
                .withName(chassis.instanceName)
                .withModelEntity(buildModelEntity(chassis))
                .withSlots(buildSlotsEntities(chassis.slots))
                .build();
    }

    private Collection<SlotEntity> buildSlotsEntities(List<SlotDTO> slots) {
        Collection<SlotEntity> results = new ArrayList<>();
        for (SlotDTO slot : slots) {
            results.add(new SlotEntity(buildModelEntity(slot), slot.instanceName));
        }
        return results;
    }

    private PortModelEntity buildPortModelEntity(PortDTO port) {
        return PortModelEntity.builder()
                .modelEntity(buildModelEntity(port))
                .portInstanceName(port.instanceName)
                .pluggableModuleSlot(port.pluggableModuleSlot.map(this::buildPluggableModuleSlotModelIdentifier))
                .portTp(port.terminationPoints)
                .physicalCrosses(port.physicalCrossconnects)
                .dsrCrosses(port.dsrCrossconnects)
                .build();
    }

    private ModelIdentifier buildPluggableModuleSlotModelIdentifier(ModelIdentifierDTO dto) {
        return ModelIdentifier.builder()
                .withName(dto.name)
                .withManufacturer(dto.manufacturer)
                .withType(PORT_HOLDER_MODEL)
                .build();
    }

    private ModelIdentifier buildCompatiblePluggableModuleModelIdentifier(ModelIdentifierDTO compatibleModel) {
        return ModelIdentifier.builder()
                .withName(compatibleModel.name)
                .withManufacturer(compatibleModel.manufacturer)
                .withType(PLUGGABLE_MODULE_MODEL)
                .withInstanceName(compatibleModel.instanceName)
                .build();
    }

    private ModelEntity buildModelEntity(Model model) {
        String type = model.getType();
        Map<String, Optional<String>> simpleAttributes = getSimpleAttributes(model);
        Map<String, ModelEntity.AttributeModel> referenceAttributes = getReferenceAttributes(model);
        String name = simpleAttributes.entrySet().stream()
                .filter(entry -> entry.getKey().equals(MODEL_NAME))
                .map(Map.Entry::getValue)
                .findFirst()
                .map(Optional::get)
                .orElseThrow(() -> new IllegalStateException("No Name defined in model"));
        queryForManufacturerAndEquipmentTypeId(referenceAttributes);
        return ModelEntity.builder()
                .withType(type)
                .withName(name)
                .withManufacturer(ModelRepository.MANUFACTURER_NAME)
                .addSimpleAttributes(simpleAttributes)
                .addReferencedAttributes(referenceAttributes)
                .build();
    }

    private Map<String, Optional<String>> getSimpleAttributes(Model model) {
        return model.getSimpleAttributes().entrySet().stream()
                .collect(Collectors.toMap(Map.Entry::getKey, entry -> Optional.of(entry.getValue())));
    }

    private Map<String, ModelEntity.AttributeModel> getReferenceAttributes(Model model) {
        return model.getReferenceAttributes().entrySet().stream()
                .collect(Collectors.toMap(Map.Entry::getKey, entry -> mapValue(entry.getValue())));
    }

    private ModelEntity.AttributeModel mapValue(ReferenceAttributeDTO value) {
        return new ModelEntity.AttributeModel(value.type, value.name, value.value.orElse(""));
    }

    private void queryForManufacturerAndEquipmentTypeId(Map<String, ModelEntity.AttributeModel> referencedAttributes) {
        if (referencedAttributes.containsKey(EQUIPMENT_TYPE)) {
            String typeName = referencedAttributes.get(EQUIPMENT_TYPE).getName();
            Long equipmentTypeId = getEquipmentType(typeName);
            referencedAttributes.get(EQUIPMENT_TYPE).setValue(equipmentTypeId.toString());
        }
        Long manufacturerId = getOrCreateManufacturer(ModelRepository.MANUFACTURER_NAME);
        referencedAttributes.put(MANUFACTURER,
                new ModelEntity.AttributeModel(MANUFACTURER_TYPE, ModelRepository.MANUFACTURER_NAME, manufacturerId.toString()));
    }

    private Long getEquipmentType(String name) {
        return typeIds.computeIfAbsent(name, id -> planningClient.getExistingObjectId(name, "EquipmentType", PlanningContext.perspectiveLive()));
    }

    private Long getOrCreateManufacturer(String name) {
        if (!typeIds.containsKey(name)) {
            Long manufacturerId;
            List<ManufacturerJPADTO> manufacturers = resourceCatalogClient.getManufacturer(name);
            Optional<ManufacturerJPADTO> manufacturer = manufacturers.stream()
                    .filter(dto -> dto.getName().isPresent() && Objects.equals(name, dto.getName().get()))
                    .findAny();
            if (manufacturer.isPresent()) {
                manufacturerId = manufacturer.get().getId().orElseThrow(() -> new IllegalStateException(MISSING_MANUFACTURER_ID_EXCEPTION));
            } else {
                manufacturerId = createManufacturer(name);
            }
            typeIds.put(name, manufacturerId);
        }
        return typeIds.get(name);
    }

    private Long createManufacturer(String name) {
        UUID uuid = UUID.fromString("11111111-1111-1111-1111-111111111111");
        PlannedObjectDTO dto = PlannedObjectDTO.builder()
                .plannedObjectId(PerformObjectIdDTO.builder()
                        .uuid(uuid)
                        .objectType(MANUFACTURER_TYPE)
                        .build())
                .putSimpleAttributes("Name", Optional.of(name))
                .putSimpleAttributes("ShortName", Optional.of(name))
                .operationType(PlannedObjectDTO.OperationTypeEnum.CREATE)
                .build();
        List<PerformObjectResultDTO> results = planningClient.performObjectInLive(Collections.singletonList(dto));
        return results.stream().filter(result -> result.getUuid().isPresent() && Objects.equals(uuid, result.getUuid().get()))
                .findAny().map(PerformObjectResultDTO::getId)
                .orElseThrow(() -> new IllegalStateException("Cannot create Manufacturer " + name));
    }
}
