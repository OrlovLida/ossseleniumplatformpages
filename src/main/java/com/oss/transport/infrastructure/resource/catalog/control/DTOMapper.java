package com.oss.transport.infrastructure.resource.catalog.control;

import com.comarch.oss.resourcecatalog.api.dto.AttributeModelDTO;
import com.comarch.oss.resourcecatalog.api.dto.CompatibilityDTO;
import com.comarch.oss.resourcecatalog.api.dto.ModelDTO;
import com.comarch.oss.resourcecatalog.api.dto.StructureChassisDTO;
import com.comarch.oss.resourcecatalog.api.dto.StructurePluggableModuleSlotModelDTO;
import com.comarch.oss.resourcecatalog.api.dto.StructurePortDTO;
import com.comarch.oss.resourcecatalog.api.dto.StructureSlotDTO;
import com.oss.transport.infrastructure.resource.catalog.entity.ChassisModelEntity;
import com.oss.transport.infrastructure.resource.catalog.entity.ModelEntity;
import com.oss.transport.infrastructure.resource.catalog.entity.ModelIdentifier;
import com.oss.transport.infrastructure.resource.catalog.entity.PortModelEntity;
import com.oss.transport.infrastructure.resource.catalog.entity.SlotEntity;

import java.util.Map;
import java.util.Map.Entry;
import java.util.Optional;
import java.util.stream.Collectors;

import static com.oss.transport.infrastructure.resource.catalog.control.ModelEntityBuilder.MANUFACTURER;

class DTOMapper {

    private static final String DEFAULT_PLUGGABLE_MODULE_SLOT_NAME = "PluggableModuleSlot";

    StructureChassisDTO mapToStructureChassisDTO(ChassisModelEntity entity) {
        Map<String, ModelEntity.AttributeModel> referencedAttributes = entity.getModelEntity().getReferencedAttributes();
        return StructureChassisDTO.builder()
                .isInstanceSelected(true)
                .chassisInstanceName(entity.getName())
                .chassisModelName(entity.getModelEntity().getName())
                .levelOfOptionality(StructureChassisDTO.LevelOfOptionalityEnum.Fixed)
                .chassisModelManufacturer(referencedAttributes.get(MANUFACTURER).getName())
                .build();
    }

    StructureSlotDTO mapToStructureSlotDTO(SlotEntity entity) {
        Map<String, ModelEntity.AttributeModel> referencedAttributes = entity.getModelEntity().getReferencedAttributes();
        return StructureSlotDTO.builder()
                .slotInstanceName(entity.getPortInstanceName())
                .slotModelName(entity.getModelEntity().getName())
                .slotModelManufacturer(referencedAttributes.get(MANUFACTURER).getName())
                .build();
    }

    StructurePortDTO mapToStructurePortDTO(PortModelEntity entity) {
        return StructurePortDTO.builder()
                .portInstanceName(entity.getPortInstanceName())
                .portModelName(entity.getModelEntity().getName())
                .build();
    }

    StructurePluggableModuleSlotModelDTO mapToStructurePluggableModuleSlotModelDTO(ModelIdentifier pluggableModuleSlot) {
        return StructurePluggableModuleSlotModelDTO.builder()
                .pluggableModuleSlotInstanceName(pluggableModuleSlot.getInstanceName().orElse(DEFAULT_PLUGGABLE_MODULE_SLOT_NAME))
                .pluggableModuleSlotModelManufacturerName(pluggableModuleSlot.getManufacturer())
                .pluggableModuleSlotModelName(pluggableModuleSlot.getName())
                .build();
    }

    CompatibilityDTO mapToCompatibilityDTO(ModelEntity slotModel, ModelIdentifier pluggableModuleModel) {
        return CompatibilityDTO.builder()
                .modelManufacturer(pluggableModuleModel.getManufacturer())
                .modelName(pluggableModuleModel.getName())
                .modelType(pluggableModuleModel.getType())
                .compatibleFromModel(AttributeModelDTO.builder()
                        .type(pluggableModuleModel.getType())
                        .value(pluggableModuleModel.getName())
                        .build())
                .compatibleToModel(AttributeModelDTO.builder()
                        .type(slotModel.getType())
                        .value(slotModel.getName())
                        .build())
                .build();
    }

    ModelDTO mapToModelDTO(ModelEntity modelEntity, boolean suppLayers) {
        return ModelDTO.builder()
                .type(modelEntity.getType())
                .putAllSimpleAttributes(modelEntity.getSimpleAttributes())
                .putAllReferenceAttributes(mapToReferencedAttributes(modelEntity.getReferencedAttributes()))
                .withAllSupportedLayers(suppLayers)
                .build();
    }

    private Map<String, Optional<AttributeModelDTO>> mapToReferencedAttributes(
            Map<String, ModelEntity.AttributeModel> referencedAttributes) {
        return referencedAttributes.entrySet().stream()
                .collect(Collectors.toMap(Entry::getKey, entry -> mapToAttributeModelDTO(entry.getValue())));
    }

    private Optional<AttributeModelDTO> mapToAttributeModelDTO(ModelEntity.AttributeModel model) {
        return Optional.of(AttributeModelDTO.builder()
                .type(model.getType())
                .value(model.getValue())
                .build());
    }
}
