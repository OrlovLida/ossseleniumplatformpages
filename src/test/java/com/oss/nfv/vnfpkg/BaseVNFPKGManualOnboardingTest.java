package com.oss.nfv.vnfpkg;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;

import com.comarch.oss.logical.function.api.dto.LogicalFunctionBulkIdentificationsDTO;
import com.comarch.oss.logical.function.api.dto.LogicalFunctionSyncIdentificationDTO;
import com.oss.BaseTestCase;
import com.oss.services.LogicalFunctionClient;
import com.oss.services.resourcecatalog.tmf.TMFCatalogClient;
import com.oss.untils.Environment;

import static com.oss.nfv.vnfpkg.VNFPKGManualOnboardConstants.EOCMNFVO_NAME;
import static com.oss.nfv.vnfpkg.VNFPKGManualOnboardConstants.MARKETPLACE_SAMSUNGNFVO_NAME;
import static com.oss.nfv.vnfpkg.VNFPKGManualOnboardConstants.SamsungNFVO_NAME;
import static com.oss.nfv.vnfpkg.VNFPKGManualOnboardConstants.VIM_SAMSUNGNFVO_NAME;
import static com.oss.nfv.vnfpkg.VNFPKGManualOnboardConstants.VNFM_EOCMNFVO_NAME;
import static com.oss.nfv.vnfpkg.VNFPKGManualOnboardConstants.VNFPKG_IDENTIFIER;

public class BaseVNFPKGManualOnboardingTest extends BaseTestCase {

    private final Environment env = Environment.getInstance();
    private final LogicalFunctionClient logicalFunctionClient = LogicalFunctionClient.getInstance(env);
    private final TMFCatalogClient tmfCatalogClient = TMFCatalogClient.getInstance(env);
    private final Map<String, Long> idByNameMap = new HashMap<>();

    @BeforeClass
    public void prepareData() throws IOException {
        createVNFPKGspec();
        createTestInstances();
    }

    private void createVNFPKGspec() {
        tmfCatalogClient.createResourceSpecification(VNFPKGManualOnboardingDtoBuilder.getVNFPKGspec());
    }

    @AfterClass(alwaysRun = true)
    public void cleanData() {
        deleteRelatedObjects();
        deleteTestInstances();
        deleteVNFPKGspec();
    }

    private void deleteVNFPKGspec() {
        tmfCatalogClient.deleteResourceSpecification(VNFPKG_IDENTIFIER, true);
    }

    private void deleteTestInstances() {
        logicalFunctionClient.deleteLogicalFunction(idByNameMap.get(EOCMNFVO_NAME));
        logicalFunctionClient.deleteLogicalFunction(idByNameMap.get(SamsungNFVO_NAME));
    }

    private void deleteRelatedObjects() {
        logicalFunctionClient.deleteLogicalFunction(idByNameMap.get(VNFM_EOCMNFVO_NAME));
        logicalFunctionClient.deleteLogicalFunction(idByNameMap.get(MARKETPLACE_SAMSUNGNFVO_NAME));
        logicalFunctionClient.deleteLogicalFunction(idByNameMap.get(VIM_SAMSUNGNFVO_NAME));
    }

    private void createTestInstances() {
        createNFVOs();
        createRelatedObjects();
    }

    private void createNFVOs() {
        LogicalFunctionBulkIdentificationsDTO bulkResponse = logicalFunctionClient
            .createLogicalFunctionBulk(VNFPKGManualOnboardingDtoBuilder.getNFVOsCreateDto());
        idByNameMap.putAll(getIdByNameMap(bulkResponse));
    }

    private void createRelatedObjects() {
        LogicalFunctionBulkIdentificationsDTO bulkResponse = logicalFunctionClient
            .createLogicalFunctionBulk(VNFPKGManualOnboardingDtoBuilder.getRelatedObjectsCreateDto(idByNameMap.get(EOCMNFVO_NAME), idByNameMap.get(SamsungNFVO_NAME)));
        idByNameMap.putAll(getIdByNameMap(bulkResponse));
    }

    private Map<String, Long> getIdByNameMap(LogicalFunctionBulkIdentificationsDTO bulkResponse) {
        return bulkResponse.getLogicalFunctionsIdentifications().stream()
            .collect(Collectors.toMap(LogicalFunctionSyncIdentificationDTO::getName, LogicalFunctionSyncIdentificationDTO::getId));
    }
}
