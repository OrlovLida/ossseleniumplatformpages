package com.oss.repositories;

import com.comarch.oss.logical.function.api.dto.LogicalFunctionBulkIdentificationsDTO;
import com.comarch.oss.logical.function.api.dto.LogicalFunctionSyncIdentificationDTO;
import com.comarch.oss.logical.function.api.dto.ModelIdentificationDTO;
import com.comarch.oss.logical.function.v2.api.dto.LogicalFunctionBulkDTO;
import com.comarch.oss.logical.function.v2.api.dto.LogicalFunctionSyncDTO;
import com.oss.services.LogicalFunctionClient;
import com.oss.untils.Environment;

public class LogicalFunctionRepository {

    private static LogicalFunctionRepository instance;
    private final LogicalFunctionClient logicalFunctionClient;

    private LogicalFunctionRepository(Environment environment) {
        logicalFunctionClient = LogicalFunctionClient.getInstance(environment);
    }

    public static LogicalFunctionRepository getInstance(Environment environment) {
        if (instance == null) {
            instance = new LogicalFunctionRepository(environment);
        }
        return instance;
    }

    public long createMME(String name) {
        return createLogicalFunction(name, "LogicalFunctionMobileCoreMME");
    }

    public long createMSC(String name) {
        return createLogicalFunction(name, "LogicalFunctionMobileCoreMSC");
    }

    public long createSTP(String name) {
        return createLogicalFunction(name, "LogicalFunctionMobileCoreSTP");
    }

    public void deleteLogicalFunction(long id) {
        logicalFunctionClient.deleteLogicalFunction(id);
    }


    private long createLogicalFunction(String name, String rsIdentifier) {
        LogicalFunctionBulkDTO dto = LogicalFunctionBulkDTO.builder()
                                         .addCreateOrUpdate(LogicalFunctionSyncDTO.builder()

                                                                .model(ModelIdentificationDTO.builder()
                                                                           .resourceSpecification(true)
                                                                           .identifier(rsIdentifier)
                                                                           .build())
                                                                .name(name)
                                                                .type(rsIdentifier)
                                                                .description(name)
                                                                .build())
                                         .build();
        LogicalFunctionBulkIdentificationsDTO logicalFunction = logicalFunctionClient.createLogicalFunction(dto);
        return logicalFunction.getLogicalFunctionsIdentifications()
                   .stream()
                   .map(LogicalFunctionSyncIdentificationDTO::getId)
                   .findFirst()
                   .orElseThrow(() -> new IllegalStateException("Logical function can not be created"));
    }


}
