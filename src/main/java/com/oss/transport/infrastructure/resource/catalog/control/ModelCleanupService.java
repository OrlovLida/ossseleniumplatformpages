package com.oss.transport.infrastructure.resource.catalog.control;

import com.comarch.oss.resourcecatalog.api.dto.SearchDTO;
import com.oss.serviceClient.EnvironmentRequestClient;
import com.oss.transport.infrastructure.resource.catalog.entity.CardModelEntity;
import com.oss.transport.infrastructure.resource.catalog.entity.ChassisModelEntity;
import com.oss.transport.infrastructure.resource.catalog.entity.DeviceModelEntity;
import com.oss.transport.infrastructure.resource.catalog.entity.ModelEntity;
import com.oss.transport.infrastructure.resource.catalog.entity.PluggableModuleEntity;
import com.oss.transport.infrastructure.resource.catalog.entity.PluggableModuleSlotEntity;
import com.oss.transport.infrastructure.resource.catalog.entity.PortModelEntity;
import com.oss.transport.infrastructure.resource.catalog.entity.SlotEntity;

import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class ModelCleanupService {

    private final ModelRepository modelRepository;
    private final ResourceCatalogClient resourceCatalogClient;

    public ModelCleanupService(EnvironmentRequestClient requestClient) {
        modelRepository = new ModelRepository(requestClient);
        resourceCatalogClient = ResourceCatalogClient.getInstance(requestClient);
    }

    public void cleanUpModels(ModelCleanupContainer models) {
        Collection<DeviceModelEntity> deviceModelsToClean = models.getDeviceModels().stream()
                .map(modelRepository::getDeviceModel)
                .collect(Collectors.toSet());
        Collection<ChassisModelEntity> chassisModelsToClean = deviceModelsToClean.stream()
                .map(DeviceModelEntity::getChassisModels)
                .flatMap(Collection::stream)
                .collect(Collectors.toSet());
        Collection<SlotEntity> slotModelsToClean = chassisModelsToClean.stream()
                .map(ChassisModelEntity::getSlots)
                .flatMap(Collection::stream)
                .collect(Collectors.toSet());
        Collection<PortModelEntity> portModelsToClean = deviceModelsToClean.stream()
                .map(DeviceModelEntity::getPortModels)
                .flatMap(Collection::stream)
                .collect(Collectors.toSet());
        Collection<CardModelEntity> cardModelsToClean = models.getCardModels().stream()
                .map(modelRepository::getCardModel)
                .collect(Collectors.toSet());
        cardModelsToClean.stream()
                .map(CardModelEntity::getSlotModels)
                .forEach(slotModelsToClean::addAll);
        cardModelsToClean.stream()
                .map(CardModelEntity::getPortModels)
                .forEach(portModelsToClean::addAll);
        Collection<PluggableModuleSlotEntity> pluggableModuleSlotModelsToClean = models.getPluggableModuleSlotModels().stream()
                .map(modelRepository::getPluggableModuleSlotModel)
                .collect(Collectors.toSet());
        Collection<PluggableModuleEntity> pluggableModuleModelsToClean = models.getPluggableModuleModels().stream()
                .map(modelRepository::getPluggableModuleModel)
                .collect(Collectors.toSet());

        deviceModelsToClean.stream()
                .map(DeviceModelEntity::getModelEntity)
                .forEach(this::cleanPhysicalDeviceModel);
        chassisModelsToClean.stream()
                .map(ChassisModelEntity::getModelEntity)
                .forEach(this::cleanModel);
        cardModelsToClean.stream()
                .map(CardModelEntity::getModelEntity)
                .forEach(this::cleanModel);
        portModelsToClean.stream()
                .map(PortModelEntity::getModelEntity)
                .forEach(this::cleanModel);
        pluggableModuleSlotModelsToClean.stream()
                .map(PluggableModuleSlotEntity::getModelEntity)
                .forEach(this::cleanPluggableModuleSlotModel);
        pluggableModuleModelsToClean.stream()
                .map(PluggableModuleEntity::getModelEntity)
                .forEach(this::cleanModel);
        slotModelsToClean.stream()
                .map(SlotEntity::getModelEntity)
                .forEach(this::cleanModel);
    }

    private void cleanPhysicalDeviceModel(ModelEntity model) {
        List<SearchDTO> models = resourceCatalogClient.getModels(model.getName(), model.getManufacturer(), model.getType());
        if (!models.isEmpty()) {
            Set<Long> ids = getModelIds(models);
            ids.forEach(resourceCatalogClient::removeSupportedLayers);
            resourceCatalogClient.delete(ids);
        }
    }

    private void cleanPluggableModuleSlotModel(ModelEntity model) {
        List<SearchDTO> models = resourceCatalogClient.getModels(model.getName(), model.getManufacturer(), model.getType());
        if (!models.isEmpty()) {
            Set<Long> ids = getModelIds(models);
            ids.forEach(id -> resourceCatalogClient.createCompatibility(id, Collections.emptyList()));
            resourceCatalogClient.delete(ids);
        }
    }

    private void cleanModel(ModelEntity model) {
        List<SearchDTO> models = resourceCatalogClient.getModels(model.getName(), model.getManufacturer(), model.getType());
        if (!models.isEmpty()) {
            Set<Long> ids = getModelIds(models);
            resourceCatalogClient.delete(ids);
        }
    }

    private Set<Long> getModelIds(List<SearchDTO> models) {
        return models.stream()
                .map(SearchDTO::getId)
                .collect(Collectors.toSet());
    }
}
