package com.oss.transport.infrastructure.resource.catalog.control;

import com.comarch.oss.resourcecatalog.api.dto.CompatibilityDTO;
import com.comarch.oss.resourcecatalog.api.dto.LogicalCrossconnectDTO;
import com.comarch.oss.resourcecatalog.api.dto.ModelDTO;
import com.comarch.oss.resourcecatalog.api.dto.PhysicalCrossconnectDTO;
import com.comarch.oss.resourcecatalog.api.dto.PhysicalDeviceModelEntityDTO;
import com.comarch.oss.resourcecatalog.api.dto.PortLogicalCrossconnectDTO;
import com.comarch.oss.resourcecatalog.api.dto.PortPhysicalCrossconnectDTO;
import com.comarch.oss.resourcecatalog.api.dto.SearchDTO;
import com.comarch.oss.resourcecatalog.api.dto.StructureChassisDTO;
import com.comarch.oss.resourcecatalog.api.dto.StructurePluggableModuleSlotModelDTO;
import com.comarch.oss.resourcecatalog.api.dto.StructurePortDTO;
import com.comarch.oss.resourcecatalog.api.dto.StructureSlotDTO;
import com.comarch.oss.resourcecatalog.api.dto.SupportedLayersDTO;
import com.comarch.oss.resourcecatalog.api.dto.TerminationPointsDTO;
import com.oss.serviceClient.EnvironmentRequestClient;
import com.oss.transport.infrastructure.planning.InventoryModuleRepository;
import com.oss.transport.infrastructure.resource.catalog.control.json.CardModelDTO;
import com.oss.transport.infrastructure.resource.catalog.control.json.DeviceModelDTO;
import com.oss.transport.infrastructure.resource.catalog.control.json.PluggableModuleModelDTO;
import com.oss.transport.infrastructure.resource.catalog.control.json.PluggableModuleSlotModelDTO;
import com.oss.transport.infrastructure.resource.catalog.entity.CardModelEntity;
import com.oss.transport.infrastructure.resource.catalog.entity.ChassisModelEntity;
import com.oss.transport.infrastructure.resource.catalog.entity.DeviceModelEntity;
import com.oss.transport.infrastructure.resource.catalog.entity.ModelContainer;
import com.oss.transport.infrastructure.resource.catalog.entity.ModelEntity;
import com.oss.transport.infrastructure.resource.catalog.entity.ModelIdentifier;
import com.oss.transport.infrastructure.resource.catalog.entity.PluggableModuleEntity;
import com.oss.transport.infrastructure.resource.catalog.entity.PluggableModuleSlotEntity;
import com.oss.transport.infrastructure.resource.catalog.entity.PortModelEntity;
import com.oss.transport.infrastructure.resource.catalog.entity.SlotEntity;

import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

public class ModelRepository {

    public static final String PATH_TYPE = "Path";
    public static final String MANUFACTURER_NAME = "TransportSeleniumTest";

    private final DTOMapper dtoMapper;
    private final ModelContainer modelsHolder;
    private final ModelEntityBuilder modelEntityBuilder;
    private final ResourceCatalogClient resourceCatalogClient;

    public ModelRepository(EnvironmentRequestClient requestClient) {
        this.dtoMapper = new DTOMapper();
        this.modelsHolder = new ModelContainer();
        this.resourceCatalogClient = ResourceCatalogClient.getInstance(requestClient);
        InventoryModuleRepository planningClient = new InventoryModuleRepository(requestClient);
        this.modelEntityBuilder = new ModelEntityBuilder(resourceCatalogClient, planningClient);
    }

    public Long createDeviceModel(String modelName) {
        try {
            DeviceModelEntity deviceModelEntity = getDeviceModel(modelName);
            SearchDTO model = createModel(deviceModelEntity.getModelEntity(), false);
            Long deviceModelId = modelsHolder.setDeviceModelId(deviceModelEntity.getModelEntity().getModelIdentifier(), model);
            addSupportedLayersToDevice(deviceModelEntity, deviceModelId);
            createAndAddChassisToDevice(deviceModelEntity.getChassisModels(), deviceModelId);
            createAndAddPortsToDevice(deviceModelEntity.getPortModels(), deviceModelId);
            addPhysicalCrossesToDevice(deviceModelId, deviceModelEntity);
            addDSRCrossesToDevice(deviceModelId, deviceModelEntity);
            return modelsHolder.addNewModel(deviceModelEntity.getModelEntity().getModelIdentifier(), model);
        } catch (Exception e) {
            removeModels(modelsHolder);
            throw e;
        }
    }

    public Long createCardModel(String modelName) {
        try {
            CardModelEntity modelEntity = getCardModel(modelName);
            Optional<Long> existingModelId = getModelId(modelEntity.getModelEntity().getModelIdentifier());
            if (existingModelId.isPresent()) {
                return existingModelId.get();
            }

            SearchDTO model = createModel(modelEntity.getModelEntity(), false);
            Long modelId = modelsHolder.addCreatedModel(modelEntity.getModelEntity().getModelIdentifier(), model);
            createAndAddPortsToCard(modelEntity.getPortModels(), modelId);
            modelEntity.getSlotModels().stream()
                    .map(this::createSlotModel)
                    .forEach(slot -> resourceCatalogClient.addSlotToObject(modelId, slot));
            addPhysicalCrossesToCard(modelId, modelEntity);
            addDSRCrossesToCard(modelId, modelEntity);
            return modelsHolder.addNewModel(modelEntity.getModelEntity().getModelIdentifier(), model);
        } catch (Exception e) {
            removeModels(modelsHolder);
            throw e;
        }
    }

    public Long createPluggableModuleSlotModel(String modelName) {
        try {
            PluggableModuleSlotEntity modelEntity = getPluggableModuleSlotModel(modelName);
            Optional<Long> existingModelId = getModelId(modelEntity.getModelEntity().getModelIdentifier());
            if (existingModelId.isPresent()) {
                return existingModelId.get();
            }

            SearchDTO model = createModel(modelEntity.getModelEntity(), false);
            Long modelId = modelsHolder.addCreatedModel(modelEntity.getModelEntity().getModelIdentifier(), model);

            modelEntity.getCompatiblePluggableModels().stream()
                    .forEach(compatiblePluggableModel -> {
                        getOrCreatePluggableModuleModel(compatiblePluggableModel);
                        createPluggableModuleWithSlotCompatibility(modelId, modelEntity.getModelEntity(),
                                compatiblePluggableModel);
                    });
            return modelsHolder.addNewModel(modelEntity.getModelEntity().getModelIdentifier(), model);
        } catch (Exception e) {
            removeModels(modelsHolder);
            throw e;
        }
    }

    public Long createPluggableModuleModel(String modelName) {
        try {
            PluggableModuleEntity modelEntity = getPluggableModuleModel(modelName);
            Optional<Long> existingModelId = getModelId(modelEntity.getModelEntity().getModelIdentifier());
            if (existingModelId.isPresent()) {
                return existingModelId.get();
            }

            SearchDTO model = createModel(modelEntity.getModelEntity(), false);
            Long modelId = modelsHolder.addCreatedModel(modelEntity.getModelEntity().getModelIdentifier(), model);

            addToPluggableModule(modelId, modelEntity);
            return modelsHolder.addNewModel(modelEntity.getModelEntity().getModelIdentifier(), model);
        } catch (Exception e) {
            removeModels(modelsHolder);
            throw e;
        }
    }

    public DeviceModelEntity getDeviceModel(String modelName) {
        Map<String, DeviceModelDTO> deviceModels = CustomTestModels.getDeviceModels();
        if (!deviceModels.containsKey(modelName)) {
            throw new IllegalStateException("Cannot find Object with Name: " + modelName);
        }

        return modelEntityBuilder.buildDeviceModelEntity(deviceModels.get(modelName));
    }

    public CardModelEntity getCardModel(String modelName) {
        Map<String, CardModelDTO> cardModels = CustomTestModels.getCardModels();
        if (!cardModels.containsKey(modelName)) {
            throw new IllegalStateException("Cannot find Object with Name: " + modelName);
        }

        return modelEntityBuilder.buildCardModelEntity(cardModels.get(modelName));
    }

    public PluggableModuleSlotEntity getPluggableModuleSlotModel(String modelName) {
        Map<String, PluggableModuleSlotModelDTO> models = CustomTestModels.getPluggableModuleSlotModels();
        if (!models.containsKey(modelName)) {
            throw new IllegalStateException("Cannot find Pluggable Module Slot Model with Name: " + modelName);
        }

        return modelEntityBuilder.buildPluggableModuleSlotModelEntity(models.get(modelName));
    }

    public PluggableModuleEntity getPluggableModuleModel(String modelName) {
        Map<String, PluggableModuleModelDTO> models = CustomTestModels.getPluggableModuleModels();
        if (!models.containsKey(modelName)) {
            throw new IllegalStateException("Cannot find Pluggable Module Model with Name: " + modelName);
        }

        return modelEntityBuilder.buildPluggableModuleModelEntity(models.get(modelName));
    }

    private void addToPluggableModule(Long modelId, PluggableModuleEntity modelEntity) {
        addTerminationPointToPluggableModule(modelId, modelEntity);
        addPhysicalCrossesToPluggableModule(modelId, modelEntity);
        addDSRCrossesToPluggableModule(modelId, modelEntity);
    }

    private void addTerminationPointToPluggableModule(Long modelId, PluggableModuleEntity modelEntity) {
        TerminationPointsDTO terminationPointsDTO = modelEntity.getTps();
        resourceCatalogClient.addTerminationPointToPluggableModule(modelId, terminationPointsDTO);
    }

    private void addPhysicalCrossesToPluggableModule(Long id, PluggableModuleEntity pluggableModelEntity) {
        List<PortPhysicalCrossconnectDTO> physicalCrosses = pluggableModelEntity.getPhysicalCrosses();
        if (!physicalCrosses.isEmpty()) {
            resourceCatalogClient.addPhysicalCrossesToPluggableModule(id, physicalCrosses);
        }
    }

    private void addDSRCrossesToPluggableModule(Long id, PluggableModuleEntity pluggableModelEntity) {
        List<PortLogicalCrossconnectDTO> dsrCrosses = pluggableModelEntity.getDSRCrosses();
        if (!dsrCrosses.isEmpty()) {
            resourceCatalogClient.addDSRCrossesToPluggableModule(id, dsrCrosses);
        }
    }

    public void removeModels(ModelContainer modelsHolder) {
        Optional<Long> modelId = modelsHolder.getDeviceModelId();
        modelId.ifPresent(resourceCatalogClient::removeSupportedLayers);

        List<Long> slotModelsIds = modelsHolder.getCreatedModelsByType(ModelEntityBuilder.PORT_HOLDER_MODEL);
        slotModelsIds.forEach(model -> resourceCatalogClient.createCompatibility(model, Collections.emptyList()));

        Set<Long> idsToRemove = new HashSet<>();
        modelId.ifPresent(idsToRemove::add);
        idsToRemove.addAll(modelsHolder.getCreatedModelIds());
        resourceCatalogClient.delete(idsToRemove);
        modelsHolder.removeAllCreatedModels();
    }

    private void addSupportedLayersToDevice(DeviceModelEntity deviceModelEntity, Long deviceId) {
        PhysicalDeviceModelEntityDTO supportedLayers = createSupportedLayers(deviceModelEntity);
        resourceCatalogClient.addSupportedLayersToDeviceModel(deviceId, supportedLayers);
    }

    private PhysicalDeviceModelEntityDTO createSupportedLayers(DeviceModelEntity deviceModelEntity) {
        return PhysicalDeviceModelEntityDTO.builder()
                .objectType(deviceModelEntity.getModelEntity().getType())
                .modelName(deviceModelEntity.getModelEntity().getName())
                .modelManufacturer(deviceModelEntity.getModelEntity().getManufacturer())
                .supportedLayers(
                        deviceModelEntity.getSupportedLayers().stream().map(this::createSupportedLayers).collect(Collectors.toList()))
                .build();
    }

    private SupportedLayersDTO createSupportedLayers(String layer) {
        return SupportedLayersDTO.builder()
                .layer(layer)
                .pathObjectType(PATH_TYPE)
                .termination(true)
                .routing(true)
                .build();
    }

    private void createAndAddChassisToDevice(List<ChassisModelEntity> chassisModels, Long deviceId) {
        chassisModels.stream()
                .map(this::createChassisModel)
                .forEach(chassis -> resourceCatalogClient.addChassisToDevice(deviceId, chassis));
    }

    private StructureChassisDTO createChassisModel(ChassisModelEntity entity) {
        Optional<Long> modelId = getModelId(entity.getModelEntity().getModelIdentifier());
        if (!modelId.isPresent()) {
            SearchDTO chassisModelDTO = createModel(entity.getModelEntity(), false);
            modelsHolder.addCreatedModel(entity.getModelEntity().getModelIdentifier(), chassisModelDTO);
            entity.getSlots().stream()
                    .map(this::createSlotModel)
                    .forEach(slot -> resourceCatalogClient.addSlotToObject(chassisModelDTO.getId(), slot));
        }
        return dtoMapper.mapToStructureChassisDTO(entity);
    }

    private StructureSlotDTO createSlotModel(SlotEntity entity) {
        Optional<Long> modelId = getModelId(entity.getModelEntity().getModelIdentifier());
        if (!modelId.isPresent()) {
            SearchDTO slotModel = createModel(entity.getModelEntity(), false);
            modelsHolder.addCreatedModel(entity.getModelEntity().getModelIdentifier(), slotModel);
        }
        return dtoMapper.mapToStructureSlotDTO(entity);
    }

    private void createAndAddPortsToDevice(List<PortModelEntity> portModels, Long id) {
        portModels.stream()
                .map(this::createPortModel)
                .forEach(structurePortDTO -> resourceCatalogClient.addPortToDevice(id, structurePortDTO));
    }

    private void createAndAddPortsToCard(Collection<PortModelEntity> portModels, Long id) {
        portModels.stream()
                .map(this::createPortModel)
                .forEach(structurePortDTO -> resourceCatalogClient.addPortToCard(id, structurePortDTO));
    }

    private StructurePortDTO createPortModel(PortModelEntity entity) {
        Optional<Long> modelId = getModelId(entity.getModelEntity().getModelIdentifier());
        if (!modelId.isPresent()) {
            SearchDTO portModelSearchDTO = createModel(entity.getModelEntity(), false);
            Long portModelId = modelsHolder.addCreatedModel(entity.getModelEntity().getModelIdentifier(), portModelSearchDTO);
            addToPort(portModelId, entity);
        }
        return dtoMapper.mapToStructurePortDTO(entity);
    }

    private void addToPort(Long portModelId, PortModelEntity portModelEntity) {
        addTerminationPointToPort(portModelId, portModelEntity);
        addPhysicalCrossesToPort(portModelId, portModelEntity);
        addDSRCrossesToPort(portModelId, portModelEntity);
        if (portModelEntity.isSlot()) {
            addPluggableSlotToPort(portModelId, portModelEntity.getPluggableModuleSlot().get());
        }
    }

    private void addTerminationPointToPort(Long id, PortModelEntity portModelEntity) {
        TerminationPointsDTO terminationPointsDTO = portModelEntity.getTps();
        resourceCatalogClient.addTerminationPointToPort(id, terminationPointsDTO);
    }

    private void addPhysicalCrossesToPort(Long id, PortModelEntity portModelEntity) {
        List<PortPhysicalCrossconnectDTO> physicalCrosses = portModelEntity.getPhysicalCrosses();
        if (!physicalCrosses.isEmpty()) {
            resourceCatalogClient.addPhysicalCrossesToPort(id, physicalCrosses);
        }
    }

    private void addDSRCrossesToPort(Long id, PortModelEntity portModelEntity) {
        List<PortLogicalCrossconnectDTO> dsrCrosses = portModelEntity.getDSRCrosses();
        if (!dsrCrosses.isEmpty()) {
            resourceCatalogClient.addDSRCrossesToPort(id, dsrCrosses);
        }
    }

    private void addPluggableSlotToPort(Long id, ModelIdentifier pluggableModuleSlot) {
        getOrCreatePluggableModuleSlotModel(pluggableModuleSlot);
        StructurePluggableModuleSlotModelDTO structurePluggableModuleSlotModelDTO =
                dtoMapper.mapToStructurePluggableModuleSlotModelDTO(pluggableModuleSlot);
        resourceCatalogClient.addPluggableModuleSlotToPort(id, structurePluggableModuleSlotModelDTO);
    }

    private Long getOrCreatePluggableModuleSlotModel(ModelIdentifier identifier) {
        Optional<Long> modelId = getModelId(identifier);
        if (modelId.isPresent()) {
            return modelId.get();
        }

        return createPluggableModuleSlotModel(identifier.getName());
    }

    private Long getOrCreatePluggableModuleModel(ModelIdentifier identifier) {
        Optional<Long> modelId = getModelId(identifier);
        if (modelId.isPresent()) {
            return modelId.get();
        }

        return createPluggableModuleModel(identifier.getName());
    }

    private void createPluggableModuleWithSlotCompatibility(Long slotId, ModelEntity slotModel, ModelIdentifier pluggableModuleModel) {
        CompatibilityDTO compatibilityDTO = dtoMapper.mapToCompatibilityDTO(slotModel, pluggableModuleModel);
        resourceCatalogClient.createCompatibility(slotId, Collections.singletonList(compatibilityDTO));
    }

    private void addPhysicalCrossesToDevice(Long id, DeviceModelEntity deviceModelEntity) {
        List<PhysicalCrossconnectDTO> physicalCrosses = deviceModelEntity.getPhysicalCrosses();
        if (!physicalCrosses.isEmpty()) {
            resourceCatalogClient.addPhysicalCrossesToDevice(id, physicalCrosses);
        }
    }

    private void addDSRCrossesToDevice(Long id, DeviceModelEntity deviceModelEntity) {
        List<LogicalCrossconnectDTO> dsrCrosses = deviceModelEntity.getDSRCrosses();
        if (!dsrCrosses.isEmpty()) {
            resourceCatalogClient.addDSRCrossesToDevice(id, dsrCrosses);
        }
    }

    private void addPhysicalCrossesToCard(Long id, CardModelEntity cardModelEntity) {
        List<PhysicalCrossconnectDTO> physicalCrosses = cardModelEntity.getPhysicalCrosses();
        if (!physicalCrosses.isEmpty()) {
            resourceCatalogClient.addPhysicalCrossesToCard(id, physicalCrosses);
        }
    }

    private void addDSRCrossesToCard(Long id, CardModelEntity cardModelEntity) {
        List<LogicalCrossconnectDTO> dsrCrosses = cardModelEntity.getDSRCrosses();
        if (!dsrCrosses.isEmpty()) {
            resourceCatalogClient.addDSRCrossesToCard(id, dsrCrosses);
        }
    }

    private SearchDTO createModel(ModelEntity modelEntity, boolean suppLayers) {
        ModelDTO modelDTO = dtoMapper.mapToModelDTO(modelEntity, suppLayers);
        return resourceCatalogClient.createModel(modelDTO);
    }

    private Optional<Long> getModelId(ModelIdentifier identifier) {
        Optional<Long> cachedModelId = modelsHolder.getModelId(identifier);
        if (cachedModelId.isPresent()) {
            return cachedModelId;
        }

        List<SearchDTO> models = resourceCatalogClient.getModels(identifier.getName(), identifier.getManufacturer(), identifier.getType());
        if (models.size() > 1) {
            String ids = models.stream()
                    .map(SearchDTO::getId)
                    .map(String::valueOf)
                    .collect(Collectors.joining(","));
            throw new IllegalArgumentException("Found more than 1 model with identifier: " + identifier + ": " + ids);
        }

        Optional<SearchDTO> foundModel = models.stream().findAny();
        foundModel.ifPresent(model -> modelsHolder.addExistingModel(identifier, model));
        return foundModel.map(SearchDTO::getId);
    }

}
