package com.oss.transport.infrastructure.logical;

import com.comarch.oss.logical.function.api.dto.LogicalFunctionDTO;
import com.comarch.oss.logical.function.api.dto.LogicalFunctionTypeDTO;
import com.comarch.oss.logical.function.api.dto.LogicalFunctionTypeIdentificationDTO;
import com.comarch.oss.logical.function.api.dto.LogicalFunctionTypeViewDTO;
import com.comarch.oss.logical.function.api.dto.LogicalFunctionViewDTO;
import com.comarch.oss.logical.function.api.dto.ModelIdentificationDTO;
import com.comarch.oss.resourcecatalog.logical.function.model.api.dto.GenericTerminationPointDescriptorsDTO;
import com.comarch.oss.resourcecatalog.logical.function.model.api.dto.LogicalFunctionModelDTO;
import com.comarch.oss.resourcecatalog.logical.function.model.api.dto.LogicalFunctionModelResponseDTO;
import com.comarch.oss.resourcecatalog.logical.function.model.api.dto.LogicalFunctionModelViewDTO;
import com.comarch.oss.resourcecatalog.logical.function.model.api.dto.ModelStructureDTO;
import com.oss.serviceClient.EnvironmentRequestClient;
import com.oss.transport.infrastructure.planning.PlanningContext;
import com.oss.transport.infrastructure.resource.catalog.control.ResourceCatalogClient;

import java.util.Collection;
import java.util.Optional;

public class LogicalFunctionFactory {

    private ResourceCatalogClient resourceCatalogClient;
    private LogicalFunctionClient logicalFunctionClient;

    private static final String TRANSPORT_FUNCTION_MODEL_TYPE = "TransportFunctionModel";
    private static final String TRANSPORT_FUNCTION_MODEL_NAME = "TransportFunctionEthernetIntegrationTests";
    private static final String TRANSPORT_FUNCTION_MODEL_MANUFACTURER = "Unknown";
    private static final String TRANSPORT_FUNCTION_MODEL_RESOURCE_TYPE = "Unknown";
    private static final String TRANSPORT_FUNCTION_MODEL_IDENTIFIER =
            TRANSPORT_FUNCTION_MODEL_MANUFACTURER + " " + TRANSPORT_FUNCTION_MODEL_NAME;
    private static final String TRANSPORT_FUNCTION_TYPE_NAME = "TransportFunctionIntegrationTests";

    public LogicalFunctionFactory(EnvironmentRequestClient client) {
        logicalFunctionClient = new LogicalFunctionClient(client);
        resourceCatalogClient = ResourceCatalogClient.getInstance(client);
    }

    public Optional<Long> getByName(String name, PlanningContext context) {
        return logicalFunctionClient.getByName(name, context).stream().findAny().flatMap(LogicalFunctionViewDTO::getId);
    }

    public Long getOrCreate(LogicalFunctionDTO dto, PlanningContext context) {
        Collection<LogicalFunctionViewDTO> logicalFunctions = logicalFunctionClient.getByName(dto.getName(), context);
        Optional<LogicalFunctionViewDTO> existingLogicalFunction = logicalFunctions.stream().findFirst();
        return existingLogicalFunction.flatMap(LogicalFunctionViewDTO::getId).orElseGet(() -> create(dto, context));
    }

    public Long create(LogicalFunctionDTO dto, PlanningContext context) {
        return logicalFunctionClient.create(dto, context);
    }

    public Long getOrCreateWithModel(String name, PlanningContext context) {
        Optional<LogicalFunctionViewDTO> logicalFunction = logicalFunctionClient.getByName(name, context).stream().findAny();
        if (logicalFunction.isPresent()) {
            return logicalFunction.get().getId().get();
        }
        return createWithModel(name, context);
    }

    public Long createWithModel(String deviceName, PlanningContext context) {
        String modelIdentifier = getOrCreateModel();
        String type = getOrCreateType();

        ModelIdentificationDTO modelIdentificationDTO = ModelIdentificationDTO.builder()
                .identifier(modelIdentifier)
                .type(TRANSPORT_FUNCTION_MODEL_TYPE)
                .build();

        return create(deviceName, modelIdentificationDTO, type, context);
    }

    private Long create(String name, ModelIdentificationDTO modelIdentificationDTO, String type, PlanningContext context) {
        LogicalFunctionDTO dto = LogicalFunctionDTO.builder()
                .name(name)
                .type(type)
                .model(modelIdentificationDTO)
                .build();
        return logicalFunctionClient.create(dto, context);
    }

    private String getOrCreateModel() {
        Optional<LogicalFunctionModelViewDTO> model =
                resourceCatalogClient.getLogicalFunctionModel(TRANSPORT_FUNCTION_MODEL_IDENTIFIER, TRANSPORT_FUNCTION_MODEL_TYPE);
        if (!model.isPresent()) {
            return createModel().getKey().getIdentifier();
        }

        return model.get().getIdentifier();
    }

    private LogicalFunctionModelResponseDTO createModel() {
        try {
            LogicalFunctionModelDTO logicalFunctionModelDTO = LogicalFunctionModelDTO.builder()
                    .name(TRANSPORT_FUNCTION_MODEL_NAME)
                    .manufacturerName(TRANSPORT_FUNCTION_MODEL_MANUFACTURER)
                    .resourceTypeName(TRANSPORT_FUNCTION_MODEL_RESOURCE_TYPE)
                    .modelStructure(ModelStructureDTO.builder()
                            .terminationPointDescriptor(GenericTerminationPointDescriptorsDTO.builder()
                                    .build())
                            .build())
                    .build();
            return resourceCatalogClient.createLogicalFunctionModel(logicalFunctionModelDTO, TRANSPORT_FUNCTION_MODEL_TYPE);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private String getOrCreateType() {
        Optional<LogicalFunctionTypeViewDTO> type =
                logicalFunctionClient.getLogicalFunctionType(TRANSPORT_FUNCTION_TYPE_NAME);
        if (!type.isPresent()) {
            return createType().getName();
        }

        return type.get().getName();
    }

    private LogicalFunctionTypeIdentificationDTO createType() {
        try {
            LogicalFunctionTypeDTO logicalFunctionTypeDTO = LogicalFunctionTypeDTO.builder()
                    .name(TRANSPORT_FUNCTION_TYPE_NAME)
                    .build();
            return logicalFunctionClient.createLogicalFunctionType(logicalFunctionTypeDTO);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

}
