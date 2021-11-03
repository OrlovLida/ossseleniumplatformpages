package com.oss.transport.infrastructure.resource.catalog.entity;

import com.comarch.oss.resourcecatalog.api.dto.SearchDTO;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

public class ModelContainer {

    private LinkedHashMap<ModelIdentifier, Long> createdModels = new LinkedHashMap<>();
    private Map<ModelIdentifier, Long> existingModels = new HashMap<>();
    private Optional<Long> createdDeviceModelId = Optional.empty();

    public Optional<Long> getDeviceModelId() {
        return createdDeviceModelId;
    }

    public List<Long> getCreatedModelIds() {
        return new ArrayList<>(createdModels.values());
    }

    public List<Long> getCreatedModelsByType(String type) {
        return createdModels.entrySet().stream()
                .filter(entry -> Objects.equals(type, entry.getKey().getType()))
                .map(Map.Entry::getValue)
                .collect(Collectors.toList());
    }

    public Long setDeviceModelId(ModelIdentifier identifier, SearchDTO searchDTO) {
        createdDeviceModelId = Optional.of(searchDTO.getId());
        return addCreatedModel(identifier, searchDTO);
    }

    public Long addCreatedModel(ModelIdentifier identifier, SearchDTO searchDTO) {
        createdModels.put(identifier, searchDTO.getId());
        return searchDTO.getId();
    }

    public Long addNewModel(ModelIdentifier identifier, SearchDTO searchDTO) {
        createdModels.entrySet().forEach(entry -> existingModels.put(entry.getKey(), entry.getValue()));
        createdModels.clear();
        existingModels.put(identifier, searchDTO.getId());
        return searchDTO.getId();
    }

    public Long addExistingModel(ModelIdentifier identifier, SearchDTO searchDTO) {
        existingModels.put(identifier, searchDTO.getId());
        return searchDTO.getId();
    }

    public Optional<Long> getModelId(ModelIdentifier identifier) {
        if (createdModels.containsKey(identifier)) {
            return Optional.of(createdModels.get(identifier));
        }

        if (existingModels.containsKey(identifier)) {
            return Optional.of(existingModels.get(identifier));
        }

        return Optional.empty();
    }

    public void removeAllCreatedModels() {
        createdModels.clear();
    }
}
