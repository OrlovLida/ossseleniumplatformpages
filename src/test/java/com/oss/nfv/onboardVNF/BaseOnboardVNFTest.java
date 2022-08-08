package com.oss.nfv.onboardVNF;

import com.comarch.oss.logical.function.api.dto.LogicalFunctionSyncIdentificationDTO;
import com.comarch.oss.logical.function.api.dto.LogicalFunctionViewDTO;
import com.comarch.oss.logical.function.v2.api.dto.LogicalFunctionBulkDTO;
import com.oss.BaseTestCase;
import com.oss.services.LogicalFunctionCoreClient;
import com.oss.services.resourcecatalog.tmf.TMFCatalogClient;
import com.oss.untils.Environment;
import com.oss.utils.TestListener;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Listeners;

import java.io.IOException;
import java.util.Optional;

import static com.oss.nfv.onboardVNF.OnboardVNFConstants.MARKETPLACE_NAME;
import static com.oss.nfv.onboardVNF.OnboardVNFConstants.NFVO_NAME;
import static com.oss.nfv.onboardVNF.OnboardVNFConstants.VIM_NAME;
import static com.oss.nfv.onboardVNF.OnboardVNFConstants.VNFPKG_NAME;
import static com.oss.nfv.onboardVNF.OnboardVNFConstants.VNFPKG_SPECIFICATION_IDENTIFIER;

@Listeners({TestListener.class})
public class BaseOnboardVNFTest extends BaseTestCase {

    protected Environment env = Environment.getInstance();
    private LogicalFunctionCoreClient logicalFunctionCoreClient;
    private TMFCatalogClient tmfCatalogClient;

    @BeforeClass
    public void prepareData() throws IOException {
        logicalFunctionCoreClient = LogicalFunctionCoreClient.getInstance(env);
        tmfCatalogClient = TMFCatalogClient.getInstance(env);

        deleteLogicalFunctions();
        deleteSpecifications();

        createRequiredSpecifications();
        createRequiredInstances();
    }

    public void createRequiredSpecifications(){
        tmfCatalogClient.createResourceSpecification(OnboardVNFResource.buildVNFPKGResourceSpecificationCreationDTO());
    }

    public void createRequiredInstances(){
        LogicalFunctionSyncIdentificationDTO masterManagementSystem =
                createLogicalFunction(OnboardVNFResource.buildNFVOLogicalFunctionBulkDTO());
        createLogicalFunction(OnboardVNFResource.buildMarketplaceLogicalFunctionBulkDTO(masterManagementSystem.getId()));
        createLogicalFunction(OnboardVNFResource.buildVIMLogicalFunctionBulkDTO(masterManagementSystem.getId()));
        createLogicalFunction(OnboardVNFResource.buildVNFPKGLogicalFunctionBulkDTO(masterManagementSystem.getId()));
    }

    private LogicalFunctionSyncIdentificationDTO createLogicalFunction(LogicalFunctionBulkDTO logicalFunctionBulkDTO) {
        return logicalFunctionCoreClient
                .createLogicalFunctionBulk(logicalFunctionBulkDTO)
                .getLogicalFunctionsIdentifications()
                .get(0);
    }

    @AfterClass(alwaysRun = true)
    public void cleanData() {
        deleteLogicalFunctions();
        deleteSpecifications();
    }

    private void deleteSpecifications() {
        tmfCatalogClient.deleteResourceSpecification(VNFPKG_SPECIFICATION_IDENTIFIER, false);
    }

    private void deleteLogicalFunctions() {
        deleteLogicalFunctionsByName(VIM_NAME);
        deleteLogicalFunctionsByName(MARKETPLACE_NAME);
        deleteLogicalFunctionsByName(VNFPKG_NAME);
        deleteLogicalFunctionsByName(NFVO_NAME);
    }

    private void deleteLogicalFunctionsByName(String masterManagementSystemName) {
        logicalFunctionCoreClient.getLogicalFunctionByName(masterManagementSystemName).stream()
                .map(LogicalFunctionViewDTO::getId)
                .filter(Optional::isPresent)
                .map(Optional::get)
                .forEach(logicalFunctionCoreClient::deleteLogicalFunction);
    }

}
