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
import com.oss.services.LogicalFunctionCoreClient;
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
    private final LogicalFunctionCoreClient logicalFunctionCoreClient = LogicalFunctionCoreClient.getInstance(env);
    private final TMFCatalogClient tmfCatalogClient = TMFCatalogClient.getInstance(env);
    protected final Map<String, Long> idByNameMap = new HashMap<>();

    @BeforeClass
    public void prepareData() throws IOException {
        deleteVnfpkgSpec();
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
        deleteVnfpkgSpec();
    }

    private void deleteVnfpkgSpec() {
        tmfCatalogClient.deleteResourceSpecification(VNFPKG_IDENTIFIER, true);
    }

    private void deleteTestInstances() {
        logicalFunctionCoreClient.deleteLogicalFunction(idByNameMap.get(EOCMNFVO_NAME));
        logicalFunctionCoreClient.deleteLogicalFunction(idByNameMap.get(SamsungNFVO_NAME));
    }

    private void deleteRelatedObjects() {
        logicalFunctionCoreClient.deleteLogicalFunction(idByNameMap.get(VNFM_EOCMNFVO_NAME));
        logicalFunctionCoreClient.deleteLogicalFunction(idByNameMap.get(MARKETPLACE_SAMSUNGNFVO_NAME));
        logicalFunctionCoreClient.deleteLogicalFunction(idByNameMap.get(VIM_SAMSUNGNFVO_NAME));
    }

    private void createTestInstances() {
        createNFVOs();
        createRelatedObjects();
    }

    private void createNFVOs() {
        LogicalFunctionBulkIdentificationsDTO bulkResponse = logicalFunctionCoreClient
            .createLogicalFunctionBulk(VNFPKGManualOnboardingDtoBuilder.getNFVOsCreateDto());
        idByNameMap.putAll(getIdByNameMap(bulkResponse));
    }

    private void createRelatedObjects() {
        LogicalFunctionBulkIdentificationsDTO bulkResponse = logicalFunctionCoreClient
            .createLogicalFunctionBulk(VNFPKGManualOnboardingDtoBuilder.getRelatedObjectsCreateDto(idByNameMap.get(EOCMNFVO_NAME), idByNameMap.get(SamsungNFVO_NAME)));
        idByNameMap.putAll(getIdByNameMap(bulkResponse));
    }

    private Map<String, Long> getIdByNameMap(LogicalFunctionBulkIdentificationsDTO bulkResponse) {
        return bulkResponse.getLogicalFunctionsIdentifications().stream()
            .collect(Collectors.toMap(LogicalFunctionSyncIdentificationDTO::getName, LogicalFunctionSyncIdentificationDTO::getId));
    }
}
