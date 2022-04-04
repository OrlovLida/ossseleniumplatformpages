package com.oss.nfv.vnfpkg;

import java.io.IOException;
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
import static com.oss.nfv.vnfpkg.VNFPKGManualOnboardConstants.SamsungNFVO_NAME;
import static com.oss.nfv.vnfpkg.VNFPKGManualOnboardConstants.VNFPKG_IDENTIFIER;

public class BaseVNFPKGManualOnboardingTest extends BaseTestCase {

    private final Environment env = Environment.getInstance();
    private final LogicalFunctionClient logicalFunctionClient = LogicalFunctionClient.getInstance(env);
    private final TMFCatalogClient tmfCatalogClient = TMFCatalogClient.getInstance(env);
    private Map<String, Long> idByTypeMap;

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
        deleteTestInstances();
        deleteVNFPKGspec();
    }

    private void deleteVNFPKGspec() {
        tmfCatalogClient.deleteResourceSpecification(VNFPKG_IDENTIFIER, true);
    }

    private void deleteTestInstances() {
        logicalFunctionClient.deleteLogicalFunction(idByTypeMap.get(EOCMNFVO_NAME));
        logicalFunctionClient.deleteLogicalFunction(idByTypeMap.get(SamsungNFVO_NAME));
    }

    private void createTestInstances() {
        LogicalFunctionBulkIdentificationsDTO bulkResponse = logicalFunctionClient.createLogicalFunctionBulk(VNFPKGManualOnboardingDtoBuilder.getCreateDto());
        idByTypeMap = bulkResponse.getLogicalFunctionsIdentifications().stream()
            .collect(Collectors.toMap(LogicalFunctionSyncIdentificationDTO::getName, LogicalFunctionSyncIdentificationDTO::getId));
    }
}
