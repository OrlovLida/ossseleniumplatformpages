/* @(#) $Id$
 *
 * Copyright (c) 2000-2021 ComArch S.A. All Rights Reserved. Any usage, duplication or redistribution of this
 * software is allowed only according to separate agreement prepared in written between ComArch and authorized party.
 */
package com.oss.repositories;

import java.util.Collection;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Optional;
import java.util.stream.Collectors;
import com.comarch.oss.resourcecatalog.api.dto.AttributeModelDTO;
import com.comarch.oss.resourcecatalog.api.dto.ModelDTO;
import com.comarch.oss.resourcecatalog.api.dto.StructurePortDTO;
import com.oss.repositories.entities.CustomModelsFactory;
import com.oss.repositories.entities.DeviceModel;
import com.oss.repositories.entities.Model;
import com.oss.repositories.entities.PortModel;
import com.oss.repositories.entities.ReferenceAttribute;
import com.oss.services.PlanningClient;
import com.oss.services.ResourceCatalogClient;
import com.oss.untils.Environment;

public class ModelsRepository {
    
    private final PlanningClient planningClient;
    private final ResourceCatalogClient resourceCatalogClient;
    
    public ModelsRepository(Environment environment) {
        planningClient = PlanningClient.getInstance(environment);
        resourceCatalogClient = ResourceCatalogClient.getInstance(environment);
    }
    
    public Long getOrCreateDeviceModel(String modelName, String modelType) {
        Optional<Long> modelId = planningClient.findObjectIdByNameAndType(modelName, modelType);
        return modelId.orElseGet(() -> createDeviceModel(modelName));
    }
    
    private Long createDeviceModel(String modelName) {
        DeviceModel deviceModel = CustomModelsFactory.findDeviceModelByName(modelName);
        Long deviceModelId = createModel(deviceModel);
        createPortsInDeviceModel(deviceModel.getPorts(), deviceModelId);
        return deviceModelId;
    }
    
    private void createPortsInDeviceModel(Collection<PortModel> portsModels, Long deviceModelId) {
        portsModels.forEach(this::createPortModelIfNecessary);
        portsModels.stream()
                .map(this::toStructurePortDTO)
                .forEach(dto -> resourceCatalogClient.addPortToDeviceModel(deviceModelId, dto));
    }
    
    private void createPortModelIfNecessary(PortModel portModel) {
        Optional<Long> modelId = planningClient.findObjectIdByNameAndType(portModel.getModelName(), portModel.getType());
        if (!modelId.isPresent()) {
            Long portModelId = createModel(portModel);
            resourceCatalogClient.addTerminationPointsToPortModel(portModelId, portModel.getTerminationPoints());
        }
    }
    
    private StructurePortDTO toStructurePortDTO(PortModel portModel) {
        return StructurePortDTO.builder()
                .portInstanceName(portModel.getInstanceName())
                .portModelName(portModel.getModelName())
                .build();
    }
    
    private Long createModel(Model model) {
        ModelDTO modelDTO = toModelDTO(model, true);
        return resourceCatalogClient.createModel(modelDTO);
    }
    
    private ModelDTO toModelDTO(Model model, boolean allSupportedLayers) {
        Map<String, Optional<String>> simpleAttributes = toSimpleAttributes(model.getSimpleAttributes());
        Map<String, Optional<AttributeModelDTO>> referenceAttributes = toReferenceAttributes(model);
        
        return ModelDTO.builder()
                .type(model.getType())
                .putAllSimpleAttributes(simpleAttributes)
                .putAllReferenceAttributes(referenceAttributes)
                .withAllSupportedLayers(allSupportedLayers)
                .build();
    }
    
    private Map<String, Optional<String>> toSimpleAttributes(Map<String, String> attributes) {
        return attributes.entrySet().stream()
                .collect(Collectors.toMap(Entry::getKey, entry -> Optional.ofNullable(entry.getValue())));
    }
    
    private Map<String, Optional<AttributeModelDTO>> toReferenceAttributes(Model model) {
        return model.getReferenceAttributes().entrySet().stream()
                .collect(Collectors.toMap(Entry::getKey, entry -> Optional.of(toReferenceAttributeModelDTO(entry.getValue()))));
    }
    
    private AttributeModelDTO toReferenceAttributeModelDTO(ReferenceAttribute refAttribute) {
        Long refAttrId = planningClient.findExistingObjectIdByNameAndType(refAttribute.getName(), refAttribute.getType());
        return AttributeModelDTO.builder()
                .type(refAttribute.getType())
                .value(Long.toString(refAttrId))
                .build();
    }
    
}
